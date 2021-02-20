package cn.dofuntech.cis.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.TowCodeResult;
import cn.dofuntech.cis.admin.service.TowCodeResultService;

/**
 * 控制器
 * TowCodeResultController
 *
 */

@Controller
@RequestMapping("/towCodeResult")
public class TowCodeResultController extends AdminController<TowCodeResult>{
	
	@Resource
	private TowCodeResultService towCodeResultService;
}
