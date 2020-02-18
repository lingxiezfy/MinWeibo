package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Weibo;

public interface WeiboDao {
    int deleteByPrimaryKey(Integer weiboId);

    int insert(Weibo record);

    int insertSelective(Weibo record);

    Weibo selectByPrimaryKey(Integer weiboId);

    int updateByPrimaryKeySelective(Weibo record);

    int updateByPrimaryKey(Weibo record);
}