<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="imReadLogs/save" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  messageId： </label>
        <div class="col-sm-9">
            <input type="text" id="messageId" name="messageId" placeholder="" class="col-xs-10 col-sm-7" value="${entity.messageId}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  userId： </label>
        <div class="col-sm-9">
            <input type="text" id="userId" name="userId" placeholder="" class="col-xs-10 col-sm-7" value="${entity.userId}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  userName： </label>
        <div class="col-sm-9">
            <input type="text" id="userName" name="userName" placeholder="" class="col-xs-10 col-sm-7" value="${entity.userName}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  isread： </label>
        <div class="col-sm-9">
            <input type="text" id="isread" name="isread" placeholder="" class="col-xs-10 col-sm-7" value="${entity.isread}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  readtime： </label>
        <div class="col-sm-9">
            <input type="text" id="readtime" name="readtime" placeholder="" class="col-xs-10 col-sm-7" value="${entity.readtime}"/> 
        </div>
    </div>
	<div class="form-group">
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
