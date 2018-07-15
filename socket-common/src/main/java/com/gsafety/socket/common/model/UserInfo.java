package com.gsafety.socket.common.model;

import com.gsafety.socket.common.enums.RoleType;

/**
 * Created by zhengyali on 2017/9/20.
 * 用户注册socket 模型
 */
public class UserInfo {
    /**
     * 用户Id
     **/
    private String userId;

    /**
     * 用户名称
     **/
    private String userName;

    /**
     * 用户状态  0为空闲 1为忙碌
     **/
    private String status;

    /**
     * 用户角色Id
     **/
    private String roleId;

    /**
     * 用户角色类型
     **/
    private RoleType roleType;

    /**
     * 中心编码
     **/
    private String centerCode;

    /**
     * SocketClient IP
     */
    private String collectIp;

    /**
     * SocketServer IP
     */
    private String socketServerIp;

    /**
     * SocketClient ID
     */
    private String socketClientId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public String getCollectIp() {
        return collectIp;
    }

    public void setCollectIp(String collectIp) {
        this.collectIp = collectIp;
    }

    public String getSocketServerIp() {
        return socketServerIp;
    }

    public void setSocketServerIp(String socketServerIp) {
        this.socketServerIp = socketServerIp;
    }

    public String getSocketClientId() {
        return socketClientId;
    }

    public void setSocketClientId(String socketClientId) {
        this.socketClientId = socketClientId;
    }
}
