<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>校园巡检运营管理平台登录</title>
    <%
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	%>
    <script type="text/javascript">
        var server_base_url = '<%=basePath%>';
        document.write("<base href='" + server_base_url + "'/>");              
    </script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="static/css/camera.css"/>
    
    <link rel="stylesheet" href="static/css/style.css"/>
    
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/font-awesome.css"/>
    <!-- text fonts -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-fonts.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace.css"/>
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-part2.css"/>
    <![endif]-->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-rtl.css"/>
    <script type="text/javascript" src="static/js/jquery-1.7.1.min.js"></script>
    <script src="static/js/jquery.easing.1.3.js"></script>
    <script src="static/js/jquery.mobile.customized.min.js"></script>
    <script src="static/js/camera.min.js"></script>
    <script src="static/js/core/core-1.0.js" type="text/javascript" charset="utf-8"></script>
    <script src="static/js/core/captcha.js" type="text/javascript" charset="utf-8"></script>

    <style type="text/css">
        .login_msg {
            height: 20px;
            margin: 0px;
            padding: 0px;
            text-align: center;
            color: red;
        }
        .login{
        	left: 50%;
    		margin-left: -300px;
        }
        .sys_title{position: absolute;
top: 130px;
left: 0;
width: 100%;
text-align: center;}
        .sys_title strong{font-size: 28px;}
    </style>
</head>
<body>

<div class="login">
	<div class="sys_title">
	<strong>校园巡检</strong>
	</div>
    <div class="login_font">
        <div class="username">
            <form method="post" action="login" onsubmit="return login(this);">
                <fieldset>
                    <label class="block clearfix">
                        <span class="block input-icon input-icon-right">
                            <input id="userId"  type="text" name="userId" class="form-control"  placeholder="请输入用户名" value="adminauth"/>
                            <i class="ace-icon fa fa-user"></i>
						</span>
                    </label>
                    <label class="block clearfix"> 
                    	<span class="block input-icon input-icon-right"> 
                        <input id="userPwd" type="password" name="userPwd" class="form-control"  placeholder="请输入密码" value="111111"/>
                            <i class="ace-icon fa fa-lock"></i>
						</span>
                    </label>
                    <label class="block clearfix">
                        <span  class="block input-icon input-icon-right">
                            <input id="captcha" type="text" name="captcha"  class="form-control" placeholder="请输入验证码" value=""/>
                            <i class="ace-icon captcha"></i>
						</span>
                </label>
                <div class="space"></div>
                <div class="clearfix">
                    <label class="inline">
                        <input id="ck_pwd" type="checkbox" class="ace"/> <span class="lbl"> 记住密码</span>
                    </label>
                    <button type="submit" class="width-35 pull-right btn btn-sm btn-primary">
                        <i class="ace-icon fa fa-key"></i> <span class="bigger-110">登录</span>
                    </button>
                </div>
                <div class="block clearfix login_msg"> </div>
                <div class="space-4"></div>
                </fieldset>
            </form>
        </div>
    </div>
</div>
<!-- 	 <div id="templatemo_banner_slide" class="container_wapper">
	    <div class="camera_wrap camera_emboss" id="camera_slide">
	        <div data-src="static/img/banner1.jpg"></div>
	        <div data-src="static/img/banner2.jpg"></div>
	        <div data-src="static/img/banner3.jpg"></div>
	    </div>
	</div> -->
	
</body>
</html>



