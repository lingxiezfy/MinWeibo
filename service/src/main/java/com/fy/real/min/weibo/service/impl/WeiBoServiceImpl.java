package com.fy.real.min.weibo.service.impl;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.dao.dao.WeiboDao;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.user.UserView;
import com.fy.real.min.weibo.model.weibo.PostWeiboRequest;
import com.fy.real.min.weibo.model.weibo.WeiBoListRequest;
import com.fy.real.min.weibo.model.weibo.WeiBoListResponse;
import com.fy.real.min.weibo.model.weibo.WeiBoViewItem;
import com.fy.real.min.weibo.service.IWeiBoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 20:28 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Service
public class WeiBoServiceImpl implements IWeiBoService {
    @Autowired
    WeiboDao weiboDao;
    @Autowired
    UserDao userDao;

    //region 发布微博
    private static final Pattern HOT_PATTERN = Pattern.compile("#(\\S+)# ");
    @Override
    public Boolean post(PostWeiboRequest request) {
        Weibo newWeiBo = new Weibo();
        newWeiBo.setUserId(request.getUser().getUserId());
        newWeiBo.setContent(request.getContent());
        if (request.getPicList() != null && request.getPicList().size() > 0) {
            newWeiBo.setPic(JSON.toJSONString(request.getPicList()));
        }
        Matcher hotMatcher = WeiBoServiceImpl.HOT_PATTERN.matcher(request.getContent());
        StringBuilder topicBuild = new StringBuilder();
        while (hotMatcher.find()){
            topicBuild.append(hotMatcher.group(1)).append(";");
        }
        newWeiBo.setTopic(topicBuild.toString());
        weiboDao.insertSelective(newWeiBo);
        User countUser = new User();
        countUser.setUserId(request.getUser().getUserId());
        countUser.setWeiboCount(1);
        userDao.updateCountColumn(countUser);
        return Boolean.TRUE;
    }
    //endregion 发布微博

    //region 获取微博列表
    @Override
    public WeiBoListResponse list(WeiBoListRequest request) {
        if(request.getCurrentUser() == null){
            return listAll(request.getPageIndex(),request.getPageSize());
        }
        return listForUser(request.getCurrentUser(),request.getPageIndex(),request.getPageSize());
    }

    private WeiBoListResponse listForUser(User user, int pageIndex,int pageSize){
        WeiBoListResponse response = new WeiBoListResponse();
        response.setList(new ArrayList<>());
        Page page = PageHelper.startPage(pageIndex,pageSize);
        PageHelper.orderBy("weibo_id desc");
        List<Weibo> weiBoList = weiboDao.selectUsefulByUserId(user.getUserId());
        response.setPageIndex(page.getPageNum());
        response.setTotalCount(page.getTotal());
        response.setTotalPage(page.getPages());
        response.setList(covertWeiList(weiBoList));
        return response;
    }

    private WeiBoListResponse listAll(int pageIndex,int pageSize){
        WeiBoListResponse response = new WeiBoListResponse();
        response.setList(new ArrayList<>());
        Page page = PageHelper.startPage(pageIndex,pageSize);
        PageHelper.orderBy("likes_count desc,collect_count desc,repost_count desc,comment_count desc");
        List<Weibo> weiBoList = weiboDao.selectUseful();
        response.setPageIndex(page.getPageNum());
        response.setTotalCount(page.getTotal());
        response.setTotalPage(page.getPages());
        response.setList(covertWeiList(weiBoList));
        return response;
    }

    private List<WeiBoViewItem> covertWeiList(List<Weibo> weiBoList){
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
        List<User> authorList = userDao.selectByUserIdList(userIdList);

        weiBoList.forEach(weiBo -> {
            WeiBoViewItem viewItem = WeiBoViewItem.covertFromWeiBo(weiBo);
            User author = authorList.stream().filter(u->u.getUserId().equals(weiBo.getUserId())).findFirst().orElse(null);
            if(author == null) return;
            viewItem.setAuthor(UserView.convertFromUser(author));
            if(viewItem.getRepostId() > 0){
                Optional<Weibo> optional = rePostWeiBoList.stream().filter(w->w.getRepostId().equals(viewItem.getRepostId())).findFirst();
                Weibo originWeiBo = optional.orElse(null);
                if(originWeiBo != null){
                    viewItem.setRepostWeiBo(WeiBoViewItem.covertFromWeiBo(originWeiBo));
                    viewItem.getRepostWeiBo().setAuthor(UserView.convertFromUser(
                            authorList.stream().filter(u->u.getUserId().equals(viewItem.getRepostWeiBo().getUserId())).findFirst().orElse(null)
                    ));
                }
            }
            list.add(viewItem);
        });
        return list;
    }

    //endregion 获取微博列表
}
