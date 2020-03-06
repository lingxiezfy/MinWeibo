package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeiBoListRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    //指定请求某个用户的微博列表,-1时表示请求所有微博，0：请求自己的微博，大于0：请求相应用户的微博
    private Integer targetUserId = 0;
}
