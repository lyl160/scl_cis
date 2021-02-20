package cn.dofuntech.core.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 * @author lxu(@2015年10月27日)
 * @version 1.0
 * filename:DefaultValue.java 
 */
public abstract class DefaultValue extends AutoIdEntity {

    /**
     * 
     */
    private static final long serialVersionUID             = 1L;
    public static String      EMPTY                        = StringUtils.EMPTY;
    public static Integer     INT_EMPTY                    = NumberUtils.INTEGER_ZERO;
    public static Long        LONG_EMPTY                   = NumberUtils.LONG_ZERO;
    public static Float       FLOAT_EMPTY                  = NumberUtils.FLOAT_ZERO;
    public static Double      DOUBLE_EMPTY                 = NumberUtils.DOUBLE_ZERO;
    public static Short       SHORT_EMPTY                  = NumberUtils.SHORT_ZERO;
    public static BigDecimal  DECIMAL_EMPTY                = BigDecimal.ZERO;

    public static String      PREORDER_TIME_PATTERN        = "yyyy-MM-dd HH:mm";
    public static String      PREORDER_DEFAULT_PATTERN     = "yyyy-MM-dd HH:mm:ss";
    public static String      PREORDER_TIME_PATTERN_SIMPLE = "yyyy-MM-dd";

    public String debugInfo() {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(this);
        return builder.toString();
    }

}
