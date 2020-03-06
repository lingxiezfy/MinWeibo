package com.fy.real.min.weibo.service.impl;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.dao.dao.*;
import com.fy.real.min.weibo.model.entity.*;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.weibo.*;
import com.fy.real.min.weibo.service.IUserService;
import com.fy.real.min.weibo.service.IWeiBoService;
import com.fy.real.min.weibo.util.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * [Create]
 * Description:
 */
@Service
public class WeiBoServiceImpl implements IWeiBoService {
    @Autowired
    WeiboDao weiboDao;
    @Autowired
    UserDao userDao;
    @Autowired
    CollectDao collectDao;

    //region 发布微博
    private static final Pattern HOT_PATTERN = Pattern.compile("#(\\S+)# ");
    @Override
    public PostWeiboResponse post(PostWeiboRequest request) {
        Weibo newWeiBo = new Weibo();
        newWeiBo.setUserId(request.getUser().getUserId());
        newWeiBo.setContent(request.getContent());
        if (request.getPicList() != null && request.getPicList().size() > 0) {
            newWeiBo.setPic(JSON.toJSONString(request.getPicList()));
        }
        Matcher hotMatcher = WeiBoServiceImpl.HOT_PATTERN.matcher(request.getContent());
        StringBuilder topicBuild = new StringBuilder();
        boolean discussionAble = false;
        while (hotMatcher.find()){
            String topic = hotMatcher.group(1);
            topicBuild.append(topic).append(";");
            if("趣味讨论".equals(topic)){
                discussionAble = true;
            }
        }
        newWeiBo.setTopic(topicBuild.toString());
        weiboDao.insertSelective(newWeiBo);
        User countUser = new User();
        countUser.setUserId(request.getUser().getUserId());
        countUser.setWeiboCount(1);
        userDao.updateCountColumn(countUser);

        if(discussionAble){
            Discussion discussion = new Discussion();
            discussion.setWeiboId(newWeiBo.getWeiboId());
            discussion.setUserId(request.getUser().getUserId());
            discussion.setAliveTime(DateUtils.addHours(new Date(),2));
            discussionDao.insertSelective(discussion);
            return new PostWeiboResponse(newWeiBo.getWeiboId(),discussion.getDiscussionId());
        }
        return new PostWeiboResponse(newWeiBo.getWeiboId(),0);
    }
    //endregion 发布微博


    @Override
    public PostWeiboResponse rePost(RePostWeiboRequest request) {
        ValidatorUtil.validate(request);
        Weibo originWeiBo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if(originWeiBo == null){
            throw new WeiBoException("微博不存在");
        }
        Weibo weibo = new Weibo();
        weibo.setUserId(request.getCurrentUser().getUserId());
        boolean discussionAble = false;
        if(StringUtils.isBlank(request.getContent())){
            request.setContent("转发了");
        }else {
            Matcher hotMatcher = WeiBoServiceImpl.HOT_PATTERN.matcher(request.getContent());
            StringBuilder topicBuild = new StringBuilder();

            while (hotMatcher.find()){
                String topic = hotMatcher.group(1);
                topicBuild.append(topic).append(";");
                if("趣味讨论".equals(topic)){
                    discussionAble = true;
                }
            }
            if(topicBuild.length() > 0){
                weibo.setTopic(topicBuild.toString());
            }
        }
        weibo.setContent(request.getContent());
        weibo.setOriginal(0);
        weibo.setRepostId(request.getWeiBoId());
        weiboDao.insertSelective(weibo);

        Weibo weiBoCount = new Weibo();
        weiBoCount.setWeiboId(originWeiBo.getWeiboId());
        weiBoCount.setRepostCount(1);
        weiboDao.updateCountColumn(weiBoCount);

        User userCount = new User();
        userCount.setUserId(request.getCurrentUser().getUserId());
        userCount.setWeiboCount(1);
        userDao.updateCountColumn(userCount);

        if(discussionAble){
            Discussion discussion = new Discussion();
            discussion.setWeiboId(weibo.getWeiboId());
            discussion.setUserId(request.getCurrentUser().getUserId());
            discussion.setAliveTime(DateUtils.addHours(new Date(),2));
            discussionDao.insertSelective(discussion);
            return new PostWeiboResponse(weibo.getWeiboId(),discussion.getDiscussionId());
        }
        return new PostWeiboResponse(weibo.getWeiboId(), 0);
    }

