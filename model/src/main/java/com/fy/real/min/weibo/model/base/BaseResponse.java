package com.fy.real.min.weibo.model.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
@Data
public class BaseResponse<T> implements Serializable {
    private boolean success;
    private String code;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date serverTime = new Date();
    private T result;

    private static final long serialVersionUID = 1L;

    public BaseResponse() {
        this(ResponseCodeEnum.Response_200);
        setSuccess(true);
    }

    public BaseResponse(ResponseCodeEnum responseCodeEnum) {
        setCode(responseCodeEnum.getCode());
        setMessage(responseCodeEnum.getMessage());
    }

    public void setResponse(ResponseCodeEnum responseCodeEnum){
        setCode(responseCodeEnum.getCode());
        setMessage(responseCodeEnum.getMessage());
    }
}
