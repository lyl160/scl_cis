package cn.dofuntech.cis.admin.controller.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.dofuntech.core.util.ResourceUtils;

/**系统控件
 * 文件上传控制器<br/>
 * 
 */
@Controller
@RequestMapping("/base/")
public class FileUploadController {
	private static final Logger log = LoggerFactory
			.getLogger(FileUploadController.class);
	/**
	 * 文件上传页面
	 * @return
	 */
	@RequestMapping(value = "fileUploadManage")
	public String fileUpoadView(){
		return "base/upload/fileUploadManage";
	} 
	
	/**
	 * 图片浏览
	 * @return
	 */
	@RequestMapping(value = "pic/view")
	public String picView(){
		return "base/upload/picview";
	} 
	
	/**
	 * 图片浏览
	 * @return
	 */
	@RequestMapping(value = "pic/view2")
	public void  picView2( @RequestParam(value="picid",required=false) String fid, HttpServletRequest request, HttpServletResponse response){
		StringBuffer sb = new StringBuffer();
		String path = ResourceUtils.getString("config","system.file.path");
		File file = new File(sb.toString());
		if(null == fid || "".equals(fid)){
			file = new File(request.getSession().getServletContext().getRealPath("/") + "static/icons/auth/root.png");
		}else{
			sb.append(path).append(fid);
			file = new File(sb.toString());
			log.debug("文件路径"+sb.toString());
	        //判断文件是否存在如果不存在就返回默认图标
	        if(!(file.exists() && file.canRead())) {
	            file = new File(request.getSession().getServletContext().getRealPath("/") + "static/icons/auth/root.png");
	        }
		}

        FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			byte[] data = new byte[(int)file.length()];
	        int length = inputStream.read(data);
	        log.debug("文件流大小:[{}B]",length);
	        inputStream.close();

	        response.setContentType("image/jpg");

	        OutputStream stream = response.getOutputStream();
	        stream.write(data);
	        stream.flush();
	        stream.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	   

		
	} 


}
