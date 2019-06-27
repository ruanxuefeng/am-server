package com.am.server.common.util

import java.nio.charset.Charset
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec

object DesUtils {
    /**
     * 加密
     * @param src 待加密字符串
     * @param desKey 解密字符串
     * @return String
     */
    @JvmStatic
    fun encrypt(src: String, desKey: String): String? {
        var result: String? = null
        try {
            val cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding")
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    SecretKeyFactory.getInstance("DESede").generateSecret(DESedeKeySpec(desKey.toByteArray(Charset.forName("UTF-8")))),
                    SecureRandom()
            )
            val bytes = Base64.getEncoder().encode(cipher.doFinal(src.toByteArray(Charset.forName("UTF-8"))))
            result = String(bytes, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * 3重DES解密
     * @param src 待解密字符串
     * @param desKey 解密字符串
     * @return String
     */
    @JvmStatic
    fun decrypt(src: String, desKey: String): String? {
        var desResult: String? = null
        try {
            val cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding")
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    SecretKeyFactory.getInstance("DESede").generateSecret(DESedeKeySpec(desKey.toByteArray(Charset.forName("UTF-8")))),
                    SecureRandom()
            )

            val bytes = cipher.doFinal(Base64.getDecoder().decode(src))
            desResult = String(bytes, Charset.forName("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return desResult
    }
}
