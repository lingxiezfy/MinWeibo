package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/29 7:41 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WeiBoActionRequest extends BaseRequest {
    private Integer weiBoId;
}
