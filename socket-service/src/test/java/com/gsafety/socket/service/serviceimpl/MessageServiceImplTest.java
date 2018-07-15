//package com.gsafety.socket.service.serviceimpl;
//
//import com.gsafety.socket.notice.contract.model.NoticeMessage;
//import com.gsafety.socket.notice.contract.model.SocketClient;
//import com.gsafety.socket.notice.manager.SocketManager;
//import org.junit.Assert;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Matchers.booleanThat;
//import static org.mockito.Mockito.reset;
//import static org.mockito.Mockito.when;
//
///**
// * Created by zhengyali on 2017/9/9.
// */
//public class MessageServiceImplTest {
//
//
//    @InjectMocks
//    private MessageServiceImpl messageService;
//
//    @Mock
//    private SocketManager socketManager;
//
//    @BeforeMethod
//    public void beforeMethod() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @AfterMethod
//    public void afterMethod() {
//        reset(socketManager);
//    }
//
//    @Test
//    public void testSendMessageOne() throws Exception {
//        boolean data=true;
//        NoticeMessage messageInfo=new NoticeMessage();
//        when(socketManager.sendMessage(messageInfo)).thenReturn(data);
//        boolean result=messageService.sendMessageOne(messageInfo);
//        Assert.assertEquals(result,true);
//    }
//    @Test
//    public void testSendMessageALl() throws Exception {
//        List<NoticeMessage> messageInfoList=new ArrayList<>();
//        NoticeMessage messageInfo=new NoticeMessage();
//        messageInfoList.add(messageInfo);
//        boolean data=true;
//        when(socketManager.sendMessage(messageInfo)).thenReturn(data);
//        boolean result=messageService.sendMessageAll(messageInfoList);
//        Assert.assertEquals(result,true);
//    }
//
//}