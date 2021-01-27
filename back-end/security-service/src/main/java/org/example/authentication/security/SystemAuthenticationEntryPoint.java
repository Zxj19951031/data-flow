package org.example.authentication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.commons.SystemResponse;
import org.example.authentication.exceptions.SystemError;
import org.example.authentication.exceptions.SystemException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证开始入口
 *
 * @author zhuxj
 * @since 2020/10/21
 */
@Component
@Slf4j
public class SystemAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("认证失败", e);
        SystemError error = SystemError.AUTHENTICATE_ERROR;
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.setStatus(error.getCode());
        ObjectMapper om = new ObjectMapper();
        httpServletResponse.getWriter().write(om.writeValueAsString(SystemResponse.error(SystemException.newException(error, e))));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}