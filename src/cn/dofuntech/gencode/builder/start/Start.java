package cn.dofuntech.gencode.builder.start;

import java.sql.Connection;
import java.util.List;

import cn.dofuntech.gencode.builder.factory.ConnectionFactory;
import cn.dofuntech.gencode.builder.factory.FileWriterFactory;
import cn.dofuntech.gencode.builder.pojo.DataSourceConfig;
import cn.dofuntech.gencode.builder.pojo.Table;
import cn.dofuntech.gencode.builder.util.TableUtil;

import freemarker.template.Configuration;

public class Start {

    private String           sqlxmlPackage;
    private String           pojoPackage;
    private String           voPackage;
    private String           daoPackage;
    private String           servicePackage;
    private String           controllerPackage;
    private String           beanPackage;

    private String           persistencePath;
    private String           adminPath;

    private String           templateUrl;
    private DataSourceConfig cfg;

    public Start(String persistenceModule, String adminModule, String persistencePath, String adminPath, String templateUrl, DataSourceConfig cfg) {
        super();
        this.sqlxmlPackage = "resources.mybatis.curd";
        this.pojoPackage = persistenceModule + ".repository.domain";
        this.voPackage = persistenceModule + ".repository.vo";
        this.daoPackage = persistenceModule + ".repository.mapper";
        this.servicePackage = adminModule + ".service";
        this.controllerPackage = adminModule + ".controller";
        this.beanPackage = adminModule + ".bean";

        this.persistencePath = persistencePath;
        this.adminPath = adminPath;

        this.templateUrl = templateUrl;
        this.cfg = cfg;
    }

    /**
     * @param genTables
     * @param pojo 是否生成pojo
     * @param vo 是否生成vo
     * @param dao 是否生成dao
     * @param service 是否生成service
     * @param controller 是否生成controller
     * @param xml 是否生成xml
     * @throws Exception
     */
    public void pay(String genTables, String comments, boolean pojo, boolean vo, boolean dao, boolean service, boolean controller, boolean xml, boolean page) throws Exception {
        Connection conn = ConnectionFactory.getConnection(cfg);
        List<Table> tables = TableUtil.getTables(conn, genTables, sqlxmlPackage, pojoPackage, voPackage, daoPackage, servicePackage, controllerPackage, beanPackage);
        Configuration configuration = FileWriterFactory.getConfiguration(templateUrl);
        String[] commentArray = comments.split(",");
        int i = 0;
        for (Table table : tables) {
            table.setComment(commentArray[i]);
            if (pojo) {
                outPojo(table, configuration);
            }
            if (dao) {
                outDao(table, configuration);
            }
            if (service) {
                outService(table, configuration);
            }
            if (controller) {
                outController(table, configuration);
            }
            if (cfg.getDataSourceType() != 0 && xml) {
                outXml(table, cfg, configuration);
            }
            if (page) {
                outPage(table, configuration);
            }
            i++;
        }
    }

    /**
     * 生成POJO对象
     * 
     * @param table
     */
    public void outPojo(Table table, Configuration configuration) {
        FileWriterFactory.dataSourceOut(configuration, "pojo.ftl", table, persistencePath + "/src", table.getClassName_d() + ".java", FileWriterFactory.POJO);
    }

    /**
     * 生成DAO对象
     * 
     * @param table
     */
    public void outDao(Table table, Configuration configuration) {
        FileWriterFactory.dataSourceOut(configuration, "mapper.ftl", table, persistencePath + "/src", table.getClassName_d() + "Mapper.java", FileWriterFactory.DAO);
    }

    /**
     * 生成IMP对象
     * 
     * @param table
     */
    public void outService(Table table, Configuration configuration) {
        FileWriterFactory.dataSourceOut(configuration, "service.ftl", table, adminPath + "/src", table.getClassName_d() + "Service.java", FileWriterFactory.SERVICE);
        FileWriterFactory.dataSourceOut(configuration, "imp.ftl", table, adminPath + "/src", table.getClassName_d() + "ServiceImpl.java", FileWriterFactory.IMPL);
    }

    public void outController(Table table, Configuration configuration) {
        FileWriterFactory.dataSourceOut(configuration, "controller.ftl", table, adminPath + "/src", table.getClassName_d() + "Controller.java", FileWriterFactory.CONTROLLER);
    }

    public void outPage(Table table, Configuration configuration) {
        FileWriterFactory.dataSourceOut(configuration, "manage.ftl", table, adminPath, "manage.jsp", FileWriterFactory.MANAGER);
        FileWriterFactory.dataSourceOut(configuration, "add.ftl", table, adminPath, "add.jsp", FileWriterFactory.ADD);
        FileWriterFactory.dataSourceOut(configuration, "edit.ftl", table, adminPath, "edit.jsp", FileWriterFactory.EDIT);
    }

    /**
     * 生成xml文件
     * 
     * @param table
     */
    public void outXml(Table table, DataSourceConfig cfg, Configuration configuration) {
        String fileName = "";
        switch (cfg.getDataSourceType()) {
            case DataSourceConfig.MYSQL:
                fileName = "xml.ftl";
                break;
            case DataSourceConfig.SQLSERVER:
                fileName = "sqlserver.xml";
                break;
        }
        FileWriterFactory.dataSourceOut(configuration, fileName, table, persistencePath, table.getClassName_x() + "-mapper.xml", FileWriterFactory.XML);
    }
}
