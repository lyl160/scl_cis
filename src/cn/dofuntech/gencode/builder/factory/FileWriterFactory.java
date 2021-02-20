package cn.dofuntech.gencode.builder.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import cn.dofuntech.gencode.builder.pojo.Table;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FileWriterFactory {

    private static Configuration cfg;
    public static final int      POJO       = 1;
    public static final int      VO         = 2;
    public static final int      DAO        = 3;
    public static final int      SERVICE    = 4;
    public static final int      CONTROLLER = 5;
    public static final int      XML        = 6;
    public static final int      BEAN       = 7;
    public static final int      IMPL       = 8;
    public static final int      MANAGER    = 9;
    public static final int      ADD        = 10;
    public static final int      EDIT       = 11;

    public static Configuration getConfiguration(String url) {
        if (cfg == null) {
            cfg = new Configuration();
            url = FileWriterFactory.class.getResource("/").getPath() + url;
            System.out.println(url);
            File file = new File(url);
            try {
                cfg.setDirectoryForTemplateLoading(file);
                cfg.setObjectWrapper(new DefaultObjectWrapper());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cfg;
    }

    /**
     * 
     * @param cfg
     *            解析对象
     * @param templateName
     *            模板名称
     * @param root
     *            数据对象
     * @param packageName
     *            包名称
     * @param fileName
     *            生成文件名称
     */
    public static void dataSourceOut(Configuration cfg, String templateName, Table table, String path, String fileName, int type) {

        Template temp = null;
        try {
            temp = cfg.getTemplate(templateName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Writer out = null;
        try {
            String packageName = "";
            switch (type) {
                case POJO:
                    packageName = table.getPojoPackage();
                    break;
                case DAO:
                    packageName = table.getDaoPackage();
                    break;
                case VO:
                    packageName = table.getVoPackage();
                    break;
                case SERVICE:
                    packageName = table.getServicePackage();
                    break;
                case IMPL:
                    packageName = table.getServicePackage() + ".impl";
                    break;
                case CONTROLLER:
                    packageName = table.getControllerPackage();
                    break;
                case BEAN:
                    packageName = table.getBeanPackage();
                    break;
                case XML:
                    packageName = table.getSqlxmlPackage();
                    break;
                case MANAGER:
                    packageName = "web.WEB-INF.page." + table.getClassName_x().toLowerCase();
                    break;
                case ADD:
                    packageName = "web.WEB-INF.page." + table.getClassName_x().toLowerCase();
                    break;
                case EDIT:
                    packageName = "web.WEB-INF.page." + table.getClassName_x().toLowerCase();
                    break;
            }
            packageName = packageName.replace(".", "/");
            String url = path + "/" + packageName + "/" + fileName;
            File file = new File(url);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            out = new FileWriter(file);
            temp.process(table, out);
            temp.process(table, new OutputStreamWriter(System.out));
            out.flush();
        }
        catch (TemplateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void dataSourceOut(Configuration cfg, String templateName, Object root, String fileName) {

        Template temp = null;
        try {
            temp = cfg.getTemplate(templateName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Writer out = null;
        try {
            if (fileName != null && !"".equals(fileName)) {
                String packageName = "";
                packageName = packageName.replace(".", "/");
                String url = "src/" + packageName + "/" + fileName;
                File file = new File(url);
                out = new FileWriter(file);
                temp.process(root, out);
            }
            temp.process(root, new OutputStreamWriter(System.out));
            out.flush();
        }
        catch (TemplateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
