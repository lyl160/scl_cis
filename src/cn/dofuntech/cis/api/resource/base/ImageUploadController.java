package cn.dofuntech.cis.api.resource.base;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import cn.dofuntech.core.util.FileUtils;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.util.EnvUtil;
import cn.dofuntech.cis.api.util.ImageUpload;
import cn.dofuntech.qiniu.common.Config;
import cn.dofuntech.qiniu.http.Response;
import cn.dofuntech.qiniu.storage.UploadManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Scope("prototype")
@Path("imageUpload")
@Api(value = "imageUpload", description = "图片上传")
@Produces(MediaType.APPLICATION_JSON)
public class ImageUploadController {

    @Autowired
    private EnvUtil             envUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUploadController.class);

    /**
    *  图片上传 
    */
    @POST
    @Path("/uploadImg")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    @ApiOperation(value = "图片上传", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
    public ReturnMsg uploadImg(String uploadImg) {
        ReturnMsg msg = new ReturnMsg();
        try {
            //图片上传返回图片保存路径
            String fileName = ImageUpload.uploadImgByBase64(uploadImg, envUtil.getSystemFilePath());

            String realName = FileUtils.getFullName(fileName);
            //准备上传
            UploadManager uploadManager = new UploadManager();
            Response res = uploadManager.put(new File(envUtil.getSystemFilePath() + fileName), realName, Config.QINIU_UTOKEN);
            LOGGER.debug(" -- 远程上传文件返回：" + res.bodyString());
            //            if (status == HttpStatus.SC_OK) {
            String realUrl = Config.QINIU_DOMAIN + "/" + realName;

            LOGGER.debug(" -- 文件URL：" + realUrl);

            if (fileName.length() > 0) { //上传成功
                msg.setObj(realUrl);
                msg.setSuccess();
            }
            else {
                msg.setFail("上传失败");
            }
        }
        catch (Exception e) {
            msg.setFail();
            e.printStackTrace();
        }
        return msg;
    }
}
