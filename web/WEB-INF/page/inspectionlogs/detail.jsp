<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="schoolInf/save" >
	<br/>

	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right" for="form-field-2">  巡检类别： </label>
        <div class="col-sm-3">
            <input type="text" lass="col-xs-10 col-sm-10" value="${ilog.value}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">  班级： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${ilog.clazz}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 年级： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${ilog.grade}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">巡检人名称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${ilog.czy}"/> 
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">教师： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="<c:if test='${ilog.teacherName != null}'>${ilog.teacherName}</c:if><c:if test='${ilog.teacherName == null ||ilog.teacherName == ""}'>全班教师</c:if>"/>
        </div>
    </div>
    	
    <c:forEach items="${ilog.list}" var="l" varStatus="vs">
  
     
          <div class="form-group">
         	 <label class="col-sm-3 control-label no-padding-right" for="form-field-2">${l.attrName}： </label>
            <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${l.attrValue}"/> 
           </div>

         	 <label class="col-sm-2 control-label no-padding-right" for="form-field-2">单项得分： </label>
            <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${l.itemScore}"/> 
            </div>
            </div>
       

    </c:forEach>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">图片： </label>
        <div class="col-sm-3">
        <c:forEach items="${entity.listimgs}" var="images">
            <img alt="" width="300px" height="300px"  src="base/pic/view2.do?picid=${images}"> 
            </c:forEach>
        </div>
        
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">文字描述： </label>
        <div class="col-sm-9">
        <textarea type="text" class="col-xs-10 col-sm-10" rows="8">${ilog.desc1}</textarea> 
        </div>
    </div>  
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 团队名称： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${ilog.teamName}"/> 
        </div>
        <label class="col-sm-2 control-label no-padding-right" for="form-field-2">总得分： </label>
        <div class="col-sm-3">
            <input type="text" class="col-xs-10 col-sm-10" value="${ilog.totalScore}"/> 
        </div>
    </div>
    
	
     

    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>
</form>

