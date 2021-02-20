package cn.dofuntech.cis.api.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import cn.dofuntech.cis.admin.repository.domain.ClazzInf;
import cn.dofuntech.cis.admin.repository.domain.Team;
import cn.dofuntech.cis.admin.service.ClazzInfService;
import cn.dofuntech.cis.admin.service.TeamService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.dfauth.bean.UserInf;


@Scope("prototype")
@Path("clazzInf")
@Api(value = "clazzInf", description = "班级查询")
@Produces(MediaType.APPLICATION_JSON)
public class ClazzInfApiController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ClazzInfApiController.class);

    @Autowired
    private ClazzInfService clazzInfService;
    @Autowired
    private TeamService teamService;


    @POST
    @Path("/teamClazz")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "团队班级查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg teamClazz(ClazzInf clazzInf) {
        ReturnMsg msg = new ReturnMsg();
        try {
            log.info("团队班级查询,开始...");
            UserInf user = getUser();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("schoolId", user.getAgentId());
            List<Team> teamList = teamService.query(param);
            for (Team team : teamList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("teamId", team.getId());
                map.put("status", "0");
                map.put("schoolId", user.getAgentId());
                if (clazzInf != null && clazzInf.getCategory2() != null) {
                    map.put("category2", clazzInf.getCategory2());
                }
                List<ClazzInf> classList = clazzInfService.query(map);
                team.setClazzList(classList);
            }
            msg.setObj(teamList);
            msg.setSuccess("团队班级查询成功");
        } catch (Exception e) {
            msg.setFail("团队班级查询，异常:" + e.getMessage());
            log.error("团队班级查询异常，{}", e.getMessage(), e);
        }
        return msg;
    }


}
