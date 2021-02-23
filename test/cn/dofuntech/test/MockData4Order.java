package cn.dofuntech.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * <p>
 * 
 * </p>
 * @author lxu(@2018年2月22日)
 * @version 1.0
 * filename:MockData4DeviceTurnover.java 
 */
public class MockData4Order extends BaseJunit4Test {

    @Autowired
    //自动注入  
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        System.out.println(">>>>>>>>>>>开始模拟数据");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        String wsUrl = "http://116.255.145.242:8801/qjhsmart/service/smart?wsdl";
//        String method = "receiptFlowHandler";//webservice的方法名 
//        Object[] result = null;
//        try {
//            Client client = dcf.createClient(wsUrl);
//            //调用
//            result = client.invoke(method, JSON.toJSONString(new ReceiptFlowBean()));
//            System.out.println(result[0]);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("%E6%95%A3%E8%A3%85%EF%BC%88%E6%B4%8B%EF%BC%89%E9%B8%A1%E8%9B%8B".startsWith("%"));
        System.out.println(URLDecoder.decode("%E6%95%A3%E8%A3%85%EF%BC%88%E6%B4%8B%EF%BC%89%E9%B8%A1%E8%9B%8B","UTF-8"));
    }

}
