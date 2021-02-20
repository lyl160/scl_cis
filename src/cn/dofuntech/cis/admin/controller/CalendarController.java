package cn.dofuntech.cis.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.core.web.AdminController;

/**
 * 控制器
 * ClazzInfController
 */

@Controller
@RequestMapping("/calendar")
public class CalendarController extends AdminController<ClazzInf> {


    @RequestMapping(value = "/init")
    public String init() {

        return "calendar/init";
    }

}
