package com.gsafety.socket.service.serviceimpl.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2018/1/25.
 */
public class LockExtend {
    private  String userId;
    private List<String> lockList=new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLockList() {
        return lockList;
    }

    public void setLockList(List<String> lockList) {
        this.lockList = lockList;
    }
}
