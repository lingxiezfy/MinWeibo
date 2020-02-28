package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Weibo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeiboDao {
    int deleteByPrimaryKey(Integer weiboId);

    int insert(Weibo record);

    int insertSelective(Weibo record);

    Weibo selectByPrimaryKey(Integer weiboId);

    int updateByPrimaryKeySelective(Weibo record);

    int updateByPrimaryKey(Weibo record);

    List<Weibo> selectUsefulByUserId(Integer userId);

    List<Weibo> selectByWeiboIdList(@Param("weiboIdList") List<Integer> weiboIdList);

    List<Weibo> selectUseful();

    List<Weibo> query(Weibo searchWeiBo);
}