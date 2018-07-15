
package com.gsafety.socket.notice.distributed;

import com.gsafety.socket.notice.contract.model.Lock;
import com.gsafety.socket.notice.manager.SocketManager;
import com.gsafety.socket.notice.redis.JedisUtils;
import com.gsafety.springboot.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


/**
 * redis实现的distributedlock ,锁占用时间不宜过长
 *
 * @author wyzssw
 * @seehttp://www.jeffkit.info/2011/07/1000/
 */
@Service
public class DistributedLock {
    //单位秒
    @Value("${jedis.pool.config.inValidTime}")
    private int inValidTime;
    private Logger logger = LoggerFactory.getLogger(SocketManager.class);

    /**
     * redis 加锁
     *
     * @param lock 锁对象
     * @return 1. get(key) 是否为空
     * a 为空 进行加锁
     * b 不为空 判断是否失效
     * 失效的话重新加锁
     * 没有失效的判断是否同一个人加的锁,同一个人加的锁更新失效时间
     */
    public boolean tryLock(Lock lock) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = JedisUtils.getJedis();
            String curLockTimeStr = jedis.get(lock.getLockName());
            // get(key)不为空 更新加锁
            if (StringUtils.isNotBlank(curLockTimeStr)) {
                result = lockExpireTime(jedis, curLockTimeStr, lock);
            } else if (jedis.setnx(lock.getLockName(), JsonUtil.toJson(lock)) == 1) {
                jedis.expire(lock.getLockName(), inValidTime);
                result = true;
                logger.info("lock->this key has  lock success", lock.getLockName());
            }
        } catch (Exception e) {
            logger.error("lock->tryLock->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    /**
     * 当前key是否存在
     * 存在 判断当前key是否失效
     * * 失效后才可重新加锁
     * * 没有失效时 判断是否是同一个人加的锁,是同一个人,更新失效时间
     *
     * @param curLockTimeStr
     * @param lock
     * @return
     */
    private boolean lockExpireTime(Jedis jedis, String curLockTimeStr, Lock lock) {
        try {
            Lock lockOld = JsonUtil.fromJson(curLockTimeStr, Lock.class);
            //此锁已经失效 需要重新加锁
            if (System.currentTimeMillis() > Long.valueOf(lockOld.getInvalidTime()) || lockOld.getUserId().equals(lock.getUserId())) {
                jedis.del(lockOld.getLockName());
                logger.info("lock->this key  need to reply lock", lock.getLockName());
                boolean result = jedis.setnx(lock.getLockName(), JsonUtil.toJson(lock)) == 1;
                jedis.expire(lock.getLockName(), inValidTime);
                return result;

            } else {
                logger.info("lock->this key  is not invalid, this key has locked from other user", lock.getLockName());
                return false;
            }
        } catch (Exception e) {
            logger.error("lock->lockExpireTime->error:{}", e);
            return false;
        }
    }

    /**
     * 判断当前key是否能够加锁
     *
     * @param lockName
     * @return
     */
    public boolean isCanLock(String userId, String lockName) {
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = JedisUtils.getJedis();
            String curLockTimeStr = jedis.get(lockName);
            Lock lockOld = JsonUtil.fromJson(curLockTimeStr, Lock.class);
            // get(key)不为空 未加锁
            if (StringUtils.isBlank(curLockTimeStr)) {
                result = true;
            } else if (System.currentTimeMillis() > Long.valueOf(lockOld.getInvalidTime()) || lockOld.getUserId().equals(userId)) {
                result = true;
                logger.info("lock->this key has  lock success", lockName);
            }
        } catch (Exception e) {
            logger.error("lock->tryLock->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    /**
     * 解锁
     *
     * @param key
     */
    public boolean unlock(String key) {
        boolean result = true;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            String curLockTimeStr = jedis.get(key);
            if (StringUtils.isBlank(curLockTimeStr)) {
                jedis.close();
                return true;
            }
            jedis.del(key);
        } catch (Exception e) {
            logger.error("lock->this key to unlock has Exception->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    /**
     * 根据人来解锁
     * @param key
     * @param userId
     * @return
     */
    public boolean unlockByUserId(String key, String userId) {
        boolean result = true;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            String curLockTimeStr = jedis.get(key);
            if (StringUtils.isBlank(curLockTimeStr)) {
                jedis.close();
                return true;
            }
            Lock lockOld = JsonUtil.fromJson(curLockTimeStr, Lock.class);
            if (lockOld != null && lockOld.getUserId().equals(userId)) {
                jedis.del(key);
            }
        } catch (Exception e) {
            logger.error("lock->this key to unlock has Exception->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }
}
