package com.fy.real.min.weibo.util.utils;

import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * Description: 使用 javax.validation.Validator 对字段进行校验
 *
 * Create Date Time: 2019/7/23 20:08
 */
public class ValidatorUtil {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * 校验默认组
     * @param obj 校验对象
     * @param <T> 泛型
     * @return true / throw Exception
     */
    public static <T> boolean validate(T obj) {
        filterNull(obj);
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        return buildError(set);
    }

    /**
     * 分组校验
     * @param obj 校验对象
     * @param groups 校验组
     * @param <T> 泛型
     * @return true / throw Exception
     */
    public static <T> boolean validate(T obj, Class<?>... groups) {
        filterNull(obj);
        Set<ConstraintViolation<T>> set = validator.validate(obj, groups);
        return buildError(set);
    }

    /**
     * 校验字段列表
     * @param set 字段列表
     * @param <T> 泛型类
     * @return true or throw Exception
     */
    private static <T> boolean buildError(Set<ConstraintViolation<T>> set) {
        if (set != null && set.size() > 0) {
            for (ConstraintViolation<T> cv : set) {
                throw new WeiBoException(ResponseCodeEnum.Response_300.getCode(),cv.getMessage());
            }
        }
        return true;
    }

    private static <T> void filterNull(T obj){
        if(obj == null){
            throw new WeiBoException(ResponseCodeEnum.Response_300);
        }
    }
}
