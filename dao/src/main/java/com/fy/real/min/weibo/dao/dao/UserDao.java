package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsername(String username);

    /**
     * [Create]
     * Description: 操作微量
     * <br/> Date: 2020/2/23 21:51
     * <br/>
     */
    void updateCountColumn(User user);

    List<User> selectByUserIdList(@Param("userIdList") List<Integer> userIdList);
}