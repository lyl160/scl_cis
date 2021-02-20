package cn.dofuntech.cis.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.task.MyTaskManager;
import cn.dofuntech.core.web.AdminController;

/**
 * 控制器
 * ClazzInfController
 */

@RestController
@RequestMapping("/task")
public class TaskController extends AdminController<ClazzInf> {

    @Resource
    private MyTaskManager myTaskManager;


    @RequestMapping(value = "/xiaoWuAutoScore")
    public String xiaoWuAutoScore(HttpServletRequest request) {
        myTaskManager.xiaoWuAutoScore();
        return "success";
    }

    @RequestMapping(value = "/houQinAutoScore")
    public String houQinAutoScore(HttpServletRequest request) {
        myTaskManager.houQinAutoScore();
        return "success";
    }

}
