package com.fy.real.min.weibo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 11:12 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}
