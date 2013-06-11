/* ************************************************
 * 以下是HTML页面的UI样式控制 兼容IE6+ 、FireFox 、Chrome以及其他W3C标准浏览器
 *************************************************/
$.fn.extend( {		
	//使某个页面元素(一般是DIV)处于固定浮动状态
	//参数location为JSON对象，例如： {left : "20px", top : "20px"}
	//支持left、right、top、bottom四个属性，并且left和right不能并存，top和bottom不能并存
	//目前只支持长度单位像素px
	//支持属性值center，即：{left:"center", bottom : "10px"}，固定DIV将会水平居中
	fixFloat : function(location){
		var style = 'position:fixed; z-index:999; ';
		var isIE6 = $.browser.msie && $.browser.version == "6.0";	//指示是否是IE6浏览器，以便于在后面对其进行特殊的兼容处理
		var top = 0;
		if(typeof(location) != "object"){
			//没有参数时的默认值
			location = {right : "10px", bottom : "60px"};
		}else{				
			//防冲突判断
			if(location.left && location.right){
				alert("Error：fixFloat()方法的参数对象不能同时具有left和right属性！");
				return;
			}
			if(location.top && location.bottom){
				alert("Error：fixFloat()方法的参数对象不能同时具备top和bottom属性！");
				return;
			}	
		}
		//水平居中、偏移的逻辑判断
		if(location.left == "center" || location.right == "center"){				
			var value = this.outerWidth() / 2;
			style += 'left:50%; margin-left:-' + value + 'px; '; 
		}else if(location.left){
			style += 'left:' + location.left + '; ';
		}else if(location.right){
			style += 'right:' + location.right + '; ';
		}
		//垂直居中、偏移的逻辑判断
		if(location.top == "center" || location.bottom == "center"){					
			var value = this.outerHeight() / 2;
			style += 'top:50%; margin-top:-' + value + 'px; ';					
		}else if(location.top){
			if(isIE6){					
				top = parseInt(location.top);
			}else{					
				style += 'top:' + location.top + '; ';
			}
		}else if(location.bottom){
			if(isIE6){					
				top = $(window).height() - parseInt(location.bottom);
			}else{
				style += 'bottom:' + location.bottom + '; ';
			}
		}
		if(isIE6){				 
			style += 'position:absolute;top:expression(documentElement.scrollTop+' + (top >= 0 ? top : 'documentElement.clientHeight-this.offsetHeight') + '); ';
			$("body").css("background-attachment", "fixed").css("background-image", "url(about:blank)");
		}
		$('<div/>').appendTo("body").attr("style", style).append(this);
		return this;
	},	
	backTop : function(){
		this.fixFloat().css("cursor", "pointer").click(function(){scrollTo(0,0);}).hide();
		var backTopObj = this;
		$(window).scroll(function(){
			//标准W3C下，document.body.scrollTop恒为0，因此此处采用兼容性写法
			var top = document.body.scrollTop || document.documentElement.scrollTop;
			if(top == 0){
				backTopObj.hide();
			}else{
				backTopObj.show();				
			}
		});		
	}
});

/**
 * 将接收到的JSON对象数组转为对应的表格 
 * @param title 表格标题
 * @param {Object} titleSetting 标题设置,例如"name=姓名;age=年龄;sex=性别;addr=地址"
 * @param {Object} obj 如果是单个对象，则将该对象转为单条记录并显示
 * 如果是数组对象，则显示数组中的全部数据；如果接收到的是字符串，则显示该字符串；
 * 如果是其他类型数据，例如null、undefined、true、false、数字等，则显示"找不到符合条件的记录！"
 */
