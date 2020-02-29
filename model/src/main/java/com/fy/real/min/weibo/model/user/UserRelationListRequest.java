package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
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
public class UserRelationListRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 20;
    @NotNull(message = "需指定关系类型")
    @Range(min = 1, max = 2,message = "关系类型不正确")
    private Integer relationState;
}
