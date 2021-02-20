package cn.dofuntech.cis.admin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.ImReadLogsMapper;
import cn.dofuntech.cis.admin.service.ImReadLogsService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.ImReadLogs;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:ImReadLogsServiceImpl.java 
 */
@Service
public class ImReadLogsServiceImpl extends DunfengServiceImpl<ImReadLogs> implements ImReadLogsService {

    @Resource
    private ImReadLogsMapper imReadLogsMapper;

	@Override
	public List<ImReadLogs> queryAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return imReadLogsMapper.queryAll(map);
	}

	@Override
	public void updateOne(ImReadLogs ir) {
		// TODO Auto-generated method stub
		imReadLogsMapper.updateOne(ir);
	}

}