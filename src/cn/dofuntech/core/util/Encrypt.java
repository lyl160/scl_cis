package cn.dofuntech.core.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.crypto.provider.SunJCE;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月5日)
 * @version 1.0
 * filename:Encrypt.java 
 */
public class Encrypt {

    private static final Logger logger         = LoggerFactory.getLogger(Encrypt.class);
    private static String       algorithm      = "DESede";
    private static String       transformation = "DESede/CBC/PKCS5Padding";
    private static byte         defaultIV[]    = { 1, 2, 3, 4, 5, 6, 7, 8 };
    private static String seed = "dofuntech";

    public Encrypt() {
    }

    public static String encodeBase64Sha1(String str) {
        byte encodeStrBytes[] = encodeSha1(str);
        return encodeBase64(encodeStrBytes);
    }

    public static String decodeBase643Des(String key, String str) {
        byte decodeStrBytes[] = decodeBase64(str);
        return new String(decryptMode3Des(key, decodeStrBytes));
    }

    public static String encodeBase643Des(String key, String str) {
        byte encodeStr[] = encryptMode3Des(key, str);
        return encodeBase64(encodeStr);
    }

    public static String encodeBase643DesSha1(String key, String str) {
        byte encodeStrBytes[] = encodeSha1(str);
        encodeStrBytes = encryptMode3Des(key, encodeStrBytes);
        return encodeBase64(encodeStrBytes);
    }

    public static byte[] encryptMode3Des(String key, String str) {
        return encryptMode3Des(key, str.getBytes());
    }

    public static byte[] decryptMode3Des(String key, String str) {
        return decryptMode3Des(key, str.getBytes());
    }

    public static byte[] encryptMode3Des(String key, byte str[]) {
        return encryptMode3Des(key, str, defaultIV);
    }

    public static byte[] decryptMode3Des(String key, byte str[]) {
        return decryptMode3Des(key, str, defaultIV);
    }

    public static byte[] encryptMode3Des(String key, byte str[], byte ivs[]) {
        return encryptMode3Des(decode(key), str, ivs);
    }

    public static byte[] decryptMode3Des(String key, byte str[], byte ivs[]) {
        return decryptMode3Des(decode(key), str, ivs);
    }

    public static boolean checkBase643DesSha1(String key, String str, String encodeBase643DesSha1Str) {
        byte encodeStrBytes[] = encodeSha1(str);
        encodeStrBytes = encryptMode3Des(key, encodeStrBytes);
        String newEncoceBase643DesSha1Str = encodeBase64(encodeStrBytes);
        return encodeBase643DesSha1Str.equals(newEncoceBase643DesSha1Str);
    }

    public static boolean checkBase64Sha1(String str, String encodeBase64Sha1Str) {
        String newEncodeBase64Sha1Str = encodeBase64Sha1(str);
        return newEncodeBase64Sha1Str.equals(encodeBase64Sha1Str);
    }

    private static byte[] encryptMode3Des(byte key[], byte str[], byte ivs[]) {
        try {
            Security.addProvider(new SunJCE());
            DESedeKeySpec desedekeyspec = new DESedeKeySpec(key);
            SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance(algorithm);
            javax.crypto.SecretKey secretKey = secretkeyfactory.generateSecret(desedekeyspec);
            IvParameterSpec ivparameterspec = new IvParameterSpec(ivs);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(1, secretKey, ivparameterspec);
            return cipher.doFinal(str);
        }
        catch (Exception e) {
            logger.debug((new StringBuilder("\u4F7F\u75283des\u65B9\u5F0F\u52A0\u5BC6:")).append(e.getMessage()).toString());
            logger.debug(null, e);
            return null;
        }
    }

    private static byte[] decryptMode3Des(byte key[], byte str[], byte ivs[]) {
        try {
            Security.addProvider(new SunJCE());
            DESedeKeySpec desedekeyspec = new DESedeKeySpec(key);
            SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance(algorithm);
            javax.crypto.SecretKey secretKey = secretkeyfactory.generateSecret(desedekeyspec);
            IvParameterSpec ivparameterspec = new IvParameterSpec(ivs);
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(2, secretKey, ivparameterspec);
            return cipher.doFinal(str);
        }
        catch (Exception e) {
            logger.debug((new StringBuilder("\u4F7F\u75283des\u65B9\u5F0F\u8FDB\u884C\u5BF9\u6570\u636E\u8FDB\u884C\u89E3\u5BC6:")).append(e.getMessage()).toString());
            logger.debug(null, e);
            return null;
        }
    }

