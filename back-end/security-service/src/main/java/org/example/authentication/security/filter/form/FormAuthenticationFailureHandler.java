package org.example.authentication.security.filter.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authentication.commons.SystemResponse;
import org.example.authentication.exceptions.SystemError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 表单认证失败处理器
 */
@Component
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(FormAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SystemError error;
        logger.error("登录失败", exception);
        if (exception instanceof BadCredentialsException) {
            error = SystemError.USERNAME_PASSWORD_ERROR;
        } else {
            error = SystemError.AUTHENTICATE_ERROR;
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(error.getCode());
        ObjectMapper om = new ObjectMapper();
        response.getWriter().write(om.writeValueAsString(SystemResponse.error(error)));
        response.getWriter().flush();
        response.getWriter().close();
    }
}