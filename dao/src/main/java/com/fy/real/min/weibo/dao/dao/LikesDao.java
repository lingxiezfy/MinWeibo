package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Likes;
import org.apache.ibatis.annotations.Param;

public interface LikesDao {
    int deleteByPrimaryKey(Integer likesId);

    int insert(Likes record);

    int insertSelective(Likes record);

    Likes selectByPrimaryKey(Integer likesId);

    int updateByPrimaryKeySelective(Likes record);

    int updateByPrimaryKey(Likes record);

    Likes selectByUserAndWeiBo(@Param("userId") Integer userId, @Param("weiBoId") Integer weiBoId);
}