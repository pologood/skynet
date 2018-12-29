package org.shieldproject.usercenter.access.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.shieldproject.usercenter.access.Token;
import org.shieldproject.usercenter.exception.TokenException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kezhijie
 * @date 2018/12/29 10:32
 */
@Aspect
@Component
public class AccessAspect {

    @Before("@annotation(org.shieldproject.usercenter.access.annotations.Access)")
    public void pointCut() throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authentication");

        if (StringUtils.isEmpty(token))
            throw new TokenException("token invalid");

        if (!Token.validate(token))
            throw new TokenException("token invalid");
    }
}
