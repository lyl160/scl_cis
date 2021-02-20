package cn.dofuntech.cis.api.resource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.admin.repository.domain.TwoCode;
import cn.dofuntech.cis.admin.service.TwoCodeService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.util.ImageUpload;
import cn.dofuntech.cis.bean.EnvUtil;
import cn.dofuntech.core.util.QrCodeUtil;

@Scope("prototype")
@Path("twocode")
@Api(value = "twocode", description = "二维码生成")
@Produces(MediaType.APPLICATION_JSON)
public class TwoCodeApiController {
	
	private static Logger log = LoggerFactory.getLogger(TwoCodeApiController.class);
	@Autowired
	private TwoCodeService twoCodeService;
	@Autowired
	private EnvUtil envUtil;
	

	@GET
	@Path("/generate/{place}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "二维码生成", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg teamClazz(@ApiParam(name = "place", required = true, value = "位置") @PathParam("place")String place){
		ReturnMsg msg = new ReturnMsg();
	   
		try {
			String text = UUID.randomUUID().toString();
			QrCodeUtil.encode(text,QrCodeUtil.getNumber());
			TwoCode t = new TwoCode();
			t.setPlace(place);
			Map<String,Object>  map = new HashMap<String,Object>();
			map.put("place",place);
			map.put("status",0);
			List<TwoCode> query = twoCodeService.query(map);
			if(null != query)
			{
				for(TwoCode tt :query){
				//将其他位置修改为不可用
				tt.setStatus("1");
				twoCodeService.update(tt);
				}
			}
			t.setText(text);
			t.setStatus("0");
			t.setCreateTime(new Timestamp(System.currentTimeMillis()));
			twoCodeService.insert(t);
			msg.setSuccess("二维码生成成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg.setFail("二维码生成失败");
		}
		return msg;
	}
	
	
	@POST
	@Path("/photograph")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.TEXT_XML })
	@ApiOperation(value = "拍照上传", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(message = "无数据", code = 200) })
	public ReturnMsg photograph(TwoCode t){
		ReturnMsg msg = new ReturnMsg();
		
		String fileName;
		try {
			fileName = ImageUpload.uploadImgByBase64(t.getImages(), envUtil.getSystemFilePath());
			log.debug("图片开始上传" + fileName);
			t.setImages(fileName);
			twoCodeService.insert(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
	

}
