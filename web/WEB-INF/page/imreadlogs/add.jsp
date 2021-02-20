<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="imReadLogs/save" >
	<br/>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> messageId： </label>
		<div class="col-sm-9">
			<input type="text" id="messageId" name="messageId" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> userId： </label>
		<div class="col-sm-9">
			<input type="text" id="userId" name="userId" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> userName： </label>
		<div class="col-sm-9">
			<input type="text" id="userName" name="userName" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> isread： </label>
		<div class="col-sm-9">
			<input type="text" id="isread" name="isread" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> readtime： </label>
		<div class="col-sm-9">
			<input type="text" id="readtime" name="readtime" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> addtime： </label>
		<div class="col-sm-9">
			<input type="text" id="addtime" name="addtime" placeholder="" class="col-xs-10 col-sm-7"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> edittime： </label>
		<div class="col-sm-9">
			<input type="text" id="edittime" name="edittime" placeholder="" class="col-xs-10 col-sm-7"/>
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
