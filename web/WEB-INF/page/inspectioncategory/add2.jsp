<!--新增2级类别（校内执勤 校外执勤）带开始结束时间-->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
	<div class="form-horizontal" >
	<table id="dialog-grid-table"></table>
	
	<div class="form-actions align-right  form-button-box" style="margin-top: 0px">
		<button class="btn btn-info" type="submit"  onclick="addDictValue(this);">
			<!-- <i class="ace-icon fa fa-check bigger-110"></i>  auth="auth/menuManage/add.do" -->保存
		</button>

		&nbsp; 
		<button class="btn" type="button" onclick="javascript:dialog.close(this);">
			 关闭
		</button>
	</div>
	</div>

<script type="text/javascript">
    var mid;
	jQuery(function($) {	
		var dialog_grid_selector = "#dialog-grid-table";
		var $dialogBox = dialog.getByChildren(dialog_grid_selector);
		
		$($dialogBox).on('resize.dialog.jqGrid', function () {
			$(dialog_grid_selector).jqGrid( 'setGridWidth',$dialogBox.width() );
	    });
		var parent_column = $(dialog_grid_selector).closest('[class*="col-"]');
		$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
			if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
				setTimeout(function() {
					$(grid_selector).jqGrid( 'setGridWidth', parent_column.width());
				}, 0);
			}
	    })
		var p = dialog.getById('dlg-add2');
		mid = p.dataParam.id;
		jQuery(dialog_grid_selector).jqGrid({
			url: "inspectionCategory/addlv2/init.do?id="+mid,
			datatype: "json",
			height: "100%",
			width:'100%',
		    colNames:[ '一级分类名称','二级分类名称','开始时间','结束时间'],
			colModel:[
				{name:'value',        index:'value',     width:'15%'},
				{name:'value_tmp',    index:'value_tmp',   width:'35%', formatter : marketNameFormat},
				{name:'startTime',    index:'start_time',  width:'25%', formatter : startFormat},
				{name:'endTime',      index:'end_time',  width:'25%', formatter : endFormat}
			], 
			viewrecords : true,
			rowNum:10,
			rowList:[10,20],
			altRows: true,
			multiselect: false,
	        multiboxonly: true,
	        loadComplete : function() {
				var table = this;
				setTimeout(function(){
					 //加载分页
					 //initPagingBar(grid_selector);
				}, 0);
			},
			beforeRequest:function(){//请求之前执行
	
			},
			gridComplete: function () {//隐藏表头
                $(this).closest('.ui-jqgrid-view').find('div.ui-jqgrid-hdiv').hide();
                $dialogBox.trigger("resize");
            },
	
		});
		function marketNameFormat(cellvalue, options, rowObject){
 			return "<input type='text' id='mName_"+options.rowId+"' placeholder='请输入二级分类名称' style='width:250px'></input>";
		}
		
		function startFormat(cellvalue, options, rowObject){
 			return '<div class="col-sm-9"><input type="text" onclick="showudate(this);" id="startTime'+options.rowId+'" placeholder="请选择开始时间" autocomplete="off" readonly ="true" /></div>';
		}
		function endFormat(cellvalue, options, rowObject){
 			return '<div class="col-sm-9"><input type="text" onclick="showudate(this);" id="endTime'+options.rowId+'" placeholder="请选择结束时间" autocomplete="off" readonly ="true" /></div>';
		}
		$($dialogBox).triggerHandler('resize.dialog.jqGrid');//trigger window resize to make the grid get the correct size
		$($dialogBox).one('ajaxloadstart.page', function(e) {
			$(grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
	});
	function showudate(tmpob) { 
			$('#'+tmpob.id).datetimepicker({ 
				format : 'hh:ii',
				autoclose : true,
				startView : "hour",
				changeMonth: false,
			    changeYear: false,
				showOtherMonths : false,
				language : 'zh-CN' }); 
		}
	function addDictValue(obj){
		var count = $("#dialog-grid-table").getGridParam("reccount");
		var jsonData = new Array();
		for (var i = 1; i <= count; i++) {
			if(($("#mName_"+i).val()) !=null && ($("#mName_"+i).val()) != ""){
			    var jsonMenu = {}; 
			    jsonMenu["value"] =$("#mName_"+i).val();
			    jsonMenu["pid1"]=mid;
			    jsonMenu["startTime"] =$("#startTime"+i).val();
			    jsonMenu["endTime"] =$("#endTime"+i).val();
	            jsonData.push(jsonMenu);
			}
		}
		
		if(jsonData.length == 0){
			msg.alert("警告", "没有要提交的数据！", 'warn');
			return;
		}
	
		 $.ajax({
		       url:'inspectionCategory/addlv2/save', 
		       type:'post',         
		       dataType:'json',    
		       contentType:"application/json",
		       data:JSON.stringify(jsonData),        
		       success : function(result) {
					if(result.returnCode == 1){
						msg.alert("提示", "操作成功", 'correct');
						$("#grid-table").trigger("reloadGrid");
						dialog.close(obj);
					}else{
						msg.alert("错误", result.msg, 'error');
					}
				},
			  error:function(XMLHttpRequest, textStatus){
				  msg.alert("错误", textStatus + " 错误代码："
							+ XMLHttpRequest.status, 'error');
				}
		     });
	}
</script>

<div class="w-load"><div class="spin"></div></div>
