package cn.dofuntech.dfauth.repository.mapper;

import org.springframework.stereotype.Repository;

import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.dfauth.bean.Dict;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:DictMapper.java 
 */

@Repository
public interface DictMapper extends BaseMapper<Dict> {

	Integer selectDictBySeqNumMax(Dict dict);

	int selectDictByDictIdMax();

	void updateStatus(Dict dict);

	int insertReturnId(Dict di);

}