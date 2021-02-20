var regexObj = {
		number:function(value){
			reg=/^([0-9]+)$/;
			return reg.test(value);
		},
		email:function(value){//邮件
			reg=/^\w{3,}@\w+(\.\w+)+$/;
			return reg.test(value);
		},
		mobile:function(value){//手机号
			reg=/^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/;
			return reg.test(value);
		},
		zipcode:function(value){ //邮编
			reg=/^[0-9]{6}$/;
			return reg.test(value);
		},
		chinese:function(value){//仅中文
			reg=/^[\u4e00-\u9fa5]+$/;
			return reg.test(value);
		},
		idcard:function(value){ //身份证
			reg=/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
			return reg.test(value);
		},
		url:function(value){ //地址url
			return ture;
		},
		notempty:function(value){ //长度限制
			reg=/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,10}$/;
			return reg.test(value);
			
		},
		ip4:function(value){
			reg=/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
			return reg.test(value);
		},
		money:function(value){
			reg=/^[\d]*(.[\d]{1,2})?$/;
			return reg.test(value);
		},
		word:function(value){
			reg=/^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
			return reg.test(value);
		},
};
var regexMsg = {
		number:"只能输入数字,请重新输入!",
		email :"邮件格式不正确,请重新输入!",
		mobile:"手机号码格式不正确,请重新输入!",
		zipcode:"编码格式不正确,请重新输入!",
		chinese:"只能输入中文字符,请重新输入!",
		idcard :"身份证号码格式不正确,请重新输入!",
		money:"输入金额不正确,请重新输入!",
		notempty:"输入长度不能超过10个字符,请重新输入!",
		word:"不能输入非法字符,请重新输入!",	
};

/**
 * 获取时间戳
 * @returns
 */
function getTimestamp(){
	return new Date().getTime();
}

/**
 * 日期格式化（年月日时分秒）
 */
function dateFormat(sdate){
	var tmpDate ;
	if(sdate == null){
		return  "";
	}
	tmpDate = new Date(sdate).Format("yyyy-MM-dd hh:mm:ss");
	return tmpDate;
}

/**
 * 日期格式化（时分秒）
 */
function tmFormat(sdate){
	var tmpDate ;
	if(sdate == null || sdate.length != 6 ){
		return  "";
	}
	tmpDate = sdate.substring(0,2) + ":";
	tmpDate = tmpDate + sdate.substring(2,4) + ":";
	tmpDate = tmpDate + sdate.substring(4);
	return tmpDate;
}
/**
 * 日期格式化（年月日）
 */
function dtFormat(sdate){
	var tmpDate ;
	if(sdate == null || sdate.length != 8 ){
		return  "";
	}
	tmpDate = sdate.substring(0,4) + "-";
	tmpDate = tmpDate + sdate.substring(4,  6) + "-";
	tmpDate = tmpDate + sdate.substring(6,  8) + " ";
	return tmpDate;
}
/**
 * 分格式化成元
 * @param cent
 * @returns
 */
function cent2Yuan(cent) { 
	if(cent==null||cent==""||isNaN(cent)){
		return "";
	}
 	return TDFormatMoney(cent/100,2) ;
} 
/**
 * 日期格式化（月日）
 */
function mdFormat(sdate){
	var tmpDate ;
	if(sdate == null || sdate.length != 4 ){
		return  "";
	}
	tmpDate = sdate.substring(0,2) + "-";
	tmpDate = tmpDate + sdate.substring(2,  4) + "";
	return tmpDate;
}
/**
 * 大小格式化(B格式成KB)
 * @param size
 * @returns {String}
 */
function sizeForamt(size){
	if(size == null || size.length == 0){
		return "0KB";
	}
	return Math.round((size/1024)*100)/100 + "KB";
}
/**
 * 毫秒格式化成秒
 * @param ms
 * @returns {String}
 */
function msecFormat(msec){
	if(msec == null || msec.length == 0){
		return "0s";
	}
	return Math.round((msec/60)*100)/100 + "s";
}

/**
 * 格式化金额:  12345.67格式化为 12,345.67 
 * @param s
 * @param n
 * @returns {String}
 */
function TDFormatMoney(s, n)   
{   
   n = n > 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse(),   
   r = s.split(".")[1];   
   var t = "";   
   for(var i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + "." + r;   
} 


//对Date的扩展，将 Date 转化为指定格式的String   
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
//例子：   
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
Date.prototype.Format = function(fmt)   
{ //author: meizz   
var o = {   
 "M+" : this.getMonth()+1,                 //月份   
 "d+" : this.getDate(),                    //日   
 "h+" : this.getHours(),                   //小时   
 "m+" : this.getMinutes(),                 //分   
 "s+" : this.getSeconds(),                 //秒   
 "q+" : Math.floor((this.getMonth()+3)/3), //季度   
 "S"  : this.getMilliseconds()             //毫秒   
};   
if(/(y+)/.test(fmt))   
 fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
for(var k in o)   
 if(new RegExp("("+ k +")").test(fmt))   
fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
return fmt;   
} 
