package cn.dofuntech.gencode.builder.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern COL_TO_PROP_REG = Pattern.compile("(?<=[a-z])_([a-z])");

    /**
     * 字段转为属性
     * 默认规则  user_status ==> userStatus
     * @param column
     * @return
     */
    private static String changeColToProp(String column) {
        Matcher matcher = COL_TO_PROP_REG.matcher(column.toLowerCase());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 大写
     * 
     * @param s
     * @return
     */
    public static final String upperFirst(String s) {
        int len = s.length();
        if (len <= 0)
            return "";
        s = changeColToProp(s);
        len = s.length();
        StringBuffer sb = new StringBuffer();
        sb.append(s.substring(0, 1).toUpperCase());
        sb.append(s.substring(1, len));
        return sb.toString();
    }

    /**
     * 小写
     * 
     * @param s
     * @return
     */
    public static final String lowerFirst(String s) {
        int len = s.length();
        if (len <= 0)
            return "";

        StringBuffer sb = new StringBuffer();
        sb.append(s.substring(0, 1).toLowerCase());
        sb.append(s.substring(1, len));
        return changeColToProp(s);
        //return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("s".toUpperCase());
    }
}
