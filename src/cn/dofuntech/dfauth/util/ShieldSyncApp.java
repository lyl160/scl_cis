package cn.dofuntech.dfauth.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflytek.fsp.shield.java.sdk.constant.HttpConstant;
import com.iflytek.fsp.shield.java.sdk.constant.SdkConstant;
import com.iflytek.fsp.shield.java.sdk.enums.Method;
import com.iflytek.fsp.shield.java.sdk.enums.ParamPosition;
import com.iflytek.fsp.shield.java.sdk.http.ApiClient;
import com.iflytek.fsp.shield.java.sdk.http.BaseApp;
import com.iflytek.fsp.shield.java.sdk.model.ApiRequest;
import com.iflytek.fsp.shield.java.sdk.model.ApiResponse;
import com.iflytek.fsp.shield.java.sdk.model.ApiSignStrategy;
import com.iflytek.fsp.shield.java.sdk.model.ResultInfo;

import cn.dofuntech.cis.admin.repository.domain.SchoolInf;
import cn.dofuntech.cis.admin.repository.domain.TeacherClazzRelInf;
import cn.dofuntech.cis.admin.service.TeacherClazzRelInfService;
import cn.dofuntech.dfauth.constants.Constant;

/**
 * 讯飞接口sdk
 *
 * @author kai.luo
 */
public class ShieldSyncApp extends BaseApp {
    @Autowired
    TeacherClazzRelInfService teacherClazzRelInfService;

    private Logger logger = LoggerFactory.getLogger(ShieldSyncApp.class);

    public ShieldSyncApp() {
    }

    public ShieldSyncApp(String appId, String appSecret, String host, int httpPort, int httpsPort,
                         String publicKey) {
        this.apiClient = new ApiClient();
        this.apiClient.init();
        this.appId = appId;
        this.appSecret = appSecret;
        this.host = host;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
        this.stage = "RELEASE";
        this.equipmentNo = "XXX";
        this.signStrategyUrl = "/getSignStrategy";
        this.tokenUrl = "/getTokenUrl";
        this.publicKey = publicKey;
    }

    /**
     * 用户角色服务
     *
     * @param userId 用户id
     * @return
     * @author ywshi 2018/11/12
     */
    public JSONArray listRoleByUserId(String userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject result = sendHttpRequest(params, "/listRoleByUserId");
            if (null != result && result.getJSONArray("data").size() > 0) {
                jsonArray = JSONArray.parseArray(result.getString("data"));
                return jsonArray;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("/listRoleByUserId调用异常", e);
        }
        return jsonArray;
    }

