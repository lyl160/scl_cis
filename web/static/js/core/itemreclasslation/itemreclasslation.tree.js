/**
 * xiejinzhong 2015-02-28
 * 
 * 树形结构渲染处理
 */

$.fn.extend({
	tdTree : function(){//得到树的对象，id为唯一标识
		var tree = TdTreeMap[$(this).first().attr('id')] || {};
		return tree;
	}
});

//数对象集合
var TdTreeMap = {};

var tdTree = function(treeNode,treeData){
	this.treeNode = treeNode;
	this.$targetObj={};
	this.treeData = treeData;
	this.cls_selected = 'tree-selected';//选中的样式类名
	this.cls_checkBox = 'tree-item';//“复选框”的样式类名
	this.cls_branch = 'tree-branch';//是树枝的li节点的class标识
	this.cls_selected_part = 'tree-selected-part';//选中但没有全选的样式类名（主要针对枝节点）
	this.init = function(){
		var $targetObj = {};
		if(typeof this.$targetObj === 'string'){
			$targetObj = $("#" + this.treeNode);
		}else{
			$targetObj = $(this.treeNode);
		}
		
		//如果存在，重新刷新
		if(TdTreeMap[$targetObj.attr('id')]){
			var treeId = $targetObj.attr('id');
			var $parent = $targetObj.parent();
			$targetObj.remove();
			var treeHtml = '<ul id="'+treeId+'"></ul>';
			$parent.append(treeHtml);
			$targetObj = $("#" + treeId);
		}
		//将创建的对象存入map,id为唯一key
		TdTreeMap[$targetObj.attr('id')] = this;
		this.$targetObj = $targetObj;
		var $this = this;
		var dataSource = function(options, callback){
			var $data = null;
			if(!("text" in options) && !("type" in options)){
				$data = $this.treeData;//the root tree
				callback({ data: $data });
				return;
			}
			else if("type" in options && options.type == "folder") {
				if("additionalParameters" in options && "children" in options.additionalParameters)
					$data = options.additionalParameters.children;
				else $data = {};//no data
			}
			
			if($data != null)//this setTimeout is only for mimicking some random delay
//				setTimeout(function(){callback({ data: $data });} ,0);
				/**加载子节点*/
				callback({ data: $data });
			
			//we have used static data here
			//but you can retrieve your data dynamically from a server using ajax call
			//checkout examples/treeview.html and examples/treeview.js for more info
		};
		$targetObj.ace_tree({
			dataSource: dataSource,
			multiSelect: false,
			itemLoadDone : function($el){//当前元素加载完成触发
				var $tree_branch = $this.getThisLiElm($el);
//				var $next = $tree_branch.nextAll();
				var $children_branch = $('.tree-branch',$tree_branch);
				//如果还有同级节点，不处理
//				if($next.length > 0){
//					return;
//				}
				//如果子节点还有枝干
				if($children_branch.length > 0){
					return;
				}
				
				setTimeout(function(){
					//关闭最后一层的展开状态
					$('.tree-branch-header',$tree_branch).trigger('click');
				}, 20);
				
				
			},
			selectItem:function(ev){//选择复选框按钮事件,返回false阻止框架处理
				//菜单树选择框叶子节点阻止事件冒泡，为了防止点击枝节点的复选框时触发展开关闭事件
				ev.stopPropagation(); 
				
				//得到当前触发事件的节点
				var $el = $this.getCurrentElm(ev);
				
				//根据当前节点的选中状态自动改变子节点的选中状态
				$this.selectChildrenByThis($el);
			},
			selectFolder:function(ev){//展开搜索枝节点事件,返回false阻止框架处理
				var $el = $this.getCurrentElm(ev);
				return true;
			},
			'open-icon' : 'ace-icon tree-minus',
			'close-icon' : 'ace-icon tree-plus',
			'selectable' : true,
			'selected-icon' : 'ace-icon fa fa-check',
			'unselected-icon' : 'ace-icon fa fa-times',
			loadingHTML : '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
		});
	};
	
	/**
	 * 判断：如果是叶子，返回 true ，否则返回 false
	 * 	判断规则：自定义如果是li表示是最后一级
	 */
	this.isItem = function($el){
//		if(this.isLi(ev.currentTarget)){
//			return true;
//		}
		//获取当前元素所属的li节点
		var $li = this.getThisLiElm($el);
		//如果是li
		if(this.isLi($li)){
			//如果class属性包含tree-item
			if($li.filter('.' + this.cls_checkBox).length){
				return true;
			}
		}
		return false;
	};
	
	this.getTagName = function($elm){
		if($elm.tagName){
			return $elm.tagName.toUpperCase();
		}else if($elm[0] && $elm[0].tagName){
			return $elm[0].tagName.toUpperCase();
		}
		return '';
	};
	
	this.isLi = function($elm){
		if(this.getTagName($elm) == 'LI'){
			return true;
		}
		return false;
	};
	
	this.getCurrentElm = function(ev){
		return $(ev.currentTarget);
	};
	
	this.getThisLiElm = function($elm){
		$elm = $($elm);
		
		//如果找到树根节点还没找到，则返回，防止死循环
		if(!$elm || $elm.length == 0 || $elm.attr('id') === this.$targetObj.attr('id') || this.getTagName($elm) === 'BODY' || $elm == $elm.parent()){
			return {};
		}
		if(this.isLi($elm)){
			return $elm;
		}else{
			return this.getThisLiElm($elm.parent());
		}
	};
	
	// 选中/取消 子节点
	this.selectChildrenByThis = function($el){
		//获得当前li节点
		$el = this.getThisLiElm($el);
		if(this.isItem($el)){
			this.jqGridReload($el.context.attributes.menuid.nodeValue);
			
		}
	};
	
	/**
	 * 条件查询
	 * @param tb 表格
	 */
	this.jqGridReload = function(classid){
		var $jqParam = jQuery("#grid-table").jqGrid("getGridParam");//获取当前参数
		var search_param = {};
		search_param.classid = classid;
		$jqParam['postData'] = search_param;
		$jqParam['page'] = 1;//搜索跳到第一页
		$jqParam['mtype'] = 'POST';
		$jqParam['url'] = $('#sfrom').attr('action') || $jqParam['url'];
		jQuery('#grid-table').jqGrid('setGridParam', $jqParam).trigger("reloadGrid");
		$classid.val(classid);
	};
	
	//生成树
	this.init();
	return this;
};