    //region 获取微博列表
    @Override
    public WeiBoListResponse list(WeiBoListRequest request) {
        if(request.getTargetUserId() == -1){
            return listAll(request.getCurrentUser(),request.getPageIndex(),request.getPageSize());
        } else if (request.getTargetUserId() == 0 || request.getTargetUserId().equals(request.getCurrentUser().getUserId())) {
            return listForUser(request.getCurrentUser(),request.getCurrentUser(),request.getPageIndex(),request.getPageSize());
        }
        User targetUser = userDao.selectByPrimaryKey(request.getTargetUserId());
        if(targetUser == null){
            throw new WeiBoException("用户不存在");
        }
        return listForUser(targetUser,request.getCurrentUser(),request.getPageIndex(),request.getPageSize());
    }

    private WeiBoListResponse listForUser(User targetUser, User currentUser, int pageIndex, int pageSize) {
        WeiBoListResponse response = new WeiBoListResponse();
        response.setList(new ArrayList<>());
        Page page = PageHelper.startPage(pageIndex, pageSize);
        PageHelper.orderBy("weibo_id desc");
        List<Weibo> weiBoList = weiboDao.selectUsefulByUserId(targetUser.getUserId());
        response.setPageIndex(page.getPageNum());
        response.setTotalCount(page.getTotal());
        response.setTotalPage(page.getPages());
        response.setList(covertWeiList(weiBoList,currentUser));
        return response;
    }

    private WeiBoListResponse listAll(User currentUser,int pageIndex,int pageSize){
        WeiBoListResponse response = new WeiBoListResponse();
        response.setList(new ArrayList<>());
        Page page = PageHelper.startPage(pageIndex,pageSize);
        PageHelper.orderBy("likes_count desc,collect_count desc,repost_count desc,comment_count desc,weibo_id desc");
        List<Weibo> weiBoList = weiboDao.selectUseful();
        response.setPageIndex(page.getPageNum());
        response.setTotalCount(page.getTotal());
        response.setTotalPage(page.getPages());
        response.setList(covertWeiList(weiBoList,currentUser));
        return response;
    }

    @Autowired
    DiscussionDao discussionDao;
    @Autowired
    IUserService userService;

