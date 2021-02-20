<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
	<form class="form-horizontal" >
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
	</form>	

<script type="text/javascript">

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
		var p = dialog.getById('dlg-dictadd');
		jQuery(dialog_grid_selector).jqGrid({
			url: "dict/dictAdd2/init.do?dictId="+p.dataParam.dictId,
			datatype: "json",
			height: "100%",
			width:'100%',
		    colNames:[ '字典名称','字典代码','名', '值'],
			colModel:[
				{name:'dictName',        index:'dictName',     width:'20%'},
				{name:'dictCode',        index:'dictCode',   width:'20%' },
				{name:'dict_name_tmp',    index:'dict_name_tmp',   width:'30%', formatter : dictValueFormat},
				{name:'dict_value_tmp',   index:'dict_value_tmp',    width:'30%', formatter : dictNameFormat}
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
		//<input type='text' ></input>
		function dictValueFormat(cellvalue, options, rowObject){
 			return "<input type='text' id='dictName_"+options.rowId+"' placeholder='请输入名' style='width:200px'></input>";
		}
		function dictNameFormat(cellvalue, options, rowObject){
 			return "<input type='text' id='dictValue_"+options.rowId+"' placeholder='请输入值' style='width:350px'></input>";
		}
		
		$($dialogBox).triggerHandler('resize.dialog.jqGrid');//trigger window resize to make the grid get the correct size
		$($dialogBox).one('ajaxloadstart.page', function(e) {
			$(grid_selector).jqGrid('GridUnload');
			$('.ui-jqdialog').remove();
		});
		
	});
	
	function addDictValue(obj){
		
		var count = $("#dialog-grid-table").getGridParam("reccount");
		var p = dialog.getById('dlg-dictadd');
		
		var jsonData = new Array();
		for (var i = 1; i <= count; i++) {
			if(($("#dictValue_"+i).val()) !=null && ($("#dictValue_"+i).val()) != ""){
			    var jsonMenu = {}; 
			    jsonMenu["dictValue"]=$("#dictValue_"+i).val();
			    jsonMenu["dictName"] =$("#dictName_"+i).val();
			    jsonMenu["dictCode"]=p.dataParam.dictCode;
			    jsonMenu["parentId"]=p.dataParam.dictId;
	            jsonData.push(jsonMenu);
			}

		}
		
		if(jsonData.length == 0){
			msg.alert("警告", "没有要提交的数据！", 'warn');
			return;
		}
		 $.ajax({
		       url:'dict/dictAdd2/add.do', 
		       type:'post',         
		       dataType:'json',    
		       contentType:"application/json",
		       data:JSON.stringify(jsonData),        
		       success : function(result) {
					if (result.rspcod != "200") {
						msg.alert("错误", result.rspmsg + " 错误代码："
								+ result.rspcod, 'error');
					} else {
						msg.alert("提示", result.rspmsg, 'correct');
						$("#grid-table").trigger("reloadGrid");
						dialog.close(obj);
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
