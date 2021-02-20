<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>红领巾运营管理平台</title>
    <%
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	%>
	<script>
	  var server_base_url = '<%=basePath%>';
	</script>
    <base href="${basePath}">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <script type="text/javascript" src="static/ichart/ichart.1.2.min.js"></script>
    <link rel="stylesheet" href="static/ichart/css/demo.css" type="text/css" />
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/bootstrap.css" />
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/font-awesome.css" />
    <!-- text fonts -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/chosen.css" />
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/jquery.gritter.css"/>

    <!-- 时间控件样式表  -->
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/datepicker.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/daterangepicker.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/datetimepicker.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/datetimepicker.min.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/bootstrap-timepicker.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/jquery-ui.custom.css"/>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/colorpicker.css" />

    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-part2.css" class="ace-main-stylesheet" />
    <![endif]-->
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="static/ace_v1.3/assets/css/ace-ie.css" />
    <![endif]-->

    <!-- ace settings handler -->
    <script src="static/ace_v1.3/assets/js/ace-extra.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="static/ace_v1.3/assets/js/html5shiv.js"></script>
    <script src="static/ace_v1.3/assets/js/respond.js"></script>
    <![endif]-->


    <link rel="stylesheet" href="static/css/core.css"/>

    <!-- 弹出框样式 -->
    <link rel="stylesheet" href="static/css/dialog.css"/>
    <!-- 数据表格中button的样式 -->
    <link rel="stylesheet" href="static/css/grid-button.css"/>

    <!-- 个性化设置 -->
    <link rel="stylesheet" href="static/css/personalized.css"/>

    <link type="text/css" rel="stylesheet" href="static/css/td.css"/>
    <link rel="stylesheet" href="static/css/base.css"/>
    <link rel="stylesheet" href="static/css/button.css"/>

    <link rel="stylesheet" href="static/lightbox/css/lightbox.css"/>

</head>