    private List<WeiBoViewItem> covertWeiList(List<Weibo> weiBoList, User currentUser){
        List<WeiBoViewItem> list = new ArrayList<>();
        if(weiBoList == null || weiBoList.size() == 0){
            return list;
        }
        List<Integer> userIdList = weiBoList.stream().map(Weibo::getUserId).collect(Collectors.toList());
        List<Integer> rePostIdList = weiBoList.stream().filter(weibo->weibo.getRepostId()>0).map(Weibo::getRepostId).collect(Collectors.toList());
        List<Weibo> rePostWeiBoList;
        if(rePostIdList.size() > 0){
            rePostWeiBoList = weiboDao.selectByWeiboIdList(rePostIdList);
            userIdList.addAll(rePostWeiBoList.stream().map(Weibo::getUserId).collect(Collectors.toList()));
        }else {
            rePostWeiBoList = new ArrayList<>();
        }
        // 找出含有热门讨论的微博
        List<Integer> weiBoIdList = weiBoList.stream().map(Weibo::getWeiboId).collect(Collectors.toList());
        weiBoIdList.addAll(rePostIdList);
        List<Discussion> discussionList = weiBoIdList.size()>0?discussionDao.queryByWeiBoIdList(weiBoIdList):new ArrayList<>();

        List<User> authorList = userDao.selectByUserIdList(userIdList);

        weiBoList.forEach(weiBo -> {
            WeiBoViewItem viewItem = WeiBoViewItem.covertFromWeiBo(weiBo);
            User author = authorList.stream().filter(u->u.getUserId().equals(weiBo.getUserId())).findFirst().orElse(null);
            if(author == null) return;
            viewItem.setAuthor(userService.convertUserWithRelation(author,currentUser));

            Discussion discussion = discussionList.stream().filter(d->d.getWeiboId().equals(weiBo.getWeiboId())).findFirst().orElse(null);
            if(discussion != null){
                viewItem.setDiscussionId(discussion.getDiscussionId());
                Date now = new Date();
                viewItem.setDiscussionAlive(discussion.getAliveTime().getTime() > now.getTime());
            }
            if(viewItem.getRepostId() > 0){
                Optional<Weibo> optional = rePostWeiBoList.stream().filter(w->w.getWeiboId().equals(viewItem.getRepostId()) && w.getUseful() == 1).findFirst();
                Weibo originWeiBo = optional.orElse(null);
                if(originWeiBo != null){
                    viewItem.setRepostWeiBo(WeiBoViewItem.covertFromWeiBo(originWeiBo));
                    viewItem.getRepostWeiBo().setAuthor(userService.convertUserWithRelation(
                            authorList.stream().filter(u->u.getUserId().equals(viewItem.getRepostWeiBo().getUserId())).findFirst().orElse(null)
                            ,currentUser
                    ));

                    Discussion discussionRe = discussionList.stream().filter(d->d.getWeiboId().equals(originWeiBo.getWeiboId())).findFirst().orElse(null);
                    if(discussionRe != null){
                        viewItem.getRepostWeiBo().setDiscussionId(discussionRe.getDiscussionId());
                        Date now = new Date();
                        viewItem.getRepostWeiBo().setDiscussionAlive(discussionRe.getAliveTime().getTime() > now.getTime());
                    }
                }
            }
            list.add(viewItem);
        });
        return list;
    }

    //endregion 获取微博列表

    //region 查找微博

    @Override
    public WeiBoListResponse search(WeiBoSearchRequest request) {
        WeiBoListResponse response = new WeiBoListResponse();
        Page page = PageHelper.startPage(request.getPageIndex(),request.getPageSize());
        Weibo searchWeiBo = new Weibo();
        if(StringUtils.isNotBlank(request.getQuery())){
            searchWeiBo.setContent(request.getQuery());
        }
        if(StringUtils.isNotBlank(request.getTopic())){
            searchWeiBo.setTopic(request.getTopic());
        }
        searchWeiBo.setUseful(1);
        List<Weibo> weiBoList = weiboDao.query(searchWeiBo);
        if(weiBoList.size() >0){
            response.setPageIndex(page.getPageNum());
            response.setTotalCount(page.getTotal());
            response.setTotalPage(page.getPages());
            response.setList(covertWeiList(weiBoList,request.getCurrentUser()));
        }else {
            response.setList(new ArrayList<>());
        }
        return response;
    }

    //endregion

    @Override
    public Boolean delete(WeiBoActionRequest request) {
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if (weibo == null
                || !(weibo.getUserId().equals(request.getCurrentUser().getUserId())
                || request.getCurrentUser().getAdminAble() == 1)) {
            throw new WeiBoException("删除失败，无删除权限");
        }
        Weibo deleteWeiBo = new Weibo();
        deleteWeiBo.setWeiboId(weibo.getWeiboId());
        deleteWeiBo.setUseful(0);
        weiboDao.updateByPrimaryKeySelective(deleteWeiBo);
        User updateUser = new User();
        updateUser.setUserId(weibo.getUserId());
        updateUser.setWeiboCount(-1);
        userDao.updateCountColumn(updateUser);
        return true;
    }

    //region 收藏相关操作
    @Override
    public Integer collect(WeiBoActionRequest request) {
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if (weibo == null) {
            throw new WeiBoException("收藏失败,没有找到该微博");
        }
        Integer currUserId = request.getCurrentUser().getUserId();
        if(weibo.getUserId().equals(currUserId)){
            throw new WeiBoException("你自己的微博你忘了吗？");
        }
        Collect collect = collectDao.selectByUserAndWeiBo(currUserId,weibo.getWeiboId());
        if(collect != null){
            throw new WeiBoException("已经在您的收藏夹了");
        }
        Collect collectNew = new Collect();
        collectNew.setUserId(currUserId);
        collectNew.setWeiboId(weibo.getWeiboId());
        collectDao.insertSelective(collectNew);
        Weibo weiBoNew = new Weibo();
        weiBoNew.setWeiboId(weibo.getWeiboId());
        weiBoNew.setCollectCount(1);
        weiboDao.updateCountColumn(weiBoNew);
        return weibo.getCollectCount()+1;
    }

