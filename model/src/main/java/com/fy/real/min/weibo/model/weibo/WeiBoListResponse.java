package com.fy.real.min.weibo.model.weibo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/24 13:42 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class WeiBoListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<WeiBoViewItem> list;

    private long totalCount;

    private int totalPage;

    private int pageIndex;
}
