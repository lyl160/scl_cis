<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="clazzInf/save1" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班级： </label>
        <div class="col-sm-3">
            <input type="text" id="clazz" name="clazz" placeholder="" class="col-xs-10 col-sm-10" value="${entity.clazz}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  年级： </label>
        <div class="col-sm-3">
            <input type="text" id="grade" name="grade" placeholder="" class="col-xs-10 col-sm-10" value="${entity.grade}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班主任： </label>
        <div class="col-sm-3">
            <input type="text" id="headerTeacher" name="headerTeacher" placeholder="" class="col-xs-10 col-sm-10" value="${entity.headerTeacher}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  学生总数： </label>
        <div class="col-sm-3">
            <input type="text" id="totalPeo" name="totalPeo" placeholder="" class="col-xs-10 col-sm-10" value="${entity.totalPeo}"/> 
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校logo： </label>    
    	<div class="col-sm-9">
			<input type="text" id="logo" name="logo" placeholder="" class="col-xs-10 col-sm-7" readonly="readonly"  value="${entity.logo}"/>
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
		<label class="col-sm-3 control-label no-padding-right"for="form-field-1"> 团队： </label>
		<div class="col-sm-3">
			<select 
				 rel="obj.options"
				 valName="" 
				 textName="" 
				 location="team/selectoption.do" 
				 name="teamId" 
				 defVal="${entity.teamId}"
				 data-placeholder="请选择" style="width: 160px;vertical-align:middle;">
				 <option value='' >---请选择---</option>
			 </select>
		</div>
		<label class="col-sm-2 control-label no-padding-right" for="form-field-2">  班级称号： </label>
        <div class="col-sm-3">
            <input type="text" id="slogan" name="slogan" placeholder="" class="col-xs-10 col-sm-10" value="${entity.slogan}"/> 
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
	
	
	
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班级简介： </label>
        <div class="col-sm-9">
            <textarea type="text" id="readme" name="readme" placeholder="" class="col-xs-10 col-sm-10"  rows="8">${entity.readme}</textarea>  
        </div>
    </div>   

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
