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
							<div class="search_input">
							<label class="label_search">菜单名称:</label>
								<input id="menuName" type="text" name="menuName"
									placeholder="请输入菜单名称">
							<button class="btn btn-small btn_search" type="submit" title="搜索">
								搜索
							</button>
							</div>
						</div>
						<div>
							<span>
					            <!-- 
								        
								    	属性说明: 用于按钮权限控制，auth地址与用户使用该功能的地址一致。
								    	
								 -->
								<a  class="btn btn-small btn-add" onclick="menuadd();">新增菜单</a>
				<!-- 				<a class="btn btn-small btn-add" onclick="menubutton();">新增按钮</a> -->
								<a class="btn btn-small btn-update" 
									onclick="menuedit_bank();">
								          修改
								</a> 
								<a class="btn btn-small btn-delete" onclick="destroyMenu();">
									<!-- 
									<i class="icon-envelope"></i> -->  删除
								</a>
								<a class="btn btn-small btn-default"
									onclick="statusUpdate0();"> <!-- 
									<i class="icon-envelope"></i> --> 启用
								</a>
								<a class="btn btn-small btn-default"
									onclick="statusUpdate1();"> <!-- 
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
	<!-- 检索 -->
			<!-- 检索  -->
		<table id="grid-table"></table>
		<div style="margin-top: 10px">
	        
			<span id="paging_bar" style="float: right">
			</span>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->


<!-- page specific plugin scripts -->
<script type="text/javascript">


