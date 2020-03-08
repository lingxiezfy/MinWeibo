package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.CommentDao;
import com.fy.real.min.weibo.dao.dao.RelationDao;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.dao.dao.WeiboDao;
import com.fy.real.min.weibo.model.entity.Comment;
import com.fy.real.min.weibo.model.entity.Relation;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.enums.MessageTypeEnum;
import com.fy.real.min.weibo.model.enums.RelationStateEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.weibo.comment.*;
import com.fy.real.min.weibo.service.ICommentService;
import com.fy.real.min.weibo.service.IMessageService;
import com.fy.real.min.weibo.util.utils.ValidatorUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [Create]
 * Description:
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    UserDao  userDao;
    @Autowired
    WeiboDao weiboDao;
    @Autowired
    RelationDao relationDao;
    @Autowired
    IMessageService messageService;

    @Override
    public CommentViewItem add(CommentRequest request) {
        ValidatorUtil.validate(request);
        Weibo weibo = weiboDao.selectByPrimaryKey(request.getWeiBoId());
        if(weibo == null){
            throw new WeiBoException("微博不存在!");
        }
        if(weibo.getUseful() == 0){
            throw new WeiBoException("微博已被封禁，暂时无法评论!");
        }
        Relation relation = relationDao.queryUserRelationSingle(weibo.getUserId(),request.getCurrentUser().getUserId());
        if(relation != null && RelationStateEnum.Black.getCode().equals(relation.getState())){
            // 博主把当前用户拉黑，不允许评论
            throw new WeiBoException("博主关闭了评论通道!您暂时无法评论");
        }

        Comment addComment = new Comment();
        addComment.setWeiboId(request.getWeiBoId());
        addComment.setCommentContent(request.getContent());
        addComment.setUserId(request.getCurrentUser().getUserId());
        commentDao.insertSelective(addComment);
        Weibo weiBoCount = new Weibo();
        weiBoCount.setWeiboId(request.getWeiBoId());
        weiBoCount.setCommentCount(1);
        weiboDao.updateCountColumn(weiBoCount);

        addComment.setCreateTime(new Date());
        addComment.setLikesCount(0);
        addComment.setUseful(1);

        //添加点赞消息
        messageService.addMessage(request.getCurrentUser()
                , new User(weibo.getUserId())
                , MessageTypeEnum.CommentWeiBo
                , String.format("%s: %s",MessageTypeEnum.CommentWeiBo.getDesc(),addComment.getCommentContent())
                , addComment.getCommentId()
                , true
        );

        return CommentViewItem.convertFromComment(addComment,request.getCurrentUser());
    }

    @Override
    public CommentListResponse list(CommentListRequest request) {
        ValidatorUtil.validate(request);
        CommentListResponse response = new CommentListResponse();
        Page page = PageHelper.startPage(request.getPageIndex(),request.getPageSize());
        PageHelper.orderBy("create_time desc");
        Comment commentQuery = new Comment();
        commentQuery.setWeiboId(request.getWeiBoId());
        commentQuery.setUseful(1);
        List<Comment> commentList = commentDao.query(commentQuery);
        if(commentList.size() > 0){
            List<Integer> userIds = commentList.stream().map(Comment::getUserId).collect(Collectors.toList());
            List<User> authorList = userDao.selectByUserIdList(userIds);
            response.setPageIndex(page.getPageNum());
            response.setTotalCount(page.getTotal());
            response.setTotalPage(page.getPages());
            response.setList(
                    commentList.stream().map(m->
                    CommentViewItem.convertFromComment(
                            m,
                            authorList.stream().filter(u->u.getUserId().equals(m.getUserId())).findFirst().orElse(null))
                    ).collect(Collectors.toList()));
        }else {
            response.setList(new ArrayList<>());
        }
        return response;
    }

    @Override
    public Boolean delete(CommentActionRequest request) {
        ValidatorUtil.validate(request);
        Comment comment = commentDao.selectByPrimaryKey(request.getCommentId());
        if(comment == null){
            throw new WeiBoException("评论不存在");
        }
        if(comment.getUseful() == 0){
            throw new WeiBoException("评论已删除，无需重复删除！");
        }
        Comment countComment = new Comment();
        countComment.setCommentId(comment.getCommentId());
        countComment.setUseful(0);
        commentDao.updateByPrimaryKeySelective(countComment);

        Weibo weiBoCount = new Weibo();
        weiBoCount.setWeiboId(comment.getWeiboId());
        weiBoCount.setCommentCount(-1);
        weiboDao.updateCountColumn(weiBoCount);
        return true;
    }

    @Override
    public Integer likes(CommentActionRequest request) {
        ValidatorUtil.validate(request);
        Comment comment = commentDao.selectByPrimaryKey(request.getCommentId());
        if(comment == null){
            throw new WeiBoException("评论不存在");
        }
        Comment countComment = new Comment();
        countComment.setCommentId(comment.getCommentId());
        countComment.setLikesCount(1);
        commentDao.updateCountColumn(countComment);
        return comment.getLikesCount()+1;
    }

    @Override
    public Integer cancelLikes(CommentActionRequest request) {
        ValidatorUtil.validate(request);
        Comment comment = commentDao.selectByPrimaryKey(request.getCommentId());
        if(comment == null){
            throw new WeiBoException("评论不存在");
        }
        if(comment.getLikesCount() <= 0){
            return 0;
        }
        Comment countComment = new Comment();
        countComment.setCommentId(comment.getCommentId());
        countComment.setLikesCount(-1);
        commentDao.updateCountColumn(countComment);
        return comment.getLikesCount()>0?(comment.getLikesCount()-1):0;
    }
}
