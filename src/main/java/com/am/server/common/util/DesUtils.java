package com.am.server.common.util;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * des加密工具
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:44
 */
public class DesUtils {
    /**
     * 加密
     *
     * @param src    待加密字符串
     * @param desKey 解密字符串
     * @return String
     */
    public static String encrypt(String src, String desKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(desKey.getBytes(StandardCharsets.UTF_8))),
                new IvParameterSpec(new byte[8])
        );
        byte[] bytes = Base64.getEncoder().encode(cipher.doFinal(src.getBytes(StandardCharsets.UTF_8)));

        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param src    待解密字符串
     * @param desKey 解密字符串
     * @return String
     */
    public static String decrypt(String src, String desKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(desKey.getBytes(StandardCharsets.UTF_8))),
                new IvParameterSpec(new byte[8])
        );

        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(src));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String text = "admin";
        String key = StringUtils.getRandomNumberStr(512);
        try {
            String encrypt = encrypt(text, key);
            System.out.println(encrypt);
            System.out.println(decrypt(encrypt, key));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }
}
