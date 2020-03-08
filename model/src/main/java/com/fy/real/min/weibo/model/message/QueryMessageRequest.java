package com.fy.real.min.weibo.model.message;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/8 16:27 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryMessageRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private Integer messageType;
    private Integer messageId;
}
