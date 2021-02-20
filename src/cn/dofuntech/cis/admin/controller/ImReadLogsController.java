package cn.dofuntech.cis.admin.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.cis.admin.repository.domain.ImReadLogs;
import cn.dofuntech.cis.admin.service.ImReadLogsService;

/**
 * 控制器
 * ImReadLogsController
 *
 */

@Controller
@RequestMapping("/imReadLogs")
public class ImReadLogsController extends AdminController<ImReadLogs>{
	
	@Resource
	private ImReadLogsService imReadLogsService;
}
