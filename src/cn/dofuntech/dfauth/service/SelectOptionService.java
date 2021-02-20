package cn.dofuntech.dfauth.service;

import java.util.Map;

/**数据字典查询接口
 * 下拉框查询接口
 * 创建时间：2017-12-27 下午12:06:33
 * @author lk
 */
public interface SelectOptionService {
	
	/**
	 * 获取下拉框对象
	 * @param did   码表代码
	 * @param value 默认值
	 * @param type  类型
	 * @return String
	 */
	public String querySelectOption(String did,String value,String type);

	/**
	 * 查询所有数据字典信息
	 * @return
	 */
	public Map<String,Object> queryDCIT();

}
