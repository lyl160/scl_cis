<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css"/>
<style>
    th {
        width: 7%;
        height: 20px
    }

    table {
        width: 100%;
        text-align: center;
    }

    table th {
        text-align: center;
    }
</style>
<body>
<div class="search_panel">
    <div style="display: block;" class="widget-body ">
        <div class="widget-main">
            <div class="rowt">
                <div class="col-sm-12">
                    <div>
                        <label class="label_search">学校名称:</label>
                        <select id="schoolId" name="schoolId">
                            <c:forEach items="${schoolList}" var="school" varStatus="vs">
                                <option value="${school.id}">${school.schoolName}</option>
                            </c:forEach>
                        </select>
                        <label class="label_search">巡检类型:</label>
                        <select id="templateName" name="templateName">
                            <option value="校务巡查">校务巡查</option>
                            <option selected value="后勤巡查">后勤巡查</option>
                        </select>
                        <label class="label_search">添加时间:</label>
                        <input class="date-pic" id="ksTime" type="text" name="ksTime" placeholder="请选择开始时间"
                               AUTOCOMPLETE="off">&nbsp--
                        <input class="date-pic" id="jsTime" type="text" name="jsTime" placeholder="请选择结束时间"
                               AUTOCOMPLETE="off">
                        <button class="btn btn-small btn_search" id="submit" type="submit" title="搜索">
                            搜索
                        </button>
                        <button class="btn btn-small btn_search" id="excel" onclick="toExcel()" title="导出">
                            导出
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<center>
    <table id="table_report" border="1">
        <tbody id="tab1">

        </tbody>
    </table>
</center>
</body>
<script type="text/javascript">
    $(function () {
        $("#ksTime").val(getCurrentDate(1));
        $("#jsTime").val(getCurrentDate(1));
        query();
    })


    var $sub = $("#submit");
    $sub.on("click", function () {
        $('#tab1').empty();
        query();
    })

    function query() {
        var data = {
            schoolId: $("#schoolId").val(),
            templateName: $("#templateName").val(),
            ksTime: $("#ksTime").val(),
            jsTime: $("#jsTime").val()
        }
        $.ajax({
            type: 'GET',
            url: 'inspectionLogs/logs/oneday',
            data: data,
            dataType: 'json',
            success: function (data) {
                var attrList = data.attr;
                var clazzList = data.list;
                var tr1 = "";
                var tr2 = "";
                var tr3 = "";
                var tr4 = "";
                var tr5 = "";
                console.log(data);
                attrList.forEach(function (attr, index) {
                    tr1 += '<th>' + attr.name + '</th>';
                    var scoreArray = attr.score.split("|");
                    tr3 += '<td>' + scoreArray[0] + '</td>';
                })
                tr2 = '<tr id="tr1"><th rowspan="2" style="width:10%">学校</th><th rowspan="2" style="width:5%">班级</th>' + tr1 + '<th rowspan="2" style="width:5%">总分</th></tr>';
                tr5 = '<tr id="tr2">' + tr3 + '</tr>';
                clazzList.forEach(function (clazzInfo, index) {
                    var list = clazzInfo.list;
                    var clazz = clazzInfo.clazz;
                    var schoolName = clazzInfo.schoolName;
                    var tr6 = "";
                    attrList.forEach(function (attr, index) {
                        var findFlag = false;
                        var result;
                        list.forEach(function (e) {
                            if (e.attrName == attr.name) {
                                findFlag = true;
                                result = e;
                            }
                        })

                        if (findFlag) {
                            tr6 += '<td>' + result.sum + '</td>';
                        } else {
                            console.info('flndFlag' + findFlag + " clazz:" + clazz);
                            tr6 += '<td></td>';
                        }

                    });

                    tr4 += '<tr id="tt' + index + '"><td>'+schoolName+'</td><td>' + clazz + '</td>' + tr6 + '<td>' + clazzInfo.itemScore + '</td></tr>';
                })
                var $tab1 = $('#tab1');
                $tab1.append(tr2);
                $tab1.append(tr5);
                $tab1.append(tr4);
            }

        })
    }

    /**
     *获取当前时间
     *format=1精确到天
     *format=2精确到分
     */
    function getCurrentDate(format) {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth();//得到月份
        var date = now.getDate();//得到日期
        var day = now.getDay();//得到周几
        var hour = now.getHours();//得到小时
        var minu = now.getMinutes();//得到分钟
        var sec = now.getSeconds();//得到秒
        month = month + 1;
        if (month < 10) month = "0" + month;
        if (date < 10) date = "0" + date;
        if (hour < 10) hour = "0" + hour;
        if (minu < 10) minu = "0" + minu;
        if (sec < 10) sec = "0" + sec;
        var time = "";
        //精确到天
        if (format == 1) {
            time = year + "-" + month + "-" + date;
        }
        //精确到分
        else if (format == 2) {
            time = year + "-" + month + "-" + date + " " + hour + ":" + minu + ":" + sec;
        }
        return time;
    }


    //导出excel
    function toExcel(){
        //获取表格
        var exportFileContent = document.getElementById("table_report").outerHTML;
        //设置格式为Excel，表格内容通过btoa转化为base64，此方法只在文件较小时使用(小于1M)
        //exportFileContent=window.btoa(unescape(encodeURIComponent(exportFileContent)));
        //var link = "data:"+MIMEType+";base64," + exportFileContent;
        //使用Blob
        var blob = new Blob([exportFileContent], {type: "text/plain;charset=utf-8"});         //解决中文乱码问题
        blob =  new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
        //设置链接
        var link = window.URL.createObjectURL(blob);
        var a = document.createElement("a");    //创建a标签
        var templateName = $("#templateName").val();
        a.download = "统计报表-"+templateName+".xls";  //设置被下载的超链接目标（文件名）
        a.href = link;                            //设置a标签的链接
        document.body.appendChild(a);            //a标签添加到页面
        a.click();                                //设置a标签触发单击事件
        document.body.removeChild(a);            //移除a标签
    }
</script>