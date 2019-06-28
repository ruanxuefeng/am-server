package com.am.server.common.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:44
 */
public class JwtUtils {

    /**
     * 过期时间
     */
    private static final Long EXPIRED_TIME = 3600 * 24 * 7 * 1000L;

    /**
     * 发行人（创建人）
     */
    private static final String ISSUER = "ad-server";

    /**
     * 创建token字符串
     * @param uid 用户主键
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/7/24 9:45
     */
    public static String sign(String uid) {
        long nowMillis = System.currentTimeMillis();

        Date now = new Date(nowMillis);
        Date expireTime = new Date(nowMillis + EXPIRED_TIME);
        // 创建token时间
        JwtBuilder builder = Jwts.builder().setSubject(uid).setIssuedAt(now)
                // 过期时间
                .setExpiration(expireTime)
                // 发行者（可以理解为创建人）
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance());

        return builder.compact();
    }


    /**
     * 使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
     * @return java.security.Key
     * @author 阮雪峰
     * @date 2018/7/24 9:45
     */
    private static Key getKeyInstance() {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.PS256;
        //使用ISSUER
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ISSUER);

        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    /**
     * 解析token获取用户信息
     * @param token     需要解析的token
     * @return String
     * @author 阮雪峰
     * @date 2018/7/25 9:21
     */
    public static String getSubject(String token) {
        return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token).getBody().getSubject();
    }
}
