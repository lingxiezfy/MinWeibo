package com.fy.real.min.weibo.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan(basePackages = "com.fy.real.min.weibo.dao",sqlSessionFactoryRef = "sqlSessionFactory")
@ComponentScan(basePackages = "com.fy.real.min.weibo")
@SpringBootApplication
public class MinWeiboApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinWeiboApplication.class, args);
	}

}
