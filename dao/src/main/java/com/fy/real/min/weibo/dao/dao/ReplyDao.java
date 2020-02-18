package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Reply;

public interface ReplyDao {
    int deleteByPrimaryKey(Integer replyId);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Integer replyId);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);
}