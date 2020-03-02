package com.fy.real.min.weibo.model.message;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/2 18:30 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendGroupMessageRequest extends BaseRequest {
    @NotNull(message = "请选择组发送消息")
    private Integer discussionId;
    @NotBlank(message = "操作类型必须")
    private String op;
    @NotBlank(message = "消息不可为空")
    private String message;
}
