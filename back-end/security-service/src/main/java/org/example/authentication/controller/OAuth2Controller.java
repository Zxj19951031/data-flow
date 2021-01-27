package org.example.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(value = "user")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
