package cn.dofuntech.core.web;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.core.entity.AutoIdEntity;
import cn.dofuntech.core.entity.TimeEnitry;
import cn.dofuntech.core.page.Paginator;
import cn.dofuntech.core.service.BaseService;
import cn.dofuntech.core.service.DunfengService;
import cn.dofuntech.core.util.CopyUtils;
import cn.dofuntech.core.util.web.CustomTimestampEditor;
import cn.dofuntech.core.util.web.WebUtil;

/**
 * <p>
 * <p>
 * </p>
 * <font size=0.25>Copyright (C) 2015 bsteel. All Rights Reserved.</font>
 *
 * @author lxu(@ 2015年11月17日)
 * @version 1.0
 * filename:AdminController.java
 */
public class AdminController<T> extends BaseController<T> {

    private static String mappingModule = StringUtils.EMPTY;

    public static String ROOTPAHH = "http://xunjian.dofuntech.cn";

    @Autowired
    protected BaseService<T> service;

    /**
     * 页面输入框自动转化Dto字段对应类型
     *
     * @param binder HH:mm:ss
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new CustomTimestampEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }


    /**
     * 分页渲染
     *
     * @param totalCount
     * @return
     */
    protected String getPage(long totalCount) {
        return "--分页" + totalCount + "--";
    }


    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/manage")
    public String manage(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Paginator paginator = WebUtil.buildPaginator(params);
        List<T> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        request.setAttribute("list", list);
        request.setAttribute("page", getPage(totalCount));
        return getRequestMapping() + "/manage";
    }


    @RequestMapping(value = "/manage1")
    public String manage1(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Paginator paginator = WebUtil.buildPaginator(params);
        List<T> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        request.setAttribute("list", list);
        request.setAttribute("page", getPage(totalCount));
        return getRequestMapping() + "/manage1";
    }

    @RequestMapping(value = "/manage2")
    public String manage2(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Paginator paginator = WebUtil.buildPaginator(params);
        List<T> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        request.setAttribute("list", list);
        request.setAttribute("page", getPage(totalCount));
        return getRequestMapping() + "/manage2";
    }

    @RequestMapping(value = "/manage3")
    public String manage3(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParameterMap(request);
        Paginator paginator = WebUtil.buildPaginator(params);
        List<T> list = service.query(params, paginator);
        long totalCount = paginator.getTotalCount();
        request.setAttribute("list", list);
        request.setAttribute("page", getPage(totalCount));
        return getRequestMapping() + "/manage3";
    }



    /**
     * 新增模板
     *
     * @param map $.post('inspectionLogs/oneDay',{},function(result){
     *            var data = result.list;
     *            <p>
     *            var criticism = [];
     *            var praise = [];
     *            var remind = [];
     *            for(var i = 0;i<data.length;i++)
     *            {
     *            criticism[i] = data[i].criticism;
     *            praise[i] = data[i].praise;
     *            remind[i] = data[i].remind;
     *            }
     * @return
     */
    @RequestMapping(value = "/add/view")
    public String addView(ModelMap map) {

        return getRequestMapping() + "/add";
    }

    @RequestMapping("/addView2")
    public String addDictView2() {
        return getRequestMapping() + "/add2";
    }

    @RequestMapping("/addView3")
    public String addDictView3() {
        return getRequestMapping() + "/add3";
    }

    /**
     * @param entity
     * @param map
     * @return
     */
    @RequestMapping(value = "/edit/view")
    public String editView(@ModelAttribute T entity, ModelMap map) {
        AutoIdEntity baseEntity = (AutoIdEntity) entity;
        T entityInDB = service.get(baseEntity.getId());
        map.put("entity", entityInDB);
        return getRequestMapping() + "/edit";
    }

    @RequestMapping(value = "/edit2/view")
    public String editView2(@ModelAttribute T entity, ModelMap map) {
        AutoIdEntity baseEntity = (AutoIdEntity) entity;
        T entityInDB = service.get(baseEntity.getId());
        map.put("entity", entityInDB);
        return getRequestMapping() + "/edit2";
    }

