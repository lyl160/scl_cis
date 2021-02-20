<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<br/>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);"
 action="auth/userAdd/add.do" >
	<!-- #section:elements.form -->
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1"> 所属系统： </label>

		<div class="col-sm-9">
			<input type="text" id="dialog_sysName" placeholder="" readonly="readonly"
				class="col-xs-10 col-sm-7" value="运营管理系统" />
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-4">所属学校：</label>

		<div class="col-sm-9">
		
				<label>
					<div class="search_input" style="padding-top: 5px;">
					<select 
						 rel="obj.options"
						 valName="" 
						 textName="" 
						 location="schoolInf/selectoption" 
						 name="agentId" 
						 id="agentId" 
						 data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
	
		</div>
	</div>
	
	

	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-4">用户角色：</label>

		<div class="col-sm-9">
		
				<label>
					<div class="search_input" style="padding-top: 5px;">
					<select 
						 rel="obj.options"
						 valName="" 
						 textName="" 
						 location="auth/selectoption/role.do" 
						 name="roleId" 
						 id="roleId" 
						 data-placeholder="请选择角色" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
	
		</div>
	</div>


	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1-1"> 登陆账号： </label>
		<div class="col-sm-9">
			<!-- 
		 		 * 属性说明:
				 * validate="true"  是否需要验证字段
				 * msg="错误时提示信息"        
				 * datatype="数据类型"
			 -->
			<input type="text" id="dialog_userId" name="userId" placeholder="" maxlength="20"
				class="col-xs-10 col-sm-7"  validate="true" />
		</div>
	</div>

	<!-- /section:elements.form -->
	<div class="space-4"></div>

	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-2"> 密码： </label>

		<div class="col-sm-9">
			<input type="password" id="userPwd" name="userPwd" placeholder=""  validate="true"
				class="col-xs-10 col-sm-7" /> <span
				class="help-inline col-xs-12 col-sm-7"> <span class="middle"></span>
			</span>
		</div>
	</div>
		<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error"
			for="form-field-1"> 用户姓名： </label>

		<div class="col-sm-9">
			<!-- 
		 		 * 属性说明:
				 * validate="true"  是否需要验证字段
				 * msg="错误时提示信息"        
				 * datatype="数据类型"
			 -->
			<input type="text" id="dialog_userName" name="userName" placeholder=""  maxlength="40"
				class="col-xs-10 col-sm-7" validate="true"  />
		</div>
	</div>
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
				class="col-xs-10 col-sm-7"  datatype="email"  />
		</div>
	</div>
	
	<div class="form-group" style="display: none">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1"> 头像： </label>
		<div class="col-sm-9">
			<input class="col-xs-10 col-sm-5" type="text" id="detail" name="detail" readonly="readonly"/>
			<span class="">
				<button class="btn btn-small btn-yellow" type="button" onclick="upload('detail')">
					选择文件
				</button>
				<button class="btn btn-small btn-info" type="button" onclick="agentPicView('detail');">
					预览
				</button>
			</span>
		
		</div>
	</div>
	<!--
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-4">状态：</label>

		<div class="col-sm-9">
		
				<label>
					<input name="userStatus" id="userStatus"  class="ace ace-switch ace-switch-4 btn-empty" type="checkbox" />
					<span class="lbl" data-lbl="启用 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;禁用"></span>
				</label>
	
		</div>
	</div>	  -->
	<div class="form-group">
			
		
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


/**
 * 	初始化下拉框
 */
$(document).ready(function() {
	// loadOption("roleId","","0","auth/selectoption/role.do");

});

function submitForm(obj){
	
	if($("#agentId").val() == null || $("#agentId").val() == ""){
		msg.alert("警告", "请选择用户学校", 'warn');
		return false;
	}
	if($("#roleId").val() == null || $("#roleId").val() == ""){
		msg.alert("警告", "请选择用户角色", 'warn');
		return false;
	}
	
	
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

/**
 * 图片预览
 */
function agentPicView(id){
	if($('#'+id).val()==''){
		alertMsg.alert("错误","图片不存在，请先上传图片!","error");
		return false;
	}
	picview($('#'+id).val());
}

/*
 * 上传带回
 */
function upload(fid){
	openFileUpload({
		fileUploadDone:function(result){
		console.log(result);
			$("#"+fid).val(result.entity);
		}
	});
}

</script>
