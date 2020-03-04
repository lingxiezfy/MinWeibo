package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationUserRequest extends BaseRequest {
    // 目标用户
    @NotNull(message = "目标用户必须")
    @Min(value = 1,message = "目标用户不正确")
    private Integer userId;
    // 0：解除关系 1：关注，2：拉黑
    @NotNull(message = "操作关系必须")
    @Range(min = 0,max = 2,message = "操作关系不正确")
    private Integer relation;

}
