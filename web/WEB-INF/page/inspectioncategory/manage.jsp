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
                            <label class="label_search">分类描述:</label>
                            <input id="value" type="text" name="value" placeholder="请输入分类描述">
                            <label class="label_search">分类级别:</label>
                            <input id="ilevel" type="text" name="ilevel" placeholder="请输入分类级别">
                            <button class="btn btn-small btn_search" type="submit" title="搜索">
                                搜索
                            </button>
                        </div>
                        <div>
						<span>
							<a class="btn btn-small btn-add" onclick="inspectionCategoryAdd();">新增</a>
							<a class="btn btn-small btn-update" onclick="inspectionCategoryEdit();"> 修改 </a>
							<a class="btn btn-small btn-delete" onclick="inspectionCategoryDelete();"> 删除</a>
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
            url: "inspectionCategory/queryList",
            datatype: "json",
            height: '100%',
            width: '95%',
            jsonReader: {
                root: "rows",
                page: "page",
                total: "totalPage",
                records: "total"
            },
            colNames: ['id', '分类描述', '分类级别', '一级编码', '开始时间', '结束时间', '学校名称', '巡检类别'],

            colModel: [
                {name: 'id', index: 'id', width: '20%'},
                {name: 'value', index: 'value', width: '20%'},
                {name: 'ilevel', index: 'ilevel', width: '20%'},
                {name: 'pid1', index: 'pid1', width: '20%'},
                {name: 'startTime', index: 'start_time', width: '20%'},
                {name: 'endTime', index: 'end_time', width: '20%'},
                {name: 'schoolname', index: 'schoolname', width: '20%'},
                {name: 'value1', index: 'value1', width: '20%'}

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
    function inspectionCategoryAdd() {


        var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
        if (rows.length > 1) {
            msg.alert("警告", "不能同时选择多项数据！", "warn");
            return;
        }
        var rowData = $('#grid-table').jqGrid('getRowData', rows[0]);
        if (rows.length == 0) {
            openDialog({
                dialogId: 'dlg-role',
                title: '新增1级类别',
                pageUrl: 'inspectionCategory/add/view',
                width: '500px',
                height: '350px'
            });
        } else {
            if (rowData.ilevel != 1) {
                msg.alert("警告", "请选择1级分类！", "warn");
                return;
            }
            if (rowData.value1 != "校内执勤" && rowData.value1 != "护校队巡查") {
                openDialog({
                    dialogId: 'dlg-add3',
                    title: '新增2级类别',
                    pageUrl: 'inspectionCategory/addView3',
                    width: '600px',
                    height: '468px',
                    dataParam: {
                        id: rowData.id,
                    },
                    dataName: 'obj,map' //表单数据存储对象名称
                });
            } else {
                openDialog({
                    dialogId: 'dlg-add2',
                    title: '新增2级类别',
                    pageUrl: 'inspectionCategory/addView2',
                    width: '780px',
                    height: '468px',
                    dataParam: {
                        id: rowData.id,
                    },
                    dataName: 'obj,map' //表单数据存储对象名称
                });
            }
        }


    }

    //编辑
    function inspectionCategoryEdit() {
        var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
        var rowData = $('#grid-table').jqGrid('getRowData', rows[0]);
        if (rows.length == 0) {
            msg.alert("警告", "当前没有选择数据项！", "error");
            return;
        }
        if (rows.length > 1) {
            msg.alert("警告", "不能同时选择多项数据！", "warn");
            return;
        }
        if (rowData.ilevel == "2" && (rowData.value1 == "校内执勤" || rowData.value1 == "护校队巡查")) {
            openDialog({
                dialogId: 'dlg-role2',
                title: '编辑分类描述',
                pageUrl: 'inspectionCategory/edit2/view?id=' + rows,
                width: '600px',
                height: '500px',
                dataName: 'obj' //表单数据存储对象名称
            });
        } else {
            openDialog({
                dialogId: 'dlg-role',
                title: '编辑分类描述',
                pageUrl: 'inspectionCategory/edit/view?id=' + rows,
                width: '600px',
                height: '500px',
                dataName: 'obj' //表单数据存储对象名称
            });
        }
    }

    //删除
    function inspectionCategoryDelete() {
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
                        url: "inspectionCategory/deleteBatch?ids=" + ids,
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
</script>
