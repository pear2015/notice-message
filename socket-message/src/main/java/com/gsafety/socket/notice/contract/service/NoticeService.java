package com.gsafety.socket.notice.contract.service;

/**
 * Created by MyGirl on 2017/8/21.
 */
public interface NoticeService {


    /**
     * Send.
     *
     * @param noticeMessage the notice message
     */
    void send(Object noticeMessage);

    /**
     * Receive.
     *
     * @param noticeMessage the notice message
     */
    void receive(Object noticeMessage);

}
