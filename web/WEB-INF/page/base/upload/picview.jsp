<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/head.jsp" %>
<form class="form-horizontal" id="useredit_form">
	<!-- #section:elements.form -->

	<div id="picview">
		<img id="img_picview" alt="" src="" width="540px" height="460px">
	</div>
	
	<div id="videeoview" style="display: none;">
		<video id="my-video" class="video-js" controls preload="auto" style="width:540px,height:460px" poster="" data-setup="{}">  
          	<source src="" type="video/mp4">  
     	</video>  
	</div>
	
	
	
	<div class="form-actions align-right  form-button-box" style="margin-top: 10px">
			
			<button class="btn" type="button" onclick="javascript:dialog.close(this);">
				 关闭
			</button>
	</div>
	
</form>
<script type="text/javascript">
<!--

//-->

$(document).ready(function() {
	var p = dialog.getById('dlg-picview');
	//document.getElementById("img_picview").src ="base/pic/view2.do?picid="+ p.dataParam.picid;
	document.getElementById("img_picview").src = p.dataParam.picid;
	var picUrl = p.dataParam.picid;
	if(isVieo(picUrl)){
		$('#picview').hide();
		$('#videeoview').attr('poster',picUrl + '?vframe/jpg/offset/5/w/540');
		$('#videeoview').find('source').attr('src',picUrl);
		$('#videeoview').show();
	}else{
		$('#videeoview').hide();
		$('#picview').find('img').attr('src',"base/pic/view2.do?picid="+picUrl);
		$('#picview').show();
	}
});

</script>
