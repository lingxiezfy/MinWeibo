package com.fy.real.min.weibo.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fy.real.min.weibo.model.base.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * [Create]
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserUpdateRequest extends BaseRequest {
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bir;
    private String faceBase64;
}
