package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/7 18:55 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserListRequest extends BaseRequest {
    private Integer pageIndex = 1;
    private Integer pageSize = 20;
    // asc,desc
    private String sortType;
    // 排序字段
    private String sortField;
}
