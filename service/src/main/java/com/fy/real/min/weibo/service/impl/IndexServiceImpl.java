package com.fy.real.min.weibo.service.impl;

import com.fy.real.min.weibo.dao.dao.AdminDao;
import com.fy.real.min.weibo.model.entity.Admin;
import com.fy.real.min.weibo.service.IIndexService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [Create]
 * Description:
 */
@Service
public class IndexServiceImpl implements IIndexService {
    @Autowired
    AdminDao adminDao;
    @Override
    public Admin testDb() {
        PageHelper.startPage(2,2);
        List<Admin> all = adminDao.getAll();
        return all.get(0);
    }
}
