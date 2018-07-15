package com.gsafety.socket.notice.contract.model;

import com.gsafety.socket.common.enums.MessageType;
import com.gsafety.socket.common.enums.RoleType;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;

/**
 * 通知消息
 * Created by MyGirl on 2017/8/21.
 */
public class NoticeMessage {
    /**
     * id
     */
    private String id;

    /**
     * 消息体
     */
    private Object content;

    /**
     * 创建的时间
     */
    private Date time;
    /**
     * 当地时区时间
     */
    private String zoneTime;
    /**
     * 发送人
     */
    private String senderId;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 接收人
     */
    private String receiverId;

    /**
     * 接收人角色
     */
    private RoleType receiverRoleType;

    /**
     * 消息类型
     */
    private MessageType messageType;


    /**
     * 订阅的事件名称
     */
    private String event;

    public boolean checkDataOnSendMessage() {
        if (StringUtils.isEmpty(getReceiverId()) || StringUtils.isEmpty(getEvent()) || getReceiverRoleType() == null) {
            return true;
        }
        return false;
    }

    /**
     * Gets event.
     *
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * Sets event.
     *
     * @param event the event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Gets sender id.
     *
     * @return the sender id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Sets sender id.
     *
     * @param senderId the sender id
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * Gets receiver id.
     *
     * @return the receiver id
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * Sets receiver id.
     *
     * @param receiverId the receiver id
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public RoleType getReceiverRoleType() {
        return receiverRoleType;
    }

    public void setReceiverRoleType(RoleType receiverRoleType) {
        this.receiverRoleType = receiverRoleType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getZoneTime() {
        return zoneTime;
    }

    public void setZoneTime(String zoneTime) {
        this.zoneTime = zoneTime;
    }
}
