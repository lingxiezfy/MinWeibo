package com.fy.real.min.weibo.dao.dao;

import com.fy.real.min.weibo.model.entity.Admin;

import java.util.List;

public interface AdminDao {
    int deleteByPrimaryKey(Integer adminId);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer adminId);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);

    List<Admin> getAll();
}