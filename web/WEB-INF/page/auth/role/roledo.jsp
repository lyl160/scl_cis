<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<title>新增角色</title>

<link rel="stylesheet" href="static/css/page/role.css" />
<link rel="stylesheet" href="static/css/td.tree.css" />

<div class="tabbable" create-buttom="auto" check-form="true" create-to="role-form-btn-box" style="height: 100%">
	<ul class="nav nav-tabs" id="myRoleTab">
		<li class="active"><a data-toggle="tab" href="#tabRoleInf"> <i
				class="green ace-icon fa fa-home bigger-120"></i> 角色信息
		</a></li>

		<li ><a data-toggle="tab"  href="#tabRoleMenu"> 角色权限 </a></li>

	</ul>
	<form id="roleForm" class="form-horizontal" role="form" onsubmit="return roleInfSubmit()"
		action="auth/roledo/addRole.do">
		<input type="hidden" name="roleId"/>
		<input type="hidden" id="menuCheckValue" name="roleadd_checkValue" />
		<div class="tab-content" style="height: 100%">
			<div id="tabRoleInf" class="tab-pane fade in active" >
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right">
						所属系统： </label>

					<div class="col-sm-9">
							<select id="sysId" name="sysId" >
							  <option value ="0001">运营管理系统</option>
							</select>
							
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right">
						角色名称： </label>
					<div class="col-sm-9">
						<input type="text" name="roleName" placeholder="角色名称" validate="true"  maxlength="80"
							class="col-xs-10 col-sm-7" value="" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right">
						角色状态： </label>
					<div class="col-sm-9">
						<label>
							<input name="roleStatus" id="roleStatus" class="ace ace-switch ace-switch-4 btn-empty" value="0" type="checkbox" />
							<span class="lbl" data-lbl="启用 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;禁用"></span>
						</label>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label no-padding-right">
						角色描述： </label>
					<div class="col-sm-9">
						<div class="col-xs-10 col-sm-7">
							<textarea name="roleDesc" class="form-control limited"  maxlength="100"></textarea>
						</div>
					</div>
				</div>
			</div>

			<div id="tabRoleMenu" class="tab-pane fade">
				<div clss="roleTreeContainer">
					<ul id="roleTree"></ul>
				</div>
				
			</div>
		</div>
		<div class="form-actions align-right form-button-box" id="role-form-btn-box">
			<button class="btn btn-info" type="submit">
				<!-- <i class="ace-icon fa fa-check bigger-110"></i>  -->保存
			</button>
			<button class="btn" type="button" onclick="javascript:dialog.close(this);">
				 关闭
			</button>
		</div>
	</form>
</div>


<script type="text/javascript" src="static/ace_v1.3/assets/js/fuelux/fuelux.tree.js" charset="utf-8"></script>
<script type="text/javascript" src="static/js/core/td.tree.js" charset="utf-8"></script>

<script type="text/javascript">
//页面初始化加载菜单树
var dlgRole ;
$(document).ready(function(){ 
	dlgRole = $("#dlg-role").Dialog();
	loadRoleMenuTree();
}); 
	/**上一步下一步切换*/
	function tabSwitch(id){
		if(id === 'tabRoleMenu'){
			//如果表单验证不通过，不能进入下一步
			var v = TDValidateForm($('#roleForm'));
			if(v === false){
				return;
			}
		}
		$('a[href*="'+id+'"]').trigger('click');
	}
	
	//角色添加，保存
	function roleInfSubmit(){
		var $form = $('#roleForm');
		
		
		var v = TDValidateForm($form);
		if(v === false){
			alertMsg.alert("错误","角色信息填写不完整，请返回填写!","error");
			return false;
		}
		
		//处理菜单选择的值
		doSelectedMenu();
		
		//没有选择权限，不可提交
		if(!$('#menuCheckValue').val()){
			alertMsg.alert("错误","请配置角色权限!","error");
			return false;
		}
		//如果是编辑
		if(isRoleEdit()){
			$form.attr('action','auth/roledo/updateRole.do');
		}
		return validateCallback($form,dialogAjaxDone);
	}
	
	/**处理选中的菜单 ，获得选中菜单的菜单ID并设值到表单元素，不获取枝节点的菜单ID*/
	function doSelectedMenu(){
		//得到选中的节点列表
		var tree = $("#roleTree").tdTree();
		var $selectedElm = tree.getSelectedElm();
		var menuIds = "";
		var i = 0;
		//获取并拼接选中的菜单ID
		$selectedElm.each(function(idx){
			var $li = $(this);
			if(!tree.isLi($li)){
				$li = tree.getThisLiElm($li);
			}
			var menuid = $li.attr('menuid');
			if(TD.isNotEmpty(menuid)){
				if(i > 0){
					menuIds += ",";
				}
				menuIds += menuid;
				i ++;
			}
		});
		$('#menuCheckValue').val(menuIds);//设值到表单元素
	}
	function isRoleEdit(){
		if(dlgRole.dataParam && dlgRole.dataParam['roleId']){
			return true;
		}
		return false;
	}
	
	function loadRoleMenuTree(){
		var sysId = $('#roledoSys').val();
		var url = 'auth/roledo/getRoleMenuForadd.do';//菜单查询
		var roleId = '';
		var roleId = dlgRole.dataParam['roleId'];
		if(isRoleEdit()){
			url = 'auth/roledo/getRoleMenuForupdate.do?roleId=' + roleId;//当前角色菜单查询
		}
		queryMenuTo($("#roleTree"),{url:url,data:{sysId:sysId}});
		
		dialog.resize();
	}
	
</script>

