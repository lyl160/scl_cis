
/**
 * 文件被选中是获取文件信息<br/>
 * Input固定ID：id-input-file-3
 */
function TDFileSelected() {
	$("#progress-bar-0").attr("data-percent", '0%');
    $("#progress-bar-1").css("width", '0%');
	  var file = document.getElementById('id-input-file-3').files[0];
	  if (file) {
	    var fileSize = 0;
	    if (file.size > 1024 * 1024)
	      fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
	    else
	      fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';    
	      document.getElementById('fileInf').innerHTML = "文件大小 : "+ fileSize;
	  }
}

/**
 * 文件上传<br/>
 * 固定上传地址:base/fileUploadManage/upload.do(备用地址请求action，暂时未用)
 * 固定上传地址:uploadfile.do （已使用，请求servlet）
 */
function TDUploadFile() {
  var xhr = new XMLHttpRequest();
  var fd = new FormData();
  fd.append("id-input-file-3", document.getElementById('id-input-file-3').files[0]);
  
  /* 事件监听 */
  xhr.upload.addEventListener("progress", td_uploadProgress, false);
  xhr.addEventListener("load", td_uploadComplete, false);
  xhr.addEventListener("error", td_uploadFailed, false);
  xhr.addEventListener("abort", td_uploadCanceled, false);
  xhr.open("POST", "uploadfile.do");
  xhr.send(fd);
}

/**
 * 进度条事件监听
 * @param evt
 */
function td_uploadProgress(evt) {
  if (evt.lengthComputable) {
    var percentComplete = Math.round(evt.loaded * 100 / evt.total);
   //document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
    $("#progress-bar-0").attr("data-percent",percentComplete.toString() + '%');
    $("#progress-bar-1").css("width",percentComplete.toString() + '%');
  }
  else {
    document.getElementById('progressNumber').innerHTML = '未知进度.';
  }
}

/**
 * 文件上传完成事件监听
 * @param evt
 */
function td_uploadComplete(evt) {
  TDUploadCallback(evt.target.responseText);
}

/**
 * 文件上传失败事件监听
 * @param evt
 */
function td_uploadFailed(evt) {
  msg.alert("文件上传失败.");
}

/**
 * 文件上传取消事件监听
 * @param evt
 */
function td_uploadCanceled(evt) {
  msg.alert("用户已取消上传的文件.");
}