    /**
     * @param entity
     * @return
     */
    @Override
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> save(@ModelAttribute T entity) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
                if (entity instanceof TimeEnitry) {
                    ((TimeEnitry) entity).setAddTime(date);
                }
                doProcessBeforeSave(entity);
                service.insert(entity);

            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                T entityInDB = service.get(baseEntity.getId());
                HttpServletRequest request = getRequest();
                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
                if (entityInDB instanceof TimeEnitry) {
                    ((TimeEnitry) entityInDB).setEditTime(date);
                }
                doProcessBeforeSave(entityInDB);
                service.update(entityInDB);
            }
            result.put(RETURN_CODE, SUCCESS);
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/saveNew", method = RequestMethod.POST)
    public void saveNew(HttpServletResponse response, @ModelAttribute T entity) {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpServletRequest request = getRequest();
//        HttpServletResponse response =  getResponse();
        if (!(entity instanceof AutoIdEntity)) {
            throw new IllegalArgumentException("save(entity) must be instace of AutoIdEntity");
        }

        try {

            AutoIdEntity baseEntity = (AutoIdEntity) entity;
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (baseEntity.getId() == null || baseEntity.getId() == 0) {
                logger.debug("entity.id 为空，新增");
                if (entity instanceof TimeEnitry) {
                    ((TimeEnitry) entity).setAddTime(date);
                }
                doProcessBeforeSave(entity);
                service.insert(entity);

                writeScript(response, "<script>alert('添加成功！');document.location.href='" + getRequestMapping() + "/manage" + "';</script>");

            } else {
                logger.debug("entity.id = " + baseEntity.getId() + ", 修改");
                T entityInDB = service.get(baseEntity.getId());

                Map<String, Object> map = WebUtil.getParameterMap(request);
                CopyUtils.copyProperties(entityInDB, map);
                if (entityInDB instanceof TimeEnitry) {
                    ((TimeEnitry) entityInDB).setEditTime(date);
                }
                doProcessBeforeSave(entityInDB);
                service.update(entityInDB);
                writeScript(response, "<script>alert('修改成功！');document.location.href='" + "/mfl_da/" + getRequestMapping() + "/edit/view?id=" + baseEntity.getId() + "';</script>");

            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 保存之前可对实体做调整
     *
     * @param entity
     */
    protected void doProcessBeforeSave(T entity) {

    }

    /**
     * @param entity
     * @return
     */
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> update(@ModelAttribute T entity) {
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            service.update(entity);
            result.put(RETURN_CODE, SUCCESS);
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }

    /**
     * @param entity
     * @return
     */
    @RequestMapping(value = "/deleteBatch")
    public @ResponseBody
    Map<String, Object> deleteBatch(HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            Type genType = getClass().getGenericSuperclass();
            if (!(genType instanceof ParameterizedType)) {
                result.put(RETURN_CODE, ERROR_UNKNOWN);
                return result;
            }
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (params.length == 0 || !(params[0] instanceof Class)) {
                result.put(RETURN_CODE, ERROR_UNKNOWN);
                return result;
            }
            Class<T> clazz = (Class) params[0];
            T entity = null;
            List<T> entitys = new ArrayList<T>();
            String[] ids = getRequest().getParameter("ids").split(",");
            for (String id : ids) {
                entity = clazz.newInstance();
                if (entity instanceof AutoIdEntity) {
                    ((AutoIdEntity) entity).setId(NumberUtils.toLong(id));
                    entitys.add(entity);
                }

            }
            ((DunfengService<T>) service).deleteBatch(entitys);
            result.put(RETURN_CODE, SUCCESS);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.put(RETURN_CODE, ERROR_UNKNOWN);
            result.put(MSG, ex.getMessage());
        }
        return result;
    }

    /**
     * @return
     */
    private String getRequestMapping() {
        String rval = "";
        RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String[] values = requestMapping.value();
            if (values.length > 0) {
                rval = values[0].substring(0).toLowerCase();
            }
        }
        return rval;
    }
}
