package cn.dofuntech.dfauth.repository.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.dofuntech.core.mybatis.DfBaseDao;
import cn.dofuntech.dfauth.bean.OrgInf;


/**
 * 机构信息 数据库操作接口
 * 
 * @author
 * 
 */
@Repository
public interface OrgDao extends DfBaseDao<OrgInf, Exception> {

	public List<OrgInf> selectListPage(Map<String, Object> cond);

	public int deleteEntityList(Map<String, Object> map);

	public List<OrgInf> selectOrgForAll(String orgId);

	public List<OrgInf> selectOrgForAllByMap(Map<String, Object> map);

	public Integer countOrgForAllByMap(Map<String, Object> map);

	public OrgInf selectParentOrgByorgIdLevel(Map<String, String> map);

	public List<OrgInf> selectOrgListPage(Map<String, Object> cond);

	public int countOrg(Map<String, Object> cond);

	public Integer countOrgStateByOrgId(String orgId);

	public Integer updateCheckOrgName(Map<String, String> con);

	public Integer addCheckOrgName(String orgName);

	public Integer checkOrgByOrgId(String orgId);
	
	public List<OrgInf> querySubordinates(Map<String, Object> cond);
	
	public Integer countOrgForQuerySubordinates(Map<String, Object> cond);
	

}
