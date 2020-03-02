package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Discussion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussionDao {
    int deleteByPrimaryKey(Integer discussionId);

    int insert(Discussion record);

    int insertSelective(Discussion record);

    Discussion selectByPrimaryKey(Integer discussionId);

    int updateByPrimaryKeySelective(Discussion record);

    int updateByPrimaryKey(Discussion record);

    List<Discussion> queryByWeiBoIdList(@Param("weiBoIdList") List<Integer> weiBoIdList);
}