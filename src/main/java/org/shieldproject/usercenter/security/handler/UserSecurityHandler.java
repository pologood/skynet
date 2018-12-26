package org.shieldproject.usercenter.security.handler;

import org.shieldproject.usercenter.bean.Resource;
import org.shieldproject.usercenter.bean.Role;
import org.shieldproject.usercenter.bean.SecurityUser;
import org.shieldproject.usercenter.bean.User;
import org.shieldproject.usercenter.mapper.ResourceMapper;
import org.shieldproject.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserSecurityHandler implements UserDetailsService {
    @Autowired  //业务服务类
    private UserMapper userService;
    @Autowired
    ResourceMapper resourceMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //SysUser对应数据库中的用户表，是最终存储用户和密码的表，可自定义
        //本例使用SysUser中的name作为用户名:
        User user = userService.getUserByUserName(userName);
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        // SecurityUser实现UserDetails并将SysUser的name映射为username
        //把用户全选读出来
        user.setRoles(userService.findRoleByUserId(user.getId()));
        //把用户资源表读出来...
        List<Resource> resources = new ArrayList<>();
        for (Role role : user.getRoles())
            resources.addAll(resourceMapper.findByRoleId(role.getId()));
        SecurityUser seu = new SecurityUser(user, resources);
        return seu;
    }
}