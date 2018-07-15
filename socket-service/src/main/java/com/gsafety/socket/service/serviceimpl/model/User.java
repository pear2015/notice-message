package com.gsafety.socket.service.serviceimpl.model;

/**
 * Created by zhengyali on 2017/9/1.
 * 用戶列表
 */
public class User {
    private String userId;
    private String status;
    private String roleId;

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
}