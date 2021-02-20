/**
 * 城市选择封装
 * @author xlsky0713
 * @param {Object} $
 * @param {Object} dofun
 */
(function($){
	
	var provinceMapping = {};
	
	var CitySelectModal = {
		initModal: function() {
			var that = this;
			var $modal = $('#myModaphone .modal-body');
			
			var modalHTML= [];
			modalHTML.push('<div><h3 class="smaller lighter blue"><small class="switch-selected">已选发货地</small></h3>');
			modalHTML.push('<span id="exist_address" data-toggle="buttons" >');
			modalHTML.push('<label class="btn btn-xs btn-yellow ">北京</label>');
			modalHTML.push('<label class="btn btn-xs btn-yellow">上海</label>');
			modalHTML.push('</span></div>');
			modalHTML.push('<div id="province_list">');
			modalHTML.push('<div>');
			modalHTML.push('<h3 class="smaller lighter blue">');
			modalHTML.push('<small class="switch-title">选择发货地</small>');
			modalHTML.push('</h3>');
			modalHTML.push('<span id="cgitems" data-toggle="buttons" >');
			modalHTML.push('<label class="btn btn-xs btn-yellow notExist" data-value="北京">北京</label>');
			modalHTML.push('<label class="btn btn-xs btn-yellow notExist" data-value="上海">上海</label>');
			modalHTML.push('<label class="btn btn-xs btn-yellow notExist" data-value="天津">天津</label>');
			modalHTML.push('<label class="btn btn-xs btn-yellow notExist" data-value="重庆">重庆</label>');
			modalHTML.push('</span></div>');
			
			modalHTML.push('	<ul class="mui-table-view ">');
			
			$.each(cityData,function(i,d){
				modalHTML.push('		<li class="mui-table-view-cell" data-value="'+d.text+'">');
				modalHTML.push('			<a class="mui-navigate-right" data-value="'+d.text+'">'+d.text+'</a>');
				modalHTML.push('			<div class="cityItems" style="display:none;padding: 5px 0px"></div>');
				modalHTML.push('		</li>');
				provinceMapping[d.text] = d.children;
			});
			modalHTML.push('	</ul>');
			
			modalHTML.push('</div>');
			
			$modal.html(modalHTML.join(''));
			
			that.modal = $modal;
			that.provinceMapping = provinceMapping;
		},
		init:function(citys,callback){
			var that = this;
			$.isFunction(citys) && (callback = citys,citys='');
			that.citys = citys;
			that.callback = callback;
			that.selectCitys = [];
			$('#cgitems label').each(function(){
				this.classList.remove('active');
				this.classList.add('notExist');
			});
			
			var citys = that.citys;
			that.selectCitys = [];
			if(citys.length){
				//渲染直辖市高亮状态
				$.each(citys.split(','),function(i,d){
					that.selectCitys.push(d);
					var cbutton = document.getElementById("cgitems").querySelector('label[data-value="'+d+'"]');
		    	 	if(cbutton){
			    	 	cbutton.classList.add("active");
			    	 	cbutton.classList.remove("notExist");
		    	 	}
				});
			}
			$('.cityItems').hide();
			that.renderSelectArea();
		},
		renderSelectArea:function(){
			var that = this;
			var existAddress=document.getElementById("exist_address");
			var adreessHTML = [];
			$.each(that.selectCitys,function(i,d){
				adreessHTML.push("<a class='mui-control-item'><label data-value='"+d+"' class='mui-btn'>"+d+"</label><span class='icon-close'><img width='15px' height='15px' src='static/img/shanchu.png' /></span></a>");
			});
			existAddress.innerHTML = adreessHTML.join('');
		},
		initEvent: function() {
			var that = this;
			//删除已选发货地
			$("#exist_address").off('click').on('click','.icon-close',function(){
				var selectCitys = that.selectCitys;
			 	var deleteNode=this.parentNode;
	    	 	deleteNode.parentNode.removeChild(deleteNode);
	    	 	var ctext = this.parentNode.querySelector('label').innerText;
	    	 	var cindex = selectCitys.indexOf(ctext);
	    	 	~cindex && selectCitys.splice(cindex,1);
	    	 	var cbutton = document.getElementById("province_list").querySelector('label[data-value="'+ctext+'"]');
	    	 	if(cbutton){
		    	 	cbutton.classList.toggle("active");
		    	 	cbutton.classList.toggle("notExist");
		    	 	cbutton.classList.toggle("choose");
	    	 	}
			})
			 //选择直辖市
			$("#province_list").off('click').on('click','#cgitems .btn',function(){
				var selectCitys = that.selectCitys;
				var classlist = this.classList;
				var ctext = this.innerText;
				classlist.toggle("notExist");
				var cindex = selectCitys.indexOf(ctext);
				if(classlist.contains('notExist')){
					~cindex && selectCitys.splice(cindex,1);
				}else{
					!~cindex && selectCitys.push(ctext);
				}
				that.renderSelectArea();
			})
			//选择省下面的市
			 $("#province_list").on('click','.mui-table-view-cell a',function(e){
			 	var selectCitys = that.selectCitys;
			 	//渲染市
			 	var text = this.dataset.value;
			 	var cityHTML = ['<h5 style="display:none" class ="title" style="align-content: center;padding: 5px 0;">'+text+'</h5>'];
			 	$.each(that.provinceMapping[text],function(i,d){
			 		cityHTML.push('<label data-value="'+d.text+'" class="btn btn-xs btn-yellow '+(~selectCitys.indexOf(d.text)?'active choose':'notExist')+'">'+d.text+'</label>');
			 	});
			 	$(this).parent().parent().find('li .cityItems').hide();
			 	$(this).parent().find('.cityItems').html(cityHTML.join('')).show();
			 })
			//选中
			 $("#province_list").on('click','.cityItems .notExist',function(){
				this.classList.add("active");
				this.classList.add("choose");
				this.classList.remove("notExist");
				var selectCitys = that.selectCitys;
				var ctext = this.innerText;
				var cindex = selectCitys.indexOf(ctext);
				!~cindex && selectCitys.push(ctext);
				that.renderSelectArea();
			});
			 //取消选中
			$("#province_list").on('click','.cityItems .choose',function(){
				this.classList.remove("active");
				this.classList.remove("choose");
				this.classList.add("notExist");
				var selectCitys = that.selectCitys;
				var ctext = this.innerText;
				var cindex = selectCitys.indexOf(ctext);
				~cindex && selectCitys.splice(cindex,1);
				that.renderSelectArea();
			});
			
			//确认按钮
			$(this).on('click',".js-confirm",function(){
				that.callback && that.callback(that.selectCitys);
				$(that).modal('hide');
			});
		}
	};
	
	$.fn.citySelectModal = function(){
		this.renderSelectArea = CitySelectModal.renderSelectArea;
		this.init = CitySelectModal.init;
		CitySelectModal.initModal.call(this);
		CitySelectModal.initEvent.call(this);
		return this;
	}
	
	
	
})(window.jQuery);