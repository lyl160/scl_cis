package cn.dofuntech.dfauth.service;

import java.util.List;

import cn.dofuntech.core.service.DunfengService;
import cn.dofuntech.dfauth.bean.Dict;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:DictService.java 
 */
public interface DictService extends DunfengService<Dict> {

	List<Dict> queryDict();

	Integer getDictBySeqNumMax(Dict dict);

	int getDictByDictIdMax();

	void updateStatus(Dict dict);

	void saveList(List<Dict> dict);
	
}