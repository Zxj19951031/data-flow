package org.example.authentication.security;

import org.example.authentication.security.filter.form.FormAuthenticationConfigurer;
import org.example.authentication.security.filter.form.FormAuthenticationFailureHandler;
import org.example.authentication.security.filter.form.FormAuthenticationSuccessHandler;
import org.example.authentication.security.filter.jwt.JwtAuthenticationConfigurer;
import org.example.authentication.security.filter.jwt.JwtAuthenticationFailureHandler;
import org.example.authentication.security.filter.jwt.JwtAuthenticationProvider;
import org.example.authentication.security.filter.jwt.JwtAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zhuxj
 * @since 2021/1/21
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    @Autowired
    private FormAuthenticationFailureHandler formAuthenticationFailureHandler;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Autowired
    private DaoUserDetailService daoUserDetailService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenStore getTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public JdbcClientDetailsService getJdbcClientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(getPasswordEncoder());
        return clientDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .apply(new FormAuthenticationConfigurer<>())
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .and()
                .apply(new JwtAuthenticationConfigurer<>())
                .successHandler(jwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenticationFailureHandler)
                .and()
                .logout()
                .and()
                .csrf().disable()
                .sessionManagement().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.daoUserDetailService);
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
