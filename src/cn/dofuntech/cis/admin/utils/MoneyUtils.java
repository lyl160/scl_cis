package cn.dofuntech.cis.admin.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 金额计算
 * 
 * @author luokai
 * 
 */
public class MoneyUtils {
	
	/**
	 * Double相加
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Double add(Double v1, Double v2) {
		   BigDecimal b1 = new BigDecimal(v1.toString());  
		   BigDecimal b2 = new BigDecimal(v2.toString());  
		   return b1.add(b2).doubleValue();  
	}

	@SuppressWarnings("unused")
	public static Double add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}
	/**
	 * Double相减
	 * @param v1 减数
	 * @param v2 被减数
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Double sub(Double v1, Double v2) {
		   BigDecimal b1 = new BigDecimal(v1.toString());  
		   BigDecimal b2 = new BigDecimal(v2.toString());  
		   return b1.subtract(b2).doubleValue();  
	}
	/**
	 * Double相乘
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings("unused")
	public static Double mul(Double v1, Double v2) {
		   BigDecimal b1 = new BigDecimal(v1.toString());  
		   BigDecimal b2 = new BigDecimal(v2.toString());  
		   return b1.multiply(b2).doubleValue();  
	}

	public static Double mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}
	/** 
	* 提供精确的小数位四舍五入处理。 
	* @param v 需要四舍五入的数字 
	* @param scale 小数点后保留几位 
	* @return 四舍五入后的结果 
	*/  
	@SuppressWarnings("unused")
	public static double round(double v, int scale) {
	   BigDecimal b = new BigDecimal(Double.toString(v));  
	   return b.divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}

	/**
	 * 数字格式化
	 * @param param 需要格式化的数据
	 * @param scale 小数点后保留几位
	 * @return 格式化后的结果
	 */
	@SuppressWarnings("unused")
	public static String formtStr(String param,int scale) {
		if(!StringUtils.isEmpty(param)) {
			return String.format("%."+scale+"f", Double.parseDouble(param));
		}
		return param;
	}
}
