package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Relation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelationDao {
    int deleteByPrimaryKey(Integer relationId);

    int insert(Relation record);

    int insertSelective(Relation record);

    Relation selectByPrimaryKey(Integer relationId);

    int updateByPrimaryKeySelective(Relation record);

    int updateByPrimaryKey(Relation record);

    List<Relation> queryUserRelation(@Param("userId") Integer userId,@Param("followUserId") Integer followUserId);
}