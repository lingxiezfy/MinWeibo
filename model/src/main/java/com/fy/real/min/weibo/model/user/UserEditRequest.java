package com.fy.real.min.weibo.model.user;

import com.fy.real.min.weibo.model.base.BaseRequest;
import com.fy.real.min.weibo.model.user.groups.AddUserValidateGroup;
import com.fy.real.min.weibo.model.user.groups.DeleteUserValidateGroup;
import com.fy.real.min.weibo.model.user.groups.EditUserValidateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/3/7 17:38 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserEditRequest extends BaseRequest {
    @NotBlank(message = "用户名不能为空",groups = AddUserValidateGroup.class)
    private String username;
    @NotBlank(message = "昵称不能为空",groups = AddUserValidateGroup.class)
    private String nickname;
    @NotNull(message = "生日不能为空",groups = AddUserValidateGroup.class)
    private String bir;
    @NotNull(message = "请确认性别",groups = AddUserValidateGroup.class)
    private Integer sex;
    private Boolean admin;
    @NotBlank(message = "请输入初始密码",groups = AddUserValidateGroup.class)
    private String password;
    private String oper;
    @Min(value = 1,message = "请明确修改的用户",groups = {EditUserValidateGroup.class, DeleteUserValidateGroup.class})
    @NotNull(message = "请明确修改的用户",groups = {EditUserValidateGroup.class, DeleteUserValidateGroup.class})
    private Integer userId;
}