var table = {
	_caption : "",	//表格标题
	_titleSetting : "",	//表头标题表达式
	_dataObj : [],	//用于加载表格的数据
	_actionName : "操作",	//【操作】栏的标题名称
	_tips : "查询不到符合条件的记录!",	//当没有数据时显示的内容
	_hasNumber : true,	//指示是否有序号
	hasTemp : false,
	_input : new Array(),	//查询输入表单
	_formAction : new Array(),	//表单操作
	_validate : new Array,	//表单验证函数组
	_rowTemplate : {},	//特殊模板，类似 {'name' : function(){}, 'age' : function(){}}
	_rowHidden : new Array(),	//隐藏列数组
	_rowAction : new Array(),	//行操作栏数组
	_action : new Array(),	//全局操作数组
	//////////////////////////////////////////
	//数据分页选项
	_id : 1,	//当前分页数
	_size : 0,	//每页显示条数
	_count : 0,	//总记录数
	_orderBy : "",	//排序语句
	_urlParam : "",	//链接参数
	_sizeArray : [10, 20, 50, 100],	//每页显示条数的下拉框值数组，默认为第一个元素
	//初始化表格设置和数据
	init : function(title, titleSetting, pageObj){
		if(!titleSetting){
			alert('Error：创建表格对象时，必须传入标题设置参数，例如："name=姓名;age=年龄;sex=性别"');
			return;
		}
		this._caption = title;	//表格标题
		this._titleSetting = titleSetting;	//表头标题表达式
		this._dataObj = pageObj.list;	//用于加载表格的数据
		this._id = parseInt(pageObj.id) || 1;		
		this._size = parseInt(pageObj.size) || 0;
		this._count = pageObj.count || 0;
		this._orderBy = pageObj.orderBy || "";
		this._urlParam = pageObj.urlParam || "";
	},
	//全局操作按钮
	actionButton : function(btnName, script, needValidate){
		if(needValidate != false){
			needValidate = true;	//默认为true
		}
		this._action.push({name : btnName, act : script, isNeed : needValidate});
	},
	//应用于每一行的某个列的特殊模板
	rowTemplate : function(columnName, temp){
		this._rowTemplate[columnName] = temp;
		this.hasTemp = true;
	},
	//【操作】栏链接
	rowLink : function(linkName, href){		
		this._rowAction.push({type : "link", name : linkName, act : href});
	},
	//【操作】栏按钮
	rowButton : function(actionName, script){
		this._rowAction.push({type : "button", name : actionName, act : script});
	},
	//隐藏指定的列
	rowHidden : function(){
		var length = arguments.length;
		for(var i=0; i< length;i++){			
			this._rowHidden.push(arguments[i]);
		}
	},
	//表格是否有序号
	hasRowNumber : function(hasNumber){
		this._hasNumer = hasNumber;	
	},
	//将指定列的对应值转为对应的值显示
	rowDecode : function(){
		var length = arguments.length;
		if(length < 3){
			alert("Error：rowDecode函数需要至少传入3个参数，第1个参数是列名，第2个参数是指定的值，第3个参数是显示的值(如果有多个列，重复后面两个参数)。");
		}else if(length % 2 == 0 ){
			alert("Error：rowDecode函数的参数数量必须为3以上的奇数。");			
		}else{
			var args = arguments;
			var columnName =args[0]; 
			this.rowTemplate(columnName, function(){
				var value = this[columnName];	//实际的值
				for(var i = 1; i < length; i=i+2){
					if( value == args[i]){	//如果实际的值等于指定的值
						return args[i+1];					
					}					
				};
				return value;	//没有相等的指定值，则返回原值
			});			
		}
	},	
	//设置【操作】栏的名称
	actionName : function(actionName){
		this._actionName = actionName;
	},
	//设置当没有数据时，显示的文字提示内容
	whenNoData : function(tips){
		this._tips = tips;		
	},
	//设置数据分页的每页默认显示条数,如果传入的第一个参数不是正数，则不开启分页
	//如果传入多个参数，就是允许用户在【显示条数】的下拉框中选择自己需要的条数
	//系统自动将传入的第一个参数作为默认的显示条数
	pageSize : function(){
		var length = arguments.length;
		if(length > 0){
				var num = arguments[0];
				if(length == 1){
					if(this._size <= 0 || num <= 0){
						this._size = num;
					}
//					if(num <= 0){	//如果不是正数，则简单处理
//						this._sizeArray[0] = num;
//					}else{				
//						for(var i in this._sizeArray){
//							if(this._sizeArray[i] == num){	//将指定条数放在sizeArray第一位
//								if(i != 0){
//									this._sizeArray[i] = this._sizeArray[0];
//									this._sizeArray[0] = num;
//								}
//								break;
//							}				
//						}
//					}
				}else{
					this._sizeArray = arguments;
					if(this._size <= 0){
						this._size = num;
					}
				}
		}		
	},
	//点击【操作】栏按钮时执行的代理函数
	run : function(rowIndex, actionIndex){
		var script = this._rowAction[actionIndex].act;
		var obj = this._dataObj[rowIndex];
		script.call(obj, rowIndex);
	},
	//全局【操作】按钮执行的代理函数
	RUN : function(actionIndex){
		var action = this._action[actionIndex];
		var data = this._dataObj;
		var selected = [];
		$('input[name="ui_checkbox"]:checked').each(function(){
			var index = this.value;
			selected.push(data[index]);			
		});
		if(action.isNeed && selected.length == 0){
			alert("没有选中任何数据！");
			return;
		}
		var script = action.act;
		script(selected);
	},
	//全选复选框执行的代理函数
	selectAll : function(obj){
		if($(obj).attr("checked")){
			$('input[name="ui_checkbox"]').attr("checked", true);
		}else{
			$('input[name="ui_checkbox"]').attr("checked", false);			
		}
	},
	//智能固定表格标题列的代理函数
	fixHead : function(){		
		$(function(){
			var tab = $("table.ui-table");						
			if(tab.height() > $(window).height()){
				//如果表格高度大于窗口的高度
				var $head = $("tr.tr-head");
				var p = $head.position();
				var copyTable = $('<table id="UI_Table_Copy" class="ui-table"></table>');
				copyTable.append($head.clone().attr("id","UI_Tr_Head_Copy")).appendTo("body");
				copyTable.fixFloat({top : "0px", left : p.left + "px"});
				var width = $head.width();				
				copyTable.parent().css("width", width + "px");
				var widthArray = [];
				$head.children().each(function(i){
					widthArray[i] = $(this).width();					
				});
				copyTable.find("th").each(function(i){
					$(this).css("width", widthArray[i] + "px");
				});
				copyTable.hide();
				var $src = $("#UI_Table .tr-head :checkbox");
				var $copy = $("#UI_Tr_Head_Copy :checkbox");
				var isHidden = true;
				$(window).scroll(function(){
					var top = document.body.scrollTop || document.documentElement.scrollTop;
					if(top > p.top){
						if(isHidden){
							$copy.attr("checked", $src.attr("checked") ? true : false);
							isHidden = false;
						}
						copyTable.show();
					}else{
						if(!isHidden){
							$src.attr("checked", $copy.attr("checked") ? true : false);
							isHidden = true;
						}
						copyTable.hide();		
					}
				});
			}
		});		
	},
	//更改每页显示条数的代理函数
	changeSize : function(obj){
		var pageSize = $(obj).val();
		table.goPage(1, pageSize);
	},
	//翻页操作的代理函数
	goPage : function(pageId, pageSize){
		pageSize = pageSize || this._size;
		if(!pageId){
			var value = $.trim($("#UI_PageId").val());
			pageId = parseInt(value); 
			if(!pageId){
				alert("跳转页数必须为数字！");
				return;
			}
			var maxPage = this._count / this._size;
			maxPage = Math.ceil(maxPage);
			if(pageId > maxPage){
				pageId = maxPage; 
			}			
			if(pageId < 1){
				pageId = 1;				
			}			
		}
		var url = self.location.href;
		url = url.split(/\?/)[0];
		var hasParam = false;
		if(this._urlParam){
			url += '?';
			url += this._urlParam;
			hasParam = true;
		}
		if(this._orderBy){
			url += hasParam ? '&' : '?';
			url += 'page.orderBy=' + this._orderBy;
			hasParam = true;
		}
		url += hasParam ? '&' : '?';
		self.location.href = url + 'page.id=' + pageId + '&page.size=' + pageSize;
	},
	//加载显示
	show : function(selector){
		if(!selector){
			alert("Error: 调用show()方法必须提供用于加载表格内容的JQuery选择器参数！");
			return;
		}
		var setting = this._titleSetting.replace(/\=/g, ";");
		var array = setting.split(";");
		var length = array.length;
		var actionLength = this._rowAction.length;
		var hasCheckBox = this._action.length > 0;
		if(length % 2 == 1 || length == 0){
			alert('Error：表格对象传入的标题设置参数格式不正确，比如采用如下格式："name=姓名;age=年龄;sex=性别"');
			return;
		}
		var titleArray = [];
		for(var i=0; i<length; i=i+2){
			titleArray.push({name : array[i], title : array[i+1]});
		}
		length = titleArray.length;
		var realSize = length + actionLength;	//包含序号列、操作列在内的所有列数
		if(hasCheckBox){
			realSize++;
		}
		if(this._hasNumber){
			realSize++;
		}
		var tableHtml = '<table id="UI_Table" class="ui-table">';
		////////////////////////////////////////////
		//表格标题
		tableHtml += '<caption>' + this._caption + '</caption>';
		///////////////////////////////////////////
		//表格列标题
		tableHtml += '<tr class="tr-head">';
		if(hasCheckBox){
			tableHtml += '<th><input type="checkbox" value="0" onclick="table.selectAll(this)" />全选</th>';
		}
		if(this._hasNumber){
			tableHtml += '<th>序号</th>'; 
		}
		for(var i = 0; i < length; i++){	//列标题
			tableHtml += '<th>' + titleArray[i].title + '</th>';
		}
		if(actionLength > 0){	//操作栏标题
			tableHtml += '<th colspan="'+ actionLength +'">' + this._actionName + '</th>';
		}
		tableHtml += "</tr>";
		//////////////////////////////////////////
		//表格正文内容
		var objType = typeof(this._dataObj);
		if(objType == "object" && !$.isArray(this._dataObj)){ //如果是单个对象，将其封装为数组对象
			this._dataObj = [this._dataObj];			
		}else if(objType == "string"){
			this._tips = this._dataObj;			
		}
		if($.isArray(this._dataObj) && this._dataObj.length > 0){	//如果是数组，并且有元素
			var size = this._dataObj.length;	//记录数			
			for(var i=0; i<size; i++){
				var className = (i % 2 == 0) ? "odd" : "even";	//奇偶列判断
				tableHtml += '<tr class="' + className + '">';
				if(hasCheckBox){
					tableHtml += '<td><input name="ui_checkbox" type="checkbox" value="' + i + '" /></td>';
				}
				if(this._hasNumber){	//如果有序号
					tableHtml += '<td>' + (i + 1) + '</td>';
				}
				var obj = this._dataObj[i];	//单条数据
				for(var j=0; j<length; j++){//循环列
					var name = titleArray[j].name;	//列名
					var text = null;	//表格内容
					var style = "";
					if(this.hasTemp){	//如果有特殊模板
						var temp = this._rowTemplate[name];
						if($.isFunction(temp)){	//如果是函数
							text = temp.apply(obj);
							if(text == null){	//如果有为null的值，一律转为空字符串
								text = "";
							}else if($.isArray(text)){	//如果设置了单元格的其他属性
								if(text[1]){
									style = " " + text[1] + " ";									
								}
								text = text[0];
							}
						}
					}
					if(text == null){	//如果没有特殊模板或特殊模板配置有误
						text = obj[name];	//取实际值
						if(text == null){//如果有为null的值，一律转为空字符串
							text = "";
						}
					}
					if(j == 0){//如果有隐藏文本域，则全部放在有效列的第一列
						var hiddenSize = this._rowHidden.length;
						if(hiddenSize > 0){
							for(var k=0; k<hiddenSize; k++){
								var hiddenName = this._rowHidden[k];
								var hiddenValue = obj[hiddenName];								
								text += '<input name="hidden_'+ hiddenName +'" type="hidden" value="' + hiddenValue + '" />';								
							}					
						}
					}
					if(style == ""){
						style = ' class="td-' + name + '" ';
					}
					tableHtml += '<td'+ style +'>' + text + '</td>';
				}		
				//追加【操作】栏
				if(actionLength > 0){
					for(var l=0; l < actionLength; l++){
						var action = this._rowAction[l];
						var script = action.act;						
						if(action.type == "link"){
							var href = script.apply(obj);
							if(href != null){
								tableHtml += '<td class="td-action"><a href="' + href + '">' + action.name + '</a></td>';								
							}else{
								tableHtml += '<td class="td-action">-</td>';
							}
						}else if(action.type == "button"){
							tableHtml += '<td class="td-action"><input class="ui-button" type="button" value="' + action.name + '" onclick="table.run('+ i +',' + l +')" /></td>';
						}
					}
				}
				tableHtml += '</tr>';				
			}
		}else {
			//如果是字符串、null、undefined、空数组等其他类型
			tableHtml += '<tr><td colspan="' + realSize+ '">'+ this._tips +'</td></tr>';		
		}
		tableHtml += '</table>';
		if(this._size > 0 && this._dataObj.length > 0){
			//如果开启了分页，并且有数据记录，则加载数据分页工具条
			tableHtml += '<div class="ui-table-widget">';
			//首页+上一页
			if(this._id > 1){
				tableHtml += '<a href="javascript:table.goPage(1)">首页</a>';
				tableHtml += '<a href="javascript:table.goPage('+ (this._id - 1) +')">上一页</a>';
			}else{
				tableHtml += '<span>首页</span><span>上一页</span>';
			}
			
			//下一页、尾页
			var maxPage = this._count / this._size;
			maxPage = Math.ceil(maxPage);
			var space = '<span class="ui-space">;</span>';
			if(maxPage > this._id){
				tableHtml += '<a href="javascript:table.goPage('+ (this._id + 1) +')">下一页</a>';
				tableHtml += '<a href="javascript:table.goPage('+ maxPage +')">尾页</a>';
			}else{
				tableHtml += '<span>下一页</span><span>尾页</span>';
			}
			//跳转到+每页显示条数
			tableHtml += '<input class="ui-button" type="button" value="跳转到" onclick="table.goPage()" />第<input class="ui-text" id="UI_PageId" type="text" />页' + space;
			tableHtml += '每页显示<select onchange="table.changeSize(this)">';
			for(var i in this._sizeArray){
				if(this._sizeArray[i] == this._size){
					tableHtml += '<option value="'+ this._sizeArray[i] +'" selected="selected">' + this._sizeArray[i] + '</option>';					
				} else{
					tableHtml += '<option value="'+ this._sizeArray[i] +'">' + this._sizeArray[i] + '</option>';				
				}
			}
			tableHtml += '</select>条' + space;
			//当前页+总页数
			tableHtml += '当前页/总页数：';
			tableHtml += this._id + '/' + maxPage +space;
			tableHtml += '总记录数：' + this._count + space;
			tableHtml += '</div>';
		}
		if(hasCheckBox){
			//如果有复选框操作
			var div = '<div class="ui-table-action">';
			for(var x in this._action){
				var action = this._action[x];
				div += '<input class="ui-button" type="button" value="'+ action.name +'" onclick="table.RUN('+ x +')" />';
			}
			div += "</div>";
			tableHtml += div;
		}
		$(selector).html(tableHtml);
		this.fixHead();
	}
}
// UI组件
function UI(){
	this.backTop = function(html) {
		html = html || '<a id="UI_BackTop" class="ui-backtop" title="返回顶部" >返回顶部</a>';
		$(html).appendTo(document.body).backTop();
	}
}
var UI = new UI();