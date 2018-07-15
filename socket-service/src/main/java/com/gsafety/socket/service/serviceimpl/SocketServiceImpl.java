package com.gsafety.socket.service.serviceimpl;

import com.corundumstudio.socketio.SocketIOClient;
import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.notice.manager.SocketEventListenerManager;
import com.gsafety.socket.notice.manager.SocketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhengyali on 2017/9/2.
 */
@Service
public class SocketServiceImpl {
    @Autowired
    private SocketManager socketManager;
    @Autowired
    private SocketEventListenerManager socketEventListenerManager;

    /**
     * 根据搜索条件获取空闲的socket
     * 分三种
     * 第一种 状态
     * 第二中 角色+状态
     * 第三中 角色列表+状态
     */
    public List<UserInfo> getAllUserList(RoleType roleType) {
        if(roleType != null){
            return socketManager.getRegisterUserByRoleType(roleType.name());
        }else{
            return socketManager.getAllRegisterUsers();
        }
    }

    /**
     * 添加监听
     */
    public boolean addEventListener(List<String> eventListenerList) {
        return socketEventListenerManager.initSocketEventListener(eventListenerList);
    }

    /***
     * 根据角色和中心编码
     * @param roleType
     * @param centerCode
     * @return
     */
    public List<UserInfo> getUserListByRoleTypeAndCenterCode(RoleType roleType, String centerCode) {
        return socketManager.getRegisterUserByRoleTypeAndCenterCode(roleType, centerCode);
    }

    /**
     * 检测socket状态
     * @param socketClientId
     * @return
     */
    public boolean checkSocketClientStatus(String socketClientId){
        SocketIOClient socketClient = socketManager.getSocketClient(socketClientId);
        if(socketClient != null){
            return true;
        }
        return false;
    }

    /**
     *  检测 redis的状态
     * @return
     */
    public boolean checkRedisStatus(String socketClientId) {
        SocketIOClient socketClient = socketManager.getSocketClient(socketClientId);
        if(socketClient != null){
            return socketManager.checkRedisStatus(socketClientId);
        }
        return false;
    }
}
