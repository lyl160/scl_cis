package ${controllerPackage};

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.dofuntech.core.web.AdminController;
import ${pojoPackage}.${className_d};
import ${servicePackage}.${className_d}Service;

/**
 * 控制器
 * ${className_d}Controller
 *
 */

@Controller
@RequestMapping("/${className_x}")
public class ${className_d}Controller extends AdminController<${className_d}>{
	
	@Resource
	private ${className_d}Service ${className_x}Service;
}
