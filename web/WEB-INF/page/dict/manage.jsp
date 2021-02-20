<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css" />

<div class="search_panel">
	<form action="dict/query.do" method="post" onsubmit="return pageSearch(this);">
	<div style="display: block;" class="widget-body ">
		<div class="widget-main">
			<div class="rowt">
				<div class="col-sm-12">
					<div>
							<label class="label_search">字典代码:</label>
								<input id="dict_code" type="text" name="dictCode"
									placeholder="请输入字典代码">
							<label class="label_search">字典名称:</label>
								<input id="dict_name" type="text" name="dictName"
									placeholder="请输入字典名称">
							<!-- <label class="label_search" for="userName">状态:</label> -->
							   <!--  <select 
									 rel="obj.DICTSTATUS"
						 			 location="local" 
									 name="status" 
						 			 id="status" 
						 			 data-placeholder="请选择状态" style="width: 120px;vertical-align:middle;">
									<option value='' >--请选择--</option>
								 </select> -->
							<button class="btn btn-small btn_search" type="submit" title="搜索" >
								搜索
							</button>
					</div>
					<div>
						<span> <a class="btn btn-small btn-add"
						onclick="dictadd();">新增</a> <a class="btn btn-small btn-update"
						onclick="dictedit();"> 修改 </a> 
						
						<a class="btn btn-small btn-default"
							onclick="status0_dictManage();"> <!-- 
							<i class="icon-envelope"></i> --> 禁用
						</a>
						<a class="btn btn-small btn_search"
							onclick="status1_dictManage();"> <!-- 
							<i class="icon-envelope"></i> --> 启用
						</a>
						<a class="btn btn-small btn-info"
							onclick="refresh_dictManage();"> <!-- 
							<i class="icon-envelope"></i> --> 数据最新化
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
			url : "dict/query.do",
			datatype : "json",
			height : '100%',
			width : '95%',
			jsonReader : {//该属性是将服务器返回的值，赋值给jqGrid初始化中的一些参数
				id : "dictId",
				root:"rows",
				page: "page",
				total: "totalPage",
				records: "total"
			},
			colNames : ['字典编号','上级编号' ,'字典代码', '字典名称', '值', '序号','级别','状态'],
			colModel : [
						 {name : 'dictId',     index : 'dictId',    width : '140px', sortable:true }, 
			             {name : 'parentId',   index : 'parentId',  width : '140px' }, 
			             {name : 'dictCode',   index : 'dictCode',  width : '140px' }, 
			             {name : 'dictName',   index : 'dictName',  width : '140px' }, 
			             {name : 'dictValue',  index : 'dictValue', width : '140px' }, 
			             {name : 'seqNum',     index : 'seqNum',    width : '140px' }, 
			             {name : 'dictLevel',  index : 'dictLevel', width : '140px' }, 
			             {name : 'status',  index : 'status', width : '140px',ditcKey:'DICTSTATUS', formatter : gridFormatByDict}
			            ],
			viewrecords : true,
			rowNum : 10,
			rowList : [ 10, 20, 30 ],
			altRows : true,
			//shrinkToFit:false,
			multiselect : true,//设置行可多选的 
			//multiboxonly : true,//

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
	
	//新增数据字典
		function dictadd() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length > 1) {
				msg.alert("警告", "不能同时选择多项数据！", "warn");
				return;
			}
			var rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
			if(rows.length ==0){
				openDialog({
					dialogId : 'dlg-dictadd',
					title : '新增字典类别',
					pageUrl : 'dict/dictManage/addView1.do',
					dataUrl : 'dict/dictManage/dictSeq.do',
					width : '600px',
					height:'380px',
					dataName : 'obj,map' //表单数据存储对象名称
				});
			}else{
				
				if(rowData.parentId != 0){
					msg.alert("警告", "请选择根节点(上级编号为0的数据)！", "warn");
					return;
				}
				openDialog({
					dialogId : 'dlg-dictadd',
					title : '新增字典键值',
					pageUrl : 'dict/dictManage/addView2.do',
					width : '600px',
					height: '468px',
					dataParam : {
						dictId : rowData.dictId,
						dictCode : rowData.dictCode
					},
					dataName : 'obj,map' //表单数据存储对象名称
				});
			}
			
			
		}
		//编辑数据字典
		function dictedit() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "error");
				return;
			}
			if (rows.length > 1) {
				msg.alert("警告", "不能同时选择多项数据！", "warn");
				return;
			}
			if(rows[0] == UID._USER_NO){
				msg.alert("警告", "系统超级用户不能被修改！", "warn");
				return ;
			}
			var rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
			//console.log(rowData);
			openDialog({
				dialogId : 'dlg-dictedit',
				title : '编辑用户',
				pageUrl : 'dict/dictManage/editView.do',
				dataUrl : 'dict/dictManage/editData.do?dictId='+rowData.dictId,
				width : '600px',
				height:'400px',
				dataName : 'obj,map' //表单数据存储对象名称
			});
		}

		function status0_dictManage(){
			status_dictManage(0);
		}
		function status1_dictManage(){
			status_dictManage(1);
		}
		
		function status_dictManage(status){
			var msg0,purl;
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择的数据项,请选择数据！", "warn");
				return;
			}
			if(status == "0"){
				purl = "dict/dictManage/statusSet/0.do";
				msg0 = "要禁用选中的数据吗?";
			}else{
				purl = "dict/dictManage/statusSet/1.do";
				msg0 = "要启用选中的数据吗?";
			}
			var ids="",rowData;
			for(var i=0; i<rows.length; i++){
				rowData = $('#grid-table').jqGrid('getRowData',rows[i]);
				ids = ids+rowData.dictId+",";
			}
			ids = ids.substring(0, ids.lastIndexOf(","));
			msg.confirm({title:'确认',position:'center',msg:msg0,call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
						url: purl+"?ids="+ids,
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
		
	function refresh_dictManage(){
		msg.confirm({title:'确认',position:'center',msg:'是否将数据最新化,数据将更新到内存中?',call:function(ok){
			$.ajax({
				url: "dict/up.do",
				type:"POST",
				dataType:"json",
				async:false,
				success: function(result){	
					if(result.rspcod == 200){
						var jobj =	eval("(" + result.obj + ")");
						DICT.obj = jobj;
						msg.alert("提示", "更新成功", 'correct');
					}else{
						alert("错误代码："+result.rspcod+",错误描述："+result.rspmsg);
					}
				},
				error:function(XMLHttpRequest, textStatus){
					alert("网络异常代码："+XMLHttpRequest.status+",异常描述："+textStatus);
				}
			});
		}});
    }
</script>
