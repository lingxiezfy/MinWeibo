package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/27 0:33 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSearchRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 20;
    private String query;
}
