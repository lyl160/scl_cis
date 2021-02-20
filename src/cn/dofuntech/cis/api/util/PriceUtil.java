package cn.dofuntech.cis.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.math.NumberUtils;

import cn.dofuntech.core.util.spring.SpringContextUtil;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2017 bsteel. All Rights Reserved.</font>
 * @author lxu(@2017年4月16日)
 * @version 1.0
 * filename:PriceUtil.java 
 */
public class PriceUtil {

    /**
     * @param tradePrice
     * @return
     */
    public static BigDecimal calSalePrice(BigDecimal tradePrice) {
        JedisService jedisService = SpringContextUtil.getBean(JedisService.class);
        String tradeRateInRedis = jedisService.STRINGS.get("tradeRate");
        double tradeRate = NumberUtils.toDouble(tradeRateInRedis);
        if (tradeRate == 0) {
            return tradePrice;
        }
        BigDecimal result = tradePrice.multiply(new BigDecimal(1 + tradeRate)).setScale(2, RoundingMode.HALF_UP).setScale(0, RoundingMode.UP);
        return result;
    }
}
