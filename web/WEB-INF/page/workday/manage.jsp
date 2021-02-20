<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css"/>
<style>
    .ui-jqgrid tr.jqgrow td {
        white-space: normal !important;
        height:auto;
        vertical-align:text-top;
        padding-top:2px;
    }
</style>
<div class="search_panel">
    <form action="" method="post" onsubmit="return pageSearch(this);">
        <div style="display: block;" class="widget-body ">
            <div class="widget-main">
                <div class="rowt">
                    <div class="col-sm-12">
                        <div>
                            <label class="label_search">关键字:</label>
                            <input id="year" type="text" name="year" placeholder="请输入年">
                            <input id="month" type="text" name="month" placeholder="请输入月">
                            <button class="btn btn-small btn_search" type="submit" title="搜索">
                                搜索
                            </button>
                        </div>
                        <div>
							<span>
								<a class="btn btn-small btn-add" onclick="workdayAdd();">新增</a>
								<a class="btn btn-small btn-update" onclick="workdayEdit();"> 修改 </a>
								<a class="btn btn-small btn-delete" onclick="workdayDelete();"> 删除</a>
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
            <span id="paging_bar" style="float: right"> </span>
        </div>
    </div>
</div>
<script type="text/javascript">
    var jqGrid;
    jQuery(function ($) {
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
            url: "workDay/query_extra",
            datatype: "json",
            height: '100%',
            width: '95%',
            jsonReader: {
                root: "rows",
                page: "page",
                total: "totalPage",
                records: "total"
            },
            colNames: ['id', '年', '月', '工作日', '休息日', '编辑时间'],
            colModel: [
                {name: 'id', index: 'id', width: '5%', hidden: true},
                {name: 'year', index: 'year', width: '10%'},
                {name: 'month', index: 'month', width: '10%'},
                {name: 'workday', index: 'workday', width: '20%'},
                {name: 'holiday', index: 'holiday', width: '20%'},
                {name: 'edittime', index: 'edittime', width: '10%'}
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

    //新增
    function workdayAdd() {
        openDialog({
            dialogId: 'dlg-role',
            title: '新增校历表',
            pageUrl: 'workDay/add/view',
            width: '1080px',
            height: '550px'
        });
    }

    //编辑
    function workdayEdit() {
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
            title: '编辑校历表',
            pageUrl: 'workDay/edit/view?id=' + rows,
            width: '1080px',
            height: '550px',
            dataName: 'obj' //表单数据存储对象名称
        });
    }

    //删除
    function workdayDelete() {
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
                        url: "workDay/deleteBatch?ids=" + ids,
                        dataType: 'json',
                        success: function (result) {
                            if (result.returnCode != 1) {
                                msg.alert("错误", result.msg, 'error');
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

</script>
