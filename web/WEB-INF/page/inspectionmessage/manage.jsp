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
                            <label class="label_search">消息类型:</label>
                            <input id="title2" type="text" name="title2" placeholder="请输入消息类型">
                            <label class="label_search">内容类型:</label>
                            <select name="type" style="width: 130px;vertical-align:middle;">
                                <option value='' selected>请选择</option>
                                <option value='1'>校务巡查反馈</option>
                                <option value='2'>一周综述</option>
                                <option value='3'>校园大事记</option>
                                <option value='6'>后勤巡查反馈</option>
                            </select>
                            <label class="label_search" style="width:44px"></label>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div>
                            <label class="label_search">提交人:</label>
                            <input id="keyword" type="text" name="userName" placeholder="请输入提交人">
                            <label class="label_search">内容描述:</label>
                            <input id="keyword" type="text" name="remark" placeholder="请输入内容描述">
                            <label class="label_search">添加时间:</label>
                            <input class="date-pic"  id="ksTime" type="text" name="ksTime" placeholder="请选择开始时间"
                                   AUTOCOMPLETE="off">&nbsp--
                            <input class="date-pic"  id="jsTime" type="text" name="jsTime" placeholder="请选择结束时间"
                                   AUTOCOMPLETE="off">
                            <%--                            <input id="title" type="text" name="title" value="校务巡查" style="display:none;">--%>
                            <button class="btn btn-small btn_search" type="submit" title="搜索">
                                搜索
                            </button>
                        </div>
                        <div>
							<span>
								<a class="btn btn-small btn-update" onclick="inspectionMessageEdit();"> 修改 </a>
								<a class="btn btn-small btn-delete" onclick="inspectionMessageDelete();"> 删除</a>
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
            <span><a class="btn btn-small btn-add" onclick="studentBillExport();">导出数据</a></span>
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
            url: "inspectionMessage/queryAll",
            datatype: "json",
            height: '100%',
            width: '95%',
            jsonReader: {
                root: "rows",
                page: "page",
                total: "totalPage",
                records: "total"
            },
            postData: {ksTime: $("#ksTime").val(), jsTime: $("#jsTime").val()},
            colNames: ['id', '内容类型', '消息类型', '内容描述', '提交人', '添加时间', '操作'],
            colModel: [
                {name: 'id', index: 'id', width: '20%'},
                {
                    name: 'type', index: 'type', width: '20%', formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue == 1) {
                            return '校务巡查反馈';
                        } else if (cellvalue == 2) {
                            return '一周综述';
                        } else if (cellvalue == 3) {
                            return '校园大事记';
                        } else if (cellvalue == 6) {
                            return '后勤巡查反馈';
                        } else {
                            return '';
                        }
                    }
                },
                {
                    name: '', index: '', width: '20%',
                    formatter: function (cellvalue, options, rowObject) {
                        var title = rowObject.title;
                        if (rowObject.type == 6) {
                            return title;
                        } else {
                            var titleArray = title.split("-");
                            return titleArray[1].replace("校务巡查反馈", '').replace("校务巡查", '');
                        }


                    }
                },
                {name: 'remark', index: 'remark', width: '20%'},
                {name: 'userName', index: 'user_name', width: '20%'},
                {name: 'addTime', index: 'fmt_date', width: '20%'},
                {
                    name: '',
                    index: '',
                    width: '20%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        var html = '<span><a class="btn btn-small btn-info" onclick="classDetail(' + rowObject.id + ');">详情</a>&nbsp';
                        return html;
                    }
                }
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

    //导出
    function studentBillExport() {
        msg.confirm({
            title: '确认', position: 'center', msg: '您确定要导出综述消息数据吗？', call: function (ok) {
                if (ok) {
                    window.location.href = "inspectionMessage/msgExport4zsxx?tpl=综述消息&" + $("form").serialize();
                }
            }
        });
    }


    //详情页面
    function classDetail(id) {
        openDialog({
            dialogId: 'dlg-detail',
            title: '内容详情',
            pageUrl: 'inspectionMessage/Detail?id=' + id,
            width: '850px',
            height: '500px'
        });
    }

    //编辑
    function inspectionMessageEdit() {
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
            title: '编辑',
            pageUrl: 'inspectionMessage/edit/view?id=' + rows,
            width: '500px',
            height: '350px',
            dataName: 'obj' //表单数据存储对象名称
        });
    }

    //删除
    function inspectionMessageDelete() {
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
                        url: "inspectionMessage/deleteBatch?ids=" + ids,
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