<body class="no-skin iflytek">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
    <script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
    </script>
    <div class="navbar-container" id="navbar-container">
        <!-- #section:basics/sidebar.mobile.toggle -->
        <button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
            <span class="sr-only">Toggle sidebar</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <!-- /section:basics/sidebar.mobile.toggle -->
        <div class="navbar-header pull-left">
          <!--  <img class="syslogo" alt="" src="static/img/logo (2).png" > -->
            <!-- #section:basics/navbar.layout.brand -->
           <a href="#" class="navbar-brand">
                <!-- 系统名称 -->
                <strong>红领巾考勤系统</strong>
                <small>
				运营管理平台
                </small>
            </a>
        </div>
        <!-- #section:basics/navbar.dropdown -->
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                    <!-- #section:basics/navbar.user_menu -->
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                        <img class="nav-user-photo" src="static/img/user.png"  />
                        <!--<img class="nav-user-photo" src="img/user.jpg"  />-->
								<span class="user-info">
									<small>欢迎您!</small>
									<small>${ sessionScope.UID.userName}</small>
								</span>
                        <i class="ace-icon fa fa-caret-down"></i>
                    </a>
                    <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="javascript:editPwd();">
                                <i class="ace-icon fa fa-cog"></i>
                                修改密码
                            </a>
                        </li>

                        <script type="text/javascript">
                            function editPwd() {
                                openDialog({
                                    dialogId : 'dlg-editPwd',
                                    title : '修改密码',
                                    pageUrl :'secUser/editPwd/view',
                                    width : '40%',
                                    height:'50%'
                                });
                            }
                        </script>
                        <li class="divider"></li>
                        <li>
                            <a href="javascript:logout();" >
                                <i class="ace-icon fa fa-power-off"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </li>
                <!-- /section:basics/navbar.user_menu -->
            </ul>
        </div>
        <!-- /section:basics/navbar.dropdown -->
    </div><!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>

    <!-- #section:basics/sidebar -->
    <div id="sidebar" class="sidebar                  responsive">
        <script type="text/javascript">
            try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
        </script>

        <div class="sidebar-shortcuts" id="sidebar-shortcuts">
            <div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
                <a class="btn btn-success" href="#" data-url="" title="返回主页">
                    <i class="ace-icon fa fa-desktop"  ></i>
                </a>
                <button class="btn btn-info" onclick="javascript:editPwd();" title="密码修改" >
                    <i class="ace-icon fa fa-key" ></i>
                </button>

              <!--   <button class="btn btn-warning" title="报表下载" onclick="javascript:fileDownloadDialog();">
                    <i class="ace-icon fa fa-download" ></i>
                </button> -->

                <button class="btn btn-danger" onclick="javascript:logout();" title="安全退出">
                    <i class="ace-icon glyphicon glyphicon-off"></i>
                </button>

            </div>

            <div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
                <span class="btn btn-success"></span>

                <span class="btn btn-info"></span>

                <span class="btn btn-warning"></span>

                <span class="btn btn-danger"></span>
            </div>
        </div>
        <ul class="nav nav-list">
            <li class="">
                <a>
                    <i class="menu-icon fa fa-home"></i>
                    <span class="menu-text"> 功能菜单 </span>
                </a>

                <b class="arrow"></b>
            </li>
        </ul><!-- /.nav-list -->

        <!-- #section:basics/sidebar.layout.minimize -->
        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>

        <!-- /section:basics/sidebar.layout.minimize -->
        <script type="text/javascript">
            try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
        </script>
    </div>

    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <!-- #section:basics/content.breadcrumbs -->
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
            </script>

            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-desktop desktop-icon"></i>
                    <a href="#" data-url="">主页</a>
                </li>
            </ul>
            
            <div id="content_main">
            	<div style="height:40px;background-color: #256bca;overflow: hidden;">
						<div id="iflytek-header" style="width: 100%; z-index: 1000;">
							<div style="width: 1280px; z-index: 1000;margin: 0 auto;">
								<div id="fix-header"
									class="nav-g-header-layer nav-g-background clearfix">
									<input id="userinfo_usertype" value="102" type="hidden">
									<div class="nav-inner w1000 clearfix">
										<div class="nav-g-links clearfix" id="nav_g_top_right">
											<div class="nav-u-info fn-right g-set" id="nav_g_uinfo_base"
												style="font-size: 12px;">
												<a id="nav_user_info_base" href="javascript:;"
													class="links nav-down">当前用户  ${ sessionScope.UID.userName}
												</a>
												
												<a id="nav_user_info_base logoutBtn" style="margin-left:58px;text-decoration:underline;"
															href="javascript:logout('http://whhlj.com:80/desktop-web/desktop.action');"
															class="nav-d-set nav-user-logout" data-role="logout">退出</a>
											</div>
											<div class="nav-g-welcome fn-right">
												<div class="nav-g-welcome-box">
													<span class="l"></span>
													<div class="nav-input-wrap">
														<span class="txt" id="nav_g_welcome">
														 <span
															title="武汉市红领巾学校<c:choose><c:when test="${sessionScope.UID.agentId == '1'}">（国际校区）</c:when><c:when test="${sessionScope.UID.agentId == '2'}">（阳光校区）</c:when><c:when test="${sessionScope.UID.agentId == '3'}">（CBD校区）</c:when><c:otherwise>（国际校区）</c:otherwise>
															</c:choose>" style="font-size: 12px;">武汉市红领巾学校...
															</span>
														</span>
													</div>
													<span class="r"></span>
												</div>
											</div>
											<div class="header-btns" style="line-height: 40px;">
												<a class="btn-icon home-icon"
													href="http://whhlj.com:80/desktop-web/index.action">首页</a>
												<a class="btn-icon computer-icon"
													href="http://whhlj.com:80/desktop-web/desktop.action">桌面</a>
												<a class="btn-icon xiaoxin-icon"
													href="http://j.changyan.com/" target="_blank">校信</a> <a
													class="btn-icon help-icon"
													href="http://whhlj.com/HelpCenter/main/default.action?appName=introduce&amp;from=ew"
													target="_blank">帮助中心</a>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				<div class="g-sub-nav app-page-nav navbar-theme-blue" style="display: block;">
					<div class="inner" style="width: 1280px;">
						<div class="g-nav-logo fn-left">
								<img src="static/img/app_icon.png" class="logoimg" alt="武汉红领巾智慧教育云平台">
							<span>校园巡检</span>
						</div>
						<div id="menuWraper"></div>
					</div>
				</div>
			</div>
        </div>
        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-content">
            
            <div class="page-content-area" data-ajax-content="true">
                <!-- ajax content goes here -->

            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
        
        
        
        
    </div><!-- /.main-content -->
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

<div id="abc">
</div>

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='static/ace_v1.3/assets/js/jquery.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='static/ace_v1.3/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='static/ace_v1.3/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="static/ace_v1.3/assets/js/bootstrap.js"></script>

<!-- ace scripts -->
<script src="static/ace_v1.3/assets/js/ace/elements.scroller.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.colorpicker.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.fileinput.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.typeahead.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.wysiwyg.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.spinner.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.treeview.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.wizard.js"></script>
<script src="static/ace_v1.3/assets/js/ace/elements.aside.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.ajax-content.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.touch-drag.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.sidebar.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.sidebar-scroll-1.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.submenu-hover.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.widget-box.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.settings.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.settings-rtl.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.settings-skin.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.widget-on-reload.js"></script>
<script src="static/ace_v1.3/assets/js/ace/ace.searchbox-autocomplete.js"></script>



