package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Relation;

public interface RelationDao {
    int deleteByPrimaryKey(Integer relationId);

    int insert(Relation record);

    int insertSelective(Relation record);

    Relation selectByPrimaryKey(Integer relationId);

    int updateByPrimaryKeySelective(Relation record);

    int updateByPrimaryKey(Relation record);
}