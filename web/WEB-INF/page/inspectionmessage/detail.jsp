<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);">
	<br/>
<c:if test="${entity.type == 1 || entity.type == 2 || entity.type == 3}">
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">内容类型： </label>
        <div class="col-sm-9">
            <label>
                <select disabled style="width: 130px;vertical-align:middle;">
                    <option value='' <c:if test="${entity.type == null}">selected</c:if>>无</option>
                    <option value='1' <c:if test="${entity.type == 1}">selected</c:if>>巡查反馈</option>
                    <option value='2' <c:if test="${entity.type == 2}">selected</c:if>>一周综述</option>
                    <option value='3' <c:if test="${entity.type == 3}">selected</c:if>>校园大事记</option>
                </select>
            </label>
        </div>
    </div>
</c:if>
 <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">内容描述： </label>
        <div class="col-sm-9">
            <textarea type="text" id="remark" name="remark" readonly placeholder="" class="col-xs-10 col-sm-10"  rows="8">${entity.remark}</textarea>
        </div>
    </div> 

	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">提交人： </label>
        <div class="col-sm-9">
            <input type="text" id="userName" name="userName" readonly placeholder="" class="col-xs-10 col-sm-7" value="${entity.userName}"/>
        </div>
    </div>
   <%--  <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">userId： </label>
        <div class="col-sm-9">
            <input type="text" id="userId" name="userId" placeholder="" class="col-xs-10 col-sm-7" value="${entity.userId}"/> 
        </div>
    </div> --%>
     <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">添加时间： </label>
        <div class="col-sm-9">
            <input type="text" id="userId" name="userId" placeholder="" readonly class="col-xs-10 col-sm-7" value="${entity.addTime}"/>
        </div>
    </div>
   <%--  <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">发送状态： </label>
        <div class="col-sm-9">
        <c:if test="${entity.status == 0}">
        <input type="text" id="userId" name="userId" placeholder="" class="col-xs-10 col-sm-7" value="未发送"/> 
        </c:if>
        <c:if test="${entity.status == 1}" >
        <input type="text" id="userId" name="userId" placeholder="" class="col-xs-10 col-sm-7" value="已发送"/> 
        </c:if>
            
        </div>
    </div> --%>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">图片 ： </label>
        <div class="col-sm-3">
           <c:forEach items="${entity.listimgs}" var="images">
            <img alt="" width="500px"   src="base/pic/view2.do?picid=${images}">
            </c:forEach>
        </div>
        
    </div>
   
    
    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>
</form>

