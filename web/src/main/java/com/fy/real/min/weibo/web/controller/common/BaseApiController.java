package com.fy.real.min.weibo.web.controller.common;

import com.fy.real.min.weibo.model.base.BaseResponse;
import com.fy.real.min.weibo.model.enums.ResponseCodeEnum;
import com.fy.real.min.weibo.model.exception.WeiBoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 0:33 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public class BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(BaseApiController.class);
    protected  <T,R> BaseResponse<R> exec(T request, Function<T,R> function) {
        BaseResponse<R> response = new BaseResponse<>();
        try {
            //执行方法
            R result = function.apply(request);
            response.setResult(result);
        } catch (WeiBoException wbe) {
            logger.warn(String.format("[%s] %s",wbe.getCode(),wbe.getMessage()));
            response.setResult(null);
            response.setSuccess(false);
            response.setCode(wbe.getCode());
            response.setMessage(wbe.getMessage());
        } catch (Exception e) {
            logger.error("未捕获异常",e);
            response.setSuccess(false);
            response.setResult(null);
            response.setResponse(ResponseCodeEnum.Response_500);
        }
        return response;
    }
}
