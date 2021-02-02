package org.example.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author zhuxj
 * @since 2021/1/26
 */
@RestController
@RequestMapping(value = "oauth")
public class OAuth2Controller {

    /**
     * 用户信息获取接口，供其他资源服务器确认token有效
     *
     * @param principal 身份
     * @return Principal
     */
    @GetMapping(value = "user")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
