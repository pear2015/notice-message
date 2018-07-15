package com.gsafety.socket.notice.contract.model;

/**
 * Created by zhengyali on 2018/1/25.
 */
public class Lock {
    /**
     * 加锁人
     */
    private  String userId;
    /**
     * lockName 值
     */
    private String lockName;
    /**
     * 锁的失效时间
     */
    private  Long invalidTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