    @Override
    public WeiBoListResponse userCollect(WeiBoSearchRequest request) {
        WeiBoListResponse response = new WeiBoListResponse();
        Integer currUserId = request.getCurrentUser().getUserId();
        Page page = PageHelper.startPage(request.getPageIndex(),request.getPageSize());
        List<Collect> collectIds = collectDao.queryByUserId(currUserId);
        if(collectIds.size() > 0){
            List<Weibo> weiBoList = weiboDao.selectByWeiboIdList(collectIds.stream().map(Collect::getWeiboId).collect(Collectors.toList()));
            response.setPageIndex(page.getPageNum());
            response.setTotalCount(page.getTotal());
            response.setTotalPage(page.getPages());
            response.setList(covertWeiList(weiBoList,request.getCurrentUser()));
        }else{
            response.setList(new ArrayList<>());
        }
        return response;
    }

    @Override
    public Integer cancelCollect(WeiBoActionRequest request) {
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if (weibo == null) {
            throw new WeiBoException("操作失败,没有找到该微博");
        }
        Integer currUserId = request.getCurrentUser().getUserId();
        Collect collect = collectDao.selectByUserAndWeiBo(currUserId,weibo.getWeiboId());
        if(collect == null){
            throw new WeiBoException("您没有收藏该微博");
        }
        collectDao.deleteByPrimaryKey(collect.getCollectId());
        Weibo weiBoNew = new Weibo();
        weiBoNew.setWeiboId(weibo.getWeiboId());
        weiBoNew.setCollectCount(-1);
        weiboDao.updateCountColumn(weiBoNew);
        return weibo.getCollectCount() <= 0 ? 0 : (weibo.getCollectCount() - 1);
    }

    //endregion 收藏相关操作

    //region 点赞相关操作

    @Autowired
    LikesDao likesDao;

    @Override
    public Integer likes(WeiBoActionRequest request) {
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if (weibo == null) {
            throw new WeiBoException("点赞失败,没有找到该微博");
        }
        Integer currUserId = request.getCurrentUser().getUserId();
        Likes likes = likesDao.selectByUserAndWeiBo(currUserId,weibo.getWeiboId());
        if(likes != null){
            throw new WeiBoException("您已经点过赞了");
        }
        Likes likesNew = new Likes();
        likesNew.setUserId(currUserId);
        likesNew.setWeiboId(weibo.getWeiboId());
        likesDao.insertSelective(likesNew);
        Weibo weiBoNew = new Weibo();
        weiBoNew.setWeiboId(weibo.getWeiboId());
        weiBoNew.setLikesCount(1);
        weiboDao.updateCountColumn(weiBoNew);
        return weibo.getLikesCount()+1;
    }

    @Override
    public Integer cancelLikes(WeiBoActionRequest request) {
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if (weibo == null) {
            throw new WeiBoException("操作失败,没有找到该微博");
        }
        Integer currUserId = request.getCurrentUser().getUserId();
        Likes likes = likesDao.selectByUserAndWeiBo(currUserId,weibo.getWeiboId());
        if(likes == null){
            throw new WeiBoException("您没有给该微博点过赞");
        }
        likesDao.deleteByPrimaryKey(likes.getLikesId());
        Weibo weiBoNew = new Weibo();
        weiBoNew.setWeiboId(weibo.getWeiboId());
        weiBoNew.setLikesCount(-1);
        weiboDao.updateCountColumn(weiBoNew);
        return weibo.getLikesCount() <= 0 ? 0 : (weibo.getLikesCount() - 1);
    }

    //endregion 点赞相关操作
}
