<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css" />
<div class="search_panel">
    <form action="" method="post" onsubmit="return pageSearch(this);">
        <div style="display: block;" class="widget-body ">
            <div class="widget-main">
                <div class="rowt">
                    <div class="col-sm-12">
                        <div>
                            <label class="label_search">班级:</label>
                            <input id="keyword" type="text" name="clazz" placeholder="请输入班级">
                            <label class="label_search">年级:</label>
                            <input id="keyword" type="text" name="grade" placeholder="请输入年级">
                            <button class="btn btn-small btn_search" type="submit" title="搜索" >
                                搜索
                            </button>
                        </div>
                        <div>
							<span>
								<a class="btn btn-small btn-add" onclick="clazzInfAdd();">新增</a>
								<a class="btn btn-small btn-update" onclick="clazzInfEdit();"> 修改 </a>
								<a class="btn btn-small btn-delete" onclick="clazzInfDelete();"> 删除</a>
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
    jQuery(function($) {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        $(window).on('resize.jqGrid',function() {
            $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid',function(ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                setTimeout(function() {
                    $(grid_selector).jqGrid('setGridWidth',parent_column.width());
                }, 0);
            }
        });
        jQuery(grid_selector).jqGrid({
            url : "clazzInf/query1",
            datatype : "json",
            height : '100%',
            width : '95%',
            jsonReader : {
                root:"rows",
                page: "page",
                total: "totalPage",
                records: "total"
            },
            colNames :['班级编号','班级','年级','班主任','学生总数','班级称号','原班级','原年级','操作'],
            colModel : [
                {name : 'id',index : 'id',width : '20%' },
                {name : 'clazz',index : 'clazz',width : '20%' },
                {name : 'grade',index : 'grade',width : '20%' },
                {name : 'headerTeacher',index : 'header_teacher',width : '20%' },
                {name : 'totalPeo',index : 'total_peo',width : '20%' },
                {name : 'slogan',index : 'slogan',width : '20%' },
                {name : 'h_clazz',index : 'slogan',width : '20%' },
                {name : 'h_grade',index : 'slogan',width : '20%' },
                {name : '',index : '',width : '20%',sortable:false,formatter:formatOper}
            ],
            viewrecords : true,
            rowNum : 10,
            rowList : [ 10, 20, 30 ],
            altRows : true,
            //shrinkToFit:false,
            multiselect : true,//设置行可多选的
            multiboxonly : true,//

            loadComplete : function() {
                var table = this;

                setTimeout(function() {
                    //加载分页
                    initPagingBar(grid_selector);
                }, 0);
            },
            beforeRequest : function() {//请求之前执行
                jqGrid = this;
            }
        });
        $(window).triggerHandler('resize.jqGrid');

        $(document).one('ajaxloadstart.page', function(e) {
            $(grid_selector).jqGrid('GridUnload');
            $('.ui-jqdialog').remove();
        });
    });

    //详情页面
    function formatOper(cellvalue, options, rowObject){
        var grade = rowObject.grade.substr(0,1);
        var grade1 =  rowObject.grade;

        if(rowObject.upgrade == 0 && grade  < 7) {
            var html='<span><a class="btn btn-small btn-info" onclick="classDetail('+rowObject.id+');">详情</a>&nbsp;'+
                '<span><a class="btn btn-small btn-info "  onclick="classupgrade('+rowObject.id+');">升级</a>&nbsp;';
        }
        else if(grade1 == "毕业年级"){
            var html='<span><a class="btn btn-small btn-info" onclick="classDetail('+rowObject.id+');">详情</a>&nbsp;'+
                '<span><a class="btn btn-small btn-info " >毕业年级</a>&nbsp;';
        }

        else
        {
            var html='<span><a class="btn btn-small btn-info" onclick="classDetail('+rowObject.id+');">详情</a>&nbsp;'+
                '<span><a class="btn btn-small btn-info " >已升级</a>&nbsp;';

        }
        return html;
    }

    function classDetail(id) {
        openDialog({
            dialogId : 'dlg-detail',
            title : '班级详情',
            pageUrl : 'clazzInf/claDetail?id='+id,
            width : '850px',
            height:'500px'
        });
    }


    //升级
    function classupgrade(id)
    {

        msg.confirm({title:'确认',position:'center',msg:'您确定要升级吗？',call:function(ok){
                if(ok){
                    $.ajax({
                        type : "post",
                        url : "clazzInf/classupgrade?id=" + id,
                        dataType : 'json',
                        success : function(result) {
                            if (result.returnCode != 1) {
                                msg.alert("错误", result.errorinfo, 'error');
                            } else {
                                msg.alert("提示", '操作成功', 'correct');
                                $("#grid-table").trigger("reloadGrid");
                            }
                        },
                        error : function(XMLHttpRequest, textStatus) {
                            msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
                        }

                    });
                }
            }});
    }

    //新增
    function clazzInfAdd() {
        openDialog({
            dialogId : 'dlg-role',
            title : '新增班级信息',
            pageUrl : 'clazzInf/add/view',
            width : '850px',
            height:'500px'
        });
    }
    //编辑
    function clazzInfEdit() {
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
            dialogId : 'dlg-role',
            title : '编辑班级信息',
            pageUrl :'clazzInf/edit/view?id='+rows,
            width :'850px',
            height:'500px',
            dataName : 'obj' //表单数据存储对象名称
        });
    }
    //删除
    function clazzInfDelete() {
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
        msg.confirm({title:'确认',position:'center',msg:'您确定要删除选中的数据吗？',call:function(ok){
                if(ok){
                    $.ajax({
                        type : "post",
                        url : "clazzInf/deleteBatch?ids=" + ids,
                        dataType : 'json',
                        success : function(result) {
                            if (result.returnCode != 1) {
                                msg.alert("错误", result.errorinfo, 'error');
                            } else {
                                msg.alert("提示", '操作成功', 'correct');
                                $("#grid-table").trigger("reloadGrid");
                            }
                        },
                        error : function(XMLHttpRequest, textStatus) {
                            msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
                        }
                    });

                }
            }});
    }
</script>
