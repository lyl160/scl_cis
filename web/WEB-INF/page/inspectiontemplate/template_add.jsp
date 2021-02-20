<%--校务巡查 动态属性--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp"%>

<input type="hidden" id="templateId" name="templateId"
value="${templateId }">
<input type="hidden" id="tname" name="tname"
value="${tname}">
<input type="hidden" id="schoolId" name="schoolId"
value="${schoolId }">
<form class="form-horizontal" >
<div class="row">
	<div class="col-xs-12">
		<table id="dialog-grid-table"></table>
		<div id="grid-pager-dialog"></div>
		<div style="margin-top: 10px">
			<span> <a class="btn btn-small btn-add" onclick="addRow();">新增一行</a>
				<a class="btn btn-small btn-add" id="showorhide" onclick="saveRow();">保存</a> </span> <span
				id="paging_bar" style="float: right"> </span>
		</div>
	</div>
</div>
 
<div class="form-actions align-right  form-button-box" style="margin-top: 0px">
		&nbsp; 
		 <input type="hidden" value="0" id="one">
		<button class="btn" type="button" onclick="javascript:dialog.close(this);">
			 关闭
		</button>
	</div>
</form>
<script type="text/javascript">

	var lastrow;
	var lastcell;
	
	var classify = '0:请选择;';
	//查询二级分类
	$.ajax({
	url:"inspectionCategory/classify",
	type:"get",
	data:{templateId:$('#templateId').val()},
	 async: false,
	success:function(data){
	  data = data.list;
	  
	  for(var i = 0;i<data.length;i++)
	  {
		  if(i < data.length-1 ){
		  classify+=data[i].id+":"+data[i].value+";";
		  }
		  else{
			  classify+=data[i].id+":"+data[i].value;
		  }
	  }
	 
	  
	}
	});
	jQuery(function($) {

		var dialog_grid_selector = "#dialog-grid-table";
		var pager_selector = "#grid-pager-dialog";
		$('#showorhide').hide();
		var $dialogBox = dialog.getByChildren(dialog_grid_selector);
		
		$($dialogBox).on('resize.dialog.jqGrid', function () {
			$(dialog_grid_selector).jqGrid( 'setGridWidth',$dialogBox.width() );
	    });
		var parent_column = $(dialog_grid_selector).closest('[class*="col-"]');
		$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
			if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
				setTimeout(function() {
					$(dialog_grid_selector).jqGrid( 'setGridWidth', parent_column.width());
				}, 0);
			}
	    })
		jQuery(dialog_grid_selector).jqGrid({
			url : "dynamicAttr/query?templateId=" + $('#templateId').val(),
			datatype : "json",
			height : '100%',
			width : '100%',
			cellEdit : true,
			cellsubmit : "remote",
			cellurl : "dynamicAttr/save",
			jsonReader : {
				root : "rows",
				page : "page",
				total : "totalPage",
				records : "total"
			},
			colNames : [ '属性名称', '属性类别', '属性值','评分标准','项目代码', '分类', '操作' ],
			colModel : [ {
				name : 'name',
				index : 'NAME',
				width : '5%',
				sortable:false,
				editable : true
			}, {
				name : 'type',
				index : 'TYPE',
				width : '5%',
				sortable:false,
				edittype : "select",
				formatter : 'select',
				editoptions : {
				
					value : "004:单选"
				},
				editable : true
			}, {
				name : 'attrOption',
				index : 'ATTR_OPTION',
				width : '15%',
				sortable:false,
				editable : true
			},{
				name : 'score',
				index : 'score',
				width : '15%',
				sortable:false,
				editable : true
			}, {
				name : 'itemCode',
				index : 'item_code',
				width : '5%',
				sortable:false,
				edittype : "select",
				formatter : 'select',
				editoptions : {
				
					value : "0:请选择;1:老师;2:学生;3:管理"
				},
				editable : true
			}, {

				name : 'seq',
				index : 'seq',
				width : '5%',
				sortable:false,
				edittype : "select",
				formatter : 'select',
				editoptions : {

					value :classify
				},
				editable : true
			}, {
				name : '',
				index : '',
				width : '5%',
				sortable:false,
				formatter : deleteFormat
			} ],
			viewrecords : true,
			rowNum : 10,
			rowList : [ 10, 20, 30 ],
			altRows : true,
			//shrinkToFit:false,
			rownumbers: true, //是否显示序号列 true显示，false不显示
			multiselect : false,//设置行可多选的 
			multiboxonly : true,//

			loadComplete : function() {
				var table = this;
				setTimeout(function() {
					//加载分页
					initPagingBar(dialog_grid_selector,"#grid-pager-dialog");
				}, 0);
			},
			beforeRequest : function() {//请求之前执行
				jqGrid = this;
			},
			beforeEditCell : function(rowid, cellname, v, iRow, iCol) {
				lastrow = iRow;
				lastcell = iCol;
			}
		});
		$(window).triggerHandler('resize.jqGrid');
		
		$(document).one('ajaxloadstart.page', function(e) {
			$(dialog_grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
		
	}
	
	);
	
	//删除行
	function deleteFormat(cellvalue, options, rowObject) {
		var html = '<span><a class="btn btn-small btn-info" onclick="dynamicAttrDelete('
				+ rowObject.id + ')">删除</a></span>';
		return html;
	}

	function typeFormat(cellvalue, options, rowObject) {
		var status = "";
		if (cellvalue == "001") {
			status = "文本";
		} else if (cellvalue == "002") {
			status = "数值";
		} else if (cellvalue == "003") {
			status = "菜单";
		} else if (cellvalue == "004") {
			status = "单选";
		}
		return status;
	}

	/* //编辑
	function dynamicAttrEdit() {
		var rows = $('#dialog-grid-table').jqGrid("getGridParam", "selarrrow");
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
			title : '编辑属性模板子表',
			pageUrl : 'dynamicAttr/edit/view?id=' + rows,
			width : '500px',
			height : '350px',
			dataName : 'obj' //表单数据存储对象名称
		});
	} */
	//删除
	function dynamicAttrDelete(ids) {
		msg.confirm({
			title : '确认',
			position : 'center',
			msg : '您确定要删除选中的数据吗？',
			call : function(ok) {
				if (ok) {
					$.ajax({
						type : "post",
						url : "dynamicAttr/delete?id=" + ids,
						dataType : 'json',
						success : function(result) {
							if (result.returnCode != 1) {
								msg.alert("错误", result.msg, 'error');
							} else {
								msg.alert("提示", '操作成功', 'correct');
								$("#dialog-grid-table").trigger("reloadGrid");
							}
						},
						error : function(XMLHttpRequest, textStatus) {
							msg.alert("错误", "错误代码：" + XMLHttpRequest.status
									+ ",错误描述：" + textStatus, 'error');
						}
					});

				}
			}
		});
	}
var idn ;
 var idn1='';
var count1 = 0;
	function addRow() {
		
		if($("#one").val() == 0)
			{
			count1 = $("#dialog-grid-table").jqGrid("getRowData").length;
			}
		
		$("#one").val($("#one").val()+1);
		
	$('#showorhide').show();
	$.ajax({
	url:"dynamicAttr/getAttrId?id="+'${templateId}',
	type:"get",
	async:false,
	success:function(data){
	idn = data;
	
	idn1+=idn+",";
	}
	});
		var parameters = {
			rowID : idn,
			initdata : {
				id : idn
			},
			position : "last",
		};
		$("#dialog-grid-table").addRow(parameters);
	}
	
	function saveRow() {
	$('#showorhide').hide();
	 var a  = idn1.split(",");
	 for(var i = 0;i<a.length;i++){
		$("#dialog-grid-table").saveRow(a[i], false, 'clientArray');
		//var rowData = $("#dialog-grid-table").jqGrid("getRowData",idn);
	 }
		var count = $("#dialog-grid-table").jqGrid("getRowData");
	
		 var jsonData = new Array();
		 
		 for(var i = 0;i<count.length;i++)
			 {
			  var jsonMenu = {};
			  if(i >= count1)
				  {
				  jsonMenu["id"]=idn1.split(",")[i-count1];
				  jsonMenu["attrOption"]=count[i].attrOption;
				  jsonMenu["name"]=count[i].name;
				  jsonMenu["type"]=count[i].type;
				  jsonMenu["sequence"]=count[i].sequence;
				  jsonMenu["templateId"]=$('#templateId').val();
				  jsonMenu["schoolId"]=$('#schoolId').val();
				  jsonMenu["score"]=count[i].score;
				  jsonMenu["itemCode"]=count[i].itemCode;
				  jsonMenu["seq"]=count[i].seq;
				  jsonData.push(jsonMenu);
				
				 }
			  else
			 {
				  jsonMenu["attrOption"]=count[i].attrOption;
				  jsonMenu["name"]=count[i].name;
				  jsonMenu["type"]=count[i].type;
				  jsonMenu["sequence"]=count[i].sequence;
				  jsonMenu["templateId"]=$('#templateId').val();
				  jsonMenu["schoolId"]=$('#schoolId').val();
				  jsonMenu["score"]=count[i].score;
				  jsonMenu["itemCode"]=count[i].itemCode;
				  jsonMenu["seq"]=count[i].seq;
				  jsonData.push(jsonMenu);
			 }
		 }
		$.ajax({
		url:"dynamicAttr/saveAttr",
		type:"post",
		 dataType:'json',    
	     contentType:"application/json",
		data:JSON.stringify(jsonData),
		success:function(data){
		msg.alert("提示", '保存成功', 'correct');
		$("#dialog-grid-table").trigger("reloadGrid");
		}
		});
	}
	
</script>
