package com.gsafety.socket.common.enums;

/**
 * Created by zhengyali on 2017/10/17.
 * 消息类型
 */
public enum MessageType {

    /**
     * 个人申请分析员首次申请
     */
    PERSON_APPLY_ANALYST(0),

    /**
     * 个人申请分析员处理驳回申请
     */
    PERSON_APPLY_AUDITOR_REFUSE(1),

    /**
     * 个人申请审核员审核分派
     */
    PERSON_APPLY_AUDITOR_START(2),
    /**
     * 个人申请审核通过
     */
    PERSON_APPLY_END(3),
    /**
     * 政府申请分派审核员
     */
    GOVERNMENT_APPLY_AUDITOR_START(4),
    /**
     * 政府申请 分析员处理驳回
     */
    GOVERNMENT_AUDITOR_REFUSE(5),

    /**
     * 政府申请结束
     */
    GOVERMENT_AUDITOR_END(6),
    /**
     * 个人申请待打印
     */

    PERSON_APPLY_READY_PRINT(7),

    /**
     * 政府申请待打印
     */
    GOVERMENT_APPLY_READY_PRINT(8),
     /**
     * 公告审核分派审核员
     */
    NOTICE_APPLY_AUDITOR_START(9),
    /**
     * 公告审核驳回
     */
    NOTICE_AUDITOR_REFUSE(10),
    /**
     * 公告审核结束
     */
    NOTICE_AUDITOR_END(11);
    private int paramType;


    MessageType(int paramType) {

        this.paramType = paramType;
    }

    /**
     * Get param type int.
     *
     * @return the int
     */
    public int getParamType() {
        return this.paramType;
    }
}
