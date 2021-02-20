package cn.dofuntech.dfauth.repository.mapper;

import org.springframework.stereotype.Repository;

import cn.dofuntech.core.mybatis.DfBaseDao;
import cn.dofuntech.dfauth.bean.RoleMenuRelInf;


/**
 * 角色菜单按钮关系 数据库操作接口
 * 
 * @author luokai
 * 
 */
@Repository
public interface RoleMenuRelDao extends DfBaseDao<RoleMenuRelInf, Exception> {

}
