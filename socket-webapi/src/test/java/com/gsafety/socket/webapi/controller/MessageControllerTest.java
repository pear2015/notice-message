package com.gsafety.socket.webapi.controller;

import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.notice.contract.model.NoticeMessage;
import com.gsafety.socket.service.serviceimpl.MessageServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by zhengyali on 2017/9/9.
 */
public class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageServiceImpl messageService;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void afterMethod() {
        reset(messageService);
    }

    @Test
    public void testSendMessageToUser() throws Exception {
        NoticeMessage messageInfo=new NoticeMessage();
        messageInfo.setReceiverId("pear");
        messageInfo.setEvent("chatEvent");
        messageInfo.setReceiverRoleType(RoleType.ANALYST);
        boolean data=true;
        when(messageService.sendMessageOne(messageInfo)).thenReturn(data);
        ResponseEntity<Boolean> result=messageController.sendMessageToUser(messageInfo);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(result.getBody());
    }
    @Test
    public void testSendMessage1() throws Exception {
        NoticeMessage messageInfo=new NoticeMessage();
        boolean data=true;
        when(messageService.sendMessageOne(messageInfo)).thenReturn(data);
        ResponseEntity<Boolean> result=messageController.sendMessageToUser(messageInfo);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),false);
    }

    @Test
    public void testSendMessageAll() throws Exception {
        List<NoticeMessage> list=new ArrayList<>();
        NoticeMessage messageInfo=new NoticeMessage();
        messageInfo.setReceiverId("pear");
        messageInfo.setEvent("chatEvent");
        list.add(messageInfo);
        boolean data=true;
        when(messageService.sendMessageAll(list)).thenReturn(data);
        ResponseEntity<Boolean> result=messageController.sendMessageAll(list);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),true);
    }
    @Test
    public void testSendMessageAll1() throws Exception {
        List<NoticeMessage> list=new ArrayList<>();
        boolean data=true;
        when(messageService.sendMessageAll(list)).thenReturn(data);
        ResponseEntity<Boolean> result=messageController.sendMessageAll(list);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(),false);
    }

}