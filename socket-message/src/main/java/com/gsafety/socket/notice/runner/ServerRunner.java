package com.gsafety.socket.notice.runner;

import com.corundumstudio.socketio.SocketIOServer;
import com.gsafety.socket.notice.NotifyConstant;
import com.gsafety.socket.notice.manager.SocketEventListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyGirl on 2017/8/21.
 */
@Component
public class ServerRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final SocketIOServer server;

    private SocketEventListenerManager socketEventListenerManager;

    /**
     * Instantiates a new Server runner.
     *
     * @param server the server
     */
    @Autowired
    public ServerRunner(SocketIOServer server, SocketEventListenerManager socketEventListenerManager) {
        this.server = server;
        this.socketEventListenerManager = socketEventListenerManager;
    }

    /**
     * 服务启动
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            //Socket Server绑定事件
            List<String> eventList = new ArrayList<>();
            eventList.add(NotifyConstant.NOTICE_EVENT);
            eventList.add(NotifyConstant.MESSAGE_EVENT);
            socketEventListenerManager.initSocketEventListener(eventList);
            server.start();
        } catch (Exception e) {
            logger.error("Exception:socket start has cell Exception", e);
        }
    }
}