package com.fy.real.min.weibo.web.config;

import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
//@Configuration
public class WebSocketConfig {

//    @Bean
    public ServerEndpointExporter serverEndpointExporter () {
        return new ServerEndpointExporter();
    }
}
