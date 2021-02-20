<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>

<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);"
 action="auth/userPwdEdit/edit.do" id="userPwdedit_form">
	<!-- #section:elements.form -->
	<br/>
	<!-- 用户  -->
	<input type="hidden" name="userId" id="userId"/>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1">请输入原始密码： </label>

		<div class="col-sm-9">
			<input type="password" id="form-field-1" id="userPwd" name="userPwd"
				class="col-xs-10 col-sm-7" validate="true"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-4">请输入新密码：</label>
		<div class="col-sm-9">
			<input type="password" id="form-field-1" id="userNewPwd" name="userNewPwd"
				class="col-xs-10 col-sm-7" validate="true"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-4">请重复输入新密码：</label>
		<div class="col-sm-9">
			<input type="password" id="form-field-1" id="reUserNewPwd" name="reUserNewPwd"
				class="col-xs-10 col-sm-7" value="" validate="true" />
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
	$("#userId").val(UID.id);
});

function submitForm(obj){
	 var ar=[];
     $("input[type=password]").each(function(){
         ar.push($(this).val());
     });
     var pwd1=ar[1];
     var pwd2=ar[2];
     if(pwd1 != pwd2){
 		msg.alert("错误", "两次输入密码不一致", 'error');
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



</script>
