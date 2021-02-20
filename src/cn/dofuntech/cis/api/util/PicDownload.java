package cn.dofuntech.cis.api.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2017 bsteel. All Rights Reserved.</font>
 * @author lxu(@2017年11月17日)
 * @version 1.0
 * filename:PicDownload.java 
 */
public class PicDownload {

    /** 
     *  
     * 根据文件id下载文件 
     *  
     *  
     *  
     * @param mediaId 
     *  
     *            媒体id 
     *  
     * @throws Exception 
     */

    public static InputStream getInputStream(String accessToken, String mediaId) {
        InputStream is = null;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id=" + mediaId;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求  
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
            http.connect();
            // 获取文件转化为byte流  
            is = http.getInputStream();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return is;

    }

    /** 
     *  
     * 获取下载图片信息（jpg） 
     *  
     *  
     *  
     * @param mediaId 
     *  
     *            文件的id 
     *  
     * @throws Exception 
     */

    public static void saveImageToDisk(String accessToken, String mediaId, String picName, String picPath) throws Exception {
        InputStream inputStream = getInputStream(accessToken, mediaId);
        byte[] data = new byte[10240];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(picPath + picName);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String accessToken = "";
        String mediaId = "";
        String picName = "";
        saveImageToDisk(accessToken, mediaId, picName, "f:/");
    }
}
