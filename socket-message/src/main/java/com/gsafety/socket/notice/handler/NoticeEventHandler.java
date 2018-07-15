package com.gsafety.socket.notice.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.common.util.HelpUtil;
import com.gsafety.socket.notice.manager.SocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Created by MyGirl on 2017/8/21.
 */
@Component
public class NoticeEventHandler {
    private Logger log = LoggerFactory.getLogger(NoticeEventHandler.class);

    @Value("${server.port}")
    private String port;

    @Value("${server.context-path}")
    private String contextPath;

    private SocketManager socketManager;

    /**
     * Instantiates a new Notice event handler.
     *
     * @param socketManager the socketManager
     */
    @Autowired
    public NoticeEventHandler(@Qualifier("socketManager") final SocketManager socketManager) {
        this.socketManager = socketManager;
    }

    /**
     * On connect.客户端建立连接
     * 传输参数
     * userId（用户Id）
     * status（用户忙碌状态 0为空闲 1为忙碌）
     * userType（用户类型）
     * serverType(服务类型) 必填
     *
     * @param client the client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        //连接的时候可以把用户信息传递到服务端
        log.info("Client[{}] is connected. Client address is [{}].", client.getSessionId(), client.getRemoteAddress());
    }

    /**
     * 客户端socket注册或修改用户空闲状态修改
     * 客户端必须传角色类型 不传可注册但不能发消息
     */
    @OnEvent(value = "register")
    public void onRegister(SocketIOClient client, UserInfo userInfo) {
        String clientIp = client.getHandshakeData().getHttpHeaders().get("X-Real-IP");
        if(clientIp == null || "".equals(clientIp)){
            clientIp = client.getRemoteAddress().toString();
            clientIp = clientIp.substring(1, clientIp.lastIndexOf(':'));
        }
        userInfo.setCollectIp(clientIp);
        userInfo.setSocketClientId(client.getSessionId().toString());
        userInfo.setSocketServerIp(HelpUtil.getServerUrl(port,contextPath));
        socketManager.register(userInfo);
    }

    /**
     * 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
     *
     * @param client the client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        //失去连接的时候移除用户信息
        socketManager.removeRegisterUser(client.getSessionId().toString());
        log.info("Client[{}] is disconnected.", client.getSessionId());
    }

}
