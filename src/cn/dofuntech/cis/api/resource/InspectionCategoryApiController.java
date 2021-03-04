package cn.dofuntech.cis.api.resource;

import java.util.ArrayList;
import java.util.Date;
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

import cn.dofuntech.cis.admin.repository.domain.InspectionCategory;
import cn.dofuntech.cis.admin.service.InspectionCategoryService;
import cn.dofuntech.cis.api.bean.ReturnMsg;
import cn.dofuntech.cis.api.resource.base.BaseController;
import cn.dofuntech.dfauth.bean.UserInf;

@Scope("prototype")
@Path("inspectionCategory")
@Api(value = "inspectionCategory", description = "校园巡查类别查询")
@Produces(MediaType.APPLICATION_JSON)
public class InspectionCategoryApiController extends BaseController {
    private static Logger log = LoggerFactory.getLogger(InspectionCategoryApiController.class);
    @Autowired
    private InspectionCategoryService inspectionCategoryService;

    @POST
    @Path("/category")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @ApiOperation(value = "校务巡查类别查询", notes = "More notes about this method", response = ReturnMsg.class, httpMethod = "POST")
    @ApiResponses(value = {@ApiResponse(message = "无数据", code = 200)})
    public ReturnMsg Category(InspectionCategory isy) {
        ReturnMsg msg = new ReturnMsg();
        Date date = new Date();
        // 查询一级分类
        log.info("分类查询===开始==");
        try {
            UserInf user = getUser();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ilevel", "1");
            param.put("templateId", isy.getTemplateId());
            param.put("schoolId", user.getAgentId());
            List<InspectionCategory> categoryListLevel1 = inspectionCategoryService.query(param);
            // 根据一级pid1查询二级分类
            Map<String, Object> param2 = new HashMap<String, Object>();
            for (InspectionCategory category : categoryListLevel1) {
                param2.put("pid1", category.getId());

                List<InspectionCategory> categoryListLevel2 = inspectionCategoryService.query(param2);
                if (isy.getTemplateName().equals("校内执勤") || isy.getTemplateName().equals("护校队巡查")) {
                    //param2.put("nowTime", nowTime);
                    List<InspectionCategory> categoryListInTime = new ArrayList<>();
                    for (InspectionCategory categoryLv2 : categoryListLevel2) {
                        if (inspectionCategoryService.inTime(categoryLv2, date)) {//检查时间是否符合，考虑自定义时段
                            categoryListInTime.add(categoryLv2);
                        }
                    }
                    category.setList(categoryListInTime);

                } else {
                    category.setList(categoryListLevel2);
                }
            }

            msg.setObj(categoryListLevel1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            msg.setFail("校务巡检类别异常:" + e.getMessage());
        }
        return msg;


    }

}
