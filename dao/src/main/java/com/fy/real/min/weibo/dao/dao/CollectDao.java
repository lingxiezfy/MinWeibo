package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Collect;

public interface CollectDao {
    int deleteByPrimaryKey(Integer collectId);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer collectId);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);
}