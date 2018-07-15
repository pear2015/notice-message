package com.gsafety.socket.notice.manager;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.common.util.HelpUtil;
import com.gsafety.socket.notice.NotifyConstant;
import com.gsafety.socket.notice.contract.model.NoticeMessage;
import com.gsafety.socket.notice.redis.JedisUtils;
import com.gsafety.springboot.common.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by qianqi on 2017/8/22.
 */
@Service
public class SocketManager {

    private Logger logger = LoggerFactory.getLogger(SocketManager.class);

    private static final String REGISTER_USERS = "register_users";
    @Value("${server.port}")
    private String port;

    @Value("${server.context-path}")
    private String contextPath;
    @Value("${server.send_msg_uri}")
    private String sendMsgUri;

    @Value("${server.client_status_uri}")
    private String clientStatusUri;

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Autowired
    private SocketIOServer socketServer;

    /**
     * 根据SocketClient ID获取SocketIOClient对象
     *
     * @param socketClientId UUID格式
     * @return
     */
    public SocketIOClient getSocketClient(String socketClientId) {
        try {
            return this.socketServer.getClient(UUID.fromString(socketClientId));
        } catch (Exception e) {
            logger.error("Get SocketClient error.{}", e);
            return null;
        }
    }

    /**
     * 用户登录注册
     * 1 检查是否有脏数据
     *  1、有脏数据
     *   a、检查是否是连接成功的数据
     *   如果是连接成功 发送消息(同一个Ip 提示你的账户重复登录,不同账户提示你的Ip在别处登录)
     *   b、移除脏数据
     *2
     * @param userInfo 用户信息
     */
    public void register(UserInfo userInfo) {
        logger.info("Save Register User{} to Redis.", userInfo.getSocketClientId());
        // 1.检查是否有脏数据
        UserInfo oldUser = this.getRegisterUserByUserIdAndRoleType(userInfo.getUserId(), userInfo.getRoleType().name());
        if (oldUser != null) {
            SocketIOClient socketIOClient = this.getSocketClient(oldUser.getSocketClientId());
            if (socketIOClient != null) {
                this.removeRegisterUser(oldUser.getSocketClientId());
                socketIOClient.sendEvent(NotifyConstant.LEAVE_EVENT,userInfo.getCollectIp());
            }
        }
        // 2.保存新登录用户
        this.saveRedisUser(userInfo);
        // 3.接收离线消息
        this.receiveOfflineMessage(userInfo.getUserId(), userInfo.getRoleType().name());
    }

    /**
     * 发送消息
     *
     * @param noticeMessage
     * @return
     */
    public boolean sendMessage(NoticeMessage noticeMessage) {
        logger.info("Receive Notice Message: {}", noticeMessage);
        if (noticeMessage == null || noticeMessage.getReceiverRoleType() == null) {
            return false;
        }
        //获取接收人
        UserInfo receiveUser = this.getRegisterUserByUserIdAndRoleType(noticeMessage.getReceiverId(), noticeMessage.getReceiverRoleType().name());
        if (receiveUser != null) {
            logger.info("Receive User({}) is on Local Server, Send Message by SocketClient.", receiveUser);
            return this.sendMessageByReceiverIdNotNull(receiveUser, noticeMessage);
        } else {
            logger.info("Receive User is offline, Save Offline Message.");
            //接收人未登录则保存离线消息
            return this.saveOfflineMessage(noticeMessage);
        }
    }

    /**
     * 接收人不为空 发送消息
     */
    private boolean sendMessageByReceiverIdNotNull(UserInfo receiveUser, NoticeMessage noticeMessage) {
        if (HelpUtil.getServerUrl(port,contextPath).equals(receiveUser.getSocketServerIp())) {
            SocketIOClient socketClient = this.getSocketClient(receiveUser.getSocketClientId());
            if (socketClient != null) {
                socketClient.sendEvent(noticeMessage.getEvent(), noticeMessage);
                return true;
            } else {
                logger.info("Receive User on Local Server is offline, Save Offline Message.");
                return this.saveOfflineMessage(noticeMessage);
            }
        } else {
            logger.info("Receive User is on Remote Server, Send Message by Restful Interface.");
            //调用远端SocketServer发送消息
            return this.sendMessage2Remote(receiveUser.getSocketServerIp(), noticeMessage);
        }
    }

