package org.example.authentication.security.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.example.authentication.security.AuthenticationConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;
    private AuthenticationManager authenticationManager;


    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    public JwtAuthenticationFilter() {
        this.requestMatcher = new RequestHeaderRequestMatcher(AuthenticationConstants.AUTHORIZATION_HEADER);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        if (!requireAuthenticate(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;

        }
        //对携带了token的请求进行拦截
        Authentication authentication = null;
        AuthenticationException exception = null;

        try {
            Authentication token = this.tokenExtractor.extract(httpServletRequest);
            if (token != null) {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(JWT.decode((String) token.getPrincipal()));
                authentication = this.getAuthenticationManager().authenticate(authToken);
            } else {
                exception = new InsufficientAuthenticationException("Token not found");
            }
        } catch (JWTDecodeException e) {
            logger.error("Token format error", e);
            exception = new InsufficientAuthenticationException("Token format error", e);
        } catch (InternalAuthenticationServiceException e) {
            logger.error("An internal error occurred while trying to authenticate the user.", e);
            exception = e;
        } catch (AuthenticationException e) {
            exception = e;
        }

        //如果认证成功，续期并递交下一个filter
        //如果认证失败，则响应错误
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            successHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        } else {
            SecurityContextHolder.clearContext();
            failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    public void setFailHandler(AuthenticationFailureHandler failHandler) {
        this.failureHandler = failHandler;
    }

    private boolean requireAuthenticate(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler handler) {
        this.successHandler = handler;
    }
}