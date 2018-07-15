package com.gsafety.socket.notice.contract.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.gsafety.socket.common.enums.RoleType;


/**
 * Created by qianqi on 2017/8/22.
 * SocketClient
 */
public class SocketClient {

    /**
     * 用户Id
     **/
    private String userId;

    /**
     * 用户名称
     * */
    private String userName;

    /**
     * 用户状态  0为空闲 1为忙碌
     **/
    private String status;


    /**
     * 用户角色Id
     * 修改为用角色列表 角色Id是变化的，无法区分
     * 暂时保留角色Id
     **/
    private String roleId;

    /**
     * 用户角色类型
     **/
    private String roleType;

    /**
     * 中心编码
     **/
    private String centerCode;
    /**
     * 连接IP
     * */

    private String collectIp;


    private SocketIOClient socketIOClient;



    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets socket io client.
     *
     * @return the socket io client
     */
    public SocketIOClient getSocketIOClient() {
        return socketIOClient;
    }

    /**
     * Sets socket io client.
     *
     * @param socketIOClient the socket io client
     */
    public void setSocketIOClient(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
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

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getCollectIp() {
        return collectIp;
    }

    public void setCollectIp(String collectIp) {
        this.collectIp = collectIp;
    }
}
