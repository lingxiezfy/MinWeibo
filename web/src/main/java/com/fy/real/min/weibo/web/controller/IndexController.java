package com.fy.real.min.weibo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@RestController
public class IndexController {

    @GetMapping("/testDb")
    public String testDb(){
        return "OK";
    }
}