    private static byte[] decodeBase64(String str) {
        byte bReturn[] = null;
        try {
            bReturn = Base64.decodeBase64(str);
        }
        catch (Exception e) {
            logger.error((new StringBuilder("\u4F7F\u7528bsae64\u65B9\u5F0F\u8FDB\u884C\u53CD\u7F16\u7801\uFF0C\u5C06String\u7C7B\u578B\u8F6C\u6362\u6210byte\u7C7B\u578B\u6570\u7EC4\u53D1\u751F\u9519\u8BEF:")).append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return bReturn;
    }

    private static String encodeBase64(byte bytes[]) {
        String sReturn = Base64.encodeBase64String(bytes);
        sReturn = sReturn.replaceAll("\r", "");
        sReturn = sReturn.replaceAll("\n", "");
        return sReturn;
    }

    private static byte[] encodeSha1(String str) {
        byte encodeStrBytes[] = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            encodeStrBytes = md.digest(str.getBytes());
        }
        catch (NoSuchAlgorithmException e) {
            logger.error((new StringBuilder("\u4F7F\u7528sha1\u7684\u65B9\u5F0F\u5C06string\u7C7B\u578B\u8F6C\u5316\u6210byte\u7C7B\u578B\u6570\u7EC4\u8FDB\u884C\u7F16\u7801\u65F6\u95F4\u53D1\u9001\u9519\u8BEF\uFF1A")).append(e.getMessage()).toString());
            logger.debug(null, e);
        }
        return encodeStrBytes;
    }

    private static byte[] decode(String s) {
        byte abyte0[] = new byte[s.length() / 2];
        String s1 = s.toLowerCase();
        for (int i = 0 ; i < s1.length() ; i += 2) {
            char c = s1.charAt(i);
            char c1 = s1.charAt(i + 1);
            int j = i / 2;
            if (c < 'a')
                abyte0[j] = (byte) (c - 48 << 4);
            else
                abyte0[j] = (byte) ((c - 97) + 10 << 4);
            if (c1 < 'a') {
                int tmp87_85 = j;
                byte tmp87_84[] = abyte0;
                tmp87_84[tmp87_85] = (byte) (tmp87_84[tmp87_85] + (byte) (c1 - 48));
            }
            else {
                int tmp104_102 = j;
                byte tmp104_101[] = abyte0;
                tmp104_101[tmp104_102] = (byte) (tmp104_101[tmp104_102] + (byte) ((c1 - 97) + 10));
            }
        }

        return abyte0;
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            StringBuilder result = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte bytes[] = md.digest(srcStr.getBytes("utf-8"));
            byte abyte0[];
            int j = (abyte0 = bytes).length;
            for (int i = 0 ; i < j ; i++) {
                byte b = abyte0[i];
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1)
                    result.append("0");
                result.append(hex);
            }

            return result.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String srcStr) {
        return hash("MD5", srcStr);
    }

    public static String sha1(String srcStr) {
        return hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr) {
        return hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr) {
        return hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr) {
        return hash("SHA-512", srcStr);
    }

    public static String encodeAES(String content, String seed){
    	try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(seed.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher ciper = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			ciper.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = ciper.doFinal(byteContent);
			return parseByte2HexStr(result);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String encodeAES(String content){
    	return encodeAES(content, seed);
    }

    public static String decodeAES(String content, String seed){
		KeyGenerator kgen;
		try {
			byte[] byteCon = parseHexStr2Byte(content);
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(seed.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher ciper = Cipher.getInstance("AES");
			ciper.init(Cipher.DECRYPT_MODE, key);
			byte[] result = ciper.doFinal(byteCon);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static String decodeAES(String content){
    	return decodeAES(content, seed);
    }
    
    /**将二进制转换成16进制 
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < buf.length; i++) {  
                    String hex = Integer.toHexString(buf[i] & 0xFF);  
                    if (hex.length() == 1) {  
                            hex = '0' + hex;  
                    }  
                    sb.append(hex.toUpperCase());  
            }  
            return sb.toString();  
    } 
    
    /**将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */  
    public static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    }  

    public static void main(String[] args) throws Exception {
    	/*String ss = encodeAES("123");
		System.out.println(ss);
		System.out.println(decodeAES(ss));*/
	}
    
}
