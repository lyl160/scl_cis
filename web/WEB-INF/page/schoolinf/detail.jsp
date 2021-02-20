<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" >
	<br/>

	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校编号： </label>
        <div class="col-sm-3">
            <input type="text" lass="col-xs-10 col-sm-10" value="${school.schoolBm}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  学校名称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.schoolName}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  校长： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.president}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  学校国际名称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.iname}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校简称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.sname}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  联系人名称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.contactsName}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  联系人手机号： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.contactsMobile}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  邮箱： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.email}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  固定电话： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.fixPhone}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  省市区： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.mainAddress}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  详细地址： </label>
        <div class="col-sm-9">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.detailAddress}"/> 
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校执照： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.businessLicense}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  校长身份证： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${school.idCard}"/> 
        </div>
    </div>
   <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校类别： </label>
        <div class="col-sm-3">
        		<label>
					<div class="search_input" style="padding-top: 5px;">
						<select 
						 location="local" 
						 name="category" 
						 id="category" 
						 defVal="${school.category}"
				 		 data= "{'1':'小学','2':'中学','3':'大学'}"
						 data-placeholder="请选择学校类别" style="width: 158px;vertical-align:middle;">
							<option value='' >----请选择----</option>
						</select>
					</div>
				</label>
        </div>
   </div>
   <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  学校简介： </label>
        <div class="col-sm-9">
            <textarea type="text" class="col-xs-10 col-sm-10" rows="10">${school.readme}</textarea> 
        </div>
    </div>
    <div class="form-group">
		<label class="col-sm-3 control-label no-padding-right"
			for="form-field-2"> 学校logo： </label>
		<div class="col-sm-3">
		 <img alt="" width="300px" height="300px"  src="base/pic/view2.do?picid=${school.logo}">		
		</div>
	</div>	
    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>
</form>

