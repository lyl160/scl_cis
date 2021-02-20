package cn.dofuntech.cis.admin.repository.mapper;

import java.util.Map;

import org.springframework.stereotype.Repository;
import cn.dofuntech.core.mybatis.BaseMapper;
import cn.dofuntech.cis.admin.repository.domain.Notice;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2015 puredee. All Rights Reserved.</font>
 * @author lxu(@2016年2月2日)
 * @version 1.0
 * filename:NoticeMapper.java 
 */

@Repository
public interface NoticeMapper extends BaseMapper<Notice> {
	/**所有通知已读更新
	 * @param param
	 */
	void upAllStatus(Map<String, Object> param);
}