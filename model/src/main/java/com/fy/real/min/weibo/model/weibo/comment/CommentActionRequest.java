package com.fy.real.min.weibo.model.weibo.comment;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 5:55 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentActionRequest extends BaseRequest {
    @NotNull(message = "必须指定评论操作")
    private Integer commentId;
}
