package com.example.zhong.paulapp.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaHelper {

    /**
     * 用公钥加密
     * @param str 待加密字符串
     * @param enCode 字符串编码格式
     * @param pubKey 公钥
     * @return 加密后字符串
     */
    public static String encryptData(String str, String enCode, PublicKey pubKey) {
        try {

            byte[] data = str.getBytes(enCode);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return new BASE64Encoder().encode(cipher.doFinal(data));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从base64字符串获取公钥
     * @param key
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = new BASE64Decoder().decodeBuffer(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
}
