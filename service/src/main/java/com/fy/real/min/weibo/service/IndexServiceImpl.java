package com.fy.real.min.weibo.service;

import com.fy.real.min.weibo.dao.dao.AdminDao;
import com.fy.real.min.weibo.model.entity.Admin;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/18 17:35 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService{
    @Autowired
    AdminDao adminDao;
    @Override
    public Admin testDb() {
        PageHelper.startPage(2,2);
        List<Admin> all = adminDao.getAll();
        return all.get(0);
    }
}
