<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="team/save1" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  团队名称： </label>
        <div class="col-sm-9">
            <input type="text" id="teamName" name="teamName" placeholder="" class="col-xs-10 col-sm-7" value="${entity.teamName}"/> 
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
						 name="schoolId" 
						  id="schoolId"
						 defVal="${entity.schoolId}"
						 data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
	
		</div>
	</div>
	<%-- <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  addtime： </label>
        <div class="col-sm-9">
            <input type="text" id="addtime" name="addtime" placeholder="" class="col-xs-10 col-sm-7" value="${entity.addtime}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  edittime： </label>
        <div class="col-sm-9">
            <input type="text" id="edittime" name="edittime" placeholder="" class="col-xs-10 col-sm-7" value="${entity.edittime}"/> 
        </div>
    </div> --%>

    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn btn-info" type="submit">保存</button>
        &nbsp;
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>

</form>
<script type="text/javascript">
    function submitForm(obj){
    	if($("#schoolId").val() == null || $("#schoolId").val() == ""){
    		msg.alert("警告", "必须选择学校", 'warn');
    		return false;
    	}
        //自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
        return validateCallback(obj,function(result){
            if(result.returnCode  == 1){
                msg.alert("提示", "操作成功", 'correct');
                $("#grid-table").trigger("reloadGrid");
                dialog.close(obj);
            }else{
                msg.alert("错误", result.msg, 'error');
            }
        });
    }
</script>
