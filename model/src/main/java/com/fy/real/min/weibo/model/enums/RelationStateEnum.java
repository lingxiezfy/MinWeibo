package com.fy.real.min.weibo.model.enums;

/**
 * [Create]
 * Description:
 * @version 1.0
 */
public enum RelationStateEnum {
    Default(0,"陌生或本人")
    ,Concern(1,"关注")
    ,Black(2,"拉黑")
    ;

    private Integer code;
    private String message;

    RelationStateEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
