package com.fy.real.min.weibo.model.weibo.comment;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CommentRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    //微博的Id
    @NotNull(message = "必须置顶需要获取评论的微博")
    @Min(value = 1,message = "指定的微博错误")
    private Integer weiBoId;
    @NotBlank(message = "回复内容不可为空")
    private String content;
}
