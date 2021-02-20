<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="schoolInf/save1" >
	<br/>
	<div class="form-group">
	    <label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校编号： </label>
		<div class="col-sm-3">
			<input type="text" id="schoolBm" name="schoolBm" placeholder="" validate="true" class="col-xs-10 col-sm-10" />
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 学校名称： </label>
		<div class="col-sm-3">
			<input type="text" id="schoolName" name="schoolName" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 校长： </label>
		<div class="col-sm-3">
			<input type="text" id="president" name="president" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 学校国际名称： </label>
		<div class="col-sm-3">
			<input type="text" id="iname" name="iname" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校简称： </label>
		<div class="col-sm-3">
			<input type="text" id="sname" name="sname" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 联系人名称： </label>
		<div class="col-sm-3">
			<input type="text" id="contactsName" name="contactsName" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 联系人手机号： </label>
		<div class="col-sm-3">
			<input type="text" id="contactsMobile" name="contactsMobile" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 邮箱： </label>
		<div class="col-sm-3">
			<input type="text" id="email" name="email" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 固定电话： </label>
		<div class="col-sm-3">
			<input type="text" id="fixPhone" name="fixPhone" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 省市区： </label>
		<div class="col-sm-3">
			<input type="text" id="mainAddress" name="mainAddress" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 详细地址： </label>
		<div class="col-sm-9">
			<input type="text" id="detailAddress" name="detailAddress" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校logo： </label>
		<div class="col-sm-9">
			<input type="text" id="logo" name="logo" placeholder="" class="col-xs-10 col-sm-7" readonly="readonly" />
			<span class="">
				<button class="btn btn-small btn-yellow" type="button" onclick="upload('logo')">
					选择文件
				</button>
				<button class="btn btn-small btn-info" type="button" onclick="agentPicView('logo');">
					预览
				</button>
			</span>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校执照： </label>
		<div class="col-sm-3">
			<input type="text" id="businessLicense" name="businessLicense" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
		<label class="col-sm-2 control-label no-padding-right tooltip-error" for="form-field-1"> 校长身份证： </label>
		<div class="col-sm-3">
			<input type="text" id="idCard" name="idCard" placeholder="" class="col-xs-10 col-sm-10"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校类别： </label>
		<div class="col-sm-3">
		   		<label>
					<div class="search_input" style="padding-top: 5px;">
					<select 
						 location="local" 
						 name="category" 
						 id="category" 
				 		 data= "{'1':'小学','2':'中学','3':'大学'}"
						 data-placeholder="请选择学校类别" style="width: 158px;vertical-align:middle;">
							<option value='' >----请选择----</option>
					</select>
					</div>
				</label>
			<!-- <input type="text" id="category" name="category" placeholder="" class="col-xs-10 col-sm-10"/> -->
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 学校简介： </label>
		<div class="col-sm-9">
			<textarea type="text"  id="readme" name="readme" placeholder="" class="col-xs-10 col-sm-10" rows="10"></textarea> 
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
