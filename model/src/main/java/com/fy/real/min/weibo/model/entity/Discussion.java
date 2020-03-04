package com.fy.real.min.weibo.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * discussion
 */
@Data
public class Discussion implements Serializable {
    /**
     * 讨论组Id
     */
    private Integer discussionId;

    /**
     * 群主id
     */
    private Integer userId;

    /**
     * 关联的微博id
     */
    private Integer weiboId;

    /**
     * 存活时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date aliveTime;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}