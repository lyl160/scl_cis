<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>

<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);"
 action="auth/userEdit/edit.do" id="useredit_form">
	<!-- #section:elements.form -->
	<br/>
	<!-- 用户  -->
	<input type="hidden" name="id" />
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1"> 所属系统： </label>

		<div class="col-sm-9">
			<input type="text" id="form-field-1" placeholder="" readonly="readonly"
				class="col-xs-10 col-sm-7" value="运营管理系统" />
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-4">所属学校：</label>

		<div class="col-sm-9">
		
				<label>
					<div class="search_input" style="padding-top: 0px;">
					<select 
						 rel="obj.options"
						 valName="" 
						 textName="" 
						 location="schoolInf/selectoption" 
						 name="agentId" 
						  id="agentId"
						 defVal="${entity.agentId}"
						 data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
	
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1"> 巡检项目： </label>

		<div class="col-sm-9">
			<input type="text"  style="width: 288px" id="templatename" name="templatename" placeholder=""  maxlength="40"
				class="col-xs-10 col-sm-7" />
				<div style="margin-top: 37px" id="a">
				 <input type="checkbox" value="校务巡查"  onclick="choice()">校务巡查
				 <input type="checkbox" value="后勤巡查" onclick="choice()">后勤巡查
				 <input type="checkbox" value="校内执勤" onclick="choice()">校内执勤
				 <input type="checkbox" value="校外执勤" onclick="choice()">校外执勤
				 </div> 
				 
				 
				
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-4">用户角色：</label>

		<div class="col-sm-9">
		
				<label>
					<div class="search_input" style="pEditing-top: 5px;">
					
						<select 
						 rel="obj.options"
						 valName="" 
						 textName="" 
						 location="auth/selectoption/role.do" 
						 name="roleId" 
						 id="roleId" 
						 data-placeholder="请选择状态" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
	
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1"> 用户姓名： </label>

		<div class="col-sm-9">
			<input type="text" id="userName" name="userName" placeholder=""  maxlength="40"
				class="col-xs-10 col-sm-7" />
		</div>
	</div>
	
	

	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1-1"> 登陆账号： </label>

		<div class="col-sm-9">
			<input type="text" id="userId" name="userId" placeholder=""
				class="col-xs-10 col-sm-7" readonly="readonly"/>
		</div>
	</div>

	<!-- /section:elements.form -->
	<div class="space-4"></div>

	
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1-1"> 手机号： </label>
		<div class="col-sm-9">
			<input type="text" id="dialog_phone" name="phone" placeholder=""
				class="col-xs-10 col-sm-7"  datatype="mobile"  />
		</div>
	</div>
	
	<div class="form-group">
			<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1-1"> 电子邮件： </label>
		<div class="col-sm-9">
			<input type="text" id="dialog_email" name="email" placeholder=""
				class="col-xs-10 col-sm-7"   datatype="email"  />
		</div>
		
	</div>
	
	<div class="form-actions align-right  form-button-box" style="margin-top: 10px">
			<button class="btn btn-info" type="submit">
				<!-- <i class="ace-icon fa fa-check bigger-110"></i>  -->保存
			</button>

			&nbsp; 
			<button class="btn" type="button" onclick="javascript:dialog.close(this);">
				 关闭
			</button>
		</div>
	
</form>
<script type="text/javascript">
<!--

//-->

$(document).ready(function() {
	//加载form
//	loadForm("useredit_form","");
//	alert($("#dlg-useredit").formData);
//	var param = dialog.getById('dlg-useredit');
//	alert(param.formData.id)
	//加载option
//	loadOption("roleId",UID.roleId,"0","auth/selectoption/role.do");
});


function submitForm(obj){
	//自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
	return validateCallback(obj,function(result){
		if(result.rspcod== 200){
			msg.alert("提示", result.rspmsg, 'correct');
			$("#grid-table").trigger("reloadGrid");
			dialog.close(obj);
		}else{
			msg.alert("错误", result.rspmsg + " 错误代码："
					+ result.rspcod, 'error');
		}
	});
}
function choice(){
	var i = 0;
	var templatename = [];
    $('#a').find('input[type="checkbox"]').each(function(){
		if($(this).is(':checked')){
			templatename[i] = $(this).val();
			i++;
		}
	});

	$('#templatename').val(templatename.join(","));
}
</script>
