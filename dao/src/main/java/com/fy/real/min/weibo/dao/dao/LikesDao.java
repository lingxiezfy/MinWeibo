package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Likes;

public interface LikesDao {
    int deleteByPrimaryKey(Integer likesId);

    int insert(Likes record);

    int insertSelective(Likes record);

    Likes selectByPrimaryKey(Integer likesId);

    int updateByPrimaryKeySelective(Likes record);

    int updateByPrimaryKey(Likes record);
}