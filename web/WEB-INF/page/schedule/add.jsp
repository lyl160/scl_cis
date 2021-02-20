<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<style>
    .M-list-a ul {
        float: left;
        height: 24px;
    }

    .M-list-a ul {
        height: 30px;
        line-height: 30px;
    }

    .M-list-a ul li, .M-list-a ul li.hover {
        width: 140px;
        margin-top: 3px;
    }

    .M-list-a ul li.hover {
        height: 22px;
        line-height: 22px;
        background: none;
        border: 2px solid #3D95D5;
        color: #3D95D5;
    }

    .M-list-a ul {
        float: left;
        height: 30px;
    }

    .M-list-a ul li {
        float: left;
        border: solid 1px #e2e1e3;
        margin-right: 10px;
        cursor: pointer;
        width: 140px;
        height: 22px;
        line-height: 22px;
        text-align: center;
    }

    ul, li {
        list-style: none;
    }
    .red {
        color: red;
    }
</style>
<form class="form-horizontal" role="form" onsubmit="return submitForm(this);"
      action="schedule/saveorupdate.do">
    <div style="min-height:180px;">
        <div class="M-list-a">
            <ul class="M-list-ul">
            </ul>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">值班日期： </label>
        <div class="col-sm-9">
            <input class="col-xs-10 col-sm-7" type="text" id="dutyDate" name="dutyDate" validate="true" readonly/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">周： </label>
        <div class="col-sm-9">
            <input class="col-xs-10 col-sm-7" type="text" id="week" name="week" style="width: 300px" validate="true"
                   readonly/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1"> 巡检项目： </label>
        <div class="col-sm-9">
            <select
                    rel="obj.options"
                    valName=""
                    textName=""
                    location="inspectionTemplate/selectoption.do"
                    name="templateId"
                    id="templateId"
                    data-placeholder="请选择" style="width: 160px;vertical-align:middle;">
                <option value=''>---请选择---</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right"
               for="form-field-4">所属学校：</label>

        <div class="col-sm-9">

            <label>
                <div class="search_input" style="padding-top: 5px;">
                    <select
                            rel="obj.options"
                            valName=""
                            textName=""
                            location="schoolInf/selectoption"
                            name="schoolId"
                            id="schoolId"
                            data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
                        <option value=''>----请选择----</option>
                    </select>
                </div>
            </label>

        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">值班老师： </label>
        <div class="col-sm-9">
			<textarea class="col-xs-10 col-sm-7" rows="8" id="usersName" name="usersName" readonly
                      onclick="javascrpit:optService();">
			</textarea>
            <input id="users" name="users" type="hidden"/>
            <span class="col-xs-12 col-sm-9" style="color:red;font-weight:bold">可以选择多个老师排班 </span>
        </div>
    </div>
    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn btn-info" type="submit">
            <!-- <i class="ace-icon fa fa-check bigger-110"></i>  -->保存
        </button>

        &nbsp;
        <button class="btn" type="button" onclick="javascript:dialog.close(this);">
            关闭
        </button>
    </div>

</form>
<script type="text/javascript">
    $(function () {
        /*  var p = dialog.getById('dlg-merassign');
         $('#ids').val(p.dataParam.ids);
         $('#recCodes').val(p.dataParam.recCodes);
         $('#merNames').val(p.dataParam.merNames); */
        $.ajax({
            type: "post",
            url: "schedule/get/date/json",
            dataType: 'json',
            success: function (result) {
                if (result.returnCode != 1) {
                    msg.alert("错误", result.msg, 'error');
                } else {
                    var html = '';
                    var data = result.data;
                    var nowWeekDay = result.nowWeekDay;
                    var index = 1;
                    data.forEach(function (d) {
                        //自动添加第一天
                        if (index == 1) {
                            if (d.week_str != "星期六" && d.week_str != "星期日") {
                                html += '<li class="baseBg hover"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            }else{
                                html += '<li class="baseBg hover red"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            }
                            //赋值
                            $('input[name=dutyDate]').val(d.date_str);
                            $('input[name=week]').val(d.week_str);
                        } else {
                            if (d.week_str != "星期六" && d.week_str != "星期日") {
                                html += '<li class="baseBg"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            }else{
                                html += '<li class="baseBg red"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            }
                        }
                        index++;
                    });
                    //保证竖列相同
                    for(var i = 0;i<nowWeekDay-1;i++){
                        html = '<li class="baseBg" style="border: solid 0px #e2e1e3;"><span></span><i></i></li>' + html;
                    }
                    $('.M-list-ul').append(html);
                }
            },
            error: function (XMLHttpRequest, textStatus) {
                msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
            }
        });
    });

    $('.M-list-ul').on('click', 'li', function (e) {
        if ($(this).find('span').html()=='') {
            return;
        }
        if ($(this).hasClass('hover')) {

            $(this).removeClass('hover');
        }
        else {
            $(this).addClass('hover');
        }
        //赋值
        var rows = $(".M-list-ul li.baseBg.hover");
        var str1 = [];
        var str2 = [];
        var i = 0;
        rows.each(function () {
            var rowData = $(this).find('span').html();
            var rowData1 = $(this).find('i').html();
            str1[i] = rowData;
            str2[i] = rowData1;
            i++;

        });
        if (rows.length == 0) {
            $('input[name=dutyDate]').val("");
            $('input[name=week]').val("");
        } else {
            $('input[name=dutyDate]').val(str1.join(","));
            $('input[name=week]').val(str2.join(","));
        }
    });

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
        if ($("input[name=dutyDate]").val() == null || $("input[name=dutyDate]").val() == "") {
            msg.alert("警告", "必须选择值班日期", 'warn');
            return false;
        }
        if ($("#templateId").val() == null || $("#templateId").val() == "") {
            msg.alert("警告", "必须选择巡检项目", 'warn');
            return false;
        }
        if ($("#schoolId").val() == null || $("#schoolId").val() == "") {
            msg.alert("警告", "必须选择学校", 'warn');
            return false;
        }
        if ($("#users").val() == null || $("#users").val() == "") {
            msg.alert("警告", "必须选择值班老师", 'warn');
            return false;
        }
        //自定义匿名回调函数。 系统默认回调函数：dialogAjaxDone
        return validateCallback(obj, function (result) {
            if (result.returnCode != 1) {
                msg.alert("错误", result.msg, 'error');
            } else {
                msg.alert("提示", "操作成功", 'correct');
                $("#grid-table").trigger("reloadGrid");
                dialog.close(obj);
            }
        });
    }

</script>
