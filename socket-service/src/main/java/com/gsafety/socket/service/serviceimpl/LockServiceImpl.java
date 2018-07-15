package com.gsafety.socket.service.serviceimpl;

import com.gsafety.socket.notice.contract.model.Lock;
import com.gsafety.socket.notice.distributed.DistributedLock;
import com.gsafety.socket.service.serviceimpl.model.LockExtend;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengyali on 2018/1/24.
 */
@Service
public class LockServiceImpl {
    @Autowired
    private DistributedLock distributedLock;
    //单位毫秒
    @Value("${jedis.pool.config.lockTimeOut}")
    private long lockTimeOut;

    /**
     * 加锁
     *
     * @param lockName
     * @return
     */
    public boolean redisLock(String userId, String lockName) {
        Lock lock = new Lock();
        lock.setUserId(userId);
        lock.setLockName(lockName);
        lock.setInvalidTime(System.currentTimeMillis() + lockTimeOut + 1);
        return distributedLock.tryLock(lock);
    }

    /**
     * 解锁
     *
     * @param lockName
     * @return
     */
    public boolean redisReleaseLock(String lockName) {
        return distributedLock.unlock(lockName);
    }

    /**
     * 判断是否加锁
     *
     * @param key
     * @return
     */
    public boolean isCanLock(String userId, String key) {
        return distributedLock.isCanLock(userId, key);
    }

    /**
     * 批量解锁
     *
     * @param keyList
     * @return
     */
    public boolean redisReleaseListLock(List<String> keyList) {
            keyList.forEach(s -> {
                if (StringUtils.isNotBlank(s)) {
                    distributedLock.unlock(s);
                }
            });
            return true;
    }

    /**
     * @param lockExtend
     * @return
     */
    public List<String> isValidLock(LockExtend lockExtend) {
        List<String> result = new ArrayList<>();
        List<String> keyList = lockExtend.getLockList();
        keyList.forEach(s -> {
            if (StringUtils.isNotBlank(s) && !distributedLock.isCanLock(lockExtend.getUserId(), s)) {
                result.add(s);
            }

        });
        //查询过程中 都是可加锁的数据 进行批量更新锁
        if (CollectionUtils.isEmpty(result)) {
            this.lockList(keyList, lockExtend.getUserId());
        }
        return result;
    }

    /**
     * 数据批量加锁
     * 此处可能存在并发
     */
    private boolean lockList(List<String> keyList, String userId) {
        keyList.forEach(s -> {
            if (StringUtils.isNotBlank(s) ) {
                this.redisLock(s, userId);
            }
        });
        return true;
    }

    /**
     * 根据人来解锁
     * @param key
     * @param userId
     * @return
     */
    public boolean redisReleaseLockByUserId(String key, String userId) {
      return distributedLock.unlockByUserId(key,userId);
    }

    /**
     * 根据人来批量解锁 用于作废的勾选
     * @param keyList
     * @param userId
     * @return
     */
    public boolean redisReleaseListLockAndUserId(List<String> keyList, String userId) {
        keyList.forEach(s -> {
            if (StringUtils.isNotBlank(s) ) {
                this.redisReleaseLockByUserId(s, userId);
            }
        });
        return true;
    }
}
