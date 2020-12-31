package org.example.job.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.job.commons.SystemResponse;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 接口代理，主要用于对全局异常的捕捉和接口耗时
 *
 * @author zhuxj
 * @since 2020/12/15
 */
@Aspect
@Component
@RestControllerAdvice
public class ControllerAOP {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

    @Pointcut("execution(public org.example.job.commons.SystemResponse org.example.job.controller..*.*(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public SystemResponse<?> around(ProceedingJoinPoint pjp) {

        long startTimestamp = System.currentTimeMillis();
        try {
            SystemResponse<?> response = (SystemResponse<?>) pjp.proceed();
            logger.info("接口{}耗时：{}ms", pjp.getSignature(), System.currentTimeMillis() - startTimestamp);
            return response;
        } catch (Throwable e) {
            logger.error("接口{}异常", pjp.getSignature(), e);
            if (e instanceof SystemException) {
                throw ((SystemException) e);
            } else {
                throw SystemException.newException(SystemError.SERVER_ERROR, e);
            }
        }
    }

    /**
     * 自定义包装异常
     *
     * @param e 异常
     * @return RespResult
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SystemException.class)
    public SystemResponse<?> parameterMissingExceptionHandler(SystemException e) {
        logger.error("服务器内部错误", e);
        return SystemResponse.error(e);
    }


    /**
     * 请求参数异常处理
     *
     * @param e 异常
     * @return SystemResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public SystemResponse<?> parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
        logger.error("请求参数异常", e);
        return SystemResponse.error(SystemException.newException(SystemError.PARAMETER_ERROR, "请求参数异常"));
    }

    /**
     * 缺少请求体异常处理
     *
     * @param e 缺少请求体异常
     * @return SystemResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public SystemResponse<?> parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
        logger.error("请求体参数异常", e);
        return SystemResponse.error(SystemException.newException(SystemError.PARAMETER_ERROR, "请求体参数异常"));
    }

    /**
     * 请求方法不支持异常处理
     *
     * @param e 缺少请求体异常
     * @return SystemResponse
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public SystemResponse<?> parameterBodyMissingExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("请求方法不支持", e);
        return SystemResponse.error(SystemException.newException(SystemError.PARAMETER_ERROR, "请求方法不支持"));
    }

    /**
     * 参数效验异常处理器
     *
     * @param e 参数验证异常
     * @return ResponseInfo
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SystemResponse<?> parameterExceptionHandler(MethodArgumentNotValidException e) {
        logger.error("参数效验异常", e);
        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        String errorMsg = "";
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                errorMsg = errors.get(0).getDefaultMessage();
            }
        }
        return SystemResponse.error(SystemException.newException(SystemError.PARAMETER_ERROR, errorMsg));
    }


}
