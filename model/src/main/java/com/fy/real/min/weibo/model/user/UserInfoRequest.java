package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/28 14:53 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoRequest extends BaseRequest {
    private Integer targetUserId;
}
