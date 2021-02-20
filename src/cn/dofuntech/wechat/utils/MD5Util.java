/**
 * 
 */
package cn.dofuntech.wechat.utils;

import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lxu
 *
 */
public class MD5Util {

    private static String[] hexDigits = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 加密
     * 
     * @param origin
     * @return 返回加密后字符
     */
    public static String stringToMD5(String origin) {
        if (origin != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = md.digest(origin.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加密
     * 
     * @param origin
     * @param chartset
     * @return 返回加密后字符
     */
    public static String stringToMD5(String origin, String chartset) {
        if (origin != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] results = null;
                if (StringUtils.isEmpty(chartset)) {
                    results = md.digest(origin.getBytes());
                }
                else {
                    results = md.digest(origin.getBytes(chartset));
                }
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultsb = new StringBuffer();
        int i = 0;
        for (i = 0 ; i < b.length ; i++) {
            resultsb.append(byteToHexString(b[i]));
        }
        return resultsb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
