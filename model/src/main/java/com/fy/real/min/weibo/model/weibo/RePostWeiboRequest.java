package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RePostWeiboRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "请指定转发的微博")
    @Min(value = 1,message = "指定的微博有误")
    private Integer weiBoId;
    private String content;
}
