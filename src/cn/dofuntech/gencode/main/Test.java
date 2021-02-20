package cn.dofuntech.gencode.main;

import java.util.ResourceBundle;

import cn.dofuntech.gencode.builder.pojo.DataSourceConfig;
import cn.dofuntech.gencode.builder.start.Start;

public class Test {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("gencode_config");
        String driverClassName = rb.getString("code.jdbc.driverClassName");
        String jdbcUrl = rb.getString("code.jdbc.url");
        String username = rb.getString("code.jdbc.username");
        String password = rb.getString("code.jdbc.password");

        // 得到数据源
        DataSourceConfig cfg = new DataSourceConfig(driverClassName, jdbcUrl, username, password, DataSourceConfig.MYSQL);
        System.out.println(cfg);

        String adminModule = rb.getString("code.admin.module");
        String persistenceModule = rb.getString("code.admin.module");
        String adminPath = rb.getString("code.file.path");
        String persistencePath = rb.getString("code.file.path");
        String tables = rb.getString("code.tables");
        String comments = rb.getString("code.tables.comment");

        Start start = new Start(persistenceModule, adminModule, persistencePath, adminPath, "cn/dofuntech/gencode/builder/template/", cfg);
        // 开始生成
        start.pay(tables, comments, true, true, true, true, true, true, true);
    }
}
