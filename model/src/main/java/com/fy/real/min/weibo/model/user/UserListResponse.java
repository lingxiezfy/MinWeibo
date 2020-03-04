package com.fy.real.min.weibo.model.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class UserListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<UserView> list;

    private long totalCount;

    private int totalPage;

    private int pageIndex;
}
