<!--修改2级类别（护校队巡查 教师执勤）带开始结束时间-->
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<div class="form-horizontal"   >
    <br/>
    <input type="hidden" id="categoryId" name="id" value="${entity.id}"/>
    <input type="hidden" id="diyTime"  value="${entity.diyTime}"/>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 分类描述： </label>
        <div class="col-sm-9">
            <input type="text" id="categoryValue" name="value" placeholder="" class="col-xs-10 col-sm-7"
                   value="${entity.value}"/>
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
                        <option value=''>----请选择----</option>
                    </select>
                </div>
            </label>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 通用巡检时间： </label>
        <input type="text" id="startTime" name="startTime" class="timeInput" style="width:80px;margin-left: 12px;"
               value="${entity.startTime}" placeholder="请选择开始时间" autocomplete="off" readonly="true"/> -
        <input type="text" id="endTime" name="endTime" class="timeInput" style="width:80px" value="${entity.endTime}"
               placeholder="请选择结束时间" autocomplete="off" readonly="true"/>
    </div>
    <c:forEach var="i" begin="0" end="6" step="1">
        <div class="form-group">
            <label class="col-sm-3 control-label no-padding-right" for="form-field-2"> 自定义巡检时间： </label>
            <select id="diyWeek${i}" name="diyWeek" style="margin-left: 12px;">
                <option value="">请选择</option>
                <option value="1">周一</option>
                <option value="2">周二</option>
                <option value="3">周三</option>
                <option value="4">周四</option>
                <option value="5">周五</option>
                <option value="6">周六</option>
                <option value="7">周日</option>
            </select>
            <input type="text" id="diyStartTime${i}" name="diyStartTime" class="timeInput" style="width:80px;margin-left: 12px;"
                   value="" placeholder="请选择开始时间" autocomplete="off" readonly="true"/> -
            <input type="text" id="diyEndTime${i}" name="diyEndTime" class="timeInput" style="width:80px" value=""
                   placeholder="请选择结束时间" autocomplete="off" readonly="true"/>
        </div>
    </c:forEach>


    <div class="form-actions align-right  " style="margin-top: 10px">
        <button class="btn btn-info"  onclick="editCategory(this);">保存</button>
        &nbsp;
        <button class="btn" type="button" onclick="javascript:dialog.close(this);"> 关闭</button>
    </div>

</div>
<script type="text/javascript">
    $(document).ready(function () {
        var diyTime = $("#diyTime").val();
        if(diyTime && diyTime!=""){
            var dayStr = diyTime.split("&");
            if (dayStr && dayStr.length>0) {
                for (var i = 0; i < dayStr.length; i++) {
                    var timeArray = dayStr[i].split(",");
                    $("#diyWeek" + i).val(timeArray[0]);
                    $("#diyStartTime" + i).val(timeArray[1]);
                    $("#diyEndTime" + i).val(timeArray[2]);
                }
            }
        }
    });


    function editCategory(obj){
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if ($("#schoolId").val() == null || $("#schoolId").val() == "") {
            msg.alert("警告", "必须选择学校", 'warn');
            return false;
        }
        if (startTime == null || startTime == "" || endTime == null || endTime == "") {
            msg.alert("警告", "必须选择时间段", 'warn');
            return false;
        }
        if (startTime.split(":")[0] + startTime.split(":")[1] >= endTime.split(":")[0] + endTime.split(":")[1]) {
            msg.alert("警告", "结束时间不能小于或等于开始时间", 'warn');
            return false;
        }

        var jsonData = new Object();
        var diyWeekArray = new Array();
        var diyStartTimeArray = new Array();
        var diyEndTimeArray = new Array();
        for (var i = 0; i <= 6; i++) {
            var diyWeek = $("#diyWeek"+i).val()
            if(diyWeek !=null && diyWeek != ""){
                var diyStartTime = $("#diyStartTime" + i).val();
                var diyEndTime = $("#diyEndTime" + i).val();
                if (diyStartTime == null || diyStartTime == "" || diyEndTime == null || diyEndTime == "") {
                    msg.alert("警告", "必须选择时间段", 'warn');
                    return false;
                }
                diyWeekArray.push(diyWeek);
                diyStartTimeArray.push(diyStartTime);
                diyEndTimeArray.push(diyEndTime);
            }
        }
        jsonData.schoolId = $("#schoolId").val();
        jsonData.id = $("#categoryId").val();
        jsonData.value = $("#categoryValue").val();
        jsonData.startTime = $("#startTime").val();
        jsonData.endTime = $("#endTime").val();
        jsonData.diyWeek = diyWeekArray;
        jsonData.diyStartTime = diyStartTimeArray;
        jsonData.diyEndTime = diyEndTimeArray;



        if(jsonData.length == 0){
            msg.alert("警告", "没有要提交的数据！", 'warn');
            return;
        }

        $.ajax({
            url:'inspectionCategory/save1',
            type:'post',
            dataType:'json',
            // contentType:"application/json",
            data:jsonData,
            success : function(result) {
                if(result.returnCode == 1){
                    msg.alert("提示", "操作成功", 'correct');
                    $("#grid-table").trigger("reloadGrid");
                    dialog.close(obj);
                }else{
                    msg.alert("错误", result.msg, 'error');
                }
            },
            error:function(XMLHttpRequest, textStatus){
                msg.alert("错误", textStatus + " 错误代码："
                    + XMLHttpRequest.status, 'error');
            }
        });
    }

    $(function () {
        $(".timeInput").datetimepicker({
            format: 'hh:ii',
            autoclose: true,
            startView: "hour",
            changeMonth: false,
            changeYear: false,
            showOtherMonths: false,
            language: 'zh-CN'
        });
    })
</script>
