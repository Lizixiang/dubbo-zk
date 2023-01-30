package com.dubbo.core.design.liability.enums;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月17日
 */
public enum HandlerEnum {

    SIGNHANDLER("signHandler", "加密处理"),
    SENSTIVEHANDLER("senstiveHandler", "敏感词处理"),
    ;

    private String className;
    private String message;

    HandlerEnum(String className, String message) {
        this.className = className;
        this.message = message;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