    /**
     * 调用远端SocketServer发送消息
     *
     * @param remoteUrl     远端地址
     * @param noticeMessage 消息对象
     * @return
     */
    public boolean sendMessage2Remote(String remoteUrl, NoticeMessage noticeMessage) {
        boolean result;
        try {
            result = httpClientUtil.httpPostReturnTypeRef(remoteUrl + sendMsgUri, noticeMessage, new TypeReference<Boolean>() {
            }, false);
        } catch (Exception e) {
            logger.error("send message to {} failed. Exception: {}", remoteUrl, e);
            result = false;
        }
        if (!result) {
            //发送失败则保存离线消息
            result = this.saveOfflineMessage(noticeMessage);
        }
        return result;
    }

    /**
     * 保存离线消息到Redis
     *
     * @param noticeMessage 消息对象
     */
    public boolean saveOfflineMessage(NoticeMessage noticeMessage) {
        Jedis jedis = null;
        boolean result = true;
        try {
            jedis = JedisUtils.getJedis();
            String offlineMsgKey = NotifyConstant.REDIS_OFFLINE_MSG_KEY + noticeMessage.getReceiverId() + "_"
                    + noticeMessage.getReceiverRoleType();
            jedis.lpush(offlineMsgKey, JSON.toJSONString(noticeMessage));
        } catch (Exception e) {
            logger.error("SocketManager->saveOfflineMessage->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    /**
     * 保存新的redis用户
     */
    public boolean saveRedisUser(UserInfo userInfo) {
        Jedis jedis = null;
        boolean result = true;
        try {
            Map<String, String> userMap = new HashMap<>();
            userMap.put(userInfo.getSocketClientId(), JSON.toJSONString(userInfo));
            jedis = JedisUtils.getJedis();
            jedis.hmset(REGISTER_USERS, userMap);
        } catch (Exception e) {
            logger.error("SocketManager->saveRedisUser->error:{}", e);
            result = false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return result;
    }

    /**
     * 接收Redis离线消息
     *
     * @param userId 接收人ID
     */
    public void receiveOfflineMessage(String userId, String roleType) {
        Jedis jedis = null;
        try {
            String offlineMsgKey = NotifyConstant.REDIS_OFFLINE_MSG_KEY + userId + "_" + roleType;
            // 判断是否有离线消息，如果有则进行接收
            jedis = JedisUtils.getJedis();
            while (jedis.llen(offlineMsgKey) > 0) {
                String msg = jedis.rpop(offlineMsgKey);
                this.sendMessage(JSON.parseObject(msg, NoticeMessage.class));
            }
        } catch (Exception e) {
            logger.error("SocketManager->receiveOfflineMessage->error:{}", e);
        }
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 获取所有注册登录的Socket用户
     *
     * @return
     */
    public List<UserInfo> getAllRegisterUsers() {
        List<UserInfo> userInfoList = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            Map<String, String> userMap = jedis.hgetAll(REGISTER_USERS);
            for (String userInfoStr : userMap.values()) {
                UserInfo user = JSON.parseObject(userInfoStr, UserInfo.class);
                if (this.checkRegisterUserStatus(user)) {
                    userInfoList.add(user);
                }
            }
        } catch (Exception e) {
            logger.error("SocketManager->getAllRegisterUsers->error:{}", e);
        }
        if (jedis != null) {
            jedis.close();
        }
        return userInfoList;
    }

    /**
     * 检测注册登录用户是否正常
     *
     * @param user
     * @return
     */
    private boolean checkRegisterUserStatus(UserInfo user) {
        boolean result = true;
        // 用户的SocketClient在本机
        if (HelpUtil.getServerUrl(port,contextPath).equals(user.getSocketServerIp())) {
            if (this.getSocketClient(user.getSocketClientId()) == null) {
                this.removeRegisterUser(user.getSocketClientId());
                result = false;
            }
        } else {  //用户的SocketClient在远端
            // 如果用户所登录SocketServerIp为空，则删除该用户
            if (StringUtils.isEmpty(user.getSocketServerIp())) {
                this.removeRegisterUser(user.getSocketClientId());
                result = false;
            } else {
                result= resolveUser(user);
            }
        }
        return result;
    }

    /**
     * SocketServerIp不为空，判断当前用户是否建立连接 未连接移除掉
     */
    private boolean resolveUser(UserInfo user) {
        try {
            String clientStatusURL = user.getSocketServerIp() + clientStatusUri + user.getSocketClientId();
            boolean isOK = httpClientUtil.httpGetWithType(clientStatusURL, new TypeReference<Boolean>() {
            });
            if (!isOK) {
                this.removeRegisterUser(user.getSocketClientId());
                return false;
            }
        } catch (Exception e) {
            logger.error("Get user[{}] of remote[{}] error:{}", user.getUserId(), user.getSocketServerIp(), e);
            this.removeRegisterUser(user.getSocketClientId());
            return false;
        }
        return true;
    }

    /**
     * 根据SocketClientId获取用户信息
     *
     * @param socketClientId
     * @return
     */
    public UserInfo getRegisterUserBySocketClientId(String socketClientId) {
        UserInfo userInfo = new UserInfo();
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            String userInfoStr = jedis.hget(REGISTER_USERS, socketClientId);
            if (!StringUtils.isEmpty(userInfoStr)) {
                userInfo = JSON.parseObject(userInfoStr, UserInfo.class);
            }
        } catch (Exception e) {
            logger.error("SocketManager->getRegisterUserBySocketClientId->error: {}", e);
        }
        if (jedis != null) {
            jedis.close();
        }
        return userInfo;
    }

    /**
     * 根据用户Id获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getRegisterUserByUserId(String userId) {
        List<UserInfo> allUsers = this.getAllRegisterUsers();
        for (UserInfo user : allUsers) {
            if (userId.equals(user.getUserId())) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据用户角色获取用户信息集合
     *
     * @param roleType
     * @return
     */
    public List<UserInfo> getRegisterUserByRoleType(String roleType) {
        return this.getAllRegisterUsers().stream().filter(user -> roleType.equals(user.getRoleType().name())).collect(Collectors.toList());
    }

    /**
     * 根据用户ID和角色获取用户信息
     *
     * @param userId   用户ID
     * @param roleType 角色
     * @return
     */
    public UserInfo getRegisterUserByUserIdAndRoleType(String userId, String roleType) {
        List<UserInfo> userInfoList = this.getAllRegisterUsers().stream().filter(user -> userId.equals(user.getUserId())
                && roleType.equals(user.getRoleType().name())).collect(Collectors.toList());
        if (userInfoList != null && !userInfoList.isEmpty()) {
            return userInfoList.get(0);
        }
        return null;
    }

    /**
     * 根据用户角色、中心编码获取用户集合
     *
     * @param roleType   角色
     * @param centerCode 中心编码
     * @return
     */
    public List<UserInfo> getRegisterUserByRoleTypeAndCenterCode(RoleType roleType, String centerCode) {
        return this.getRegisterUserByRoleType(roleType.name()).stream()
                .filter(user -> centerCode.equals(user.getCenterCode())).collect(Collectors.toList());
    }

    /**
     * 根据SocketClientId移除用户
     * 此处判断socketClientId 是否在redis中是否存在
     *
     * @param socketClientId
     */
    public void removeRegisterUser(String socketClientId) {
        if (StringUtils.isEmpty(socketClientId)) {
            logger.info("SocketClientId is null, remove RegisterUser fail.");
            return;
        }
        logger.info("Delete Register User where sessionId={}", socketClientId);
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            if(!CollectionUtils.isEmpty(jedis.hmget(REGISTER_USERS,socketClientId))){
                jedis.hdel(REGISTER_USERS, socketClientId);
            }
        } catch (Exception e) {
            logger.error("SocketManager->removeRegisterUser->error:{}", e);
        }
        if (jedis != null) {
            jedis.close();
        }
    }
    /**
     * 检测redis的状态
     */
    public boolean checkRedisStatus(String socketClientId){
        boolean result=true;
        Jedis jedis = null;
        try {
            jedis = JedisUtils.getJedis();
            jedis.hmget(REGISTER_USERS,socketClientId);
        } catch (Exception e) {
            logger.error("SocketManager->checkRedisStatus->error:{}", e);
            result=false;
        }
        if (jedis != null) {
            jedis.close();
        }
        return  result;
    }

    /**
     * 根据用户ID移除用户
     *
     * @param userId
     */
    public void removeRegisterUserByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            logger.info("UserId is null, remove RegisterUserByUserId fail.");
            return;
        }
        logger.info("Delete Register User where userId={}", userId);
        UserInfo delUser = this.getRegisterUserByUserId(userId);
        if (delUser != null && !StringUtils.isEmpty(delUser.getSocketClientId())) {
            this.removeRegisterUser(delUser.getSocketClientId());
        }
    }

}
