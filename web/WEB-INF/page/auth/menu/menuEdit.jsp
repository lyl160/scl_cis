<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>

<meta charset="utf-8" />	

<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);"
 action="auth/menuEdit/edit.do" id="useredit_form">
	<!-- #section:elements.form -->
	<br/>
	<!-- 用户  -->
	<input type="hidden" name="menuId" id="menuId"/>
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1"> 上级菜单： </label>

		<div class="col-sm-9">
			<input type="text" id="menuParId" name="menuParId" placeholder="" readonly="readonly"
				class="col-xs-10 col-sm-7" value="运营管理系统" />
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1-1"> 菜单名称： </label>

		<div class="col-sm-9">
			<input type="text" id="menuName" name="menuName" placeholder=""
				class="col-xs-10 col-sm-7" validate="true"/>
		</div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-3 control-label no-pEditing-right"
			for="form-field-1-1"> 菜单路径： </label>

		<div class="col-sm-9">
			<input type="text" id="menuUrl" name="menuUrl" placeholder=""
				class="col-xs-10 col-sm-7" />
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

</script>

