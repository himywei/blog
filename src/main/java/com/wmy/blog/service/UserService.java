package com.wmy.blog.service;

import com.wmy.blog.pojo.User;

/**
 * @author wmy
 * @date 2021/4/29 10:51
 */
public interface UserService {

    User checkUser(String username, String password);
}
