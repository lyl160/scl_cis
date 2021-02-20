<%--校务巡查--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css"/>
<div class="search_panel">
    <form action="" method="post" onsubmit="return pageSearch(this);">
        <div style="display: block;" class="widget-body ">
            <div class="widget-main">
                <div class="rowt">
                    <div class="col-sm-12">
                        <div>
                            <label class="label_search">班级:</label>
                            <input id="clazz" type="text" name="clazz" placeholder="请输入班级">
                            <label class="label_search">年级:</label>
                            <input id="grade" type="text" name="grade" placeholder="请输入年级">
                            <label class="label_search">添加时间:</label>
                            <input class="date-pic"  id="ksTime" type="text" name="ksTime" placeholder="请选择开始时间"
                                   AUTOCOMPLETE="off">&nbsp--
                            <input class="date-pic"  id="jsTime" type="text" name="jsTime" placeholder="请选择结束时间"
                                   AUTOCOMPLETE="off">
                            <input id="name" type="text" name="name" value="校务巡查" style="display:none;">
                            <button class="btn btn-small btn_search" type="submit" title="搜索">
                                搜索
                            </button>
                        </div>
                        <div>
							<span>
							</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="row">
    <div class="col-xs-12">
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
        <div style="margin-top: 10px">
            <span><a class="btn btn-small btn-add" onclick="studentBillExport();">导出校务巡查数据</a></span>
            <span id="paging_bar" style="float: right"> </span>
        </div>
    </div>
</div>
<script type="text/javascript">
    var jqGrid;
    jQuery(function ($) {
        $("#ksTime").val(getPrevDate());
        $("#jsTime").val(getCurrentDate());
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                setTimeout(function () {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }, 0);
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "inspectionLogs/query1",
            datatype: "json",
            height: '100%',
            width: '95%',
            jsonReader: {
                root: "rows",
                page: "page",
                total: "totalPage",
                records: "total"
            },
            postData: {name: "校务巡查",ksTime:$("#ksTime").val(),jsTime:$("#jsTime").val()},
            colNames: ['id', '巡检类别','教师', '班级Id', '班级', '年级', '提交时间', '操作'],
            colModel: [
                {name: 'id', index: 'id', width: '20%'},
                {name: 'value', index: 'value', width: '20%'},
                {name: 'teacherName', index: 'teacherName', width: '20%'},
                {name: 'clazzId', index: 'clazz_id', width: '20%'},
                {name: 'clazz', index: 'clazz', width: '20%'},
                {name: 'grade', index: 'grade', width: '20%'},
                {name: 'addTime', index: 'grade', width: '20%'},
                {name: '', index: '', width: '20%', sortable: false, formatter: functionFormat}
            ],
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            altRows: true,
            //shrinkToFit:false,
            multiselect: true,//设置行可多选的
            multiboxonly: true,//

            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    //加载分页
                    initPagingBar(grid_selector);
                }, 0);
            },
            beforeRequest: function () {//请求之前执行
                jqGrid = this;
            }
        });
        $(window).triggerHandler('resize.jqGrid');

        $(document).one('ajaxloadstart.page', function (e) {
            $(grid_selector).jqGrid('GridUnload');
            $('.ui-jqdialog').remove();
        });
    });

    //操作格式化
    function functionFormat(cellvalue, options, rowObject) {

        var str = '';
        str += '<span><a class="btn btn-small btn-info" onclick="pledgeGoodsMx('
            + rowObject.id + ');">详情</a>&nbsp;' + '</span>';

        return str;
    }

    //查看详情
    function pledgeGoodsMx(id) {
        openDialog({
            dialogId: 'dlg-mx',
            title: '校务巡查详情',
            pageUrl: 'inspectionLogs/detail?id=' + id + '&value=校务巡查',
            width: '750px',
            height: '500px',
            dataName: 'obj' //表单数据存储对象名称
        });
    }


    //编辑
    function inspectionLogsEdit() {
        var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");

        if (rows.length == 0) {
            msg.alert("警告", "当前没有选择数据项！", "error");
            return;
        }
        if (rows.length > 1) {
            msg.alert("警告", "不能同时选择多项数据！", "warn");
            return;
        }
        openDialog({
            dialogId: 'dlg-role',
            title: '编辑会员积分',
            pageUrl: 'inspectionLogs/edit/view?id=' + rows,
            width: '500px',
            height: '350px',
            dataName: 'obj' //表单数据存储对象名称
        });
    }

    //删除
    function inspectionLogsDelete() {
        var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
        if (rows.length == 0) {
            msg.alert("警告", "当前没有选择数据项！", "warn");
            return;
        }
        var ids = "";
        for (var i = 0; i < rows.length; i++) {
            ids = ids + rows[i] + ",";
        }
        ids = ids.substring(0, ids.lastIndexOf(","));
        msg.confirm({
            title: '确认', position: 'center', msg: '您确定要删除选中的数据吗？', call: function (ok) {
                if (ok) {
                    $.ajax({
                        type: "post",
                        url: "inspectionLogs/deleteBatch?ids=" + ids,
                        dataType: 'json',
                        success: function (result) {
                            if (result.returnCode != 1) {
                                msg.alert("错误", result.errorinfo, 'error');
                            } else {
                                msg.alert("提示", '操作成功', 'correct');
                                $("#grid-table").trigger("reloadGrid");
                            }
                        },
                        error: function (XMLHttpRequest, textStatus) {
                            msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
                        }
                    });

                }
            }
        });
    }

    function studentBillExport() {
        msg.confirm({
            title: '确认', position: 'center', msg: '您确定要导出巡查数据吗？', call: function (ok) {
                if (ok) {
                    window.location.href = "inspectionLogs/msgExport4Xw?tpl=校务巡查&" + $("form").serialize();
                }
            }
        });
    }

    /**
     *获取当前时间 精确到天
     */
    function getCurrentDate() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth();//得到月份
        var date = now.getDate();//得到日期
        var day = now.getDay();//得到周几
        month = month + 1;
        if (month < 10) month = "0" + month;
        if (date < 10) date = "0" + date;
        var time = year + "-" + month + "-" + date;
        return time;
    }
    /**
     *获取上个月时间 精确到天
     */
    function getPrevDate() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth()-1;//得到月份
        var date = now.getDate()+1;//得到日期
        var day = now.getDay();//得到周几
        month = month + 1;
        if (month < 10) month = "0" + month;
        if (date < 10) date = "0" + date;
        var time = year + "-" + month + "-" + date;
        return time;
    }

</script>