<script src="static/ace_v1.3/assets/js/jquery-ui.custom.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/jquery.ui.touch-punch.js" charset="utf-8"></script>
<!-- 下拉列表 -->
<script src="static/ace_v1.3/assets/js/chosen.jquery.min.js"></script>
<script src="static/ace_v1.3/assets/js/fuelux/fuelux.spinner.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/date-time/bootstrap-timepicker.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/date-time/bootstrap-datepicker.js"></script>
<script src="static/ace_v1.3/assets/js/date-time/moment.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/date-time/daterangepicker.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/date-time/bootstrap-datetimepicker.min.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/datetimepicker.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/datetimepicker.min.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/bootstrap-colorpicker.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/jquery.knob.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/jquery.autosize.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/jquery.inputlimiter.1.3.1.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/jquery.maskedinput.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/bootstrap-tag.js" charset="utf-8"></script>

<!-- 下拉列表 -->
<script src="static/ace_v1.3/assets/js/jquery.gritter.js" charset="utf-8"></script>
<!-- 进度条 -->
<script src="static/ace_v1.3/assets/js/spin.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/spin.min.js" charset="utf-8"></script>

<!-- jqGrid -->

<script src="static/ace_v1.3/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="static/ace_v1.3/assets/js/jqGrid/i18n/grid.locale-cn.js"></script>

<!--引入弹窗组件start-->
<!-- <script type="text/javascript" src="static/js/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="static/js/attention/zDialog/zDialog.js"></script> -->
<!--引入弹窗组件end-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript" src="static/lightbox/jquery.lightbox.js" charset="utf-8"></script>
<!--
<script src="http://code.jquery.com/jquery-latest.pack.js" type="text/javascript"></script> -->

<!-- 自定义JS START -->
<!-- 核心功能组件 -->
<script type="text/javascript" src="static/js/core/core-1.0.js" charset="utf-8"></script>
<!-- 基础功能 退出等 -->
<script type="text/javascript" src="static/js/core/base.js" charset="utf-8"></script>
<!-- 加载菜单 -->
<script type="text/javascript" src="static/js/core/menu.js" charset="utf-8"></script>
<!-- 加载完成处理 -->
<script type="text/javascript" src="static/js/core/loadDone.js" charset="utf-8"></script>
<!-- 个性化设置 -->
<script type="text/javascript" src="static/js/core/personalized.js" charset="utf-8"></script>

<!-- 加载selectoption -->
<script type="text/javascript" src="static/js/core/selectOption.js" charset="utf-8"></script>
<!-- 加载loadForm -->
<script type="text/javascript" src="static/js/core/loadForm.js" charset="utf-8"></script>

<!-- 对话框组件 -->
<script type="text/javascript" src="static/js/core/dialog.js" charset="utf-8"></script>
<!-- 加载系统数据, 数据字典和用户权限信息 -->
<script type="text/javascript" src="static/js/core/initdata.js" charset="utf-8"></script>
<!-- js工具类 -->
<script type="text/javascript" src="static/js/core/utils.js" charset="utf-8"></script>
<!-- 分页组件 -->
<script type="text/javascript" src="static/js/core/pagebar.js" charset="utf-8"></script>
<!-- 表单验证 -->
<script type="text/javascript" src="static/js/core/validateForm.js" charset="utf-8"></script>
<!-- 按钮权限控制 -->
<script type="text/javascript" src="static/js/core/auth.js" charset="utf-8"></script>
<!-- nav-tabs 切换组件 -->
<script type="text/javascript" src="static/js/core/td.nav-tabs.js" charset="utf-8"></script>
<!--  文件上传组件 -->
<script type="text/javascript" src="static/js/core/uploadFile.js" charset="utf-8"></script>
<!-- 编辑器 -->
<script src="static/ace_v1.3/assets/js/markdown/markdown.js"></script>
<script src="static/ace_v1.3/assets/js/markdown/bootstrap-markdown.js"></script>
<script src="static/ace_v1.3/assets/js/jquery.hotkeys.js"></script>
<script src="static/ace_v1.3/assets/js/bootstrap-wysiwyg.js"></script>
<script src="static/ace_v1.3/assets/js/bootbox.js"></script>
<!-- 自定义JS END -->
<!-- 所有初始化内容之后最后渲染的处理js -->
<script type="text/javascript" src="static/js/core/after.js" charset="utf-8"></script>
<script src="static/ace_v1.3/assets/js/date-time/bootstrap-datetimepicker.zh-CN.js"></script>
<div id="alertConfirm" class="alertConfirm">
    <button type="button" class="close"><i class="ace-icon fa fa-times"></i></button>
    <div class="msg">
        <div class="msg-content">
            您确定要退出本系统吗？
        </div>
    </div>
    <div class="tool">
        <div class="alert-btn btn-yes"></div>
        <div class="alert-btn btn-no"></div>
    </div>
