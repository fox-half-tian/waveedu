package com.zhulang.waveedu.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @author 狐狸半面添
 * @create 2023-01-17 21:08
 */
public class JwtUtils {
    /**
     * JWT有效期：60 * 60 * 1000 * 2 两个小时
     */
    public static final Long JWT_TTL = 60 * 60 * 1000 * 2L;
    /**
     * 设置秘钥明文
     */
    public static final String JWT_KEY = "waveEdu";

    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtils.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                // 唯一的ID
                .setId(uuid)
                // 主题 可以是JSON数据
                .setSubject(subject)
                // 签发者
                .setIssuer("fox")
                // 签发时间
                .setIssuedAt(now)
                //使用HS256对称加密算法签名, 第二个参数为秘钥
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    /**
     * 创建token
     *
     * @param id 唯一的ID
     * @param subject 主题
     * @param ttlMillis 过期时间
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        // 设置过期时间
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
//        create();
        parse("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyNWMzZjg3YzFhMzU0ODgzOGNkYzg3OWY4YjgxNDRiNyIsInN1YiI6IjIwMjEyODgwIiwiaXNzIjoiZm94IiwiaWF0IjoxNjczOTY0ODM5LCJleHAiOjE2NzM5NzIwMzl9.BAafCCPDy3VroLFV4AiVn3cFPnp_G4tU26tELofVEvY");
    }
    public static void create(){
        String id = "20212880";
        String jwt = createJWT("20212880");
        System.out.println(jwt);
    }

    public static void parse(String token) throws Exception {
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }


    /**
     * 生成加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
