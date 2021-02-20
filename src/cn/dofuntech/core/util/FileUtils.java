package cn.dofuntech.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileUtils {

    private FileUtils() {
    };

    /**
     * 文件复制
     * @param path 原路径
     * @param path1 目标路径
     */
    public static void IOCopy(String path, String path1) {
        File file = new File(path);
        File file1 = new File(path1);
        if (!file.exists()) {
            System.out.println(file.getName() + "文件不存在");
        }
        else {
            System.out.println("存在");
        }
        byte[] b = new byte[(int) file.length()];
        if (file.isFile()) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(file);
                fos = new FileOutputStream(file1);
                fis.read(b);
                fos.write(b);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (fis != null) {
                    try {
                        fis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (file.isDirectory()) {
            if (!file.exists())
                file.mkdir();
            if (!file1.exists())
                file1.mkdir();
            String[] list = file.list();
            for (int i = 0 ; i < list.length ; i++) {
                IOCopy(path + list[i], path1 + list[i]);
            }
        }
    }

    /**
     * 获得一个文件夹下的所有文件名称
     * @param path
     * @return Collection<String>
     */
    public static Collection<String> getFilenames(String basepath, String path) {
        List<String> filesname = new ArrayList<String>();
        File file = new File(basepath + path);
        File[] lf = file.listFiles();
        if (lf != null) {
            for (int i = 0 ; i < lf.length ; i++) {
                filesname.add(path + lf[i].getName());
            }
        }
        return filesname;
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileExt(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFullName(String filePath) {
        return filePath.indexOf("/") != -1 ? filePath.substring(filePath.lastIndexOf("/") + 1) : filePath.substring(filePath.lastIndexOf("\\") + 1);
    }

    /**
     * @param filePath
     * @return
     */
    public static String getName(String filePath) {
        return filePath.indexOf("/") != -1 ? filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".")) : filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."));
    }
}
