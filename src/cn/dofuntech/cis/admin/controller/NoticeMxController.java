package cn.dofuntech.cis.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.NoticeMx;
import cn.dofuntech.cis.admin.service.NoticeMxService;

/**
 * 控制器
 * NoticeMxController
 *
 */

@Controller
@RequestMapping("/noticeMx")
public class NoticeMxController extends AdminController<NoticeMx>{
	
	@Resource
	private NoticeMxService noticeMxService;
}
