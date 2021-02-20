package cn.dofuntech.dfauth.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.dfauth.bean.Dict;
import cn.dofuntech.dfauth.repository.mapper.DictMapper;
import cn.dofuntech.dfauth.service.DictService;
import cn.dofuntech.dfauth.util.DictUtils;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:DictServiceImpl.java 
 */
@Service
public class DictServiceImpl extends DunfengServiceImpl<Dict> implements DictService {

	private static Logger  log = LoggerFactory.getLogger(DictServiceImpl.class);
	private static boolean bFlag = false;
	private Dict entity;
	
    @Resource
    private DictMapper dictMapper;
    
	@Override
	public List<Dict> queryDict() {
		log.info("开始初始化数据字典...");
		List dictList = new ArrayList();
		if (entity == null) {
			entity = new Dict();
		}
		try {
			dictList = dictMapper.query();
			log.debug("数据字典=[{}]", dictList);
			if (!bFlag) {
				log.info("开始同步数据字典...");
				DictUtils.updateDictMap(dictList);
				log.info("初始化数据字典表成功！");
			}
		} catch (Exception e) {
			log.error("查询字典表出现错误！", e);
		}
		return dictList;
	}
	
	@Override
	public Integer getDictBySeqNumMax(Dict dict) {
		return dictMapper.selectDictBySeqNumMax(dict);
	}

	@Override
	public int getDictByDictIdMax() {
		return dictMapper.selectDictByDictIdMax();
	}

	@Override
	public void updateStatus(Dict dict) {
		dictMapper.updateStatus(dict);
	}

	@Override
	public void saveList(List<Dict> list) {
		int rv = 0;
		int index = 1;
		try {
			Dict dict = new Dict();
			dict.setParentId(((Dict) list.get(0)).getParentId());
			int dict_id = getDictByDictIdMax();
			int seq_num = getDictBySeqNumMax(dict)==null?0:getDictBySeqNumMax(dict);
			for (Dict di : list) {
				di.setDictId(String.valueOf(dict_id + index));
				di.setSeqNum(String.valueOf(seq_num + index));
				di.setDictLevel("2");
				di.setStatus("1");
				rv += this.dictMapper.insertReturnId(di);
				index++;
			}
		} catch (Exception e) {
			log.error("数据字典数值添加异常", e);
		}

		if (rv <= 0) {
			log.error("数据字典数值添加异常");
		}
	}

}