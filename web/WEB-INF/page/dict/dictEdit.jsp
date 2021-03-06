<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<br/>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);"
 action="dict/update.do" >
	<!-- #section:elements.form -->
	<input type="hidden" name="seqNum" id="dialog_seq_num">
	<input type="hidden" name="parentId" id="">
	<input type="hidden" name="dictLevel" id="">
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error"
			for="form-field-1"> 字典编号： </label>

		<div class="col-sm-9">
			<!-- 
		 		 * 属性说明:
				 * validate="true"  是否需要验证字段
				 * msg="错误时提示信息"        
				 * datatype="数据类型"
			 -->
			<input type="text" id="dialog_dict_id" name="dictId" placeholder=""
				class="col-xs-10 col-sm-7" validate="true" readonly="readonly" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1-1"> 字典代码： </label>
		<div class="col-sm-9">
			<!-- 
		 		 * 属性说明:
				 * validate="true"  是否需要验证字段
				 * msg="错误时提示信息"        
				 * datatype="数据类型"
			 -->
			<input type="text" id="dialog_dict_code" name="dictCode" placeholder=""
				class="col-xs-10 col-sm-7"  validate="true" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-2"> 字典名称： </label>

		<div class="col-sm-9">
			<input type="text" id="dialog_dict_name" name="dictName" placeholder=""
				class="col-xs-10 col-sm-7" /> <span
				class="help-inline col-xs-12 col-sm-7"> <span class="middle"></span>
			</span>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-2"> 字典值： </label>

		<div class="col-sm-9">
			<input type="text" id="dialog_dict_value" name="dictValue" placeholder=""
				class="col-xs-10 col-sm-7" /> <span
				class="help-inline col-xs-12 col-sm-7"> <span class="middle"></span>
			</span>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-1"> 字典状态： </label>
		<div class="col-sm-9">
			<select 
				 rel="obj.DICTSTATUS"
	 			 location="local" 
				 name="status" 
	 			id="status_edit" 
	 			data-placeholder="请选择状态" style="width: 130px;vertical-align:middle;">
			</select>
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

function submitForm(obj){
	//自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
	return validateCallback(obj,function(result){
		if(result.returnCode== 1){
			msg.alert("提示", "更新成功", 'correct');
			$("#grid-table").trigger("reloadGrid");
			dialog.close(obj);
		}else{
			msg.alert("错误", result.msg + " 错误代码："
					+ result.returnCode, 'error');
		}
	});
}

</script>
