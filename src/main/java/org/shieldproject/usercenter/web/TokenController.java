package org.shieldproject.usercenter.web;

import org.shieldproject.usercenter.access.Token;
import org.shieldproject.usercenter.access.annotations.Access;
import org.shieldproject.usercenter.exception.BusinessException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author kezhijie
 * @date 2018/12/29 10:59
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping("/")
    public String token(@RequestParam("userId") String userId) {
        if (StringUtils.isEmpty(userId))
            throw new BusinessException("userId is empty");
        return Token.generate(userId);
    }

    @GetMapping("/test")
    @Access
    @ResponseBody
    public String hello(){
        return "Hello Shield-prject";
    }
}
