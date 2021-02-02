package org.example.authentication.service;

import org.example.authentication.model.User;

/**
 * @author zhuxj
 * @since 2021/1/28
 */
public interface UserService {
    /**
     * 查询用户信息
     *
     * @param username 用户名称
     * @return 用户
     */
    User findByUsername(String username);
}
