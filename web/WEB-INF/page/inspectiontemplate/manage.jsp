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
							<label class="label_search">关键字:</label>
								<input id="keyword" type="text" name="name" placeholder="请输入模板名称">
							<button class="btn btn-small btn_search" type="submit" title="搜索" >
								搜索
							</button>
						</div>
						<div>
							<span>
								<a class="btn btn-small btn-add" onclick="inspectionTemplateAdd();">新增</a>
								<a class="btn btn-small btn-update" onclick="inspectionTemplateEdit();"> 修改 </a>
								<!-- <a class="btn btn-small btn-delete" onclick="inspectionTemplateDelete();"> 删除</a> -->
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
			url : "inspectionTemplate/queryAll",
			datatype : "json",
			height : '100%',
			width : '95%',
			jsonReader : {
				root:"rows",
				page: "page",
				total: "totalPage",
				records: "total"
			},
			colNames :['id','模板名称','模板备注','学校名称','新增时间','schoolId','修改时间','操作'],
			colModel : [
						{name : 'id',index : 'id',width : '20%' },
						{name : 'name',index : 'name',width : '20%' },
						{name : 'remark',index : 'remark',width : '20%' },
						{name : 'schoolname',index : 'schoolname',width : '20%' },
						{name : 'addTime',index : 'add_time',width : '20%' },
						{name : 'schoolId',index : 'school_id',hidden:true },
						{name : 'editTime',index : 'edit_time',width : '20%' },
			            {name : '',index : '',width : '20%',sortable:false,formatter: functionFormat }
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
	//新增
	function inspectionTemplateAdd() {
		openDialog({
			dialogId : 'dlg-role',
			title : '新增模板',
			pageUrl : 'inspectionTemplate/add/view',
			width : '500px',
		    height:'350px'
		});
	}
	//编辑
	function inspectionTemplateEdit() {
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
            title : '编辑模板',
            pageUrl :'inspectionTemplate/edit/view?id='+rows,
            width :'500px',
            height:'350px',
            dataName : 'obj' //表单数据存储对象名称
        });
	}
    //删除
    function inspectionTemplateDelete() {
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
                    url : "inspectionTemplate/deleteBatch1?ids=" + ids,
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
    //跳转到动态属性设置页面
	function queryDetail(id,tname,schoolId) {
		openDialog({
			dialogId : 'dlg-template',
			title : '属性详情&nbsp&nbsp&nbsp&nbsp[提示：单选对应前台下拉框，多选对应前台复选框，文本对应前台输入框，数值对应前台输入框且只能输入数字]',
			pageUrl : 'dynamicAttr/manageAttr?templateId=' + id+"&tname="+tname+"&schoolId="+schoolId,
			width : '1000px',
			height : '630px',
			dataName : 'obj' //表单数据存储对象名称
		});
	}

	function functionFormat(cellvalue, options, rowObject) {
		var html = '';
		if('校务巡查'==rowObject.name||'后勤巡查'==rowObject.name){
			html += '<span><a class="btn btn-small btn-info" onclick="queryDetail('
				+ rowObject.id+",'"+rowObject.name+"',"+rowObject.schoolId + ');">属性管理</a></span>';
		}
		return html;
	}
</script>