    /**
     * 根据用户id查询用户基本信息
     *
     * @param userId 用户id
     * @return
     * @author ywshi 2018/11/12
     */
    public JSONObject getUserByUserId(String userId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/getUserByUserId");
        } catch (UnsupportedEncodingException e) {
            logger.error("/getUserByUserId调用异常", e);
        }
        return result;

    }

    /**
     * @param userId 用户id
     * @return
     * @author ywshi 2018/11/12
     */
    public JSONObject getSchool(String schoolId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("schoolId", schoolId);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/getSchool");
        } catch (UnsupportedEncodingException e) {
            logger.error("/getSchool调用异常", e);
        }
        return result;

    }

    /**
     * 获取部门下的用户
     *
     * @return
     */
    public JSONObject listDepartmentInSchool(String schoolId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("schoolId", schoolId);
        JSONObject param = new JSONObject();
        param.put("PAGE_INDEX_MIN", 1);
        param.put("LIMIT_MAX", 10);
        param.put("pageIndex", 1);
        param.put("limit", 20);
        param.put("orderBy", "asc");
        params.put("param", param);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/listDepartmentInSchool");
        } catch (UnsupportedEncodingException e) {
            logger.error("/listDepartmentInSchool调用异常{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取学校下的用户
     *
     * @return
     */
    public JSONObject listUserByRoleInSchool(String schoolId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("schoolId", schoolId);
        params.put("roleEnName", Constant.RoleConstants.TEACHER);
        JSONObject param = new JSONObject();
        param.put("PAGE_INDEX_MIN", 1);
        param.put("LIMIT_MAX", 500);
        param.put("pageIndex", 1);
        param.put("limit", 500);
        param.put("orderBy", "asc");
        params.put("param", param);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/listUserByRoleInSchool");
        } catch (UnsupportedEncodingException e) {
            logger.error("/listUserByRoleInSchool调用异常{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据教师查询行政班级
     *
     * @return
     */
    public JSONObject listOrgClassByTeacher(String teacherId, String teachingCycleId) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(teacherId)) {
            params.put("teacherId", teacherId);
        }
        if (StringUtils.isNotEmpty(teachingCycleId)) {
            params.put("teachingCycleId", teachingCycleId);
        }
        JSONObject param = new JSONObject();
        param.put("PAGE_INDEX_MIN", 1);
        param.put("LIMIT_MAX", 10);
        param.put("pageIndex", 1);
        param.put("limit", 20);
        param.put("orderBy", "asc");
        params.put("param", param);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/listOrgClassByTeacher");
        } catch (UnsupportedEncodingException e) {
            logger.error("/listOrgClassByTeacher调用异常{}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取学校当前教学周期
     *
     * @param schoolId
     * @return
     */
    public JSONObject getCurrentTeachingCycleInSchool(String schoolId) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(schoolId)) {
            params.put("schoolId", schoolId);
        }
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/getCurrentTeachingCycleInSchool");
        } catch (UnsupportedEncodingException e) {
            logger.error("/getCurrentTeachingCycleInSchool调用异常{}", e.getMessage(), e);
        }
        return result;
    }

    public JSONObject teacherTimeTable(String teacherId) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(teacherId)) {
            params.put("teacherId", teacherId);
        }
        //params.put("xn", "2019");
        //params.put("xq", 1);
        //params.put("week", 1);
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, Method.GET, "/teacherTimeTable");
        } catch (UnsupportedEncodingException e) {
            logger.error("/teacherTimeTable调用异常{}", e.getMessage(), e);
        }
        return result;
    }

    public JSONObject sendMesasge() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appId", "a2106507aa2d475085a47f934d272a81");//String
        params.put("bizId", "22");//String 消息相关联的业务id
        params.put("content", "消息内容");//String 消息内容
        params.put("messageDetailLink", "http://www.baidu.com");//String 消息详情url
        params.put("orgId", Constant.SchoolId.GJ);//String 学校id /机构id
        params.put("orgType", 1);//Integer 机构类型：0机构，1:学校,2:集团校
        params.put("receivers", "2390000238000002737");//String 一对一、一对多消息的接收者 （英文逗号分隔表示多个接收者）
        params.put("sendIm", "true");//boolean 是否发送校信消息，默认false 不发送 如果值为true，则参数orgType、orgId 必填
        params.put("sendType",0);//Integer 消息发布类型，默认值为0 （ 0 normal 1 im 2 push）
        params.put("title", "test主题");//String 主题
        params.put("type", 0);    //Integer 消息类型，默认值为 0 0 全部 1 代办事项 2 通知公告 3 系统消息 4 应用消息 7邮件类型
        params.put("userId", "2390000238000002737");//String 用户id
        JSONObject result = new JSONObject();
        try {
            result = sendHttpRequest(params, "/sendMesasge");
        } catch (UnsupportedEncodingException e) {
            logger.error("/sendMesasge调用异常{}", e.getMessage(), e);
        }
        return result;
    }


    /**
     * 发送http请求并且处理返回的结果
     *
     * @param params 请求参数
     * @param method 方法名称
     * @return
     * @throws UnsupportedEncodingException
     * @author ywshi 2018-11-8
     */
    private JSONObject sendHttpRequest(Map<String, Object> params, String method) throws UnsupportedEncodingException {
        return sendHttpRequest(params, Method.POST, method);
    }

    /**
     * 发送http请求并且处理返回的结果
     *
     * @param params 请求参数
     * @param method 方法名称
     * @return
     * @throws UnsupportedEncodingException
     * @author ywshi 2018-11-8
     */
    private JSONObject sendHttpRequest(Map<String, Object> params, Method httpMethod, String method)
            throws UnsupportedEncodingException {
        ApiRequest apiRequest =
                new ApiRequest(HttpConstant.SCHEME_HTTP, httpMethod, method,
                        SdkConstant.AUTH_TYPE_DEFAULT, "1be66879dfd44fc8b4195fe92e1ced4e");
        initSignStrategy(apiRequest);
        if (null != params) {
            logger.info("调用地址:{},方法入参:{}", method, new JSONObject(params).toString());
            Set<String> paramskeys = params.keySet();
            if (paramskeys.size() > 0) {
                for (String key : paramskeys) {
                    if (httpMethod.equals(Method.POST)) {
                        apiRequest.addParam(key, params.get(key), ParamPosition.FORM, true);
                    } else if (httpMethod.equals(Method.GET)) {
                        apiRequest.addParam(key, params.get(key), ParamPosition.QUERY, true);
                    }
                }
            }
        }

        // 发送请求，返回请求结果
        ApiResponse apiResponse = syncInvoke(apiRequest);
        int httpStuts = apiResponse.getStatusCode();
        String resultStr = new String(apiResponse.getBody(), "UTF-8");
        logger.info("调用地址:{},返回的数据:{}", method, resultStr);
        if (httpStuts == HttpStatus.SC_OK) {// 200 请求成功
            JSONObject jsonResult = JSONObject.parseObject(resultStr);
            String code = jsonResult.getString("code");
            if ("1".equals(code)) {// 成功，1：成功，-1：失败
                return jsonResult;
            } else {
                logger.info("调用地址:{},调用失败原因:{}", method, jsonResult.getString("message"));
                return null;
            }
        } else {
            logger.info("调用地址:{},请求调用失败，失败请求状态{}", method, httpStuts);
            return null;
        }
    }

    /**
     * 初始化，获取服务签名策略
     */
    private void initSignStrategy(ApiRequest apiRequest) {
        //获取服务安全策略信息
        ApiSignStrategy signStrategy = super.getSignStrategy(apiRequest.getPath());
        //判断是否需要token校验
        if (null != signStrategy && "token".equals(signStrategy.getSignType())) {
            //从本地缓存获取token信息,如果本地缓存存在token信息，验证本地缓存token的有效次数，
            //1.如果验证通过，token次数-1，回写到本地缓存；
            //2.如果验证不通过，从新获取token信息，并写到本地缓存。

            //从token服务获取token信息
            ResultInfo resultInfo = super.getTokenInfo(signStrategy);
            if (null != resultInfo && SdkConstant.SUCCESS.equals(resultInfo.getCode())) {
                apiRequest.setTokenValue(resultInfo.getData().getTokenValue());
                apiRequest.getHeaders().put(SdkConstant.AUTH_EQUIPMENTNO, equipmentNo);
            } else {
                System.err.println("获取token信息失败");
                System.err.println(resultInfo);
            }
        }
    }

    public void findTeacherAndClazzRel(SchoolInf school) {
        String schoolId = school.getSchoolBm();
        Map<String, String> teacherMap = new HashMap<String, String>();
        String teacherCycleId = null;
        //根据学校id找到全部老师 listUserByRoleInSchool
        JSONObject teacherResult = listUserByRoleInSchool(schoolId);
        if (teacherResult != null && teacherResult.getString("code").equals("1")) {
            JSONArray teacherJsonArray = teacherResult.getJSONArray("data");
            JSONObject teacher;
            if (teacherJsonArray != null) {
                for (Object t : teacherJsonArray) {
                    teacher = (JSONObject) t;
                    teacherMap.put(teacher.getString("id"), teacher.getString("loginName") + "&&&" + teacher.getString("userName"));

                }
            }

        }

        // 获取学校当前教学周期id getCurrentTeachingCycleInSchool
        JSONObject teacherCycleResult = getCurrentTeachingCycleInSchool(schoolId);
        if (teacherCycleResult != null && teacherCycleResult.getString("code").equals("1")) {
            JSONObject teacherCycleJson = teacherCycleResult.getJSONObject("data");
            if (teacherCycleJson != null) {
                teacherCycleId = teacherCycleJson.getString("id");
            }
        }

        //根据教师查询行政班级 listOrgClassByTeacher
        if (StringUtils.isNotEmpty(teacherCycleId) && !teacherMap.isEmpty()) {
            for (String userId : teacherMap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                String userName = teacherMap.get(userId).toString();//
                System.out.println("userId:" + userId + " userName:" + userName);
                JSONObject clazzJsonResult = listOrgClassByTeacher(userId, teacherCycleId);

                if (clazzJsonResult != null && clazzJsonResult.get("code").equals("1")) {
                    JSONArray clazzJsonArray = clazzJsonResult.getJSONArray("data");
                    if (clazzJsonArray != null) {
                        JSONObject clazzJson;
                        String clazzName;
                        for (Object c : clazzJsonArray) {
                            clazzJson = (JSONObject) c;
                            clazzName = clazzJson.getString("className");
                            TeacherClazzRelInf teacherClazzRelInf = new TeacherClazzRelInf(school.getId(), clazzName, userName.split("&&&")[0], userName.split("&&&")[1]);
                            teacherClazzRelInfService.insert(teacherClazzRelInf);
                        }
                    }
                }
            }
        }
        logger.info("同步完成，共同步教师{}", teacherMap.size());
    }
}
