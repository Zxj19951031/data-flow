
package org.example.authentication.security.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authentication.commons.SystemResponse;
import org.example.authentication.exceptions.SystemError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        SystemError error = SystemError.AUTHENTICATE_ERROR;
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(error.getCode());
        ObjectMapper om = new ObjectMapper();
        response.getWriter().write(om.writeValueAsString(SystemResponse.error(error)));
        response.getWriter().flush();
        response.getWriter().close();
    }
}