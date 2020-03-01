package com.fy.real.min.weibo.model.weibo.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/1 21:29 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class CommentListResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CommentViewItem> list;

    private long totalCount;

    private int totalPage;

    private int pageIndex;
}
