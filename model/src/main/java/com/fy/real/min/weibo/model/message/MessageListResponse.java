package com.fy.real.min.weibo.model.message;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/8 17:24 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class MessageListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<MessageViewItem> list;

    private long totalCount;

    private int totalPage;

    private int pageIndex;
}
