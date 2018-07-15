package com.gsafety.socket.webapi.controller;

/**
 * Created by zhengyali on 2017/8/31.
 */

import com.gsafety.socket.common.annotation.LimitIPRequestAnnotation;
import com.gsafety.socket.common.configs.HttpError;
import com.gsafety.socket.notice.contract.model.NoticeMessage;
import com.gsafety.socket.service.serviceimpl.MessageServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息转发
 * Created by zhengyali on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
//Api swagger注解
@Api(value = "/api/v1", description = "Sample socket Api")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    /**
     * 给单个用户发送消息
     * 判断接收人Id 、接收人角色和消息事件不可为空
     * @param messageInfo
     * @return
     */
    @ApiOperation(value = "message And Message controller", notes = "给单个用户发送消息")
    //ApiResponses swagger相应注解
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/message/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> sendMessageToUser(@RequestBody NoticeMessage messageInfo) {
        if (messageInfo.checkDataOnSendMessage()) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        boolean result = messageService.sendMessageOne(messageInfo);
        return new ResponseEntity<>(result, result?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 给多个用户发送送消息
     **/
    //ApiOperation swagger操作注解
    @ApiOperation(value = "message And Message controller", notes = "给多个用户发送消息")
    //ApiResponses swagger相应注解
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error", response = HttpError.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = HttpError.class)})
    @RequestMapping(value = "/message/sendAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @LimitIPRequestAnnotation(limitCounts = 500, timeSecond = 1000)
    public ResponseEntity<Boolean> sendMessageAll(@RequestBody List<NoticeMessage> messageInfoList) {
        if (CollectionUtils.isEmpty(messageInfoList)) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        boolean result = messageService.sendMessageAll(messageInfoList);
        return new ResponseEntity<>(result, result?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    

}
