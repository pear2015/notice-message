package com.gsafety.socket.webapi.controller;

import com.gsafety.socket.service.serviceimpl.LockServiceImpl;
import com.gsafety.socket.service.serviceimpl.model.LockExtend;
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

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by zhengyali on 2018/2/22.
 */
public class LockControllerTest {
    @InjectMocks
    private LockController lockController;
    @Mock
    private LockServiceImpl lockService;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void afterMethod() {
        reset(lockService);
    }

    @Test
    public void testRedisLock() throws Exception {
        String userId = null;
        String key = null;
        ResponseEntity<Boolean> result = lockController.redisLock(key, userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }

    @Test
    public void testRedisLock1() throws Exception {
        String  userId = "1";
        String key = null;
        ResponseEntity<Boolean> result = lockController.redisLock(key, userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }

    @Test
    public void testRedisLock2() throws Exception {
        String userId = "1";
        String key = "1";
        when(lockService.redisLock(userId, key)).thenReturn(true);
        ResponseEntity<Boolean> result = lockController.redisLock(key, userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), true);
    }

    @Test
    public void testIsHasLock() throws Exception {
        String userId = "1";
        String key = "1";
        when(lockService.isCanLock(userId, key)).thenReturn(true);
        ResponseEntity<Boolean> result = lockController.isHasLock(key, userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), true);
    }
    @Test
    public void testIsHasLock1() throws Exception {
        String userId = null;
        String key = null;
        ResponseEntity<Boolean> result = lockController.isHasLock(key, userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }

    @Test
    public void testIsValidLock() throws Exception {
        LockExtend lockExtend=new LockExtend();
        ResponseEntity<List<String>> result = lockController.isValidLock(lockExtend);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsValidLock1() throws Exception {
        LockExtend lockExtend=new LockExtend();
        List<String>list=new ArrayList<>();
        list.add("1");
        lockExtend.setLockList(list);
        lockExtend.setUserId("1");
        List<String>listData=new ArrayList<>();
        when(lockService.isValidLock(lockExtend)).thenReturn(listData);
        ResponseEntity<List<String>> result = lockController.isValidLock(lockExtend);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
    }

    @Test
    public void testRedisReleaseLock() throws Exception {
        String key = null;
        ResponseEntity<Boolean> result = lockController.redisReleaseLock(key,null);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }
    @Test
    public void testRedisReleaseLock1() throws Exception {
        String key = "1";
        String userId = "1";
        when(lockService.redisReleaseLockByUserId(key,userId)).thenReturn(true);
        ResponseEntity<Boolean> result = lockController.redisReleaseLock(key,userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), true);
    }
    @Test
    public void testRedisReleaseListLock() throws Exception {
        List<String> keyList=new ArrayList<>();
        ResponseEntity<Boolean> result = lockController.redisReleaseListLock(keyList);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }
    @Test
    public void testRedisReleaseListLock1() throws Exception {
        List<String> keyList=new ArrayList<>();
        keyList.add("1");
        when(lockService.redisReleaseListLock(keyList)).thenReturn(true);
        ResponseEntity<Boolean> result = lockController.redisReleaseListLock(keyList);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), true);
    }
    @Test
    public void testRedisReleaseListLockByUser() throws Exception {
        List<String> keyList=new ArrayList<>();
        String userId=null;
        ResponseEntity<Boolean> result = lockController.redisReleaseListLockByUser(keyList,userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), false);
    }
    @Test
    public void testRedisReleaseListLockByUser1() throws Exception {
        List<String> keyList=new ArrayList<>();
        keyList.add("1");
        String userId="1";
        when(lockService.redisReleaseListLockAndUserId(keyList,userId)).thenReturn(true);
        ResponseEntity<Boolean> result = lockController.redisReleaseListLockByUser(keyList,userId);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getBody(), true);
    }
}