package org.example.authentication.security.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.security.DaoUserDetailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.Calendar;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private DaoUserDetailService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();

        UserDetails user = userService.loadUserByToken(jwt);
        if (user == null || user.getPassword() == null) {
            throw new BadCredentialsException("User not found or Token invalid");
        }

        if (jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
            throw new AccountExpiredException("Token expired");
        }

        String key = DaoUserDetailService.SALT_REDIS_KEY_PREFIX + ":" + user.getUsername();
        String encryptSalt = (String) redisTemplate.opsForValue().get(key);
        if (encryptSalt == null) {
            throw new AccountExpiredException("Token expired");
        }
        try {
            String username = user.getUsername();
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build();
            verifier.verify(jwt.getToken());
        } catch (Exception e) {
            log.error("JWT token verify fail", e);
            throw new BadCredentialsException("JWT token verify fail", e);
        }

        return new JwtAuthenticationToken(user, jwt, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}