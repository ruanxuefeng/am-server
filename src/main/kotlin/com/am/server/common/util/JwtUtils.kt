package com.am.server.common.util

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

object JwtUtils {
    /**
     * 过期时间
     */
    private const val EXPIRED_TIME = 3600 * 24 * 7 * 1000L

    /**
     * 发行人（创建人）
     */
    private const val ISSUER = "ad-server"

    /**
     * 创建token字符串
     * @param uid 用户主键
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/7/24 9:45
     */
    @JvmStatic
    fun sign(uid: String): String {
        val nowMillis = System.currentTimeMillis()

        val now = Date(nowMillis)
        val expireTime = Date(nowMillis + EXPIRED_TIME)
        // 创建token时间
        val builder = Jwts.builder().setSubject(uid).setIssuedAt(now)
                // 过期时间
                .setExpiration(expireTime)
                // 发行者（可以理解为创建人）
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())

        return builder.compact()
    }


    /**
     * 使用HS256签名算法和生成的signingKey最终的Token,claims中是有效载荷
     * @return java.security.Key
     * @author 阮雪峰
     * @date 2018/7/24 9:45
     */
    private fun getKeyInstance(): Key {

        val signatureAlgorithm = SignatureAlgorithm.PS256
        //使用ISSUER
        val apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ISSUER)

        return SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
    }

    /**
     * 解析token获取用户信息
     * @param token     需要解析的token
     * @return String
     * @author 阮雪峰
     * @date 2018/7/25 9:21
     */
    @JvmStatic
    fun getSubject(token: String): String {
        return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token).body.subject
    }
}
