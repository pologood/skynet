package org.shieldproject.usercenter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shieldproject.usercenter.bean.Role;
import org.shieldproject.usercenter.bean.User;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    User getUserByUserName(@Param("username") String username);

    Set<Role> findRoleByUserId(@Param("id") Integer id);

    List<User> getUsers(@Param("offset") Integer offset, @Param("limit") Integer limit);

    int getUserSize();

    int addUser(User user);

    Integer bindRoles(@Param("uid") Integer uid, @Param("rid") Integer rid);

    Integer modifyUserById(@Param("id") Integer id, @Param("name") String name, @Param("email") String email);


    Integer remove(@Param("id") Integer id);

    User getUserById(@Param("id") Integer id);

    Integer unbindRoles(@Param("uid") Integer uid, @Param("rid") Integer rid);
}
