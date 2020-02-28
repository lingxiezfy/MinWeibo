package com.fy.real.min.weibo.model.enums;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/28 14:48 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public enum  RelationEnum {
    Default(0,"陌生或本人")
    ,Concern(1,"关注")
    ,Black(2,"拉黑")
    ;

    private Integer code;
    private String message;

    RelationEnum(Integer code, String message) {
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
