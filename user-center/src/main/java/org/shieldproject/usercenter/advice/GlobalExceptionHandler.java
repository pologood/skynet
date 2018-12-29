package org.shieldproject.usercenter.advice;

import org.shieldproject.usercenter.exception.BusinessException;
import org.shieldproject.usercenter.exception.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kezhijie
 * @date 2018/12/6 9:53
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<?> businessException(HttpServletRequest request, BusinessException e) {
        return new ResponseEntity<>(
                buildErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TokenException.class)
    @ResponseBody
    public ResponseEntity<?> businessException(HttpServletRequest request, TokenException e) {
        return new ResponseEntity<>(
                buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> exception(Exception e) {
        logger.error("发生不可预知异常：", e);
        return new ResponseEntity<>(
                buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "出现严重异常，请联系管理员,内容：" + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    protected Map<String, Object> buildErrorResponse(int code, String message) {
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("code", code);
        returnData.put("message", message);
        return returnData;
    }
}
