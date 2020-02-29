package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectDao {
    int deleteByPrimaryKey(Integer collectId);

    int insert(Collect record);

    int insertSelective(Collect record);

    Collect selectByPrimaryKey(Integer collectId);

    int updateByPrimaryKeySelective(Collect record);

    int updateByPrimaryKey(Collect record);

    Collect selectByUserAndWeiBo(@Param("userId") Integer userId,@Param("weiBoId") Integer weiBoId);

    List<Collect> queryByUserId(@Param("userId") Integer userId);
}