<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="inspectionMessage/save" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">内容类型： </label>
        <div class="col-sm-9">
            <label>
                <select id="type" name="type" style="width: 130px;vertical-align:middle;">
                    <option value='1' <c:if test="${entity.type == 1}">selected</c:if>>巡查反馈</option>
                    <option value='2' <c:if test="${entity.type == 2}">selected</c:if>>一周综述</option>
                    <option value='3' <c:if test="${entity.type == 3}">selected</c:if>>校园大事记</option>
                </select>
            </label>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">类容描述： </label>
        <div class="col-sm-9">
            <textarea type="text" id="remark" name="remark" placeholder="" class="col-xs-10 col-sm-10"  rows="8">${entity.remark}</textarea>  
        </div>
    </div> 
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">提交人： </label>
        <div class="col-sm-9">
            <input type="text" id="userName" name="userName" placeholder="" class="col-xs-10 col-sm-7" value="${entity.userName}"/> 
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
