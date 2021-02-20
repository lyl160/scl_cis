<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="dynamicAttr/save" >
	<br/>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> templateId： </label>
		<div class="col-sm-9">
			<input type="text" id="templateId" name="templateId" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> name： </label>
		<div class="col-sm-9">
			<input type="text" id="name" name="name" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> type： </label>
		<div class="col-sm-9">
			<input type="text" id="type" name="type" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> attrOption： </label>
		<div class="col-sm-9">
			<input type="text" id="attrOption" name="attrOption" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> seq： </label>
		<div class="col-sm-9">
			<input type="text" id="seq" name="seq" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> required： </label>
		<div class="col-sm-9">
			<input type="text" id="required" name="required" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> schoolId： </label>
		<div class="col-sm-9">
			<input type="text" id="schoolId" name="schoolId" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> addTime： </label>
		<div class="col-sm-9">
			<input type="text" id="addTime" name="addTime" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> editTime： </label>
		<div class="col-sm-9">
			<input type="text" id="editTime" name="editTime" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	
	<div class="form-actions align-right form-button-box">
		<button class="btn btn-info" type="submit">保存</button>
		&nbsp; 
		<button class="btn" type="button" onclick="javascript:dialog.close(this);">关闭</button>
	</div>
</form>
<script type="text/javascript">
function submitForm(obj){
	//自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
	return validateCallback(obj,function(result){
		if(result.returnCode == 1){
			msg.alert("提示", "操作成功", 'correct');
			$("#grid-table").trigger("reloadGrid");
			dialog.close(obj);
		}else{
			msg.alert("错误", result.msg, 'error');
		}
	});
}
</script>
