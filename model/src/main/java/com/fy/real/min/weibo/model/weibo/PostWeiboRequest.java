package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 */
@Data
public class PostWeiboRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> picList;
    private User user;
    private String content;

}
