package cn.dofuntech.gencode.builder.pojo;

import java.util.List;
import java.util.Set;

public class Table {

    private String            className;         // 原表名称
    private String            className_d;       // 大写表名称
    private String            className_d_p;     // 大写表名称
    private String            className_x;       // 小写表名称
    private String            className_u;       // 小写表名称(首字母大写)

    private String            sqlxmlPackage;
    private String            pojoPackage;
    private String            voPackage;
    private String            daoPackage;
    private String            servicePackage;
    private String            controllerPackage;
    private String            beanPackage;
    private List<TableCarray> tableCarrays;      // 表字段
    private List<TableIndex>  tableIndexs;       // 表索引
    private List<TableBind>   tableBinds;        // 表主外键

    private Set<String>       importPojos;       // 需要导入的POJO

    private String            stringCarrayNames1; // ","拼接大写字段
    private String            stringCarrayNames2; // int id ,String userCord ,..
    private String            stringCarrayNames3; // ","拼接原字段
    private String            stringCarrayNames4; // "#%s#,"拼接小写字段
    private String            stringCarrayNames5; // "%s=#%s#,"拼接原字段-小写字段
    private String            stringCarrayNames6; // ","拼接大写字段
    private String            comment;           //名称

    public Table(String className, String classNameD, String classNameDP, String classNameX, String sqlxmlPackage, String pojoPackage, String voPackage, String daoPackage, String servicePackage, String controllerPackage, String beanPackage, List<TableCarray> tableCarrays, List<TableIndex> tableIndexs, List<TableBind> tableBinds, Set<String> importPojos, String stringCarrayNames1, String stringCarrayNames2, String stringCarrayNames3, String stringCarrayNames4, String stringCarrayNames5, String stringCarrayNames6) {
        super();
        this.className = className;
        className_d = classNameD;
        className_d_p = classNameDP;
        className_x = classNameX;
        this.sqlxmlPackage = sqlxmlPackage;
        this.pojoPackage = pojoPackage;
        this.daoPackage = daoPackage;
        this.voPackage = voPackage;
        this.daoPackage = daoPackage;
        this.servicePackage = servicePackage;
        this.controllerPackage = controllerPackage;
        this.beanPackage = beanPackage;
        this.tableCarrays = tableCarrays;
        this.tableIndexs = tableIndexs;
        this.tableBinds = tableBinds;
        this.importPojos = importPojos;
        this.stringCarrayNames1 = stringCarrayNames1;
        this.stringCarrayNames2 = stringCarrayNames2;
        this.stringCarrayNames3 = stringCarrayNames3;
        this.stringCarrayNames4 = stringCarrayNames4;
        this.stringCarrayNames5 = stringCarrayNames5;
        this.stringCarrayNames6 = stringCarrayNames6;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the beanPackage
     */
    public String getBeanPackage() {
        return beanPackage;
    }

    /**
     * @param beanPackage the beanPackage to set
     */
    public void setBeanPackage(String beanPackage) {
        this.beanPackage = beanPackage;
    }

    /**
     * @return the className_d_p
     */
    public String getClassName_d_p() {
        return className_d_p;
    }

    /**
     * @param className_d_p the className_d_p to set
     */
    public void setClassName_d_p(String className_d_p) {
        this.className_d_p = className_d_p;
    }

    /**
     * @return the sqlxmlPackage
     */
    public String getSqlxmlPackage() {
        return sqlxmlPackage;
    }

    /**
     * @param sqlxmlPackage the sqlxmlPackage to set
     */
    public void setSqlxmlPackage(String sqlxmlPackage) {
        this.sqlxmlPackage = sqlxmlPackage;
    }

    /**
     * @return the voPackage
     */
    public String getVoPackage() {
        return voPackage;
    }

    /**
     * @param voPackage the voPackage to set
     */
    public void setVoPackage(String voPackage) {
        this.voPackage = voPackage;
    }

    /**
     * @return the servicePackage
     */
    public String getServicePackage() {
        return servicePackage;
    }

    /**
     * @param servicePackage the servicePackage to set
     */
    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    /**
     * @return the controllerPackage
     */
    public String getControllerPackage() {
        return controllerPackage;
    }

    /**
     * @param controllerPackage the controllerPackage to set
     */
    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    /**
     * @return the className_u
     */
    public String getClassName_u() {
        return className_u;
    }

    /**
     * @param className_u the className_u to set
     */
    public void setClassName_u(String className_u) {
        this.className_u = className_u;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName_d() {
        return className_d;
    }

    public void setClassName_d(String classNameD) {
        className_d = classNameD;
    }

    public String getClassName_x() {
        return className_x;
    }

    public void setClassName_x(String classNameX) {
        className_x = classNameX;
    }

    public String getPojoPackage() {
        return pojoPackage;
    }

    public void setPojoPackage(String pojoPackage) {
        this.pojoPackage = pojoPackage;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public List<TableCarray> getTableCarrays() {
        return tableCarrays;
    }

    public void setTableCarrays(List<TableCarray> tableCarrays) {
        this.tableCarrays = tableCarrays;
    }

    public List<TableIndex> getTableIndexs() {
        return tableIndexs;
    }

    public void setTableIndexs(List<TableIndex> tableIndexs) {
        this.tableIndexs = tableIndexs;
    }

    public List<TableBind> getTableBinds() {
        return tableBinds;
    }

    public void setTableBinds(List<TableBind> tableBinds) {
        this.tableBinds = tableBinds;
    }

    public Set<String> getImportPojos() {
        return importPojos;
    }

    public void setImportPojos(Set<String> importPojos) {
        this.importPojos = importPojos;
    }

    public String getStringCarrayNames1() {
        return stringCarrayNames1;
    }

    public void setStringCarrayNames1(String stringCarrayNames1) {
        this.stringCarrayNames1 = stringCarrayNames1;
    }

    public String getStringCarrayNames2() {
        return stringCarrayNames2;
    }

    public void setStringCarrayNames2(String stringCarrayNames2) {
        this.stringCarrayNames2 = stringCarrayNames2;
    }

    public String getStringCarrayNames3() {
        return stringCarrayNames3;
    }

    public void setStringCarrayNames3(String stringCarrayNames3) {
        this.stringCarrayNames3 = stringCarrayNames3;
    }

    public String getStringCarrayNames4() {
        return stringCarrayNames4;
    }

    public void setStringCarrayNames4(String stringCarrayNames4) {
        this.stringCarrayNames4 = stringCarrayNames4;
    }

    public String getStringCarrayNames5() {
        return stringCarrayNames5;
    }

    public void setStringCarrayNames5(String stringCarrayNames5) {
        this.stringCarrayNames5 = stringCarrayNames5;
    }

    /**
     * @return the stringCarrayNames6
     */
    public String getStringCarrayNames6() {
        return stringCarrayNames6;
    }

    /**
     * @param stringCarrayNames6 the stringCarrayNames6 to set
     */
    public void setStringCarrayNames6(String stringCarrayNames6) {
        this.stringCarrayNames6 = stringCarrayNames6;
    }

}
