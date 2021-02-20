package cn.dofuntech.cis.admin.service;

import java.util.Map;

import cn.dofuntech.core.service.DunfengService;
import cn.dofuntech.cis.admin.repository.domain.Notice;
/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@@2016年2月2日)
 * @version 1.0
 * filename:NoticeService.java 
 */
public interface NoticeService extends DunfengService<Notice> {
	/**所有通知已读更新
	 * @param param
	 */
	void upAllStatus(Map<String, Object> param);
}