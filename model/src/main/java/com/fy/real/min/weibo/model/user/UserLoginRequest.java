package com.fy.real.min.weibo.model.user;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {
    @NotBlank(message = "用户名必填")
    private String username;
    @NotBlank(message = "密码必填")
    private String password;

    private static final long serialVersionUID = 1L;
}
