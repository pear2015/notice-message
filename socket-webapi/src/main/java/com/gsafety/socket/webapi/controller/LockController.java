package com.gsafety.socket.webapi.controller;

/**
 * Created by zhengyali on 2018/1/24.
 */

import com.gsafety.socket.common.annotation.LimitIPRequestAnnotation;
import com.gsafety.socket.common.configs.HttpError;
import com.gsafety.socket.service.serviceimpl.LockServiceImpl;
import com.gsafety.socket.service.serviceimpl.model.LockExtend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * redis 分布式锁
 * Created by zhengyali on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
//Api swagger注解
@Api(value = "/api/v1", description = "Lock socket Api")
public class LockController {
    @Autowired
    private LockServiceImpl lockService;

    /**
     * 回填或合并的犯罪人加锁
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "redis加锁")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/lock/{userId}/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> redisLock(@PathVariable(required = true) String key, @PathVariable(required = true) String userId) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(userId)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(lockService.redisLock(userId, key), HttpStatus.OK);
    }

    /**
     * redis是否能够进行加锁
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "redis是否能够进行加锁")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/isCanLock/{userId}/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> isHasLock(@PathVariable(required = true) String key, @PathVariable(required = true) String userId) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(userId)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(lockService.isCanLock(userId, key), HttpStatus.OK);
    }

    /**
     * 锁 数据验证(公告提交时)
     * 验证通过进行批量更新锁
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "锁验证")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/validate/lock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<String>> isValidLock(@RequestBody LockExtend lockExtend) {
        if (lockExtend != null && !CollectionUtils.isEmpty(lockExtend.getLockList()) && StringUtils.isNotBlank(lockExtend.getUserId())) {
            return new ResponseEntity<>(lockService.isValidLock(lockExtend), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    /**
     * redis解锁
     *
     * @param key
     * @return
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "redis解锁")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/release/{key}/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> redisReleaseLock(@PathVariable(required = true) String key,@PathVariable(required = true)String userId) {
        if (StringUtils.isEmpty(key)|| StringUtils.isEmpty(userId)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(lockService.redisReleaseLockByUserId(key,userId), HttpStatus.OK);
    }

    /**
     * 批量解锁
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "redis批量解锁")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/release/list/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> redisReleaseListLock(@RequestBody List<String> keyList) {
        if (CollectionUtils.isEmpty(keyList)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(lockService.redisReleaseListLock(keyList), HttpStatus.OK);
    }
    /**
     * 批量解锁
     */
    @ApiOperation(value = "Lock And Socket controller", notes = "根据人来redis批量解锁")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/release/list/delete/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> redisReleaseListLockByUser(@RequestBody List<String> keyList,@PathVariable(required = true)String userId) {
        if (CollectionUtils.isEmpty(keyList)||StringUtils.isEmpty(userId)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(lockService.redisReleaseListLockAndUserId(keyList,userId), HttpStatus.OK);
    }
}
