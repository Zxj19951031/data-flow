package org.example.authentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息查询服务
 *
 * @author zhuxj
 * @since 2020/10/21
 */
@Service
public class DaoUserDetailService implements UserDetailsService {

    /**
     * token 超时时间 秒
     */
    private static final long TIMEOUT = 3600;
    /**
     * token 缓存前缀
     */
    public static final String SALT_REDIS_KEY_PREFIX = "jwt";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 通过用户名获取用户信息
     *
     * @param s 用户名
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        return User.builder()
                .username(s)
                .password(passwordEncoder.encode("123456"))
                .authorities(new ArrayList<>())
                .build();
    }

    /**
     * 生成并保存token至缓存
     *
     * @param user 用户信息
     * @return token
     */
    public TokenResponse saveToken(UserDetails user) {
        String salt = BCrypt.gensalt();
        String key = SALT_REDIS_KEY_PREFIX + ":" + user.getUsername();
        long timestamps = System.currentTimeMillis();
        Date issuedAt = new Date(timestamps);
        Date expiresAt = new Date(timestamps + TIMEOUT * 1000);
        this.redisTemplate.opsForValue().set(key, salt);
        this.redisTemplate.expireAt(key, expiresAt);
        String token = String.format("Bearer %s", JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC256(salt)));


        long expire = this.redisTemplate.getExpire(key, TimeUnit.SECONDS);

        return new TokenResponse(token, expire);
    }

    /**
     * 移除token
     *
     * @param name 用户名
     */
    public void removeToken(String name) {
        String key = SALT_REDIS_KEY_PREFIX + ":" + name;
        redisTemplate.delete(key);
    }

    public UserDetails loadUserByToken(DecodedJWT jwt) {
        String username = jwt.getSubject();
        return loadUserByUsername(username);
    }
}