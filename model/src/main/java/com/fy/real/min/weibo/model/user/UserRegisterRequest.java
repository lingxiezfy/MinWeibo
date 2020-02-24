package com.fy.real.min.weibo.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:30 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {
    @NotBlank(message = "用户名必填")
    @Pattern(regexp = "[A-Za-z0-9_]{3,10}",message = "用户名3-10位由字母或数字组成")
    private String username;
    @NotBlank(message = "密码必填")
    @Pattern(regexp = "[A-Za-z0-9_]{6,18}",message = "密码由6-18位数字、字母、下划线组成")
    private String password;
    @NotBlank(message = "请填写确认密码")
    private String rePassword;
    /**
     * 昵称
     */
    @NotBlank(message = "请填写昵称")
    private String nickname;
    @NotNull(message = "请填写生日")
    @PastOrPresent(message = "穿越是不可能穿越的")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date bir;
    //(0男，1女)
    @NotNull(message = "请选择性别")
    @Range(min = 0L,max = 1L,message = "请选择性别")
    private Integer sex;

    private static final long serialVersionUID = 1L;
}
