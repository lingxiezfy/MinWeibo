package com.fy.real.min.weibo.model.weibo;

import com.alibaba.fastjson.JSON;
import com.fy.real.min.weibo.model.entity.Weibo;
import com.fy.real.min.weibo.model.user.UserView;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * [Create]
 * Description: 微博展示元素 单项
 * <br/>Date: 2020/2/21 0:16 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class WeiBoViewItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 微博id
     */
    private Integer weiboId;
    /**
     * 用户id
     */
    private Integer userId;

    private UserView author;

    /**
     * 发送时间
     */
    private String postTime;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 附图列表
     */
    public List<String> pic;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 点赞数
     */
    private Integer likesCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 转发数
     */
    private Integer repostCount;

    /**
     * 是否原创（1是，0否）
     */
    private Integer original;

    /**
     * 转发来源微博Id
     */
    private Integer repostId;

    private Boolean delete;

    private Integer discussionId = 0;
    private Boolean discussionAlive = false;

    /**
     * 转发的微博单项
     */
    private WeiBoViewItem repostWeiBo;

    public static WeiBoViewItem covertFromWeiBo(Weibo weibo){
        if(weibo == null){
            return null;
        }
        WeiBoViewItem view = JSON.parseObject(JSON.toJSONString(weibo),WeiBoViewItem.class);
        view.setPic(weibo.getPic() != null && weibo.getPic().length() > 0 ? JSON.parseArray(weibo.getPic(), String.class) : new ArrayList<>());
        view.setDelete(weibo.getUseful() != 1);
        return view;
    }
}
