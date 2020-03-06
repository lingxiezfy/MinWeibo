package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [Create]
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequestDto extends User {
    private String oldPassword;
    private User currentUser;
}
