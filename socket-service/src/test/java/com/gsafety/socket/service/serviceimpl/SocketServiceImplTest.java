package com.gsafety.socket.service.serviceimpl;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.protocol.Packet;
import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.notice.contract.model.SocketClient;
import com.gsafety.socket.notice.manager.SocketEventListenerManager;
import com.gsafety.socket.notice.manager.SocketManager;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by zhengyali on 2017/9/9.
 */
public class SocketServiceImplTest {


    @InjectMocks
    private SocketServiceImpl socketService;

    @Mock
    private SocketManager socketManager;
    @Mock
    private  SocketIOClient socketIOClient;
    @Mock
    private SocketEventListenerManager socketEventListenerManager;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void afterMethod() {
        reset(socketManager);
        reset(socketIOClient);
        reset(socketEventListenerManager);
    }
    @Test
    public void testGetAllUserList() throws Exception {
        List<UserInfo> userInfoList=new ArrayList<>();
        when(socketManager.getAllRegisterUsers()).thenReturn(userInfoList);
        userInfoList = socketService.getAllUserList(null);
        Assert.assertTrue(userInfoList.size() == 0);

        List<UserInfo> userInfoList1=new ArrayList<>();
        when(socketManager.getRegisterUserByRoleType(any())).thenReturn(userInfoList);
        userInfoList1 = socketService.getAllUserList(RoleType.ANALYST);
        Assert.assertTrue(userInfoList1.size() == 0);
    }

    @Test
    public void testAddEventListener() throws Exception {
        List<String> eventList = new ArrayList<>();
        eventList.add(anyObject());
        boolean data = true;
        when(socketEventListenerManager.initSocketEventListener(eventList)).thenReturn(data);
        boolean result = socketService.addEventListener(eventList);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetUserListByRoleTypeAndCenterCode() throws Exception {
        List<UserInfo> userInfoList=new ArrayList<>();
        when(socketManager.getRegisterUserByRoleTypeAndCenterCode(any(),anyString())).thenReturn(userInfoList);
        userInfoList = socketService.getUserListByRoleTypeAndCenterCode(any(),anyString());
        Assert.assertTrue(userInfoList.size() == 0);
    }

    @Test
    public void testCheckSocketClientStatus() throws Exception {
        SocketIOClient socketClient=null;
        when(socketManager.getSocketClient(anyString())).thenReturn(socketClient);
        boolean  data= socketService.checkSocketClientStatus(anyString());
        Assert.assertEquals(data,false);

    }
    @Test
    public void testCheckSocketClientStatus1() throws Exception {
        when(socketManager.getSocketClient(anyString())).thenReturn(socketIOClient);
        boolean  data= socketService.checkSocketClientStatus("1");
        Assert.assertTrue(data);

    }
}
