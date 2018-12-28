package org.shieldproject.usercenter.config;

//@Controller
//@SessionAttributes("authorizationRequest")
public class OAuth2ApprovalController {

//    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation() {

        return "oauth-grant";
    }

}  