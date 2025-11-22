package com.csu.userservice.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    // 密钥（长度至少 32 字符）
    private final String secret = "6v9y$B&E)H@McQfTjWnZr4u7x!A%D*G-";
    // 过期时间（秒）—— 1 天
    private final Long expiration = 86400L;

    // 内存黑名单
    private final Cache<String, Boolean> tokenBlacklist = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(10000)
            .build();

    // 生成签名用的密钥
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** 生成 Token（适配 JJWT 0.11.x 写法） */
    public String generateToken(Long userid, String username, int tokenVersion) {
        return Jwts.builder()
                .claim("user_id", userid)
                .claim("ver", tokenVersion)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)   // ✅ 正确写法
                .compact();
    }

    /** 解析 Token（0.11.x 必须用 parserBuilder） */
    private Claims extractClaim(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 从 token 中拿 user_id
    public Long extractUserId(String token) {
        return extractClaim(token).get("user_id", Long.class);
    }

    // 拿用户名
    public String extractUsername(String token) {
        return extractClaim(token).getSubject();
    }

    // 拿版本号
    public int extractTokenVersion(String token) {
        return extractClaim(token).get("ver", Integer.class);
    }

    // 拿过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** 校验 token 是否属于指定用户、未过期 & 版本号正确 & 不在黑名单 */
    public boolean validateToken(String token, Long userid, String username, int tokenVersion) {
        return extractUserId(token).equals(userid)
                && extractUsername(token).equals(username)
                && extractTokenVersion(token) >= tokenVersion
                && !isTokenExpired(token)
                && !isTokenBlacklisted(token);
    }

    /** 只检查 token 本身是否还有效 */
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token) && !isTokenBlacklisted(token);
    }

    /** 让 token 失效（加入黑名单） */
    public void invalidateToken(String token) {
        if (token == null) return;
        if (!isTokenExpired(token)) {
            long ttl = extractExpiration(token).getTime() - System.currentTimeMillis();
            if (ttl > 0) {
                tokenBlacklist.put(token, true);
            }
        }
    }

    private boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.getIfPresent(token) != null;
    }
}
