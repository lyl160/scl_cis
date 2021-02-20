package cn.dofuntech.cis.admin.utils;

import cn.dofuntech.core.util.RandomUtils;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2016 bsteel. All Rights Reserved.</font>
 * @author lxu(@2016年12月15日)
 * @version 1.0
 * filename:TokenUtils.java 
 */
public class TokenUtils {

    public static String genToken() {
        return RandomUtils.getString(10);
    }

    
    
    public static void main(String s []){
    	System.out.println(genToken());
    }
}
