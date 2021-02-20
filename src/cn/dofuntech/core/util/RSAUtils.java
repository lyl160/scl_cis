/**
 * 
 */
package cn.dofuntech.core.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAUtils {

	public static final String charset = "UTF-8";
	public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOI6X7fMBdIYpgpxOwIXmMOWyEq+qby/DQzfrtc7+N0fBhvRLMHnvZF7/y5rHGnH8QtPy7wxejiXk3uhNyivRqOpauqIMm1afDo17IgPKIzilJylDftOzUmezfXz0qaee3QTfdA6vFVru6Smqc1OV4JUI8FHrdwVyIGcKkfOfpRQIDAQAB";
	public static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI4jpft8wF0himCnE7AheYw5bISr6pvL8NDN+u1zv43R8GG9Eswee9kXv/LmscacfxC0/LvDF6OJeTe6E3KK9Go6lq6ogybVp8OjXsiA8ojOKUnKUN+07NSZ7N9fPSpp57dBN90Dq8VWu7pKapzU5XglQjwUet3BXIgZwqR85+lFAgMBAAECgYBEpM6qD2tPDr2nQ8jsTJ5IrArOX8AjkXAxRuih+D5QHU07xnXngelJxIB8rBC63CU5Sk6r1uH5ppCV6xUtQhIa6ErZgzKqqMOkiwxp91TrHYEZSVtvvGcTTsKrnhLu3fFnPX3SxKr0vUmmYNncArYgNRlkGFeew7Md/n19BFqpQQJBAOKHa7vs2VW0B6zMkM+dJgC68FB67ungicExbITEu4ZAku36E5fCZlpQzGocntnzgGXTUf7ra91NsNclyVi42fkCQQCgoZ2gu2XJNwkvryZeiGeF0GQ62mFizBrJSI6XQDXweqEr6BhxH3BsOsHKPMXbm/8AVp+G4c8TU/OCIKdLqHytAkAjNh+V74P84WYMAyRDUU8V7/jo2aMHcaKYCCGxJY9sl97+5M9k2I/mXAbaO6aphEMiEM3/DQMQPEJPvwzkIJ+pAkABvh8eqknNaApyePz6k5JD8mHT/aCG2N4FhwL9AxZSuJHdDxSMzaGDmxOVJRmka0nV8Bqk9PgxJn0C0WXfPUG1AkEAj+t3iPOlEWxOvwwc/hbcOqtzAVwX6Oo3z+6c/SGrkaYeDxnNwb+bO7kTa7MqYKYbmXWZp1xhw0CoCv+n/TPGAQ==";
	
	/**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encode(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encode(key.getEncoded());
    }
	
    public static void main(String[] args) throws Exception {
    	/*Map<String, Object> keyMap = RSAUtils.genKeyPair();  
        String publicKey = RSAUtils.getPublicKey(keyMap);  
        String privateKey = RSAUtils.getPrivateKey(keyMap);  
        System.err.println("公钥:\n" + publicKey);  
        System.err.println("私钥:\n" + privateKey);  */
    	String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOI6X7fMBdIYpgpxOwIXmMOWyEq+qby/DQzfrtc7+N0fBhvRLMHnvZF7/y5rHGnH8QtPy7wxejiXk3uhNyivRqOpauqIMm1afDo17IgPKIzilJylDftOzUmezfXz0qaee3QTfdA6vFVru6Smqc1OV4JUI8FHrdwVyIGcKkfOfpRQIDAQAB";
    	String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI4jpft8wF0himCnE7AheYw5bISr6pvL8NDN+u1zv43R8GG9Eswee9kXv/LmscacfxC0/LvDF6OJeTe6E3KK9Go6lq6ogybVp8OjXsiA8ojOKUnKUN+07NSZ7N9fPSpp57dBN90Dq8VWu7pKapzU5XglQjwUet3BXIgZwqR85+lFAgMBAAECgYBEpM6qD2tPDr2nQ8jsTJ5IrArOX8AjkXAxRuih+D5QHU07xnXngelJxIB8rBC63CU5Sk6r1uH5ppCV6xUtQhIa6ErZgzKqqMOkiwxp91TrHYEZSVtvvGcTTsKrnhLu3fFnPX3SxKr0vUmmYNncArYgNRlkGFeew7Md/n19BFqpQQJBAOKHa7vs2VW0B6zMkM+dJgC68FB67ungicExbITEu4ZAku36E5fCZlpQzGocntnzgGXTUf7ra91NsNclyVi42fkCQQCgoZ2gu2XJNwkvryZeiGeF0GQ62mFizBrJSI6XQDXweqEr6BhxH3BsOsHKPMXbm/8AVp+G4c8TU/OCIKdLqHytAkAjNh+V74P84WYMAyRDUU8V7/jo2aMHcaKYCCGxJY9sl97+5M9k2I/mXAbaO6aphEMiEM3/DQMQPEJPvwzkIJ+pAkABvh8eqknNaApyePz6k5JD8mHT/aCG2N4FhwL9AxZSuJHdDxSMzaGDmxOVJRmka0nV8Bqk9PgxJn0C0WXfPUG1AkEAj+t3iPOlEWxOvwwc/hbcOqtzAVwX6Oo3z+6c/SGrkaYeDxnNwb+bO7kTa7MqYKYbmXWZp1xhw0CoCv+n/TPGAQ==";
    	//私钥加密  
        byte[] encodedData = RSAUtils.encryptByPrivateKey("哈哈".getBytes("UTF-8"), privateKey);  
    	String sign = RSAUtils.sign(encodedData, privateKey);  
        System.err.println("签名:\r" + sign);  
        boolean status = RSAUtils.verify(encodedData, publicKey, sign);  
        System.err.println("验证结果:\r" + status);  
    
    }
    
}
