package org.shieldproject.usercenter.exception;

/**
 * @author kezhijie
 * @date 2018/12/29 11:29
 */
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
