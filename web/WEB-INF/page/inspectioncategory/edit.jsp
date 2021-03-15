<!--修改2级类别（非 校外执勤 教师执勤）选择老师-->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form" onsubmit="return submitForm(this);" action="inspectionCategory/save1">
    <br/>
    <input type="hidden" name="id" value="${entity.id}"/>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 分类描述： </label>
        <div class="col-sm-9">
            <input type="text" id="value" name="value" placeholder="" class="col-xs-10 col-sm-7"
                   value="${entity.value}"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-4">所属学校：</label>

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
                        <option value=''>----请选择----</option>
                    </select>
                </div>
            </label>

        </div>
    </div>
<c:if test="${entity.ilevel == 2}">
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">值班老师： </label>
        <div class="col-sm-9">
			<textarea class="col-xs-10 col-sm-7" rows="8" id="usersName" name="usersName" readonly onclick="javascrpit:optService();">${entity.usersName}</textarea>
            <input id="users" name="users" type="hidden" value="${entity.users}"/>
            <span class="col-xs-12 col-sm-9" style="color:red;font-weight:bold">可以选择多个用户，只有指定的用户才可巡查该分类</span>
        </div>
    </div>
</c:if>

    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn btn-info" type="submit">保存</button>
        &nbsp;
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>

</form>
<script type="text/javascript">

    //分配值班老师
    function optService() {
        if ($("#schoolId").val() == null || $("#schoolId").val() == "") {
            msg.alert("警告", "必须选择学校", 'warn');
            return false;
        }
        var schoolId = $("#schoolId").val();
        openDialog({
            dialogId: 'dlg-optservice',
            title: '分配值班老师',
            pageUrl: 'schedule/optuser/view.do?schoolId=' + schoolId,
            width: '1200px',
            height: '600px'
        });
    }


    function submitForm(obj) {

        if ($("#schoolId").val() == null || $("#schoolId").val() == "") {
            msg.alert("警告", "必须选择学校", 'warn');
            return false;
        }
        //自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
        return validateCallback(obj, function (result) {
            if (result.returnCode == 1) {
                msg.alert("提示", "操作成功", 'correct');
                $("#grid-table").trigger("reloadGrid");
                dialog.close(obj);
            } else {
                msg.alert("错误", result.msg, 'error');
            }
        });
    }
</script>
