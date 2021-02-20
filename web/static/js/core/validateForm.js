/**
 * 验证表单
 * 属性说明:
 * validate="true"  需要验证
 * msg="错误时提示信息"        
 * datatype="数据类型"
 * 
 * 
 */
function TDValidateForm($form){
	 var bool = true;
	 var dmsg = "该字段不能为空!";
	 var tmsg = null;
	 var $id = null;
	 var $datatype = null;
	 var area = "input[type=text],input[type=email],input[type=password]";
	 $(area,$form).each(function(){
		 bool = true;
		 tmsg = null;
		 $id = null;
		 $datatype = null;
		 $id = $(this);
		 if($id.attr('validate') == 'true'){
			 if($id.attr('msg') != undefined && $id.attr('msg') != ""){
				 tmsg = $id.attr('msg');
			 }
			 if($id.val() === undefined || $.trim($id.val()) === "" || $id.val() == $id.attr('placeholder')){

				 TDTips($id,tmsg==null?dmsg:tmsg);
				 bool = false;
				 return bool;
			 }else{//正则表达式效验
				 $datatype = $.trim($id.attr('datatype'));
				 if($datatype != undefined && $datatype != ""){
					 if($datatype == "email"){			
						 if(!regexObj.email($id.val())){
							 TDTips($id,tmsg==null?regexMsg.email:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "url"){
						 //return true;
					 }else if($datatype == "number"){
						 if(!regexObj.number($id.val())){
							 TDTips($id,tmsg==null?regexMsg.number:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "date"){
						 //return true;
					 }else if($datatype == "datetime"){
						 //return true;
					 }else if($datatype == "money"){
						 if(!regexObj.money($id.val())){
							 TDTips($id,tmsg==null?regexMsg.money:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "mobile"){
						 if(!regexObj.mobile($id.val())){
							 TDTips($id,tmsg==null?regexMsg.mobile:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "chinese"){
						 if(!regexObj.chinese($id.val())){
							 TDTips($id,tmsg==null?regexMsg.chinese:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "idcard"){
						 if(!regexObj.idcard($id.val())){
							 TDTips($id,tmsg==null?regexMsg.idcard:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "notempty"){
						 if(!regexObj.notempty($id.val())){
							 TDTips($id,tmsg==null?regexMsg.notempty:tmsg);
							 bool = false;
							 return bool;
						 }
					 }else if($datatype == "word"){
						 if(!regexObj.word($id.val())){
							 TDTips($id,tmsg==null?regexMsg.word:tmsg);
							 bool = false;
							 return bool;
						 }
					 }
					 else{
						 //return true;
					 }
				 }
			 }
		 }
	});
	if(bool){
		 return true;
	}else{
		return false;
	}
	
}
/**
 * 提示
 * @param $id  校验对象
 * @param tmsg 提示信息
 */
function TDTips($id,tmsg){
	 $id.tips({
			side:3,
	        msg:tmsg,
	        bg:'#E3C94C',
	        time:2
		 });
		 $id.focus();
}
