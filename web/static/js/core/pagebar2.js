// 分页组件(针对页面多列表，分页组件样式变形问题)----------------------------------------------------------START

function initPagingBar2(gtable,$paging_bar){
	//var paging_info = " &nbsp;页次:&nbsp;<label id='current_page_lbl'>{0}</label>/{1} &nbsp;<label id='total_count_lbl'>共 {2} 条记录</label>，&nbsp;10条/页"  
	var page = $(gtable).getGridParam('page');
	pagebarLoad2(gtable,page,$paging_bar);
}

//渲染
function pagebarLoad2(gtable,page,$paging_bar){
	var records = $(gtable).jqGrid('getGridParam','records');
	var rowNum  = $(gtable).jqGrid('getGridParam','rowNum');
	var pageTotal  = parseInt((records + rowNum -1) / rowNum);
    var pagebar = "";
    var page_1 = 1;
    var total = 0;
	
	pagebar = "<ul class=\"pagination pull-right no-margin\">" +
//	"<li class=\"prev disabled\">" + 
//	"	<a href=\"#\">" +
//	"		<i class=\"ace-icon fa fa-angle-double-left\"></i>" +
//	"	</a>" +
//	"</li>" +
	"<li >" +
	"	<a>共"+records+"条</a>" +
	"</li>" ;
	if(page == 1){
		pagebar = pagebar +
		"<li class=\"prev disabled\">" +
		"	<a href=\"javascript:pageIndex('"+gtable+"','1','"+$paging_bar+"')\">首页</a>" +
		"</li>" +
		"<li class=\"prev disabled\">" +
		"	<a href=\"javascript:pageUp('"+gtable+"','"+$paging_bar+"')\">上一页</a>" +
		"</li>" ;
	}else{
		pagebar = pagebar +
		"<li >" +
		"	<a href=\"javascript:pageHead('"+gtable+"','"+$paging_bar+"')\">首页</a>" +
		"</li>" +
		"<li >" +
		"	<a href=\"javascript:pageUp('"+gtable+"','"+$paging_bar+"')\">上一页</a>" +
		"</li>" ;
	}
//	"<li class=\"next\">" +
//	"	<a href=\"#\">" +
//	"		<i class=\"ace-icon fa fa-angle-double-right\"></i>" +
//	"	</a>" +
//	"</li>" +
	if(page == pageTotal){
		pagebar = pagebar +
		"<li class=\"prev disabled\">" +
		"	<a href=\"javascript:pageNext('"+gtable+"','"+$paging_bar+"')\">下一页</a>" +
		"</li>" +
		"<li class=\"prev disabled\">" +
		"	<a href=\"javascript:pageIndex('"+gtable+"','"+pageTotal+"','"+$paging_bar+"')\">尾页</a>" +
		"</li>" ;
	}else{
		pagebar = pagebar +
		"<li>" +
		"	<a href=\"javascript:pageNext('"+gtable+"','"+$paging_bar+"')\">下一页</a>" +
		"</li>" +
		"<li>" +
		"	<a href=\"javascript:pageTail('"+gtable+"','"+$paging_bar+"')\">尾页</a>" +
		"</li>" ;
	}
	
	pagebar = pagebar + "</ul>";
	
	var paging_bar = $paging_bar || "#paging_bar";
	
	$(paging_bar).html(pagebar);
	
}

//制定页跳转
function pageIndex(gtable,index,paging_bar){
	var grid_url  = $(gtable).getGridParam("url");
	
	pagebarLoad2(gtable,index,paging_bar);
	jQuery(gtable).jqGrid('setGridParam', {
        url : grid_url,
        page:index
        //postData: $.toJSON(params)
    }).trigger("reloadGrid");
}

//上一页
function pageUp(gtable,paging_bar){
	var grid_url  = $(gtable).getGridParam("url");
	var up = parseInt($(gtable).getGridParam('page'))-1;
	if(up == 0){
		return ;
	}
	pagebarLoad2(gtable,up,paging_bar);
	jQuery(gtable).jqGrid('setGridParam', {
        url : grid_url,
        page:up
    }).trigger("reloadGrid");
}
//下一页
function pageNext(gtable,paging_bar){
	var grid_url  = $(gtable).getGridParam("url");
	var next = parseInt($(gtable).getGridParam('page'))+1;
	
	var records = $(gtable).jqGrid('getGridParam','records');
	var rowNum = $(gtable).jqGrid('getGridParam','rowNum');
	var pages  = parseInt((records + rowNum -1) / rowNum);
	if(pages+1 == next){
		return ;
	}
	pagebarLoad2(gtable,next,paging_bar);
	jQuery(gtable).jqGrid('setGridParam', {
        url : grid_url,
        page:next
    }).trigger("reloadGrid");
	
}
//首页
function pageHead(gtable,paging_bar){
	var grid_url  = $(gtable).getGridParam("url");
	var next = 1;
	
	pagebarLoad2(gtable,next,paging_bar);
	jQuery(gtable).jqGrid('setGridParam', {
        url : grid_url,
        page:next
    }).trigger("reloadGrid");
}
//尾页
function pageTail(gtable,paging_bar){
	var grid_url  = $(gtable).getGridParam("url");
	var records = $(gtable).jqGrid('getGridParam','records');
	var rowNum = $(gtable).jqGrid('getGridParam','rowNum');
	var pageTotal  = parseInt((records + rowNum -1) / rowNum);

	var next = pageTotal;
	pagebarLoad2(gtable,next,paging_bar);
	jQuery(gtable).jqGrid('setGridParam', {
        url : grid_url,
        page:next
    }).trigger("reloadGrid");
}
//分页组件----------------------------------------------------------END
