package com.fy.real.min.weibo.model.message;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * [Create]
 * Description: 系统通知
 * <br/>Date: 2020/3/8 16:27 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemNoticeRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "公告内容不能为空")
    private String content;
}
