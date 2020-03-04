package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoRequest extends BaseRequest {
    private Integer targetUserId;
}
