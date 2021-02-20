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
						<!--  
							<div class="table-from">
							<input id="userId" name="userId" class="search-input" type="text" autocomplete="off" placeholder="操作员账号 ...">
							<i class="ace-icon fa fa-search glyphicon-search"></i>
							</div>-->
							<label class="label_search">角色编号:</label>
								<input id="roleId" type="text" name="roleId"
									placeholder="请输入角色编号">
							<label class="label_search">角色名称:</label>
								<input id="roleName" type="text" name="roleName"
									placeholder="请输入角色名称">
							<button class="btn btn-small btn_search" type="submit" title="搜索" >
								搜索
							</button>
					</div>
					<div>
						<span> <a class="btn btn-small btn-add"
					onclick="roleadd();">新增</a> <a class="btn btn-small btn-update"
					onclick="roleedit();"> 修改 </a> 
					<!-- <a class="btn btn-small btn-delete"
					onclick="roledelete();"> 
					<i class="icon-envelope"></i> 删除
				</a> -->
				<a class="btn btn-small btn-default"
					onclick="upRoleStatus('0');"> <!-- 
					<i class="icon-envelope"></i> --> 启用
				</a>
				<a class="btn btn-small btn-default"
					onclick="upRoleStatus('1');"> <!-- 
					<i class="icon-envelope"></i> --> 禁用
				</a>
				
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

			<!-- PAGE CONTENT ENDS -->
		</div>
		<!-- /.col -->
	</div>
	<!-- /.row -->

	<!-- page specific plugin scripts -->
	<script type="text/javascript">
		var jqGrid;
		jQuery(function($) {
			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";

			$(window).on(
					'resize.jqGrid',
					function() {
						$(grid_selector).jqGrid('setGridWidth',
								$(".page-content").width());
					})
			var parent_column = $(grid_selector).closest('[class*="col-"]');
			$(document).on(
					'settings.ace.jqGrid',
					function(ev, event_name, collapsed) {
						if (event_name === 'sidebar_collapsed'
								|| event_name === 'main_container_fixed') {
							setTimeout(function() {
								$(grid_selector).jqGrid('setGridWidth',
										parent_column.width());
							}, 0);
						}
					})
			jQuery(grid_selector).jqGrid({
				url : "auth/role/queryRoleList.do",
				datatype : "json",
				height : '100%',
				width : '95%',
				colNames : [ '角色编号','角色名称' ,'角色状态', '描述' ],
				colModel : [ {name : 'roleId',      index : 'roleId',      width : '300px' }, 
				             {name : 'roleName',    index : 'roleName',    width : '290px' }, 
				             {name : 'roleStatus',  index : 'roleStatus',  width : '220px',ditcKey:'ROLESTATUS', formatter : gridFormatByDict }, 
				             {name : 'roleDesc',    index : 'roleDesc',    width : '350px' }
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
				},
			});
			$(window).triggerHandler('resize.jqGrid');

			$(document).one('ajaxloadstart.page', function(e) {
				$(grid_selector).jqGrid('GridUnload');
				$('.ui-jqdialog').remove();
			});
		});

		//新增角色
		function roleadd() {
			openDialog({
				dialogId : 'dlg-role',
				title : '新增角色',
				pageUrl : 'auth/role/addRoleView.do',
				width : '50%',
			    height:'90%'
			});
		}
		//编辑角色
		function roleedit() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "error");
				return;
			}
			if (rows.length > 1) {
				msg.alert("警告", "不能同时选择多项数据！", "warn");
				return;
			}
			if(rows[0] == "0001"){
				msg.alert("警告", "系统超级角色不能被修改！", "warn");
				return ;
			}
			
			var rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
			var roleId = rowData['roleId'];
			
			openDialog({
				dialogId : 'dlg-role',
				title : '编辑角色',
				pageUrl : 'auth/role/updateRoleView.do',
				dataUrl : 'auth/queryRole.do',
				dataName : 'obj',
				dataParam : {
					roleId : roleId
				},
				width : '50%',
			    height:'90%'

			});
		}
		//角色删除
		function roledelete() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "warn");
				return;
			}
			//
			var ids = "";
			for (var i = 0; i < rows.length; i++) {
				var rowData = $('#grid-table').jqGrid('getRowData',rows[i]);
				var roleId = rowData['roleId'];
				if(i > 0){
					ids += ",";
				}
				ids += roleId;
			}
			msg.confirm({title:'确认',position:'center',msg:'您确定要删除选中的数据吗？',call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
						url : "auth/userManage/delete.do?roleId=" + ids,
						dataType : 'json',
						success : function(result) {
							if (result.rspcod != 200) {
								msg.alert("错误", result.rspmsg + " 错误代码："
										+ result.rspcod, 'error');
							} else {
								msg.alert("提示", result.rspmsg, 'correct');
								$("#grid-table").trigger("reloadGrid");
							}
						},
						error : function(XMLHttpRequest, textStatus) {
							msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述："
									+ textStatus, 'error');
						}
					});

				}
			}});
			
			}
		/**角色状态修改*/
		function upRoleStatus(status){
			var tmsg = "启用";
			if(status == 1){
				tmsg = "禁用";
			}
			
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "warn");
				return;
			}
			//
			var ids = "";
			for (var i = 0; i < rows.length; i++) {
				var rowData = $('#grid-table').jqGrid('getRowData',rows[i]);
				var roleId = rowData['roleId'];
				if(i > 0){
					ids += ",";
				}
				ids += roleId;
			}
			
			msg.confirm({title:'确认',position:'center',msg:'您确定要'+tmsg+'选择的角色吗？',call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
					    url : "auth/updateRoleStatusBatch.do?status="+status+"&rid="+ids,
						dataType : 'json',
						success : function(result) {
							if (result.rspcod != 200) {
								msg.alert("错误", result.rspmsg + " 错误代码："
										+ result.rspcod, 'error');
							} else {
								alertMsg.correct({title:'成功',msg:result.rspmsg});
							}
							$("#grid-table").trigger("reloadGrid");
						},
						error : function(XMLHttpRequest, textStatus) {
							msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述："
									+ textStatus, 'error');
							$("#grid-table").trigger("reloadGrid");
						}
					});

				}
			}});
			
		}
	</script>