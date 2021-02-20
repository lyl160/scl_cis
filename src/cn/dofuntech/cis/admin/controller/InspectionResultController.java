package cn.dofuntech.cis.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.InspectionResult;
import cn.dofuntech.cis.admin.service.InspectionResultService;

/**
 * 控制器
 * InspectionResultController
 *
 */

@Controller
@RequestMapping("/inspectionResult")
public class InspectionResultController extends AdminController<InspectionResult>{
	
	@Resource
	private InspectionResultService inspectionResultService;
}
