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
							<label class="label_search">学校编号:</label>
								<input id="schoolBm" type="text" name="schoolBm"
									placeholder="请输入学校编号">
							<label class="label_search">学校名称:</label>
								<input id="schoolName" type="text" name="schoolName"
									placeholder="请输入学校名称">
							<button class="btn btn-small btn_search" type="submit" title="搜索" >
								搜索
							</button>
						</div>
						<div>
							<span id="auth_btn">
								<a class="btn btn-small btn-add" onclick="schoolInfAdd();">新增</a>
								<a class="btn btn-small btn-update" onclick="schoolInfEdit();"> 修改 </a>
								<a class="btn btn-small btn-delete" onclick="schoolInfDelete();"> 删除</a>
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
			url : "schoolInf/query",
			datatype : "json",
			height : '100%',
			width : '95%',
			jsonReader : {
				root:"rows",
				page: "page",
				total: "totalPage",
				records: "total"
			},
			colNames :['学校ID','学校编号','学校名称','校长','学校国际名称','学校简称','联系人名称','操作'],
			colModel : [
						{name : 'id',index : 'id',width : '20%' ,hidden:true},
						{name : 'schoolBm',index : 'school_bm',width : '20%' },
						{name : 'schoolName',index : 'school_name',width : '20%' },
						{name : 'president',index : 'president',width : '20%' },
						{name : 'iname',index : 'iname',width : '20%' },
						{name : 'sname',index : 'sname',width : '20%' },
						{name : 'contactsName',index : 'contacts_name',width : '20%' },
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
		var html='<span><a class="btn btn-small btn-info" onclick="schoolDetail('+rowObject.id+');">详情</a>&nbsp;';
	    return html;  
	}
	
	function schoolDetail(id) {
		openDialog({
			dialogId : 'dlg-detail',
			title : '学校详情',
			pageUrl : 'schoolInf/schDetail?id='+id,
			width : '850px',
		    height:'500px'
		});
	}
	
	//新增
	function schoolInfAdd() {
		openDialog({
			dialogId : 'dlg-role',
			title : '新增学校信息',
			pageUrl : 'schoolInf/add/view',
			width : '850px',
		    height:'500px'
		});
	}
	//编辑
	function schoolInfEdit() {
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
            title : '编辑学校信息',
            pageUrl :'schoolInf/edit/view?id='+rows,
            width :'850px',
            height:'500px',
            dataName : 'obj' //表单数据存储对象名称
        });
	}
    //删除
    function schoolInfDelete() {
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
                    url : "schoolInf/deleteBatch?ids=" + ids,
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
