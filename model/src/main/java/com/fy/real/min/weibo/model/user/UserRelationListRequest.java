package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * [Create]
 * Description: 用户关系列表
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRelationListRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 20;
    // 查找的用户，0代表当前登录用户
    private Integer targetUserId = 0;
    // 是否被动关系 0,主动（关注，拉黑）；1,被动（被关注:粉丝，被拉黑）
    private Integer byFollow = 0;
    // 关系1，关注（被关注），2拉黑（被拉黑）
    @NotNull(message = "需指定关系类型")
    @Range(min = 1, max = 2,message = "关系类型不正确")
    private Integer relationState;
}
