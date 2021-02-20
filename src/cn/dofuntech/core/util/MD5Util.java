package cn.dofuntech.core.util;

import java.security.MessageDigest;

public class MD5Util {

	private static String[] hexDigits = new String[] { "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/**
	 * 加密
	 * 
	 * @param md
	 * @return 返回加密后字符
	 */
	public static String stringToMD5(String password) {
		if (password != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(password.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	public static String stringToMD5Low(String password) {
		if (password != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(password.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultsb = new StringBuffer();
		int i = 0;
		for (i = 0; i < b.length; i++) {
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
		int d2 = n / 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
