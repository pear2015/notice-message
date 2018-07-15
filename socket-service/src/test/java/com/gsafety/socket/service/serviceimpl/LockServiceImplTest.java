package com.gsafety.socket.service.serviceimpl;

import com.gsafety.socket.notice.contract.model.Lock;
import com.gsafety.socket.notice.distributed.DistributedLock;
import com.gsafety.socket.service.serviceimpl.model.LockExtend;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.CollectionUtils;
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
public class LockServiceImplTest {
    @InjectMocks
    private LockServiceImpl lockService;

    @Mock
    private DistributedLock distributedLock;

    @BeforeMethod
    public void beforeMethod() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void afterMethod() {
        reset(distributedLock);
    }

    @Test
    public void testRedisLock() throws Exception {
        Lock lock = new Lock();
        lock.setUserId("1");
        lock.setLockName("1");
        when(distributedLock.tryLock(lock)).thenReturn(false);
        boolean result=lockService.redisLock("1","1");
        Assert.assertEquals(result,false);
    }

    @Test
    public void testRedisReleaseLock() throws Exception {
        when(distributedLock.unlock("1")).thenReturn(true);
        boolean result=lockService.redisReleaseLock("1");
        Assert.assertEquals(result,true);
    }

    @Test
    public void testIsCanLock() throws Exception {
        when(distributedLock.isCanLock("1","1")).thenReturn(true);
        boolean result=lockService.isCanLock("1","1");
        Assert.assertEquals(result,true);
    }

    @Test
    public void testRedisReleaseListLock() throws Exception {
        List<String> keyList=new ArrayList<>();
        keyList.add("1");
        when(distributedLock.unlock("1")).thenReturn(true);
        boolean result=lockService.redisReleaseListLock(keyList);
        Assert.assertEquals(result,true);
    }

    @Test
    public void testIsValidLock() throws Exception {
        LockExtend lockExtend=new LockExtend();
        List<String> keyList=new ArrayList<>();
        lockExtend.setUserId("1");
        keyList.add("1");
        lockExtend.setLockList(keyList);
        when(distributedLock.isCanLock(lockExtend.getUserId(), "1")).thenReturn(true);
        List<String> result=lockService.isValidLock(lockExtend);
        Assert.assertEquals(CollectionUtils.isEmpty(result),true);
    }

}