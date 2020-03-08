package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Message;

import java.util.List;

public interface MessageDao {
    int deleteByPrimaryKey(Integer messageId);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer messageId);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> query(Message record);

}