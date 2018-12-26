package org.shieldproject.usercenter.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.Set;
@Data
public class User implements java.io.Serializable {
    private Integer id;
    private String name; //用户名
    private String email;//用户邮箱
    private String userName;//名称
    @JsonIgnore
    private String password;//用户密码
    private int enabled;//用户是否启用
    private Date createTime;//时间
    private int errCount;//登录错误次数
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private Set<Role> roles = null;// 所对应的角色集合
    private Date LastLoginTime;

    public User() {
    }

    public User(String name, String email, String userName, String password, int enabled, Date dob, int errCount) {
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.createTime = dob;
        this.errCount = errCount;
    }

    public User(String name, String email, String password, Date dob, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createTime = dob;
        this.roles = roles;
    }

}