</div>
<div id="dialog-open" style="display: none"></div>
<script>
function isVieo(img){
	var ext = img.split('.').pop();
	if(ext =='mp4' || ext =='rmvb' || ext =='avi' || ext =='ts'){
	 	return true;
	}
	return false;
}

/*
 * 以下为适配科大讯飞
 */
var $menuWraper = $('#menuWraper');
$menuWraper.on('click','.js-app-link',function(){
	var $this = $(this);
	$menuWraper.find('.active').removeClass('active');
	$this.addClass('active');
	location.hash = $this.attr('url');
});

$menuWraper.on('mouseenter','.manage-menu',function(){
	var $this = $(this);
	$this.addClass('state-hover');
	$this.find('.manage-menu-dropdown').show();
}).on('mouseleave','.manage-menu',function(){
	var $this = $(this);
	$this.removeClass('state-hover');
	$this.find('.manage-menu-dropdown').hide();
});

var menuData = [];
var isFront = true;
//增加菜单加载监听，传递菜单数据
$menuWraper.on('menuLoad',function(e,data){
	renderMenu(data);
	//缓存数据
	menuData = data;
	var hash = location.hash;
	/**
	if(!hash){
		$menuWraper.find('.js-app-link').eq(0).trigger('click');
	}else{
		$menuWraper.find('a[url="'+hash.substr(1)+'"]').addClass('active');
	}**/
	$menuWraper.find('.js-app-link').eq(0).trigger('click');
});


$menuWraper.on('click','.js-nav-manage',function(){
	isFront = !isFront;
	renderMenu(menuData);
	$menuWraper.find('.js-app-link').eq(0).trigger('click');
});

$menuWraper.on('click','.top-a',function(){
	var $this = $(this);
	var url = $this.attr('url');
	isFront = !isFront;
	renderMenu(menuData);
	location.hash = url;
	$menuWraper.find('a[url="'+url+'"]').addClass('active');
});


function renderMenu(data){
	var bgHTML = ['<div class="manage-menu fn-right">'];
	bgHTML.push('<a href="javascript:;" class="js-nav-manage fn-right app-nav-manage" hidefocus="true" title="进入后台"></a>');
	bgHTML.push('<div class="g-dropdown manage-menu-dropdown" style="display: none;">');
	bgHTML.push('<ul class="menu-ul-blue">');
	
	var menuHTML = ['<a href="javascript:;" class="js-nav-next app-nav-next fn-right" title="下一页" hidefocus="true" style="display: none;"></a>'];
	menuHTML.push('<ul id="menus" class="js-nav-scroller nav-scroller fn-right">');
	var hasBgMenu = false;
	var frontMenuSize = 1;
	var maxSize = 7;
	data.forEach(function(d,i){
		if(d.text != (isFront? '运营管理':'系统管理')){
			d.children.forEach(function(m,i2){
				bgHTML.push('<li class="app-parent-li">');
				bgHTML.push('<a href="javascript:void(0);" url="'+m.attributes.url+'" class="d-app top-a">'+m.text+'</a>');
				bgHTML.push('</li>');
			});
			bgHTML.push('</ul></div></div>');
			if(d.children && d.children.length){
				hasBgMenu = true;
			}
		}else{
			frontMenuSize = d.children.length;
			d.children.forEach(function(m,i2){
				var displayShow = (i2>=maxSize?'display:none':'display:block');
				menuHTML.push('<li class="nav-item" style="'+displayShow+'">');
				menuHTML.push('<a hidefocus="true" class="nav-txt  js-app-link" href="javascript:void(0)"  url="'+m.attributes.url+'">'+m.text+'</a>');
				menuHTML.push('</li>');
			});
		}
	});
	if(!hasBgMenu){
		bgHTML = [];
	}
	menuHTML.push('</ul>');
	menuHTML.push('<a href="javascript:;" class="js-nav-prev app-nav-prev fn-right" title="上一页" hidefocus="true" style="display: none;"></a>');
	$menuWraper.html('<div class="js-app-nav app-nav-links" style="opacity: 1;">' + bgHTML.join('') + menuHTML.join('')+'</div>');
	$('.js-nav-next').toggle(frontMenuSize > maxSize);
}

//上一页  下一页 事件
$menuWraper.on('click','.js-nav-next,.js-nav-prev',function(){
	var $this = $(this);
	var $menus = $('#menus');
	var $visible = $menus.find(':visible');
	$hidden = $menus.find(':hidden');
	$hidden.show();
	$visible.hide();
	$this.hide();
	$($this.is('.js-nav-next')?'.js-nav-prev':'.js-nav-next').show();
});

</script>
</body>
</html>
