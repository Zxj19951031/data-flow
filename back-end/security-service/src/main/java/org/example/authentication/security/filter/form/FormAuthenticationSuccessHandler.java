package org.example.authentication.security.filter.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authentication.commons.SystemResponse;
import org.example.authentication.security.AuthenticationConstants;
import org.example.authentication.security.DaoUserDetailService;
import org.example.authentication.security.TokenResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 表单认证成功处理器，集成JWT认证，所以在登录成功后构建一个token给到客户端
 */
@Component
public class FormAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private DaoUserDetailService daoUserDetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功后返回token
        TokenResponse tokenResp = daoUserDetailService.saveToken((UserDetails) authentication.getPrincipal());
        String token = tokenResp.getToken();
        response.setContentType("application/json;charset=utf-8");
        response.setHeader(AuthenticationConstants.AUTHORIZATION_HEADER, token);
        ObjectMapper om = new ObjectMapper();
        response.getWriter().write(om.writeValueAsString(SystemResponse.success(tokenResp)));
        response.getWriter().flush();
        response.getWriter().close();
    }
}