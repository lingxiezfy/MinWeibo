package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Comment;

import java.util.List;

public interface CommentDao {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> query(Comment record);

    int updateCountColumn(Comment record);
}