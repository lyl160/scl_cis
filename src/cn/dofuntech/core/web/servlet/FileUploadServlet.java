package cn.dofuntech.core.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.dofuntech.core.entity.UploadfileEntity;
import cn.dofuntech.core.util.Multimedia;
import cn.dofuntech.core.util.web.RequestUtil;


public class FileUploadServlet extends HttpServlet {

    private static final long   serialVersionUID = 4480619680981030702L;
    private String              uploadFilePath;
    private String              baseUrl;
    private String              allowedExt;
    private long                maxSize;
    private boolean             readSize         = false;
    private static final long   MAX_SIZE         = 50L;
    private static final Logger LOGGER           = LoggerFactory.getLogger(FileUploadServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        DiskFileItemFactory dfif = new DiskFileItemFactory();
        dfif.setSizeThreshold(4096);
        String uploadpath = "/" + Calendar.getInstance().get(1) + "/" + (Calendar.getInstance().get(2) + 1) + "/" + Calendar.getInstance().get(5);
        File temp = new File(this.uploadFilePath + uploadpath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        dfif.setRepository(temp);
        ServletFileUpload sfu = new ServletFileUpload(dfif);

        sfu.setSizeMax(this.maxSize * 1024L * 1024L);
        List fileList = null;
        try {
            fileList = sfu.parseRequest(request);
        }
        catch (FileUploadException e) {
            if ((e instanceof FileUploadBase.SizeLimitExceededException)) {
                RequestUtil.renderJson(UploadfileEntity.getInterance(0, "文件尺寸超过规定大小:" + this.maxSize + "M"), response);
                return;
            }
            e.printStackTrace();
        }

        if ((fileList == null) || (fileList.size() == 0)) {
            RequestUtil.renderJson(UploadfileEntity.getInterance(0, "请选择上传文件"), response);
            return;
        }

        Iterator fileItr = fileList.iterator();

        StringBuilder sb = new StringBuilder();
        String realName = "";
        String localFilePath = "";
        int fileSize = 0;
        while (fileItr.hasNext()) {
            FileItem fileItem = null;
            String path = null;
            long size = 0L;

            fileItem = (FileItem) fileItr.next();

            if ((fileItem != null) && (!fileItem.isFormField())) {
                path = fileItem.getName();

                size = fileItem.getSize();
                if (("".equals(path)) || (size == 0L)) {
                    if (fileSize == 0) {
                        RequestUtil.renderJson(UploadfileEntity.getInterance(0, "请选择上传文件"), response);
                    }

                }
                else {
                    String t_name = path.substring(path.lastIndexOf("\\") + 1);

                    String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);

                    if (this.allowedExt.indexOf(t_ext.toLowerCase()) == -1) {
                        if (fileSize == 0) {
                            RequestUtil.renderJson(UploadfileEntity.getInterance(0, "请上传以下类型的文件:" + this.allowedExt), response);
                        }

                    }
                    else {
                        String prefix = UUID.randomUUID().toString();

                        String u_name = uploadpath + "/" + prefix + "." + t_ext;
                        String cLocalFile = this.uploadFilePath + u_name;
                        try {
                            File file = new File(cLocalFile);
                            fileItem.write(file);
                            if (this.readSize) {
                                int[] imgSize = Multimedia.getImageSize(file);
                                u_name = uploadpath + "/" + prefix + "_" + imgSize[0] + "_" + imgSize[1] + "." + t_ext;
                                File newFile = new File(this.uploadFilePath + u_name);
                                FileUtils.copyFile(file, newFile);
                                realName = prefix + "_" + imgSize[0] + "_" + imgSize[1] + "." + t_ext;
                            }
                            else {
                                u_name = uploadpath + "/" + prefix + "." + t_ext;
                                realName = prefix + "." + t_ext;
                            }
                            localFilePath = this.uploadFilePath + u_name;
                            sb.append(this.baseUrl + u_name);
                            if (fileSize > 0) {
                                sb.append("|");
                            }
                            LOGGER.info("上传文件本地路径：" + cLocalFile);
                            fileSize++;
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        RequestUtil.renderJson(UploadfileEntity.getInterance(0, "", sb.toString(),"" , true), response);
    }

    public void init(ServletConfig config) throws ServletException {

        //扫描Spring，实例service层
        WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        //attachmentService = (AttachmentService)cont.getBean("attachmentService");

        this.uploadFilePath = config.getInitParameter("uploadFilePath");

        this.baseUrl = config.getInitParameter("baseUrl");
        if (StringUtils.isBlank(this.baseUrl)) {
            this.baseUrl = "";
        }

        this.allowedExt = config.getInitParameter("allowedExt");
        if (StringUtils.isBlank(this.allowedExt)) {
            this.allowedExt = "apk,APK";
        }
        String max = config.getInitParameter("maxSize");
        if (StringUtils.isNotBlank(max))
            this.maxSize = NumberUtils.toLong(max, 50L);
        else {
            this.maxSize = 50L;
        }
        String readSizes = config.getInitParameter("readSize");
        if (StringUtils.isNotBlank(readSizes)) {
            this.readSize = BooleanUtils.toBoolean(Boolean.valueOf(this.readSize));
        }
        LOGGER.info("FileUploadServlet init sucess!");
    }
}
