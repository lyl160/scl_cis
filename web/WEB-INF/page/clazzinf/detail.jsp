<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);">
	<br/>

	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班级： </label>
        <div class="col-sm-3">
            <input type="text" lass="col-xs-10 col-sm-10" value="${clazz.clazz}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  年级： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${clazz.grade}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班主任： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${clazz.headerTeacher}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  学生总数： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${clazz.totalPeo}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班级logo： </label>
        <div class="col-sm-3">
            <img alt="" width="300px" height="300px"  src="base/pic/view2.do?picid=${clazz.logo}"> 
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
				 defVal="${clazz.teamId}"
				 data-placeholder="请选择" style="width: 160px;vertical-align:middle;">
				 <option value='' >---请选择---</option>
			 </select>
		</div>
	</div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  班级简介： </label>
        <div class="col-sm-9">
        <textarea type="text" class="col-xs-10 col-sm-10" rows="8">${clazz.readme}</textarea> 
        </div>
    </div>    
    <div class="form-group">
       <label class="col-sm-3 control-label no-padding-right"
			for="form-field-4">所属学校：</label>
		<div class="col-sm-3">
				<label>
					<div class="search_input" style="padding-top: 0px;">
					<select 
						 rel="obj.options"
						 valName="" 
						 textName="" 
						 location="schoolInf/selectoption" 
						 name="schoolId" 
						 defVal="${clazz.schoolId}"
						 data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						 </select>
					</div>
				</label>
		</div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-1">  班级称号： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${clazz.slogan}"/> 
        </div>
    </div>
    
    
    <div class="form-group">
		
	</div>
    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>
</form>

