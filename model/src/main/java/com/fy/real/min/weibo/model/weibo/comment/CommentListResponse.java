package com.fy.real.min.weibo.model.weibo.comment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 */
@Data
public class CommentListResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CommentViewItem> list;

    private long totalCount;

    private int totalPage;

    private int pageIndex;
}
