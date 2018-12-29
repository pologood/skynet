package org.shieldproject.usercenter.security.handler;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ShawnShoper on 2017/3/11.
 */
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
