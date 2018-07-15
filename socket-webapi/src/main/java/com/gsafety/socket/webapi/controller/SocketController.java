package com.gsafety.socket.webapi.controller;

import com.gsafety.socket.common.annotation.LimitIPRequestAnnotation;
import com.gsafety.socket.common.configs.HttpError;
import com.gsafety.socket.common.enums.RoleType;
import com.gsafety.socket.common.model.UserInfo;
import com.gsafety.socket.service.serviceimpl.SocketServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhengyali on 2017/9/8.
 * 获取socket信息
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@Api(value = "/api/v1", description = "Sample socket Api")
public class SocketController {
    @Autowired
    private SocketServiceImpl socketService;

    /**
     * 获取注册登录的用户列表（通过角色和服务类型）
     * @param roleType roleId角色
     * @return
     */
    @ApiOperation(value = "socket And Socket controller", notes = "通过角色类型获取注册登录的用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/socket/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<UserInfo>> getUserList(@ApiParam(value = "角色类型", required = false) @RequestParam(required = false) RoleType roleType) {
        return new ResponseEntity<>(socketService.getAllUserList(roleType), HttpStatus.OK);
    }


    /**
     * 通过角色类型和中心编码获取注册登录的用户列表
     * @param roleType
     * @param centerCode
     * @return
     */
    @ApiOperation(value = "socket And Socket controller", notes = "通过角色类型和中心编码获取注册登录的用户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/socket/getAll/{roleType}/{centerCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<List<UserInfo>> getUserListByRoleTypeAndCenterCode(@PathVariable(required = true) RoleType roleType,@PathVariable(required = true) String  centerCode) {
        return new ResponseEntity<>(socketService.getUserListByRoleTypeAndCenterCode(roleType,centerCode), HttpStatus.OK);
    }

    /**
     * 添加事件监听
     * @param eventList
     * @return
     */
    @ApiOperation(value = "socket And Socket controller", notes = "添加事件监听")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/socket/event/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> addEventListener(@RequestBody List<String> eventList) {
        if (CollectionUtils.isEmpty(eventList)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        boolean result = socketService.addEventListener(eventList);
        return new ResponseEntity<>(result, result?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 检测SocketClient Status
     * @param socketClientId
     * @return
     */
    @ApiOperation(value = "socket And Socket controller", notes = "检测SocketClient Status")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/socket/client/status/{socketClientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> checkSocketClientStatus(@PathVariable(required = true) String socketClientId) {
        return new ResponseEntity<>(socketService.checkSocketClientStatus(socketClientId), HttpStatus.OK);
    }
    /**
     * 检测Redis Status
     * @param
     * @return
     */
    @ApiOperation(value = "socket And Socket controller", notes = "检测Redis Status")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/socket/redis/status/{socketClientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> checkRedisStatus(@PathVariable(required = true) String socketClientId) {
        return new ResponseEntity<>(socketService.checkRedisStatus(socketClientId), HttpStatus.OK);
    }

}
