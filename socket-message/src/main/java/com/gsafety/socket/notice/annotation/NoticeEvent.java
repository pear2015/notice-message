package com.gsafety.socket.notice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by qianqi on 2017/8/22.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoticeEvent {


    /**
     * Event string.
     *
     * @return the string
     */
    String event();

}
