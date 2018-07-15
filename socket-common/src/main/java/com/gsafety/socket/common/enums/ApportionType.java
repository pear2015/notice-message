package com.gsafety.socket.common.enums;

/**
 * Created by qianqi on 2017/8/25.
 * 分派类型
 */
public enum ApportionType {

    /**
     * 出生证明审核
     */
    BIRTHCERTIFICATEAUDIT(0);

    private int paramType;

    ApportionType(int paramType) {
        this.paramType = paramType;
    }

    /**
     * Gets param type.
     *
     * @return the param type
     */
    public int getParamType() {
        return paramType;
    }

}
