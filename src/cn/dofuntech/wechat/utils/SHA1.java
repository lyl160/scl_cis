/**
 * 
 */
package cn.dofuntech.wechat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.springframework.util.StringUtils;

import cn.dofuntech.wechat.exception.AesException;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 * 
 * @author lxu
 *
 */
public class SHA1 {

    /**
     * 用SHA1算法生成安全签名
     * 
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param encrypt 密文
     * @return 安全签名
     * @throws AesException 
     */
    public static String encrypt(String token, String timestamp, String nonce, String encrypt) {
        String[] array = new String[] { token, timestamp, nonce, encrypt };
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0 ; i < 4 ; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        return encrypt(str);
    }

    /**
     * SHA-1 加密
     * 
     * @param decript
     * @return
     */
    public static String encrypt(String decript) throws AesException {
        if (StringUtils.isEmpty(decript)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0 ; i < messageDigest.length ; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AesException(AesException.ComputeSignatureError);
        }
    }
}
