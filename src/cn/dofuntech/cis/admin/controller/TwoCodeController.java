package cn.dofuntech.cis.admin.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.TwoCode;
import cn.dofuntech.cis.admin.service.TwoCodeService;


/**
 * 控制器
 * TwoCodeController
 *
 */

@Controller
@RequestMapping("/twoCode")
public class TwoCodeController extends AdminController<TwoCode>{
	
	@Resource
	private TwoCodeService twoCodeService;
	
	
}
