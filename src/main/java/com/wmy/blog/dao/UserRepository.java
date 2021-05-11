package com.wmy.blog.dao;

import com.wmy.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wmy
 * @date 2021/4/30 9:02
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}

