package cn.dofuntech.tools;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import cn.dofuntech.core.util.spring.SpringContextUtil;
import cn.dofuntech.tools.redis.JedisService;

/**
 * <p>
 * 会员卡号生成
 * </p>
 * <font size=0.25>Copyright (C) 2018 dofuntech. All Rights Reserved.</font>
 * @author lxu(2018年4月3日)
 * @version 1.0
 * filename:CardNoUtils.java 
 */
public class CardNoUtil {

    @Autowired
    private JedisService        jedisService;

    private static final String CARD_NO_KEY     = "card_no_seq";
    private static final String CARD_NO_PRE     = "300";
    private static final String CARD_NO_PREFIX  = "W";
    private static final String OLD_CARD_NO_KEY = "old_card_no_seq";
    private static final String OLD_CARD_NO_PRE = "300";

    /**
     * @return
     * @throws Exception 
     */
    public static String buildCardNo() {
        JedisService jedisService = SpringContextUtil.getBean(JedisService.class);
        long seq = jedisService.STRINGS.incrBy(CARD_NO_KEY, 1);
        //生成6位序号
        String fullno = CARD_NO_PRE + StringUtils.leftPad(seq + "", 6, "0");
        //生成96校验码
        String fullnoWith96 = fullno + gen96code(fullno);
        //生成crc16校验码
        int crc16 = 0;
        try {
            crc16 = CRC16.do_crc(fullnoWith96.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(">>>>>>>>生成会员卡号失败", e);
        }
        String crc16code = StringUtils.leftPad(crc16 % 1000 + "", 3, "0");
        return CARD_NO_PREFIX + fullnoWith96 + crc16code;
    }

    /**
     * @return
     * @throws Exception 
     */
    public static String buildOldCardNo() {
        JedisService jedisService = SpringContextUtil.getBean(JedisService.class);
        long seq = jedisService.STRINGS.incrBy(CARD_NO_KEY, 1);
        //生成6位序号
        String fullno = CARD_NO_PRE + StringUtils.leftPad(seq + "", 6, "0");
        //生成96校验码
        String fullnoWith96 = fullno + gen96code(fullno);
        return fullnoWith96;
    }

    private static String gen96code(String no) {
        int[] dvclt = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        int sumNum = 0;
        for (int i = 0 ; i < dvclt.length ; i++) {
            sumNum += (NumberUtils.toInt(String.valueOf(no.charAt(i)))) * dvclt[i];
        }
        //乘以19取余数
        return StringUtils.leftPad(sumNum * 19 % 100 + "", 2, "0");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //生成6位序号
        String fullno = "W30000019369351".substring(1,10);
        //生成96校验码
        String fullnoWith96 = fullno + gen96code(fullno);
        //生成crc16校验码
        int crc16 = 0;
        try {
            crc16 = CRC16.do_crc(fullnoWith96.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(">>>>>>>>生成会员卡号失败", e);
        }
        String crc16code = StringUtils.leftPad(crc16 % 1000 + "", 3, "0");
        System.out.println(CARD_NO_PREFIX + fullnoWith96 + crc16code);
    }

}
