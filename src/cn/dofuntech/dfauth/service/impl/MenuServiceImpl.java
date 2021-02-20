package cn.dofuntech.dfauth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dofuntech.dfauth.bean.Attributes;
import cn.dofuntech.dfauth.bean.MenuInf;
import cn.dofuntech.dfauth.bean.RoleMenuRelInf;
import cn.dofuntech.dfauth.bean.Tree;
import cn.dofuntech.dfauth.constants.Constant;
import cn.dofuntech.dfauth.repository.mapper.MenuDao;
import cn.dofuntech.dfauth.service.MenuService;
import cn.dofuntech.dfauth.util.UAI;

/**
 * 菜单接口实现
 * @author luokai
 *
 */
@Service
public class MenuServiceImpl implements MenuService {
	private static Logger  log = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Autowired
	private MenuDao menuDao;
	
	
	
	public MenuInf getEntity(MenuInf entity) throws Exception {
		return menuDao.selectEntity(entity);
	}

	public List<MenuInf> getList(MenuInf entity) throws Exception {
		return menuDao.selectList(entity);
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int addEntity(MenuInf entity) throws Exception {
		int rt = -1 ;
		String menuId = "";
		String pid = "";
		if(entity.getMenuParId() == null ||
				entity.getMenuParId().equals("")){
			entity.setMenuParId("00");
		}
		String mid = menuDao.selectMenuId(entity.getMenuParId());
		if(!entity.getMenuParId().equals("00")){
			pid = entity.getMenuParId();
		}

		int id = Integer.valueOf(mid.substring(mid.length()==2?0:mid.length()-2));
		if(id <9){
			menuId = pid + "0" + String.valueOf(id + 1);
		}else{
			menuId = pid + String.valueOf(id + 1);
		}
		if(entity.getMenuCode() == null){
			entity.setMenuCode("");
		}
		if(entity.getSysId() == null){
			entity.setSysId("0000");
		}
		if(entity.getMenuIsLeaf() == null){
			entity.setMenuIsLeaf(1);
		}
		
		entity.setMenuId(menuId);
		try{
			menuDao.updateEntity(new MenuInf(entity.getMenuParId(),0));
			rt = menuDao.insertEntity(entity);
		}catch(Exception e){
			throw new Exception("添加菜单失败",e);
		}
		return rt;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int addList(List<MenuInf> list,String sysId) throws Exception {
		List<RoleMenuRelInf> rms = new ArrayList<RoleMenuRelInf>();
		MenuInf entity = list.get(0);
		
		int rt = -1 ;
		String pid = "";
		String mid ="";
		if(entity.getMenuParId() == null ||
				entity.getMenuParId().equals("") || entity.getMenuParId().equals("0")){
			entity.setMenuParId("00");
		}
		log.info(entity.getMenuParId());
		if(!entity.getMenuParId().equals("00")){
			pid = entity.getMenuParId();
		}
		
		for(MenuInf menui:list){
			//对空字段设置
			if(menui.getMenuCode() == null){
				menui.setMenuCode("");
			}
			if(menui.getSysId() == null){
				menui.setSysId("0001");
			}
			if(menui.getMenuIsLeaf() == null){
				menui.setMenuIsLeaf(1);
			}
			//当添加一级菜单时设定父级为00
			if(menui.getMenuParId() == null || menui.getMenuParId().equals("")){
				menui.setMenuParId("00");
			}
			
			log.info(menui.toString());
		}
		
		try{
			//更新上级叶子节点字段
			if(entity.getMenuType() != null && entity.getMenuType().equals("0")){
		        if(!entity.getMenuParId().equals("00")){
		        	log.debug(new MenuInf(entity.getMenuParId(),0).toString());
		        	menuDao.updateEntity(new MenuInf(entity.getMenuParId(),0));
		        }
			}
			//插入数据
			for(MenuInf menu:list){
				//menu.setSysId(sysId);
				rt += menuDao.insertEntity(menu);
				rms.add(new RoleMenuRelInf(UAI.TOP_ROLE_ID,menu.getMenuId()));
			}
			//菜单权限直接赋给超级管理员
			addRoleMenuButtonRelInf(rms);

		}catch(Exception e){
			throw new Exception("添加菜单失败",e);
		}
		return rt;
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int modifyEntity(MenuInf entity) throws Exception {
		if(entity.getMenuUrl() != null){
			entity.setMenuUrl(entity.getMenuUrl().trim());
		}
		if(entity.getMenuName() != null){
			entity.setMenuName(entity.getMenuName().trim());
		}
		return menuDao.updateEntity(entity);
	}

	public int modifyList(List<MenuInf> list) {
		return 0;
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int removeEntity(MenuInf entity) throws Exception {
		return menuDao.deleteEntity(entity);
	}



	public List<MenuInf> queryAuthAccording(Integer u_Id,String sysId){
		
		
		List <MenuInf> menuList = menuDao.selectAuthMenu(u_Id);
		
		List<MenuInf> result = new ArrayList<MenuInf> ();
		
		getResultAccording(result,menuList);
		
		return result ;
	}
	
	
	
	private void getResultAccording(List<MenuInf> result,
			List<MenuInf> menuList) {
		for(MenuInf menu:menuList){
			log.info(menu.toString());
			if(menu.getMenuParId()!=null){
				if("00".equals(menu.getMenuParId().trim())){
					result.add(menu);
				}
			}
		}
	}

	public List<Tree> queryAuthMenuTree(String parentId,Integer u_Id,String sysId,String agentId) {
		
		List<Tree> tree = null;
		
		List <MenuInf> menuList = menuDao.selectAuthMenu(u_Id);
		
		tree  = makeMenuToTreeForM(menuList,parentId);
		
		return tree;
				
	}
	/**
	 * 带条件的查询用户对应的权限菜单
	 * @author luokai
	 * @param userId   用户编码  用于菜单查询条件   
	 * @return List <Tree>  菜单树 
	 */
	public  List <Tree> getMenuByUid(Integer u_Id) {
		return getMenuByUid(u_Id,null,Constant.SYS_AGENT_ID);
	}
	
	public  List <Tree> getMenuByUid(Integer u_Id,String sysId,String agentId) {
		// TODO Auto-generated method stub
		List<MenuInf> menuList=new ArrayList<MenuInf>();
		List<Tree> MenTree = null;
		try {
			if(u_Id!=null){
				Map<String,Object> paramMap = new HashMap<String, Object>();
				paramMap.put("uId", u_Id);
				paramMap.put("sysId", sysId);
				menuList=menuDao.selectMenuByUid(paramMap);
			}
		    MenTree=makeMenuToTree(menuList,"00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MenTree;
	}

	/**
	 * 将菜单信息中的url ，菜单名字，放置到map<url,menuName>中
	 * @param menuList
	 * @param auditMenu
	 */
	private  void auditMenuListToMap(List<MenuInf> menuList,HashMap<String,String>auditMenu){
		MenuInf menu=new MenuInf();
		for (int i = 0; i < menuList.size(); i++) {
			if(menuList.get(i)!=null){
				menu=menuList.get(i);
				String url=menu.getMenuUrl();
				if(url.isEmpty())continue;//如果url为空
				url=url.trim();
				if(url.trim()=="")continue;//如果url为空
				if(url.contains("?")){
					url=url.substring(0,url.indexOf('?') );
				}
				auditMenu.put(url, menu.getMenuName());
			}
			
		}
	}
	/**
	 * 将菜单信息中的url ，菜单名字，放置到map<url,Checked>中
	 * @param menuList
	 * @param auditMenu
	 */
	private  void MenuListCheckToMap(List<MenuInf> menuList,HashMap<String,Boolean>map){
		MenuInf menu=new MenuInf();
		for (int i = 0; i < menuList.size(); i++) {
			if(menuList.get(i)!=null){
				menu=menuList.get(i);
				String url=menu.getMenuUrl();
				if(url.isEmpty())continue;//如果url为空
				url=url.trim();
				if(url.trim()=="")continue;//如果url为空
				if(url.contains("?")){
					url=url.substring(0,url.indexOf('?') );
				}
				map.put(url, menu.isChecked());
			}
			
		}
		log.debug("权限集合:[{}]",map.toString());
	}
	/**
	 * 将菜单信息中的url ，放置到set<url>中
	 * @param menuList
	 * @param auditSet
	 */
	private  void MenuListToSet(List<MenuInf> menuList,Set<String>auditSet){
		MenuInf menu=new MenuInf();
		for (int i = 0; i < menuList.size(); i++) {
			if(menuList.get(i)!=null){
				menu=menuList.get(i);
				String url=menu.getMenuUrl();
				if(url.isEmpty())continue;//如果url为空
				url=url.trim();
				if(url.trim()=="")continue;//如果url为空
				if(url.contains("?")){
					url=url.substring(0,url.indexOf('?') );
				}
				auditSet.add(url);
			}
			
		}
	}
	/**
	 * 带条件的查询审计对应的菜单--展示，带有所有的菜单
	 * @author luokai
	 * @param auditId    审计-编码  用于菜单查询条件
	 * @return List <Tree>  菜单树 
	 */
	public  List <Tree> getMenuByAudit(String auditId) {
		// TODO Auto-generated method stub
		List<MenuInf> menuList=new ArrayList<MenuInf>();
		List<Tree> MenTree = null;
		try {
			if(auditId!=null&&auditId!=""){
				menuList=menuDao.selectMenuByAuditId(auditId);
			}
		    MenTree=makeMenuCheckToTree(menuList,"00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MenTree;
	}
	/**
	 * 带条件的查询用户对应的权限菜单
	 * @author luokai
	 * @param roleId    角色-编码  用于菜单查询条件
	 * @param userId   用户-编码  用于菜单查询条件     
	 * @return List <Tree>  菜单树 
	 */
	public  List <Tree> getMenuByRidUid(String roleId, Integer u_Id,String sysId) {
		// TODO Auto-generated method stub
		List<MenuInf> menuList=new ArrayList<MenuInf>();
		List<Tree> MenTree = null;
		try {
			if(u_Id!=null&&roleId!=null&&roleId!=""){
				HashMap<String,Object> map=new HashMap<String,Object>();
			   map.put("u_Id",u_Id);
			   map.put("roleId",roleId);
			   map.put("sysId",sysId);
				menuList=menuDao.selectMenuByUidRid(map);
			}
		    MenTree=makeMenuCheckToTree(menuList,"00");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MenTree;
	}
	/**
	 * 使用父节点和菜单list 重组为List<Tree>  结构的树 ，不带checked 校验
	 * @param menuList  需要处理的菜单列表
	 * @param parentId   菜单的父节点编码
	 * @return  List<Tree> 格式的树
	 */
	private List<Tree> makeMenuToTree(List<MenuInf> menuList,String parentId) {
		List<Tree> treeList = new ArrayList<Tree>();
		for(MenuInf menu:menuList){
			if(menu.getMenuParId()!=null){
				if(parentId.equals(menu.getMenuParId().trim())){
					Tree tree = new Tree();
					tree.setId(menu.getMenuId());
					tree.setText(menu.getMenuName());
					tree.setMenuParId(menu.getMenuParId());
					//菜单属性元素
					Attributes attr = new Attributes();
					attr.setMenuStatus(menu.getMenuStatus());
					attr.setMenuType(menu.getMenuType());
					
					//将属性设置到树节点
					tree.setAttributes(attr);
					addChildrenToTree(tree.children,menu.getMenuId().trim(),menuList);
					tree.setChecked(false);
					treeList.add(tree);
				}
			}
		}
		return treeList;
	}
	
	/**
	 * 使用父节点和菜单list 重组为List<Tree>  结构的树 ，带checked 校验
	 * @param menuList  需要处理的菜单列表
	 * @param parentId   菜单的父节点编码
	 * @return  List<Tree> 格式的树
	 */
	private List<Tree> makeMenuCheckToTree(List<MenuInf> menuList,String parentId) {
		List<Tree> treeList = new ArrayList<Tree>();
		boolean checked=true;
		Attributes attr;
		for(MenuInf menu:menuList){
			if(menu.getMenuParId()!=null){
				if(parentId.equals(menu.getMenuParId().trim())){
					Tree tree = new Tree();
					tree.setId(menu.getMenuId());
					tree.setText(menu.getMenuName());
					tree.setMenuParId(menu.getMenuParId());
					
					//菜单属性元素
					attr = new Attributes();
					attr.setMenuStatus(menu.getMenuStatus());
					attr.setMenuType(menu.getMenuType());
					
					//将属性设置到树节点
					tree.setAttributes(attr);
					
//					log.info("menu.getMenuId()="+menu.getMenuId());
					/**
					 * boolean 用&& 计算时，如 a&&b  如果a为 false ,则不再执行或者判断 b. 所以这个&&的左右顺序很重要。
					 */
					checked=addChildrenCheckToTree(tree.children,menu.getMenuId().trim(),menuList)&&menu.isChecked();
					tree.setChecked(checked);
					treeList.add(tree);
				}
			}
		}
		return treeList;
	}

	private void addChildrenToTree(List<Tree> treeList,String parentId,List<MenuInf> menuList) {
		Attributes attr;
		for(MenuInf menu:menuList){
		if(menu.getMenuParId()!=null){
			if(parentId.equals(menu.getMenuParId().trim())){
				Tree tree = new Tree();
				tree.setId(menu.getMenuId());
				tree.setText(menu.getMenuName());
				tree.setMenuParId(menu.getMenuParId());
				addChildrenToTree(tree.children,menu.getMenuId().trim(),menuList);
				tree.setChecked(false);
				
				//菜单属性元素
				attr = new Attributes();
				attr.setMenuStatus(menu.getMenuStatus());
				attr.setMenuType(menu.getMenuType());
				
				//将属性设置到树节点
				tree.setAttributes(attr);
				
				treeList.add(tree);
			}
		}
	}
}
	private boolean addChildrenCheckToTree(List<Tree> treeList,String parentId,List<MenuInf> menuList) {
		Attributes attr;
		boolean checked=true;
		for(MenuInf menu:menuList){
//		log.info("menu.getMenuId()="+menu.getMenuId()+"addChildrenCheckToTree  getMenuParId "+menu.getMenuParId()+"  parentId="+parentId);
		if(menu.getMenuParId()!=null){
			if(parentId.equals(menu.getMenuParId().trim())){
				Tree tree = new Tree();
				tree.setId(menu.getMenuId());
				tree.setText(menu.getMenuName());
				tree.setMenuParId(menu.getMenuParId());
				
				//菜单属性元素
				attr = new Attributes();
				attr.setMenuStatus(menu.getMenuStatus());
				attr.setMenuType(menu.getMenuType());
				
				//将属性设置到树节点
				tree.setAttributes(attr);
				/**
				 * boolean 用&& 计算时，如 a&&b  如果a为 false ,则不再执行或者判断 b. 所以这个&&的左右顺序很重要。
				 */
				boolean chk=addChildrenCheckToTree(tree.children,menu.getMenuId().trim(),menuList)&&menu.isChecked();
				tree.setChecked(chk);
				checked=chk&&checked;
				treeList.add(tree);
			}
		}
	}
		return checked;
}
	private List<Tree> makeMenuToTreeForM(List<MenuInf> menuList,String parentId) {
		
		List<Tree> treeList = new ArrayList<Tree>();
		
		for(MenuInf menu:menuList){
			if(menu.getMenuParId()!=null){
				if(parentId.equals(menu.getMenuParId().trim())){
					Tree tree = new Tree();
					tree.setId(menu.getMenuId());
					tree.setText(menu.getMenuName());
					Attributes attr = new Attributes();
					attr = new Attributes();
					attr.setUrl(menu.getMenuUrl());
					//菜单属性元素
					attr.setMenuStatus(menu.getMenuStatus());
					attr.setMenuType(menu.getMenuType());
					
					//将属性设置到树节点
					tree.setAttributes(attr);
					addChildrenToTreeForM(tree.children,menu.getMenuId().trim(),menuList);
					treeList.add(tree);
				}
			}
		}
		return treeList;
	}

	private void addChildrenToTreeForM(List<Tree> treeList,String parId,List<MenuInf> menuList) {
		for(MenuInf menu:menuList){
			if(menu.getMenuParId()!=null){
				if(parId.equals(menu.getMenuParId().trim())){
					Tree tree = new Tree();
					tree.setId(menu.getMenuId());
					tree.setText(menu.getMenuName());
					Attributes attr = new Attributes();
					attr.setMenuStatus(menu.getMenuStatus());
					attr.setMenuType(menu.getMenuType());
					attr.setUrl(menu.getMenuUrl());
					tree.setAttributes(attr);
					addChildrenToTreeForM(tree.children,menu.getMenuId().trim(),menuList);
					treeList.add(tree);
				}
			}
		}
	}

	public Integer getCount(MenuInf menu) {
		
		int i = 0;
		try {
			i= menuDao.countEntity(menu);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public List<MenuInf> getListPage(String menuName, int start, int end) {
		
		HashMap <String,Object> cond = new HashMap<String,Object>();
		
		cond.put("menuName", menuName);
		cond.put("pageSize", "10");
		cond.put("start", start);
		cond.put("end", end);
		
		return menuDao.findListPage(cond);
	}
	
	public List<MenuInf> getListPage(MenuInf menuInf, int start, int end) {
		HashMap <String,Object> cond = new HashMap<String,Object>();
		
		cond.put("menuName", menuInf.getMenuName());
		cond.put("menuUrl", menuInf.getMenuUrl());
		cond.put("menuType", menuInf.getMenuType());
		cond.put("menuStatus",menuInf.getMenuStatus());
		cond.put("pageSize", menuInf.getPageSize());
		cond.put("sysId", menuInf.getSysId());
		cond.put("start", start);
		cond.put("end", end);
		
		return menuDao.findListPage(cond);
	
	}


	public int removeList(String[] menuId) throws Exception{
		/*
		int count = 0;
		try {
			//删除父节点
			for(String id:menuId){
				count = menuDao.deleteMenu(id);
				count +=count;
				
				//查询该菜单对应的父节点的子节点数。如果子节点为0，将叶子节点设置为1
				//int menuDao.
			}
			
			if(count <=0){
				throw new Exception("删除菜单失败！");
			}
			return 0;
		} catch (Exception e) {
			throw new Exception("删除菜单失败！",e); 
		}*/
		return 0;
	}
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public int removeList(List<MenuInf> list) throws Exception {
		List<String> rms = new ArrayList<String>();
		int count = 0;
		try {
			MenuInf menuInf = new MenuInf();
			for(MenuInf menui:list){
				//SQL语句执行递归删除
				count = menuDao.deleteMenu(menui.getMenuId());
				
				count +=count;
				
				if(!menui.getMenuParId().equals("00") && menui.getMenuType().equals("0")){
					//查询该菜单对应的父节点的子节点数。如果子节点为0，将叶子节点设置为1
					menuInf.setMenuParId(menui.getMenuParId());
					int num = menuDao.countEntity(menuInf);
					if(num == 0){
						//叶子节点设为1
						menuDao.updateEntity(new MenuInf(menui.getMenuParId(),1));
					}
				}
				rms.add(menui.getMenuId());
			}
			removeRoleMenuButtonRelInf(rms);
			if(count <=0){
				throw new Exception("删除菜单失败！");
			}
			return 0;
		} catch (Exception e) {
			throw new Exception("删除菜单失败！",e); 
		}
	
	}

	/**
	 * 根据用户Id查询权限HashMap<String,Boolean>
	 * @return 
	 */
	public  HashMap<String, Boolean> queryAuthMap(Integer u_Id,String sysId) {
		// TODO Auto-generated method stub
		HashMap<String,Boolean> auditMap=new HashMap<String,Boolean>();
		List<MenuInf> menuList = null;
		try{
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("u_Id", u_Id);
			map.put("sysId", sysId);
		    menuList= menuDao.selectMenuUrlByUid(map);
		    log.debug("用户[{}]的菜单权限：[{}]",u_Id,menuList);
		}catch(Exception e){
			log.error("菜单权限地址查询异常!",e);
		}
		MenuListCheckToMap(menuList, auditMap);
		return auditMap;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int modifyMenuStatus(String ids, Integer status) throws Exception {
		Map<String, Object> con = new HashMap<String, Object>(2);
		con.put("menuStatus", status);
		con.put("menuId", ids);
		log.debug(ids);
		int num = 0;
		try {
			num =menuDao.updateMenuStatus(con);
			if(num <= 0){
				throw new Exception("菜单状态更新失败！");
			}
		} catch (Exception e) {
			log.error("菜单状态更新异常！",e);
			throw new Exception("菜单状态更新异常！",e);
		}
		return num;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int addRoleMenuButtonRelInf(List<RoleMenuRelInf> roleMenuButtonRelInf) throws Exception {
		int num = 0;
		try{
			for(RoleMenuRelInf rm:roleMenuButtonRelInf){
				num = num + menuDao.insertMenuRoleRel(rm);
			}
	        if(num != roleMenuButtonRelInf.size()){
	        	throw new Exception("菜单角色关系添加失败！");
	        }
		}catch(Exception e){
			log.error("菜单角色关系添加异常！",e);
			throw new Exception("菜单角色关系添加异常！",e);
		}
		return num;
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int removeRoleMenuButtonRelInf(List<String> menuIds)
			throws Exception {
		int num = 0;
		try{
			for(String menuId:menuIds){
				num = num + menuDao.deleteMenuRoleRel(menuId);
			}
			/*
	        if(num <= 0){
	        	throw new Exception("菜单角色关系删除失败！");
	        }*/
		}catch(Exception e){
			log.error("菜单角色关系删除异常！",e);
			throw new Exception("菜单角色关系删除异常！",e);
		}
		return num;
	}


	public List<MenuInf> getListPage(MenuInf menuInf){
		HashMap <String,Object> cond = new HashMap<String,Object>();
		try {
			cond.putAll(menuInf.toMap());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menuDao.findListPage(cond);
	}

	@Override
	public String[] queryMenuIds(String sysId) throws Exception {
		
		String menuIds[] = null;
		List<MenuInf> list = getList(new MenuInf(sysId,0));
		menuIds = new String[list.size()];
		int index = 0;
		for(MenuInf menu:list){
			menuIds[index] = menu.getMenuId();
			index ++ ;
		}
		return menuIds;
	}

}
