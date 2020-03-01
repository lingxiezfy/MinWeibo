package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.CommentDao;
import com.fy.real.min.weibo.dao.dao.UserDao;
import com.fy.real.min.weibo.dao.dao.WeiboDao;
import com.fy.real.min.weibo.model.entity.Comment;
import com.fy.real.min.weibo.model.entity.User;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import com.fy.real.min.weibo.model.weibo.comment.*;
import com.fy.real.min.weibo.service.ICommentService;
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
 * <br/>Date: 2020/3/1 21:37 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    CommentDao commentDao;
    @Autowired
    UserDao  userDao;
    @Autowired
    WeiboDao weiboDao;

    @Override
    public CommentViewItem add(CommentRequest request) {
        ValidatorUtil.validate(request);
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
        if(request.getCurrentUser().getAdminAble() == 0){
            commentQuery.setUseful(1);
        }
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
        return comment.getLikesCount()-1;
    }
}
