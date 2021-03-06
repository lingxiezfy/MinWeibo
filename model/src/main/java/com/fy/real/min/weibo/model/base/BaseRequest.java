package com.fy.real.min.weibo.model.base;

import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private User currentUser;
}
