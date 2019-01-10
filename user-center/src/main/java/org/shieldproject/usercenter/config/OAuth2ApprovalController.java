package org.shieldproject.usercenter.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("authorizationRequest")
public class OAuth2ApprovalController {
    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(HttpServletRequest request) {
        System.out.println(request);
        return "oauth-grant";
    }

}  