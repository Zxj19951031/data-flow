package org.example.authentication.security.filter.form;

import org.example.authentication.security.filter.jwt.JwtAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class FormAuthenticationConfigurer<T extends FormAuthenticationConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private FormAuthenticationFilter filter;

    public FormAuthenticationConfigurer() {
        this.filter = new FormAuthenticationFilter();
    }

    @Override
    public void configure(B builder) throws Exception {
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));

        builder.addFilterAfter(filter, LogoutFilter.class);
    }


    public FormAuthenticationConfigurer<T, B> failureHandler(AuthenticationFailureHandler handler) {
        filter.setAuthenticationFailureHandler(handler); ;
        return this;
    }

    public FormAuthenticationConfigurer<T, B> successHandler(AuthenticationSuccessHandler handler) {
        this.filter.setAuthenticationSuccessHandler(handler);
        return this;
    }
}
