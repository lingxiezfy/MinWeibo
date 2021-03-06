package com.fy.real.min.weibo.model.exception;

import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * [Create]
 * Description: 基础异常
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WeiBoException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;

    public WeiBoException(String code,String message) {
        super(message);
        this.code = code;
    }

    public WeiBoException(String message) {
        super(message);
        this.code = ResponseCodeEnum.Response_600.getCode();
    }

    public WeiBoException(ResponseCodeEnum responseCode){
        this(responseCode.getCode(),responseCode.getMessage());
    }

    public WeiBoException(ResponseCodeEnum responseCode, String message) {
        this(responseCode.getCode(), message);
    }

}
