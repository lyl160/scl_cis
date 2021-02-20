package cn.dofuntech.tools;

import java.math.BigDecimal;

public class BigDemicalUtil {
	
    /** 
     * 提供精确加法计算的add方法 
     * @param value1 被加数 
     * @param value2 加数 
     * @return 两个参数的和 
     */  
    public static BigDecimal add(BigDecimal value1,BigDecimal value2){  
        return value1.add(value2);  
    }  
	
	  /** 
     * 提供精确乘法运算的mul方法 
     * @param value1 被乘数 
     * @param value2 乘数 
     * @return 两个参数的积 
     */  
    public static BigDecimal mul(BigDecimal value1,BigDecimal value2){  
        return value1.multiply(value2);  
    }  

    /**
     * 比较大小
     * @param value1
     * @param value2
     * @return
     */
    public static int compareTo(BigDecimal value1,BigDecimal value2){  
        return value1.compareTo(value2);  
    }  
    
    
    /** 
     * 提供精确减法运算的sub方法 
     * @param value1 被减数 
     * @param value2 减数 
     * @return 两个参数的差 
     */  
    public static BigDecimal sub(BigDecimal value1,BigDecimal value2){  
        return value1.subtract(value2) ; 
    }  

    
    /** 
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。  
     * @param v1 被除数  
     * @param v2 除数  
     * @param scale 表示表示需要精确到小数点以后几位。  
     * @return 两个参数的商 
     */  
    public static BigDecimal div(BigDecimal value1,BigDecimal value2,int scale) throws IllegalAccessException{  
        if(scale<0){        
            //如果精确范围小于0，抛出异常信息。  
            throw new IllegalArgumentException("精确度不能小于0");  
        }
        return value1.divide(value2, scale, BigDecimal.ROUND_HALF_UP);      
    }  
  

}
