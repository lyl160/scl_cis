package cn.dofuntech.cis.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import sun.misc.BASE64Decoder;

public class ImageUpload {
	
	//手机图片上传 图片采用base64编码
	public static String uploadImgByBase64(String file,String basePath) throws Exception {
  		String fileName="";
	    try {  
	    	String uploadpath = "/" + Calendar.getInstance().get(1) + "/" + (Calendar.getInstance().get(2) + 1) + "/" + Calendar.getInstance().get(5);
	    	File temp = new File(basePath + uploadpath);
	 	    if (!temp.exists()) {
	 	    	temp.mkdirs();
	 	    }
	 	   String prefix = UUID.randomUUID().toString();//生成随机码
	 	   fileName = uploadpath + "/" + prefix + ".png" ;//图片保存完整路径
  	 	  //Base64解码
	 	  file=file.substring(file.indexOf(",")+1, file.length());
	 	  byte[] buffer = new BASE64Decoder().decodeBuffer(file);
	 	  FileOutputStream out = new FileOutputStream(basePath+fileName);
	 	  out.write(buffer);
	 	  out.close();
	      } catch (IOException e) {  
	          e.printStackTrace();  
	      } 
          return fileName;  
      } 
  
}
