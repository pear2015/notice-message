package com.gsafety.socket.backend.aspectj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xiaodm on 2016/4/26.
 */
@ControllerAdvice
public class AppWideExceptionHandle {
    private Logger log = LoggerFactory.getLogger(AppWideExceptionHandle.class);

    /**
     * modelNotFoundExceptionHandler
     *
     * @param runtimeException RuntimeException
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String modelNotFoundExceptionHandler(RuntimeException runtimeException) {
        log.error("",runtimeException);
        //前面如果throw了ex，这里return的view不会被解析
        return "Server Inner Exception";
    }
}
