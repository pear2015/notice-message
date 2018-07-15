package com.gsafety.socket.notice.redis;

import com.gsafety.socket.notice.manager.SocketManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gaoqiang on 2016/5/12.
 */
@Component
public class JedisUtils {

    private static JedisSentinelPool jedisSentinelPool;
    private static JedisPool jedisPool;
    private static JedisUtils jedisUtils;
    private static Logger logger = LoggerFactory.getLogger(SocketManager.class);
    @Value("${jedis.pool.swarm.url}")
    private String url;

    @Value("${jedis.pool.swarm.masterName}")
    private String masterName;

    @Value("${jedis.pool.single.redisHost}")
    private String redisHost;

    @Value("${jedis.pool.single.redisPort}")
    private int redisPort;

    @Value("${jedis.isSelectSwarm}")
    private boolean isSelectSwarm;

    @Value("${jedis.pool.config.maxIdle}")
    private int maxIdle;

    @Value("${jedis.pool.config.maxTotal}")
    private int maxTotal;

    @Value("${jedis.pool.config.checkingIntervalSecs}")
    private int checkingIntervalSecs;

    @Value("${jedis.pool.config.evictableIdleTimeSecs}")
    private int evictableIdleTimeSecs;

    private JedisUtils() {
        jedisUtils = this;
    }

    /**
     * 设置
     */
    @PostConstruct
    public void init() {
        jedisUtils.url = this.url;
        jedisUtils.isSelectSwarm = this.isSelectSwarm;
        jedisUtils.maxIdle = this.maxIdle;
        jedisUtils.maxTotal = this.maxTotal;
        jedisUtils.redisHost = this.redisHost;
        jedisUtils.redisPort = this.redisPort;
        jedisUtils.checkingIntervalSecs = this.checkingIntervalSecs;
        jedisUtils.evictableIdleTimeSecs = this.evictableIdleTimeSecs;
    }



    /**
     * 从连接池获取redis连接 @return the jedis
     * 2018-2-26 加入redis集群
     * * 从哨兵获取redis连接 @return the jedis from sentinel
     *
     * @return the jedis
     */
    public static synchronized Jedis getJedis() {
        if (jedisUtils.isSelectSwarm) {
         return getJedisBySwarm();
        }else{
            return  getJedisBySingle();
        }
    }

    /**
     * swarm 集群模式
     * @return
     */
    public static synchronized Jedis getJedisBySwarm() {
        if (jedisSentinelPool == null) {
            JedisPoolConfig jedisPoolConfig = JedisUtils.createPoolConfig(jedisUtils.maxIdle, jedisUtils.maxTotal, jedisUtils.checkingIntervalSecs, jedisUtils.evictableIdleTimeSecs);
            Set<String> sentinels = new HashSet<>(Arrays.asList(StringUtils.split(jedisUtils.url, ",")));
            jedisSentinelPool = new JedisSentinelPool(jedisUtils.masterName, sentinels, jedisPoolConfig, 5000);
            HostAndPort currentHostMaster = jedisSentinelPool.getCurrentHostMaster();
            logger.info("Get current host message" + currentHostMaster.getHost() + "Get current port message" + currentHostMaster.getPort());
        }
        return jedisSentinelPool.getResource();
    }

    /**
     * 单容器模式
     * @return
     */
    public static synchronized Jedis getJedisBySingle() {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = JedisUtils.createPoolConfig(300, 1000, 300, 300);
            jedisPool = new JedisPool(poolConfig, jedisUtils.redisHost, jedisUtils.redisPort);
        }
        return jedisPool.getResource();
    }

    /**
     * 快速设置JedisPoolConfig, 不执行idle checking。
     *
     * @param maxIdle  the max idle
     * @param maxTotal the max total
     * @return the jedis pool config
     */
    public static JedisPoolConfig createPoolConfig(int maxIdle, int maxTotal) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setTimeBetweenEvictionRunsMillis(-1);
        return poolConfig;
    }

    /**
     * 快速设置JedisPoolConfig, 设置执行idle checking的间隔和可被清除的idle时间.
     * 默认的checkingIntervalSecs是30秒，可被清除时间是60秒。
     *
     * @param maxIdle               the max idle
     * @param maxTotal              the max total
     * @param checkingIntervalSecs  the checking interval secs
     * @param evictableIdleTimeSecs the evictable idle time secs
     * @return the jedis pool config
     */
    public static JedisPoolConfig createPoolConfig(int maxIdle, int maxTotal,
                                                   int checkingIntervalSecs,
                                                   int evictableIdleTimeSecs) {
        JedisPoolConfig poolConfig = createPoolConfig(maxIdle, maxTotal);

        poolConfig.setTimeBetweenEvictionRunsMillis(checkingIntervalSecs * 1000L);
        poolConfig.setMinEvictableIdleTimeMillis(evictableIdleTimeSecs * 1000L);
        return poolConfig;
    }
}
