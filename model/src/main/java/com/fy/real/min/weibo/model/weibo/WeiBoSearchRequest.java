package com.fy.real.min.weibo.model.weibo;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/24 13:39 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeiBoSearchRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    @NotBlank(message = "请输入要查找的内容")
    private String query;
}
