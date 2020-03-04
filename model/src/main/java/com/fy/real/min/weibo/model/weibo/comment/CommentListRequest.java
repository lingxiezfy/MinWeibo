package com.fy.real.min.weibo.model.weibo.comment;

import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * [Create]
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentListRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageIndex = 1;
    private Integer pageSize = 10;
    //微博的Id
    @NotNull(message = "必须置顶需要获取评论的微博")
    @Min(value = 1,message = "指定的微博错误")
    private Integer weiBoId;
}
