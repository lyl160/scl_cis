package cn.dofuntech.core.util;

public class StringUtil {

	/**
	 * uri截取
	 * @param str uri
	 * @param n 出现的次数
	 * @return
	 */
	public static String getStr(String str, int n) {
		int i = 0;
		int s = 0;
		while (i++ < n) {
			s = str.indexOf("/", s + 1);
			if (s == -1) {
				return str;
			}
		}
		return str.substring(s, str.length());
	}
	
	/**
	 * URI截取
	 * @param str URI
	 * @param n 顺序出现次数
	 * @param end 逆序出现次数
	 * @return
	 */
	public static String getStr(String str, int n, int end, char c) {
		int i = 0;
		int s = 0;
		while (i++ < n) {
			s = str.indexOf("/", s + 1);
			if (s == -1) {
				return str;
			}
		}
		str = str.substring(s, str.length());
		int j=str.length();
		i = 0;
		while (i++ < end) {
			j = str.lastIndexOf(c, j - 1);
			if (j == -1) {
				return str;
			}
		}
		return str.substring(0, j);
	}

}
