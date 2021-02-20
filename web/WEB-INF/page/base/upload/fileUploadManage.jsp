<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" role="form" id="file_form" enctype="multipart/form-data">
	<!-- #section:elements.form -->
	<div style="margin-right: 10px;margin-left: 10px;margin-top: 8px">
			<div class="form-group" style="height: 25px;">
				<div class="col-xs-12">
					<input type="file" id="id-input-file-3" onchange="TDFileSelected()" />

					<!-- /section:custom/file-input -->
				</div>
			</div>
				
			<!-- <div id="progressNumber"></div> -->

			<div  id="progress-bar-0"  style="width:520px;" class="progress pos-rel" data-percent="0%" style="margin-bottom: 0px !important">
				<div id="progress-bar-1" class="progress-bar" style="width:0%;"></div>
			</div>
			<div id="fileInf" style="text-align: right;"></div>

			<!-- /section:custom/file-input.filter -->
	</div>
	
	<div class="form-actions align-right  form-button-box" style="margin-top: 10px">
			<button class="btn btn-info" type="button" onclick="TDUploadFile()">保存</button>
			&nbsp; 
			<button class="btn" type="button" onclick="javascript:dialog.close(this);">关闭</button>
	</div>
	
</form>
<script type="text/javascript">

$(document).ready(function() {
});

/**
 * 文件上传处理完成后该方法被调用
 */
function TDUploadCallback(result){
	var result =	eval("(" + result + ")");
	if(result.success== true){
		msg.alert("成功", '上传成功！', 'correct');
		
	}else{
		msg.alert("错误", "上传失败");
	}
	var d = dialog.getFileUploadDialog();
	
	if(d && typeof d.fileUploadDone === 'function'){
		var r = d.fileUploadDone(result);
		if(r === true || r === 'true'){
			d.close();
		}
	}
	d.close();
}




/**
 * 文件上传控件渲染
 */
$('#id-file-format').removeAttr('checked').on('change', function() {
	var whitelist_ext, whitelist_mime;
	var btn_choose
	var no_icon
	if(this.checked) {
		btn_choose = "将文件拖入或单击选择";
		no_icon = "ace-icon fa fa-picture-o";

		whitelist_ext = ["jpeg", "jpg", "png", "gif" , "bmp"];
		whitelist_mime = ["image/jpg", "image/jpeg", "image/png", "image/gif", "image/bmp"];
	}
	else {
		btn_choose = "将文件拖入或单击选择";
		no_icon = "ace-icon fa fa-cloud-upload";
		
		whitelist_ext = null;//all extensions are acceptable
		whitelist_mime = null;//all mimes are acceptable
	}
	var file_input = $('#id-input-file-3');
	file_input
	.ace_file_input('update_settings',
	{
		'btn_choose': btn_choose,
		'no_icon': no_icon,
		'allowExt': whitelist_ext,
		'allowMime': whitelist_mime
	})
	file_input.ace_file_input('reset_input');
	
	file_input
	.off('file.error.ace')
	.on('file.error.ace', function(e, info) {
		//console.log(info.file_count);//number of selected files
		//console.log(info.invalid_count);//number of invalid files
		//console.log(info.error_list);//a list of errors in the following format
		
		//info.error_count['ext']
		//info.error_count['mime']
		//info.error_count['size']
		
		//info.error_list['ext']  = [list of file names with invalid extension]
		//info.error_list['mime'] = [list of file names with invalid mimetype]
		//info.error_list['size'] = [list of file names with invalid size]
		
		
		/**
		if( !info.dropped ) {
			//perhapse reset file field if files have been selected, and there are invalid files among them
			//when files are dropped, only valid files will be added to our file array
			e.preventDefault();//it will rest input
		}
		*/
		
		
		//if files have been selected (not dropped), you can choose to reset input
		//because browser keeps all selected files anyway and this cannot be changed
		//we can only reset file field to become empty again
		//on any case you still should check files with your server side script
		//because any arbitrary file can be uploaded by user and it's not safe to rely on browser-side measures
	});

});


</script>
