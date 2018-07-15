package com.gsafety.socket.notice.manager;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketIOServer;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.notice.contract.model.NoticeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by qianqi on 2017/8/22.
 */
@Service
public class SocketEventListenerManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SocketIOServer server;
    private SocketManager socketManager;

    /**
     * 初始化构造
     * @param server
     * @param socketManager
     */
    public SocketEventListenerManager(SocketIOServer server, SocketManager socketManager) {
        this.server = server;
        this.socketManager = socketManager;
    }

    /**
     * 初始化 SocketEventListenerManager
     * 通过api接口添加事件监听
     **/
    public boolean initSocketEventListener(List<String> eventListeners) {

        if (CollectionUtils.isEmpty(eventListeners))
            return false;
        for (String event : eventListeners) {
            if (event != null) {
                server.removeAllListeners(event);
                server.addEventListener(event, Object.class, (client, data, ackSender) -> {
                    String json = JSON.toJSONString(data);
                    NoticeMessage msg = JSON.parseObject(json, NoticeMessage.class);
                    msg.setId(UUID.randomUUID().toString());
                    msg.setEvent(event);
                    msg.setTime(new Date());

                    UserInfo sendUser = socketManager.getRegisterUserBySocketClientId(client.getSessionId().toString());
                    msg.setSenderId(sendUser != null ? sendUser.getUserId() : "");
                    msg.setSenderName(sendUser != null ? sendUser.getUserName() : "");
                    socketManager.sendMessage(msg);
                    logger.info("Send Message: {}", JSON.toJSON(msg));
                });
            }
        }
        return true;
    }

}
