package com.gsafety.socket.service.serviceimpl;
import com.gsafety.socket.notice.contract.model.NoticeMessage;
import com.gsafety.socket.notice.manager.SocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by zhengyali on 2017/9/8.
 */
@Service
public class MessageServiceImpl {
    @Autowired
    private SocketManager socketManager;
    @Value("${server.dateType}")
    private String dateType;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * （单个） 发送消息
     * 判断redis是否存在当前用户
     */
    public boolean sendMessageOne(NoticeMessage noticeMessage) {
        noticeMessage.setTime(new Date());
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone(dateType));
        noticeMessage.setZoneTime(dateFormat.format(new Date()));
        noticeMessage.setId(UUID.randomUUID().toString());
        return socketManager.sendMessage(noticeMessage);
    }

    /**
     * 给(多个用户)发送消息
     */
    public boolean sendMessageAll(List<NoticeMessage> messageInfo) {
        messageInfo.forEach(message -> {
            logger.info("socket send one message to ", message.getReceiverId());
            sendMessageOne(message);
        });
        return true;
    }

}
