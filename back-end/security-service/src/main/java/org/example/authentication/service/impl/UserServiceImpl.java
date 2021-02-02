package org.example.authentication.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.mapper.UserMapper;
import org.example.authentication.model.User;
import org.example.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhuxj
 * @since 2021/1/28
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {

        return this.userMapper.selectByUsername(username);
    }
}
