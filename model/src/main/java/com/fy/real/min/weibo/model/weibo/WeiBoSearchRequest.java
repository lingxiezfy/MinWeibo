package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeiBoSearchRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    private String query;
    private String topic;
    private Integer weiBoId;
}
