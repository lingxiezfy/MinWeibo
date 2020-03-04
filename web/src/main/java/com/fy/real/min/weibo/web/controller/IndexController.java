package com.fy.real.min.weibo.web.controller;

import com.fy.real.min.weibo.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@RestController
public class IndexController {

    @Autowired
    IIndexService indexService;

    @GetMapping("/testDb")
    public String testDb(){
        return indexService.testDb().getUsername();
    }
}
