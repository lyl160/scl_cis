<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form"  onsubmit="return submitForm(this);" action="place/save1" >
	<br/>
    <input type="hidden" name="id" value="${entity.id}"/>
    
	<div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2">  地点： </label>
        <div class="col-sm-9">
            <input type="text" id="placeName" name="placeName" placeholder="" class="col-xs-10 col-sm-7" value="${entity.placeName}"/> 
        </div>
    </div>
	<div class="form-group">
		<label class="col-sm-3 control-label no-padding-right tooltip-error" for="form-field-1"> 是否默认： </label>
		<div class="col-sm-9">
		<input  name="defaultFlag" type="radio" value="0000"/>是
		<input  name="defaultFlag" type="radio" value="1" />否
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
    $(function(){
    	var val = ${entity.defaultFlag};
    	var defaultFlag = $("input[name='defaultFlag']");
    	defaultFlag.each(function(i){
    		if($(this).val() == val){
    			$(this).attr("checked","checked");
    		}
    	})
    	
    })
</script>
