<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<style>
.search_panel .search_elm{width: 250px;}
</style>
<link rel="stylesheet" href="static/ace_v1.3/assets/css/ui.jqgrid.css" />

<div class="search_panel">
<form action="" method="post" onsubmit="return pageSearch(this);">
	<div style="display: block;" class="widget-body ">
		<div class="widget-main">
			<div class="rowt">
				<div class="col-sm-12">
					<div>
							<label class="label_search">操作员账号:</label>
								<input id="userId" type="text" name="userId"
									placeholder="请输入操作员账号">
							<label class="label_search">操作员名称:</label>
								<input id="userName" type="text" name="userName" 
									placeholder="请输入操作员名称">
									<button class="btn btn-small btn_search" type="submit" title="搜索" >
								搜索
							</button>
					</div>
					<div>
						<span id="auth_btn">
				    <!-- 
				        auth="auth/userManage/add.do"
				    	属性说明: 用于按钮权限控制，auth地址与用户使用该功能的地址一致。
				    	
				     -->
					<a auth="auth/userManage/add.do" class="btn btn-small btn-add" onclick="useradd();">新增</a> 
					<a auth="auth/userManage/editView.do" class="btn btn-small btn-update" onclick="useredit();"> 修改 </a> 
					<a auth="auth/userManage/delete.do" class="btn btn-small btn-delete" onclick="userdelete();"> <!-- 
					<i class="icon-envelope"></i> --> 删除
				</a>
				<a class="btn btn-small btn-default"
					onclick="statusUpdate0();"> <!-- 
					<i class="icon-envelope"></i> --> 启用
				</a>
				<a class="btn btn-small btn-default"
					onclick="statusUpdate1();"> <!-- 
					<i class="icon-envelope"></i> --> 禁用
				</a>
				<a class="btn btn-small btn_search" "
					onclick="resetUserPwd();"> <!-- 
					<i class="icon-envelope"></i> --> 重置密码
				</a>
				<!-- <a class="btn btn-small btn_search" "
					onclick="saveUser();"> 
					<i class="icon-envelope"></i> 数据同步
				</a> -->
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

				url : "auth/userManage/query.do",
				datatype : "json",
				height : '100%',
				width : '100%',
				colNames : [ '操作员账号', '操作员名称','电话','邮件', '状态', '登录次数','最后登录时间','最后登录地址' ],
				colModel : [ 
				             {name : 'userId',       index : 'userId',       width : '10%' },
				             {name : 'userName',     index : 'userName',     width : '10%' }, 
				             {name : 'phone',        index : 'phone',        width : '10%' }, 
				             {name : 'email',        index : 'email',        width : '10%' }, 
				             {name : 'userStatus',   index : 'userStatus',   width : '6%' ,formatter:userStatusFormat}, 
				             {name : 'lnum',         index : 'lnum',         width : '7%' }, 
				             {name : 'lastLoginTime',index : 'lastLoginTime',width : '12%' ,formatter : selfFormatDate},
				             {name : 'lastLoginIp',  index : 'lastLoginIp',  width : '12%' }
				            ],
				viewrecords : true,
				rowNum : 10,
				rowList : [ 10, 20, 30 ],
				altRows : true,

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
			
			
			//格式化用户状态
			function userStatusFormat(cellvalue, options, rowObject){
				return "正常";
			}
			//自定义时间格式
			function selfFormatDate(cellvalue, options, rowObject){
				var tmpDate ;
				if(cellvalue == null || cellvalue.length != 14 ){
					return  "";
				}
				tmpDate = cellvalue.substring(0,4) + "-";
				tmpDate = tmpDate + cellvalue.substring(4,  6) + "-";
				tmpDate = tmpDate + cellvalue.substring(6,  8) + " ";
				tmpDate = tmpDate + cellvalue.substring(8, 10) + ":";
				tmpDate = tmpDate + cellvalue.substring(10,12) + ":";
				tmpDate = tmpDate + cellvalue.substring(12,14) ;
				return tmpDate;
			}
		});

		//新增用户
		function useradd() {
			openDialog({
				dialogId : 'dlg-useradd',
				title : '',
				pageUrl : 'auth/addView.do',
				width : '600px',
			    height:'500px'
			});
		}
		//编辑用户
		function useredit() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "error");
				return;
			}
			if (rows.length > 1) {
				msg.alert("警告", "不能同时选择多项数据！", "warn");
				return;
			}
			if(rows[0] == "1000"){
				msg.alert("警告", "系统超级用户不能被修改！", "warn");
				return ;
			}
			openDialog({
				dialogId : 'dlg-useredit',
				title : '编辑用户',
				pageUrl : 'auth/userManage/editView.do',
				dataUrl : 'auth/userManage/queryUserById.do',
				width :'600px',
				height:'400px',
				dataParam : {
					id : rows[0]
				},
				dataName : 'obj' //表单数据存储对象名称

			});
		}
		//用户删除
		function userdelete() {
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "warn");
				return;
			}

			//
			var ids = "";
			for (var i = 0; i < rows.length; i++) {
// 				if (rows[i] == UID.id) {
// 					msg.alert("警告", "无法删除自己 ！", "warn");
// 					return;
// 				}
				ids = ids + rows[i] + ",";
			}
			ids = ids.substring(0, ids.lastIndexOf(","));
			msg.confirm({title:'确认',position:'center',msg:'您确定要删除选中的数据吗？',call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
						url : "auth/userManage/delete.do?userId=" + ids,
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
		
			
	    function statusUpdate0(){
	    	setUserStatus(0);
	    }
	    function statusUpdate1(){
	    	setUserStatus(1);
	    }
		function setUserStatus(status){
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择的数据项,请选择数据！", "warn");
				return;
			}
			
			var ids="",uname="";
			for(var i=0; i<rows.length; i++){
				if(rows[i] == "1000"){
					msg.alert("警告", "超级管理员信息不能被修改！", "warn");
					return ;
				}
// 				if(rows[i] == UID.id){
// 					msg.alert("警告", "不能修改当前正在使用的用户！", "warn");
// 					return ;
// 				}
				ids   = ids+rows[i]+",";
			}
			ids   = ids.substring(0, ids.lastIndexOf(","));
			var tmsg = "启用";
			if(status == 1){
				tmsg = "禁用";
			}
			
			msg.confirm({title:'确认',position:'center',msg:'您确定要'+tmsg+'选中的数据吗？',call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
					    url : "auth/userManage/modifyUsersStatus.do?status="+status+"&userId="+ids,
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
		
		function resetUserPwd(){
			var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");
			if (rows.length == 0) {
				msg.alert("警告", "当前没有选择数据项！", "warn");
				return;
			}

			//
			var ids = "";
			for (var i = 0; i < rows.length; i++) {
// 				if (rows[i] == UID.id) {
// 					msg.alert("警告", "无法重置自己的密码 ！", "warn");
// 					return;
// 				}
				ids = ids + rows[i] + ",";
			}
			ids = ids.substring(0, ids.lastIndexOf(","));
			msg.confirm({title:'确认',position:'center',msg:'您确定要重置密码吗？',call:function(ok){
				if(ok){
					$.ajax({
						type : "post",
						url : "auth/userManage/resetUserPwd.do?userId=" + ids,
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
		
	    //更新数据
	    function saveUser() {
	        var rows = $('#grid-table').jqGrid("getGridParam", "selarrrow");

	        msg.confirm({title:'确认',position:'center',msg:'您确定要更新数据吗？',call:function(ok){
	            if(ok){
	                $.ajax({
	                    type : "post",
	                    url : "auth/adduser",
	                    dataType : 'json',
	                    success : function(result) {
	                        if (result.returnCode != 1) {
	                        	msg.alert("提示", '操作成功', 'correct');
	                            $("#grid-table").trigger("reloadGrid");
	                        } else {
	                        	msg.alert("错误", result.errorinfo, 'error');
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