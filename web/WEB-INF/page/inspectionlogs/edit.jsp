<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="inspectionLogs/save" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">巡检类别： </label>
        <div class="col-sm-9">
            <input type="text" id="category1" name="category1" placeholder="" class="col-xs-10 col-sm-7" value="${entity.category1}"/> 
        </div>
    </div>
	
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">班级： </label>
        <div class="col-sm-9">
            <input type="text" id="clazz" name="clazz" placeholder="" class="col-xs-10 col-sm-7" value="${entity.clazz}"/> 
        </div>
    </div>
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 年级： </label>
        <div class="col-sm-9">
            <input type="text" id="grade" name="grade" placeholder="" class="col-xs-10 col-sm-7" value="${entity.grade}"/> 
        </div>
    </div>	
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 巡检人名称： </label>
        <div class="col-sm-9">
            <input type="text" id="czy" name="czy" placeholder="" class="col-xs-10 col-sm-7" value="${entity.czy}"/> 
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
