package com.fy.real.min.weibo.model.enums;

/**
 * [Create]
 * Description: 响应码
 * @version 1.0
 */
public enum ResponseCodeEnum {
    Response_200("200","成功")
    ,Response_300("300","参数无效")
    ,Response_301("301","未登录或登录已过期")
    ,Response_500("500","服务器错误")
    ,Response_600("600","操作不允许")
    ;
    private String code;
    private String message;

    ResponseCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
