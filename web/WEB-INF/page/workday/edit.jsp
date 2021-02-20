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
        /*border: 2px solid #3D95D5;*/
        /*color: #3D95D5;*/
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
      action="workDay/saveorupdate.do">
    <input type="hidden" name="id" value="${entity.id}"/>
    <div style="min-height:160px;">
        <div class="M-list-a">
            <ul class="M-list-ul">
            </ul>
        </div>
    </div>
    <div class="form-group">
        <p class="col-sm-12 red" style="padding-left: 40px;" for="form-field-1">说明：点击按钮改变字体颜色，红色为休息日，黑色为工作日</p>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">年： </label>
        <div class="col-sm-1">
            <select id="yearInput" name="year" onchange="reloadDay()" readonly>
                <option value="2020">2020年</option>
                <option value="2021">2021年</option>
                <option value="2022">2022年</option>
                <option value="2023">2023年</option>
                <option value="2024">2024年</option>
            </select>
        </div>
        <label class="col-sm-1 control-label no-padding-right" for="form-field-1">月： </label>
        <div class="col-sm-7">
            <select id="monthInput" name="month" onchange="reloadDay()" readonly>
                <option value="1">1月</option>
                <option value="2">2月</option>
                <option value="3">3月</option>
                <option value="4">4月</option>
                <option value="5">5月</option>
                <option value="6">6月</option>
                <option value="7">7月</option>
                <option value="8">8月</option>
                <option value="9">9月</option>
                <option value="10">10月</option>
                <option value="11">11月</option>
                <option value="12">12月</option>
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
                            defVal="${entity.schoolId}"
                            data-placeholder="请选择学校" style="width: 193px;vertical-align:middle;">
                        <option value=''>----请选择----</option>
                    </select>
                </div>
            </label>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">工作日： </label>
        <div class="col-sm-9">
			<textarea class="col-xs-10 col-sm-7" rows="8" id="workday" name="workday" readonly>
			</textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-1">休息日： </label>
        <div class="col-sm-9">
			<textarea class="col-xs-10 col-sm-7" rows="8" id="holiday" name="holiday" readonly>
			</textarea>
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
        $("#yearInput").val('${entity.year}');
        $("#monthInput").val('${entity.month}');
        $('#yearInput').attr("disabled", true);
        $('#monthInput').attr("disabled", true);
        reloadDay();

    });

    function reloadDay() {
        var workdayinDB = '${entity.workday}'.split(",");
        var holidayinDB = '${entity.holiday}'.split(",");
        var year = $("#yearInput").val();
        var month = $("#monthInput").val();
        if (year && month) {
            $.ajax({
                type: "post",
                url: "workDay/get/date/json",
                data: {
                    year: year,
                    month: month
                },
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
                            if (holidayinDB.indexOf(d.date_str) >= 0) {
                                html += '<li class="baseBg red"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            } else {
                                html += '<li class="baseBg"><span>' + d.date_str + '</span>【<i>' + d.week_str + '</i>】</li>';
                            }
                            index++;
                        });
                        //保证竖列相同
                        for (var i = 0; i < nowWeekDay - 1; i++) {
                            html = '<li class="baseBg clear" style="border: solid 0px #e2e1e3;"><span></span><i></i></li>' + html;
                        }
                        $('.M-list-ul').empty();
                        $('.M-list-ul').append(html);
                        setWorkday();
                    }
                },
                error: function (XMLHttpRequest, textStatus) {
                    msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
                }
            });
        }

    }

    $('.M-list-ul').on('click', 'li', function (e) {
        if ($(this).find('span').html() == '') {
            return;
        }
        if ($(this).hasClass('hover')) {

            $(this).removeClass('hover');
        } else {
            $(this).addClass('hover');
        }
        $(this).toggleClass("red");
        //赋值
        setWorkday();
    });

    function setWorkday() {
        var rows = $(".M-list-ul li.baseBg").not(".clear");
        var holidaystr = [];
        var workstr = [];
        rows.each(function () {
            var rowData = $(this).find('span').html();
            if ($(this).hasClass("red")) {
                holidaystr.push(rowData);
            } else {
                workstr.push(rowData);
            }
        });
        if (holidaystr.length == 0) {
            $('textarea[name=holiday]').html("");
        } else {
            $('textarea[name=holiday]').html(holidaystr.join(","));
        }
        if (workstr.length == 0) {
            $('textarea[name=workday]').html("");
        } else {
            $('textarea[name=workday]').html(workstr.join(","));
        }
    }


    function submitForm(obj) {
        if ($("select[name=year]").val() == null || $("select[name=year]").val() == "") {
            msg.alert("警告", "必须选择年份", 'warn');
            return false;
        }
        if ($("select[name=month]").val() == null || $("select[name=month]").val() == "") {
            msg.alert("警告", "必须选择月份", 'warn');
            return false;
        }
        if ($("#schoolId").val() == null || $("#schoolId").val() == "") {
            msg.alert("警告", "必须选择学校", 'warn');
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
