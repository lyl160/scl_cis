<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="inspectionResult/save" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  logsId： </label>
        <div class="col-sm-9">
            <input type="text" id="logsId" name="logsId" placeholder="" class="col-xs-10 col-sm-7" value="${entity.logsId}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  attrId： </label>
        <div class="col-sm-9">
            <input type="text" id="attrId" name="attrId" placeholder="" class="col-xs-10 col-sm-7" value="${entity.attrId}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  attrName： </label>
        <div class="col-sm-9">
            <input type="text" id="attrName" name="attrName" placeholder="" class="col-xs-10 col-sm-7" value="${entity.attrName}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  attrValue： </label>
        <div class="col-sm-9">
            <input type="text" id="attrValue" name="attrValue" placeholder="" class="col-xs-10 col-sm-7" value="${entity.attrValue}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  addTime： </label>
        <div class="col-sm-9">
            <input type="text" id="addTime" name="addTime" placeholder="" class="col-xs-10 col-sm-7" value="${entity.addTime}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  editTime： </label>
        <div class="col-sm-9">
            <input type="text" id="editTime" name="editTime" placeholder="" class="col-xs-10 col-sm-7" value="${entity.editTime}"/> 
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