//	var scripts = [null,"ace_v1.3/assets/js/date-time/bootstrap-datepicker.js",
//	               "ace_v1.3/assets/js/jqGrid/jquery.jqGrid.src.js",
//	               "ace_v1.3/assets/js/jqGrid/i18n/grid.locale-en.js", null]
//	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
	  //inline scripts related to this page

	  
	
	jQuery(function($) {
		
		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";

		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
	    })
		//resize on sidebar collapse/expand
		var parent_column = $(grid_selector).closest('[class*="col-"]');
		$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
			if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
				//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
				setTimeout(function() {
					$(grid_selector).jqGrid( 'setGridWidth', parent_column.width());
				}, 0);
			}
	    })
		
		jQuery(grid_selector).jqGrid({

			url: "auth/menuManage/query.do",
			datatype: "json",
			height: "100%",
			width:'100%',
			colNames:[ '菜单编号','所属系统','菜单名称','菜单地址', '上级菜单', '是否叶子结点','菜单类型','菜单状态'],
			colModel:[
				{name:'menuId',     index:'menuId',     width:'150px'},
				{name:'sysId',      index:'sysId',      width:'150px'},
				{name:'menuName',   index:'menuName',   width:'170px'},
				{name:'menuUrl',    index:'menuUrl',    width:'200px'},
				{name:'menuParId',  index:'menuParId',  width:'150px'},
				{name:'menuIsLeaf', index:'menuIsLeaf', width:'150px'},
				{name:'menuType', index:'menuTypeCh', width:'150px'},
				{name:'menuStatus', index:'menuStatus', width:'100px'}
			], 
	
			viewrecords : true,
			rowNum:10,
			rowList:[10,20],
			altRows: true,
			//shrinkToFit:false,
			multiselect: true,
	        multiboxonly: true,
			postData : getSearchParams(),
	        loadComplete : function() {
				var table = this;
				setTimeout(function(){
					 //加载分页
					 initPagingBar(grid_selector);
				}, 0);
			},
			beforeRequest:function(){//请求之前执行
				
			}
	
		});
		
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
		$(document).one('ajaxloadstart.page', function(e) {
			$(grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
		
	
	});
//   定义grid中的按钮
//	function operateEdit(cellvalue, options, rowObject){// "<div class='hidden-phone visible-desktop btn-group'>"
//		var button = "<div class=\"hidden-sm hidden-xs btn-group\">";  //定义button
//		button += "<button class='btn btn-xs btn-warning' onclick='menuedit(\""+rowObject["menuId"]+"\");'><i class=\"ace-icon fa fa-pencil-square-o  bigger-120\"></i></button>";	
//	return button;	
//	}
	//新增菜单
	function menuadd() {
		var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
		if (rows.length > 1) {
			msg.alert("警告", "不能同时选择多项数据！", "warn");
			return;
		}
		
		var mid ="";
		var menuIsLeaf ="1";//默认子菜单
		var rowData;
		var sysId = "0001";
		if (rows.length == 1) {
			rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
			console.log(rowData);
			mid = rowData.menuId;
			if(rowData.menuType == 1){
				msg.alert("警告", "请选择【菜单】类型，类型选择错误！", 'warn');
				return ;				
			}
		}else{
			mid = "00";//一级菜单的父级菜单指定为固定值
			menuIsLeaf = "0";
		}
		openDialog({
			dialogId : 'dlg-menuadd',
			title : '新增菜单',
			pageUrl : 'auth/menuManage/addMenuView.do',
			width : '620px',
			height: '468px',
			dataParam : {
				menuId : mid,
				sysId:sysId,
				menuIsLeaf:menuIsLeaf
			},
			dataName : 'obj' //表单数据存储对象名称
		});
	}
	
	//新增按钮
// 	function menubutton() {
// 		var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
// 		if (rows.length == 0) {
// 			msg.alert("警告", "当前没有选择的数据项,请选择数据！", "warn");
// 			return;
// 		}
// 		if (rows.length > 1) {
// 			msg.alert("警告", "不能同时选择多项数据！", "warn");
// 			return;
// 		}
		
// 		var mid ="";
// 		var rowData;
// 		var sysId = "";
// 		if (rows.length == 1) {
// 			rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
// 			mid = rowData.menuId;
// 			if(DICT.getKey("MTYPE",rowData.menuType) == 1){
// 				msg.alert("警告", "请选择【菜单】类型，类型选择错误！", 'warn');
// 				return ;				
// 			}
// 			if(DICT.getKey("ISLEAF",rowData.menuIsLeaf) == 0){
// 				msg.alert("警告", "请选择叶子节点，【按钮】只能在叶子节点下添加！", 'warn');
// 				return ;				
// 			}
// 			sysId = DICT.getKey("SYSTYPE",rowData.sysId);
// 		}else{
// 			mid = "0";
// 		}
		
// 		openDialog({
// 			dialogId : 'dlg-buttonadd',
// 			title : '新增按钮',
// 			pageUrl : 'auth/menuManage/addButtonView.do',
// 			width : '600px',
// 			height: '468px',
// 			dataParam : {
// 				menuId : mid,
// 				sysId:sysId
// 			},
// 			dataName : 'obj' //表单数据存储对象名称
// 		});
// 	}
	
	//删除菜单
	function destroyMenu() {
		var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
		if (rows.length == 0) {
			msg.alert("警告", "当前没有选择的数据项,请选择数据！", "warn");
			return;
		}
		
		var jsonData = new Array();
		for(var i=0; i<rows.length; i++){
			rowData = $('#grid-table').jqGrid('getRowData',rows[i]);
			//alert(rowData.menuStatus);
			//alert(DICT.getKey("MTYPE",rowData.menuStatus));
			if(rowData.menuStatus==0){
				msg.alert("警告", "启用中,无法删除！", "warn");
				return ;
			}
			var jsonMenu = {}; 
		    jsonMenu["menuId"]=rowData.menuId;
		    jsonMenu["menuParId"]=rowData.menuParId;
		    jsonMenu["menuType"]=rowData.menuType; 
            jsonData.push(jsonMenu);
		}
		
		if(jsonData.length>0){
			msg.confirm({title:'确认',position:'center',msg:'此操作会导致该节点下的所有子节点被删除。\n确认要删除选中的菜单吗?',call:function(ok){
				if(ok){
					$.ajax({
						type: "post",
						url: "auth/menuManage/delete.do",
						dataType:'json',    
					    contentType:"application/json",
					    data:JSON.stringify(jsonData),  
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
		}
	
	//freeze
	function statusUpdate1(){
		statusUpdate(1);
	}
	//free
	function statusUpdate0(){
		statusUpdate(0);
	}
	
	//禁用和启用
	function statusUpdate(status) {
		var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
		if (rows.length == 0) {
			msg.alert("警告", "当前没有选择的数据项,请选择数据！", "warn");
			return;
		}
		
		var ids="";
		var sendurl = "";
		var rowData ;
		for(var i=0; i<rows.length; i++){
			rowData = $('#grid-table').jqGrid('getRowData',rows[i]);
			ids = ids+rowData.menuId+",";
		}
		ids = ids.substring(0, ids.lastIndexOf(","));
        var showmsg = "";
        if(status == 0){
        	showmsg = "确认要启用选中的菜单吗?";
        	sendurl = "auth/menuManage/modifyMenuStatus0.do";
        }else{
        	showmsg = "确认要禁用选中的菜单吗?";
        	sendurl = "auth/menuManage/modifyMenuStatus1.do";
        }
        msg.confirm({title:'确认',position:'center',msg:showmsg,call:function(ok){
			if(ok){
				$.ajax({
					type: "post",
					url: sendurl+"?status="+status+"&menuId="+ids,
					dataType:'json',
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
	
	//编辑菜单
	function menuedit_bank() {
		var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
		
		if (rows.length == 0) {
			msg.alert("警告", "当前没有选择数据项！", "warn");
			return;
		}
		if (rows.length > 1) {
			msg.alert("警告", "不能同时选择多项数据！", "warn");
			return;
		}
		var rowData = $('#grid-table').jqGrid('getRowData',rows[0]);
		openDialog({
			dialogId : 'dlg-menuedit',
			title : '编辑菜单',
			pageUrl : 'auth/menuManage/editMenuView.do',
			dataUrl : 'auth/menuManage/queryMenuById.do',
			width :'600px',
			height:'400px',
			dataParam : {
				timestamp:getTimestamp(),
				menuId : rowData.menuId
			},
			dataName : 'obj' //表单数据存储对象名称
		});
	}
	//编辑菜单
// 	function menuedit(menuId) {
// 		openDialog({
// 			dialogId : 'dlg-menuedit',
// 			title : '编辑菜单',
// 			pageUrl : 'auth/menuManage/editMenuView.do',
// 			dataUrl : 'auth/menuManage/queryMenuById.do',
// 			width :'600px',
// 			height:'400px',
// 			dataParam : {
// 				timestamp:getTimestamp(),
// 				menuId : menuId
// 			},
// 			dataName : 'map' //表单数据存储对象名称
// 		});
// 	}

</script>
