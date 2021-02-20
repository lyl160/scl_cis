package cn.dofuntech.dfauth.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dofuntech.core.util.DateUtils;
import cn.dofuntech.core.util.ReturnMsg;
import cn.dofuntech.core.util.json.JUtil;
import cn.dofuntech.core.web.AdminController;
import cn.dofuntech.dfauth.bean.Dict;
import cn.dofuntech.dfauth.service.DictService;
import cn.dofuntech.dfauth.service.SelectOptionService;

/**
 * 控制器 DictController
 * 
 */

@Controller
@RequestMapping("/dict")
public class DictController extends AdminController<Dict> {

	private static Logger  log = LoggerFactory.getLogger(DictController.class);
	
	@Resource
	private DictService dictService;
	@Resource
	private SelectOptionService selectOptionService;

	@ResponseBody
	@RequestMapping("/dictManage/query")
	public ReturnMsg queryForinitData() {
		ReturnMsg msg = new ReturnMsg();
		try{
			String dict = JUtil.toJsonString(selectOptionService.queryDCIT());
			msg.setObj(dict);
			log.debug(dict);
			msg.setMsg("200", "查询成功！");
		}catch(Exception e){
			log.error(e.getMessage(), e);
			msg.setMsg("202", e.getMessage());
		}
		return msg;
	}

	@RequestMapping({"/dictManage/dictSeq"})
    @ResponseBody
    public ReturnMsg querySeq(HttpServletRequest request) {
      ReturnMsg msg = new ReturnMsg();
      int dict_id = 0; int seq_num = 0;
      try {
        Dict dict = new Dict();
        dict.setParentId("0");
        seq_num = dictService.getDictBySeqNumMax(dict);
        dict_id = dictService.getDictByDictIdMax();
        msg.setMsg("200", "查询成功!");
        log.debug("seq_num={},dict_id={}", Integer.valueOf(seq_num), Integer.valueOf(dict_id));
      } catch (Exception e) {
    	  log.error("查询字典信息异常", e);
        msg.setMsg("201", "查询失败!");
      }
      msg.put("dictId", Integer.valueOf(dict_id + 1));
      msg.put("seqNum", Integer.valueOf(seq_num + 1));
      return msg;
    }
	
	@RequestMapping({"/dictManage/addView1"})
    public String addDictView1(HttpServletRequest request) {
      return "dict/dictAdd1";
    }

	/**保存字典
     * @param dict
     * @return
     */
    @RequestMapping({"/dictAdd1/add"})
    @ResponseBody
    public ReturnMsg addDict1(Dict dict) {
      ReturnMsg msg = new ReturnMsg();
      try {
    	  dict.setParentId("0");
    	  dict.setDictLevel("1");
    	  dict.setAddtime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss "));
    	  dict.setEdittime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss "));
    	  dictService.save(dict);
        msg.setMsg("200", "添加成功！");
      } catch (Exception e) {
    	 log.error(e.getMessage(), e);
        msg.setMsg("201", e.getMessage());
      }
      return msg;
    }
    
    @RequestMapping("/dictManage/editView")
    public String editDictView()
    {
      return "dict/dictEdit";
    }
    
    @RequestMapping({"/dictManage/editData"})
    @ResponseBody
    public ReturnMsg editData(@ModelAttribute("dict") Dict dict) {
		log.debug(dict.toString());
		ReturnMsg msg = new ReturnMsg();
		Dict d = null;
		try {
		    d = dictService.get(Long.valueOf(dict.getDictId()));
		    msg.setMsg("200", "查询成功！");
		    log.debug("查询结果:{}", d.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		    msg.setMsg("201", "查询失败！");
		}
		msg.setObj(d);
		return msg; 
    } 
    
    /**更改数据字典状态   1：正常 0禁用
     * @param ids
     * @return
     */
    @RequestMapping("dictManage/statusSet/{status}")
    @ResponseBody
    public ReturnMsg modifyDictStatus0(@RequestParam("ids") String ids,@PathVariable String status)
    {
      ReturnMsg msg = new ReturnMsg();
      try {
          String[] str = StringUtils.split(ids, ",");
          for (int i = 0; i < str.length; i++) {
        	  Dict dict = new Dict();
        	  dict.setStatus(status);
        	  dict.setDictId(str[i]);
        	  dict.setEdittime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss "));
        	  dictService.updateStatus(dict);
		  }
          msg.setMsg("200", "操作成功！");
      } catch (Exception e) {
    	  log.error(e.getMessage(), e);
          msg.setMsg("202", e.getMessage());
      }
      return msg;
   }
    
    @Override
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(Dict entity) {
    	entity.setEdittime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss "));
    	return super.update(entity);
    }
    
    /**
	 * 更新 数据字典
	 * 
	 * @return
	 */
	@RequestMapping(value = "/up")
	@ResponseBody
	public ReturnMsg upDict() {
		ReturnMsg msg = new ReturnMsg();
		try {
			dictService.queryDict();
		    String dict = JUtil.toJsonString(selectOptionService.queryDCIT());
			msg.setObj(dict);
			log.debug(dict);
			msg.setMsg("200", "查询成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg.setMsg("202", e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping("dictManage/addView2")
    public String addDictView2() {
      return "dict/dictAdd2";
    }
	
	@RequestMapping("dictAdd2/init")
    @ResponseBody
    public Map<String, Object> addDcitInit(Dict dict) {
      ReturnMsg msg = new ReturnMsg();
      List list = new ArrayList();
      Dict di = null;
      try {
    	  log.info(dict.toString());
        di = this.dictService.get(Long.valueOf(dict.getDictId()));
        log.info(di.toString());
      } catch (Exception e) {
    	  log.error(e.getMessage(), e);
      }
      for (int i = 0; i < 10; i++) {
    	  Dict dictCache = new Dict(di.getDictId(), di.getDictCode(), di.getDictName());
    	  dictCache.setId(Long.valueOf(i+1));  
    	  list.add(dictCache);
      }
      Map result = new HashMap();
      result.put("rows", list);

      return result;
    }
	
	/**批量保存字典
     * @param dict
     * @return
     */
    @RequestMapping(value={"/dictAdd2/add"}, method={RequestMethod.POST})
    @ResponseBody
    public ReturnMsg addbutton(@RequestBody List<Dict> dict)
    {
      ReturnMsg msg = new ReturnMsg();
      try
      {
        log.info(dict.toString());
        this.dictService.saveList(dict);
        msg.setMsg("200", "添加数据字典成功！");
      } catch (Exception e) {
        msg.setMsg("201", e.getMessage());
        log.error(e.getMessage(), e);
      }
      return msg;
    }
	
}
