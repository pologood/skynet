package org.shieldproject.usercenter.exception;

/**
 * 业务异常
 * @author kezhijie
 * @date 2018/12/6 9:50
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
