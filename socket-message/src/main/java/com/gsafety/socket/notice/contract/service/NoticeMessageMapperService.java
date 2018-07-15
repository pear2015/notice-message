package com.gsafety.socket.notice.contract.service;

/**
 * Created by qianqi on 2017/8/22.
 *
 * @param <TMessage> the type parameter
 */
public interface NoticeMessageMapperService<TMessage> {


    /**
     * Message to object object.
     *
     * @param message the message
     * @return the object
     */
    Object messageToObject(TMessage message);

    /**
     * Object to message t message.
     *
     * @param message the message
     * @return the t message
     */
    TMessage objectToMessage(Object message);
}
