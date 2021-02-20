package cn.dofuntech.cis.admin.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import cn.dofuntech.cis.admin.repository.mapper.NoticeMapper;
import cn.dofuntech.cis.admin.service.NoticeService;
import cn.dofuntech.core.service.impl.DunfengServiceImpl;
import cn.dofuntech.cis.admin.repository.domain.Notice;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:NoticeServiceImpl.java 
 */
@Service
public class NoticeServiceImpl extends DunfengServiceImpl<Notice> implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

	@Override
	public void upAllStatus(Map<String, Object> param) {
		noticeMapper.upAllStatus(param);
	}

}