package org.example.authentication.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhuxj
 * @since 2021/1/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    /**
     * token
     */
    private String token;
    /**
     * 剩余超时时间（秒）
     */
    private long expire;
}
