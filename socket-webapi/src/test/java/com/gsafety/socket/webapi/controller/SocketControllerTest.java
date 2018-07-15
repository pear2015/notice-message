package com.gsafety.socket.webapi.controller;

import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.service.serviceimpl.SocketServiceImpl;
import com.gsafety.socket.service.serviceimpl.model.User;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by zhengyali on 2017/9/9.
 */
public class SocketControllerTest {
    @InjectMocks
    private  SocketController socketController;
    @Mock
    private SocketServiceImpl socketService;
    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void afterMethod() {
        reset(socketService);
    }

    @Test
    public void testGetUserList() throws Exception {
        UserInfo searchInfo =new UserInfo();
        List<UserInfo> userList =new ArrayList<>();
        when(socketService.getAllUserList(searchInfo.getRoleType())).thenReturn(userList);
        ResponseEntity<List<UserInfo>> result=socketController.getUserList(null);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
    }
    @Test
    public void testGetUserList1() throws Exception {
        UserInfo searchInfo =new UserInfo();
        List<UserInfo> userList =new ArrayList<>();
        when(socketService.getAllUserList(searchInfo.getRoleType())).thenReturn(userList);
        ResponseEntity<List<UserInfo>> result=socketController.getUserList(RoleType.ANALYST);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().size()==0);
    }
    @Test
    public void testAddEventListener() throws Exception {
        List<String> eventList=new ArrayList<>();
        eventList.add("chatEvent");
        boolean data=true;
        when(socketService.addEventListener(eventList)).thenReturn(data);
        ResponseEntity<Boolean> result=socketController.addEventListener(eventList);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),true);
    }

    @Test
    public void testAddEventListener1() throws Exception {
        List<String> eventList=new ArrayList<>();
        boolean data=true;
        when(socketService.addEventListener(eventList)).thenReturn(data);
        ResponseEntity<Boolean> result=socketController.addEventListener(eventList);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),false);
    }
    @Test
    public void testGetUserListByRoleTypeAndCenterCode() throws Exception {
        List<UserInfo>userInfoList=new ArrayList<>();
        when(socketService.getUserListByRoleTypeAndCenterCode(any(),anyString())).thenReturn(userInfoList);
        ResponseEntity<List<UserInfo>> result=socketController.getUserListByRoleTypeAndCenterCode(any(),anyString());
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody().size()==0);
    }
    @Test
    public void testCheckSocketClientStatus() throws Exception {
        boolean data=true;
        String socketClientId=null;
        when(socketService.checkSocketClientStatus(socketClientId)).thenReturn(data);
        ResponseEntity<Boolean> result=socketController.checkSocketClientStatus(socketClientId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),true);
    }
}