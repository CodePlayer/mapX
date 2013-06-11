/* ***************************************
 * 此文件包含所有基于jQuery的扩展方法，是jquery-extend.js、jquery-ui-extend.js的合集。
 * 相关使用说明请参见代码中的注释或附属的使用帮助文档。
 * @Author: Ready
 * @Date: 2012-06-23
 * @Mail: CodePlayer360@gmail.com
 ****************************************/
 //ajax请求异常，弹出提示信息
jQuery("body").ajaxError(function(e, request, setObj, ex) {
			alert("发送请求到“" + setObj.url + "”失败：\n" + ex);
});
//根据元素ID获取对应的DOM元素
function fid(id){
	return document.getElementById(id);	
}
//根据元素name获取对应的DOM元素数组
function fname(name){
	return document.getElementsByName(name);
}
//根据元素标签名获取对应的DOM元素数组
function ftag(tagName){
	return document.getElementsByTagName(tagName);
}

//以居中的模式窗口的形式打开指定连接
function openDialog(url, width, height){
	if(typeof url != "string"){
		alert("必须指定模式窗口打开的网页地址！");
		return;
	}
	if(isNaN(width) || width < 0) width = 0.8;
	if(isNaN(height) || height < 0) height = 0.8;
	if(width <= 1) width *= screen.width;
	if(height <= 1) height *= screen.height;
	var args = "dialogWidth=" + width + "px; dialogHeight=" + height + "px;edge=Raised;center=Yes;help=no;resizable=Yes;status=no";
	if(!$.browser.msie){
		args += ";dialogLeft=" + ((screen.width - width) / 2) + "px"
		args += ";dialogTop=" + ((screen.height - height) / 2) + "px"
	}
	return window.showModalDialog(url, this, args);
}

//打印或记录错误信息
function logError(msg){
	alert(msg);
}
//表达式解析函数
function decode(){
	var length = arguments.length;
	if(length < 2){
		logError("函数decode至少需要2个参数！");
		return false;
	}
	var value = arguments[0];
	var lastLength = (length & 1) == 0 ? length - 1 : length;
	for(var i = 1; i < lastLength; i += 2){
		if( value == arguments[i]){	//如果实际的值等于指定的值
			return arguments[i+1];					
		}
	}
	return lastLength == length ? value : arguments[lastLength];
}

//以POST、同步方式发送Ajax请求
function ajax(url, args, ext){
	return jQuery.AJAX(url, args, ext);
}
/* 
 * 实现JS的常用工具操作函数，并以基于jQuery的方式进行扩展。
 * 程序会先检测前面的JS文件是否已经扩展了相关函数，如果已经扩展过，此处将不再扩展。
 */
if(!jQuery.isFunction(jQuery.isEmpty)){
	//扩展jQuery静态方法
	jQuery.extend({
		//检测字符串是否为空字符串
		isEmpty : function(str){
			return !jQuery.trim(str);
		},
		//检测字符串是否为整数形式或指定长度的整数
		isInt : function(str, length){
			if(str == ""){
				return false;				
			}
			length = isNaN(length) || length < 0 ? 0 : parseInt(length);
			var regObj = length == 0 ? /^\d+$/ : new RegExp("/^\\d{" + length + "}$/");
			return regObj.test(str);
		},
		//检测字符串是否为整数形式或小数形式
		isNumber : function(str, intOnly){
			str = jQuery.trim(str);
			if(intOnly !== false)
				intOnly = true;	
			var regObj = intOnly ? /^\d+$/ : /^\d+(\.\d+)?$/;
			return regObj.test(str);
		},
		//检测字符串是否为整数或小数类型
		isDouble : function(str){
			return /^\d+(\.\d+)?$/.test(str);			
		},
		//检测字符串是否符合日期类型格式
		isDate : function(str, format){
			format = format || "yyyymmdd";
			format = format.toLowerCase();
			var regStr = format.replace(/yyyy/, "(19\\d{2}|2\\d{3})"); //1900-2999，如需改动范围，请自行修改
			regStr = regStr.replace(/mm/, "(0[1-9]|1[0-2])"); //01-12
			regStr = regStr.replace(/dd/, "(0[1-9]|[1-2]\\d|3[0-1])"); //01-31，验证相对宽松，如需严格验证，请自行修改
			regStr = "/^" + regStr + "$/"
			var regObj = new RegExp(regStr);
			return regObj.test(str);
		},
		//直接返回Ajax同步请求返回的数据
		AJAX : function(url, args, ext){
			var result;
			var sender = {"url":url, "type":"POST", "async":false, "data":args, "success":function(data){
				result = data; 								
			}};
			if(ext && typeof ext == "object"){
				for(var i in ext){
					sender[i] = ext[i];
				}	
			}
			jQuery.ajax(sender);
			return result;
		},
		//创建动态表单，并以指定方式发送(提交表单)HTTP请求
		sendForm : function(url, args, method, target){
			if(!url) url = self.location.href.split(/\?|#/)[0];
			if(!method) method = "POST";
			if(!target) target = "_self";
			var f = $('<form action="' + url + '" method="' + method + '" target="' + target + '"></form>');
			if(args && typeof args == "object"){
				for(var i in args){
					var $input = $('<input name="' + i + '" type="hidden" />')
					if(args[i] != null && args[i] != undefined){
						$input.val(args[i]);//此处采用动态赋值，以防止特殊字符被转义
					}
					f.append($input);
				}
			}
			f.appendTo(document.body);
			f.submit();
			f.remove();	
		}
		,
		//动态创建FORM，并以POST方式发送请求
		//如果URL为null，则默认发送至不带参数的当前页面URL
		sendPost : function(url, args, target){
			jQuery.sendForm(url, args, "POST", target);
		},
		//动态创建FORM，并以GET方式发送请求
		//如果URL为null，则默认发送至不带参数的当前页面URL
		sendGet : function(url, args, target){
			jQuery.sendForm(url, args, "GET", target);
		}
	});
	
	//扩展jQuery实例方法
	jQuery.fn.extend({
		//非空验证，如果为空返回true
		isEmpty : function(msg) {
			var isEmpty = jQuery.isEmpty(this.val());
			if (isEmpty && msg) { //如果为空，并有提示消息，则弹出提示消息
				alert(msg);
				this.focus();
			}
			return isEmpty;
		},
		//整数验证，如果不是整数形式返回true
		//length、msg，一个代表长度，一个代表提示信息，支持参数位置调换，支持任意参数缺失
		notNumber : function(length, msg) {
			var _length = 0; //默认不验证长度			
			if (typeof length == "string") {
				_length = msg || _length;
				msg = length;
			}
			var notNumber = jQuery.isNumber(this.val());
			if (notNumber && msg) { //如果不符合条件，并且有提示消息
				alert(msg);
				this.focus();
			}
			return notNumber;
		},
		//小数或整数验证，如果不是小数或整数形式返回true
		notDouble : function(msg) { //此方法只接收提示信息参数
			var notDouble = !/^\d+(\.\d+)?$/.test($.trim(this.val()));
			if (notDouble && msg) { //如果不符合条件，又有提示消息
				alert(msg);
				this.focus();
			}
			return notDouble;
		},
		//验证日期，支持缺失msg参数、支持缺失format和msg两个参数
		//由于两个参数都是字符串，因此不支持参数位置交换
		notDate : function(msg, format) {
			var value = $.trim(this.val()); //内部去空格，如需变动，请自行修改			
			format = format|| "yyyymmdd"; //没有format参数，默认为yyyymmdd格式
			format = format.toLowerCase(); 
			var regStr = format.replace(/yyyy/, "(19\\d{2}|2\\d{3})"); //1900-2999，如需改动范文，请自行修改
			regStr = regStr.replace(/mm/, "(0[1-9]|1[0-2])"); //01-12
			regStr = regStr.replace(/dd/, "(0[1-9]|[1-2]\\d|3[0-1])"); //01-31，验证相对宽松，如需严格验证，请自行修改
			regStr = "/^" + regStr + "$/"
			var regExp = new RegExp(regStr);
			var notDate = !regExp.test(value);
			if (notDate && msg) {
				alert(msg);
				this.focus();
			}
			return notDate;
		},
		//正则表达式验证，支持参数位置交换，正则表达式对象必须传入，否则出错
		notRegExp : function(regExpObj, msg) {
			if (typeof regExpObj == "string") {
				var temp = regExpObj;
				regExpObj = msg;
				msg = temp;
			}
			var notReg = !regExpObj.test(this.val());
			if (notReg && msg) {
				alert(msg);
				this.focus();
			}
			return notReg;
		},
		
		//返回去空格后的字符串
		trim : function(){
			return jQuery.trim(this.val());
		}	
	});	
}
/* ***************************************************
 * 使用说明：以上扩展建立在JQuery之上，必须在引入此JS扩展文件之前先引入jQuery。 
 * 在此以notNumber()和notDate()函数为例进行说明。 
 * 如果某表单中存在ID为money的元素需要进行数字验证，则可以进行如下书写：
 * 
 *  $(function(){
 *  	$("form").submit(function(){	//注册表单提交事件调用的函数
 * 			//写法1：只验证是否为整数，不是数字时，返回true
 * 			if($("#money").notNumber()) return false;
 * 
 * 			//写法2：只验证是否为整数，不是数字时，将会弹出提示，返回true
 * 			if($("#money").notNumber("金额必须为数字！")) return false;
 * 
 *			//写法3：验证是否为整数并且长度为6 ，不是数字时，返回true
 * 			if($("#money").notNumber(6)) return false;
 * 
 * 			//写法4：验证是否为整数、长度为4，不符合时，弹出提示，返回true
 * 			if($("#money").notNumber(6, "金额必须是4位整数！")) return false;
 * 			
 * 			//传入多个参数，并且传入的参数类型不同时，支持参数位置交换
 * 			if($("#money").notNumber("金额必须是4位整数！", 6)) return false;
 * 		});
 *  });
 *  
 * 
 * 
 * 如果某表单中存在ID为birthday的元素需要进行日期验证，则可以进行如下书写：
 * 
 * $(function(){
 *  	$("form").submit(function(){	//注册表单提交事件调用的函数
 * 			//写法1：验证是否为"yyyymmdd"格式，如果不是，返回true
 *			//默认格式为yyyymmdd，不同项目需求不同，可以自行更改源代码中的默认格式
 * 			if($("#birthday").notDate()) return false;
 * 
 * 			//写法2：验证是否为"yyyymmdd"格式，如果不是，弹出提示并返回true
 * 			if($("#birthday").notDate("生日必须为yyyymmdd格式，例如20120315！")) return false;
 * 
 *			//写法3：验证是否为"yyyy年mm月dd日"格式，如果不是，弹出提示并返回true
 * 			//由于两个参数都是字符串类型，因此不支持参数位置调换
 * 			if($("#birthday").notDate("生日必须为yyyy年mm月dd日格式，例如2012年03月15日", "yyyy年mm月dd日")) return false;
 * 
 * 			//写法4：验证是否为"yyyy-mm-dd"格式，如果不是，不弹出提示，返回true
 * 			//由于都是字符串参数，因此无法省略参数，可以用空值(""或null)替代
 * 			if($("#birthday").notDate("", "yyyy-mm-dd")) return false;
 * 		});
 *  });
 *********************************************************************/
/**
 * 页面表单验证器类，用于进行表单验证
 * @memberOf {TypeName} 
 */
function V(selector, eventName){
	var current = this;	
	this.count = 5;	//非严格模式下发现错误时，收集到指定个数的错误才一起报错
	this.selector = selector || "form";	//绑定的jQuery选择器
	this.eventName = eventName || "submit";
	this.isRegister = false; //是否已经向表单注册提交事件
	this.array = new Array();	//需要验证的对象数组
	this.msgMode = true;	//消息模式，如果为true，遇错则报错，如果为false，就全部校验，最后一起报错
	this.dateFormat;	//日期格式
	this._notAuth = false;
	//验证器调度执行方法
	this.validate = function(event){
		if(this._notAuth) return false;
		var globalMsg = "";	//全局提示
		var counter = 0;
		var $first;
		var notFirst = true;
		var item = null;	//单个验证项
		var msg = null;		//单个提示信息 
		var $id = null;
		for(i in current.array){
			item = current.array[i];
			$id = jQuery(item.id);
			var value = jQuery.trim($id.val());
			switch(item.type){	//判断验证项类型
				case "isRequire":
					if(!value){
						msg = "[" + item.name + "]不能为空！";
					}
					break;
				case "isNumber":
					if((!item.canEmpty || value) && !jQuery.isNumber(value, item.intOnly)){
						msg = "[" + item.name + "]格式不正确，必须为"+ (item.intOnly ? "整数" : "小数或整数") +"格式！";
					}
					break;
				case "isDate":
					if((!item.canEmpty || value) && !jQuery.isDate(value, item.format)){
						msg = "[" + item.name + "]格式不正确，必须为"+ item.format +"格式！";
					}
					break;
				default:
					msg = null;
					break;
			
			}
			if(msg){	//如果有错误消息
				if(current.msgMode){	//如果是严格的消息模式，遇错就报错
					if(event) event.preventDefault();
					alert(msg);
					$id.focus();
					return false;
				}else{	//非严格模式，达到指定数量的提示信息后才报错
					globalMsg += msg + "\n";
					counter++;
					if(notFirst){
						$first = $id;
						notFirst = false;
					}else if(counter == current.count){						
						if(event) event.preventDefault();
						alert(globalMsg);
						$first.focus();
						return false;
					}
				}
			}
		}
		if(!current.msgMode && globalMsg){	//如果所有验证都已验证完毕，如果是非严格模式，没有达到指定数量，但是仍然有错误消息，仍然要提示
			if(event) event.preventDefault();			
			alert(globalMsg);
			$first.focus();
			return false;
		}
		if(jQuery.isFunction(current.callback) && current.callback() === false){
			return false;			
		}
		return true;
	};
	//
	//以单例方式扩展相关函数
	//
	if(typeof this.isRequire != "function"){
		/*
		 *注册事件 
		 */
		V.prototype.regEvent = function(){
			if(!this.isRegister){	//如果未注册事件就注册事件
				jQuery(document.body).delegate(this.selector, this.eventName, this.validate);
				this.isRegister = true;
			}			
		};
		/*
		 *非空验证器
		 */
		V.prototype.isRequire = function(str){
			if(this._notAuth) return false;
			if(jQuery.trim(str)){	//内容不为空
				this.regEvent();
				var sArray = str.split(/=|;/);
				var theLength = sArray.length;
				if(theLength == 0 || theLength % 2 != 0){
					this._notAuth = true;
					logError("非空验证器isRequire的验证表达式参数设置不正确，表达式格式应该形如：“#name=姓名;#password=密码”！");
					return false;
				}
				for(var x = 0; x < theLength; x++){
					var sObj = {"id" : sArray[x], "name" : sArray[++x], "type" : "isRequire"};
					this.array.push(sObj);				
				}
			}		
		};
		/*
		 * 整数、小数格式验证器
		 */
		V.prototype.isNumber = function(str, integerOnly, canEmpty){
			if(this._notAuth) return false;
			if(integerOnly !== false){
				integerOnly = true;
			}
			canEmpty = canEmpty || false;
			if(jQuery.trim(str)){	//内容不为空
				this.regEvent();
				var strArray = str.split(/=|;/);
				var length = strArray.length;
				if(length == 0 || length % 2 != 0){
					this._notAuth = true;
					logError("数字验证器isNumber的验证表达式参数设置不正确，表达式格式应该形如：“#name=姓名;#password=密码”！");
					return false;
				}
				for(var i = 0; i < length; i++){
					var sObj = {"id" : strArray[i], "name":strArray[++i], "type":"isNumber", "intOnly":integerOnly, "canEmpty" : canEmpty};				
					this.array.push(sObj);
				}
			}
		};
		/*
		 *整数、小数格式验证器，如果为空，则不验证
		 */
		V.prototype.opNumber = function(str, integerOnly){
			this.isNumber(str, integerOnly, true);	
		};
		/*
		 * 日期、时间格式验证器
		 */
		V.prototype.isDate = function(str, formatStr, canEmpty){	
			formatStr = formatStr || this.dateFormat || "yyyy-MM-dd";
			canEmpty = canEmpty || false;		
			if($.trim(str)){	//内容不为空
				this.regEvent();
				var strArray = str.split(/=|;/);
				var length = strArray.length;
				if(length == 0 || length % 2 != 0){
					logError("日期验证器isDate的验证表达式参数设置不正确，表达式格式应该形如：“#name=姓名;#password=密码”！");
					return;
				}else{
					for(var i = 0; i < length; i++){
						var sObj = {id : strArray[i], name : strArray[++i], type :"isDate", format:formatStr, "canEmpty" : canEmpty};
						this.array.push(sObj);
					}
				}
			}
		};
		/*
		 * 日期、时间格式验证器，如果为空，则不验证 
		 */
		V.prototype.opDate = function(str, formatStr){
			this.isDate(str, formatStr, true);
		};		
	}
};
//默认拦截当前页面的所有表单的提交事件进行验证，如只针对某个表单(例如表单ID为loginForm)验证，直接在js代码中加入如下代码：
// V = new V("#loginForm");  //覆盖默认验证器V
//或者
// var V1 = new V("#loginForm"); //新建一个验证器V1
//(接收的参数不只是表单ID，只要符合JQuery选择器规则并返回指定JQuery表单对象的参数均可)

//如果需要绑定click等其他事件，只需添加如下类似代码：
// $("#id").click(V.validate);
//
//在执行完以上验证后如果还想执行其他自定义的验证，可以自行编写after()函数
// V.after=function(){
//		//其他验证代码，return false即可阻止事件的执行
// }
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
				logError("$.fixFloat()方法的参数对象不能同时具有left和right属性！");
				return;
			}
			if(location.top && location.bottom){
				logError("$.fixFloat()方法的参数对象不能同时具备top和bottom属性！");
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


function Table(title, titleSetting, pageObj){
	if(typeof this.init != "function"){		
		Table.prototype.init = function(title, titleSetting, pageObj){
			this._caption = title || this._caption || "表格";		//表格标题
			this._titleSetting = titleSetting || this._titleSetting;		//表头标题表达式
			//检测标题设置
			if(typeof this._titleSetting != "string" || !this._titleSetting){
				this._notAuth = true;
				logError('创建表格对象时，必须传入标题设置参数，例如："name=姓名;age=年龄;sex=性别"');
				return false;
			}
			//检测pageObj对象
			if(!pageObj || typeof pageObj != "object"){
				pageObj = {};	//没有传递数据，默认为空			
			}
			if(!Table.size){	//Table.size为计算当前页面累计生成的表格数量的计数器
				Table.size = 0;
				Table._Name = "Table";	//表格对象变量的字面值
				//以下是生成HTML元素的Id、name、class命名的前缀，用于Id的属性首字母大写，用于name的属性全部小写，用于class的属性全部小写
				//所有命名都以UI/ui开头
				//Id和name的属性命名中都使用"_"分隔，class的命名中都使用"-"分隔
				Table._Id = "UI_Table";	//表格ID
				Table._checkbox = "ui_checkbox";	//表格复选框name
				Table._Widget = "UI_Widget";	//表格分页条ID
				Table._PageId = "UI_PageId";	//表格分页条的页面跳转文本框的ID
				Table._Action = "UI_Action";	//表格全局操作栏的ID
			}
			if(isNaN(this.index)){												
				this.index = Table.size++;
				Table[this.index] = this;
			}
			this._name = Table._Name + "[" + this.index + "]";	//当前表格对象的字面值
			this._data = pageObj.list || [];		//用于加载表格的数据
			this._id = parseInt(pageObj.id) || 1;	//分页数据的当前页数
			this._size = parseInt(pageObj.size) || 0;	//每页显示的记录数
			this._count = parseInt(pageObj.count) || 0;	//符合查询条件的数据总记录数
			this._orderBy = pageObj.orderBy || "";
			this._args = pageObj.args || "";
		};
	}
	if(this.init(title, titleSetting, pageObj) == false){
		return;
	};
	this.actionName = "操作";		//【操作栏】的标题名称
	this.tips = "查询不到符合条件的记录!";	////当没有数据时显示的提示内容
	this.hasRowNumber = true;		//指示是否有行号
	this.ajaxMode = false;			//指示是否启用Ajax模式
	this.link = null;			//页面跳转的链接地址，默认取当前地址
	this._rowTemplate = {};			//特殊模板，类似 {'name' : function(obj){}, 'age' : function(obj){}}
	this._rowHidden = new Array();	//行隐藏表单数组
	this._rowAction = new Array();	//行操作栏数组
	this._action = new Array();		//全局操作栏数组
	this._sizeArray = [10, 20, 50, 100];	//用于每页显示条数的下拉框值数组，默认选中第一个元素
	
	if(typeof this.actionButton != "function"){
		//全局操作栏按钮
		Table.prototype.actionButton = function(btnName, callback, needValidate){
			if(needValidate != false){
				needValidate = true;	//默认为true
			}
			this._action.push( {"name" : btnName, "callback" : callback, "isNeed" : needValidate} );
		};
		//应用于每一行的某个列的特殊模板
		Table.prototype.rowTemplate = function(columnName, temp){
			this._rowTemplate[columnName] = temp;
			this._rowTemplate.__hasP__ = true;	//rowTemplate中存在属性
		};
		//【操作栏】链接
		Table.prototype.rowLink = function(linkName, href){		
			this._rowAction.push({"name" : linkName, "href" : href});
		};
		//【操作栏】按钮
		Table.prototype.rowButton = function(actionName, callback){
			this._rowAction.push({"name" : actionName, "callback" : callback});
		};
		//隐藏指定的列
		Table.prototype.rowHidden = function(){
			var length = arguments.length;
			for(var i=0; i< length; i++){			
				this._rowHidden.push(arguments[i]);
			}
		};
		//将指定列的对应值转为对应的值显示
		Table.prototype.rowDecode = function(){
			var length = arguments.length;
			if(length < 2){
				logError("rowDecode函数至少需要传入2个参数。");
			}else{
				var args = arguments;
				var columnName =args[0]; 
				this.rowTemplate(columnName, function(obj){
					var value = obj[columnName];	//实际的值
					var lastLength = (length & 1) == 0 ? length - 1 : length;
					for(var i = 1; i < lastLength; i += 2){
						if( value == args[i]){	//如果实际的值等于指定的值
							return args[i+1];					
						}
					}
					return lastLength == length ? value : args[lastLength];
				});
			}
		};	
		//设置分页条数下拉框的值
		Table.prototype.pageSize = function(){
			var length = arguments.length;
			if(length > 0){
				var num = arguments[0];
				if(length == 1){
					if(this._size <= 0 || num <= 0){
						this._size = num;
					}
				}else{
					this._sizeArray = arguments;
					if(this._size <= 0){
						this._size = num;
					}
				}
			}
		}
		//点击行【操作栏】按钮时执行的代理函数
		Table.prototype.run = function(rowIndex, actionIndex){
			var callback = this._rowAction[actionIndex].callback;
			var obj = this._data[rowIndex];
			callback.call(obj, obj, rowIndex);
		};
		//全局【操作栏】按钮执行的代理函数
		Table.prototype.RUN = function(actionIndex){
			var action = this._action[actionIndex];
			var data = this._data;
			var selected = [];
			$('input[name="' + Table._checkbox + this.index + '"]:checked').each(function(){
				var index = this.value;
				selected.push(data[index]);			
			});
			if(action.isNeed && selected.length == 0){
				alert("没有选中任何数据！");
				return;
			}
			action.callback(selected);
		};
		//全选复选框执行的代理函数
		Table.prototype.selectAll = function(checkDom){
			$('input[name="' + Table._checkbox + this.index + '"]').attr("checked", checkDom.checked);
		};
		//获取表格分页组件
		Table.prototype.getTableWidget = function(){
			var widget = "";
			var name = this._name;
			var index = this.index;
			var id = this._id;
			var size = this._size;
			if(size > 0 && this._data.length > 0){
				//如果开启了分页，并且有数据记录，则加载数据分页工具条
				widget += '<div id="' + Table._Widget + index + '" class="ui-table-widget">';
				//首页+上一页
				if(id > 1){
					widget += '<a href="javascript:;" onclick="return ' + name + '.goPage(1);">首页</a>';
					widget += '<a href="javascript:;" onclick="return ' + name + '.goPage('+ (id - 1) +');">上一页</a>';
				}else{
					widget += '<span>首页</span><span>上一页</span>';
				}
				
				//下一页、尾页
				var maxPage = Math.ceil(this._count / size);
				var space = '<span class="ui-space">;</span>';
				if(maxPage > id){
					widget += '<a href="javascript:;"  onclick="return ' + name + '.goPage('+ (id + 1) +');">下一页</a>';
					widget += '<a href="javascript:;" onclick="return ' + name + '.goPage('+ maxPage +');">尾页</a>';
				}else{
					widget += '<span>下一页</span><span>尾页</span>';
				}
				//跳转到+每页显示条数
				widget += '<input class="ui-button" type="button" value="跳转到" onclick="' + name + '.goPage()" />第<input class="ui-text" id="' + Table._PageId + this.index + '" type="text" />页' + space;
				widget += '每页显示<select onchange="' + name + '.changeSize(this)">';
				var sizeArray = this._sizeArray;
				for(var i in sizeArray){
					if(sizeArray[i] == size){
						widget += '<option value="'+ sizeArray[i] +'" selected="selected">' + sizeArray[i] + '</option>';					
					} else{
						widget += '<option value="'+ sizeArray[i] +'">' + sizeArray[i] + '</option>';				
					}
				}
				widget += '</select>条' + space;
				//当前页+总页数
				widget += '当前页/总页数：';
				widget += id + '/' + maxPage +space;
				widget += '总记录数：' + this._count + space;
				widget += '</div>';
			}
			return widget;			
		};
		//智能固定表格标题列的代理函数
		Table.prototype.fixHead = function(){
			//表格索引，//表格ID
			var index= this.index, tabId = Table._Id + index;
			$(function(){
				var $tab = $("#" + tabId);
				if($tab.height() > $(window).height()){
					//如果表格高度大于窗口的高度
					var $head = $tab.find("tr.tr-head");
					var p = $head.position();
					var copyTable = $('<table id="UI_Table_Copy' + index + '" class="ui-table"></table>');
					copyTable.append($head.clone().attr("id", "UI_Tr_Head_Copy" + index)).appendTo("body");
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
					var $src = $("#" + tabId + " .tr-head :checkbox");
					var $copy = $("#UI_Tr_Head_Copy" + index + " :checkbox");
					var isHidden = true;
					$(window).scroll(function(){
						var top = document.body.scrollTop || document.documentElement.scrollTop;
						if(top > p.top){
							if(isHidden){
								$copy.attr("checked", $src.attr("checked"));
								isHidden = false;
								copyTable.show();
							}
						}else{
							if(!isHidden){
								$src.attr("checked", $copy.attr("checked"));
								isHidden = true;
								copyTable.hide();
							}
						}
					});
				}
			});		
		};
		//选中当前行的复选框
		Table.prototype.selectCurrentRow = function(){
			var selector = "#" + Table._Id + this.index + " tr";
			var checkBox = "[name='" +  Table._checkbox + this.index + "']";
			jQuery(document.body).delegate(selector, "click", function(){
				var $checkBox = jQuery(this).find(checkBox);
				$checkBox.attr("checked", !$checkBox.attr("checked"))
			});			
		};
		//更改每页显示条数的代理函数
		Table.prototype.changeSize = function(textDOM){
			this.goPage(1, textDOM.value);
		};
		//以Ajax方式加载数据
		Table.prototype.ajaxLoad = function(selector, url){
			url = url || this.link || (self.location.host + self.location.pathname);
			if(!this.link) this.link = url;
			if(!selector){	//如果没有选择器
				selector = this._selector;
			}else if(!this._selector){	//如果指定了选择器并且this的选择器为空
				this._selector = selector
			}
			var data = jQuery.AJAX(url);
			var json = null;
			try{
				json = eval("(" + data + ")");				
			}catch(e){
				logError("无法正确解析指定的JSON格式字符串！请检查链接“" + url + "”响应的数据是否为JSON格式！");
				return;
			}
			this.init(null, null, json);
			this.show(selector);
		};
		
		//翻页操作的代理函数
		Table.prototype.goPage = function(pageId, pageSize){
			if(!pageId){	//如果没有传递参数进来，则表示是点击跳转按钮的执行方法
				var value = $.trim($("#" + Table._PageId + this.index).val());
				pageId = parseInt(value); 
				if(!pageId){
					alert("跳转页数必须为数字！");
					return false;
				}
				var maxPage = this._count / this._size;
				maxPage = Math.ceil(maxPage);	//向上取整
				if(pageId > maxPage){
					pageId = maxPage; 
				}			
				if(pageId < 1){
					pageId = 1;				
				}
			}
			pageSize = parseInt(pageSize) || this._size;
			var url = this.link ? this.link : (this.link = self.location.host + self.location.pathname);
			var hasParam = false;
			if(this._args){
				url += '?' + this._args;
				hasParam = true;
			}
			if(this._orderBy){
				url += (hasParam ? '&' : '?') + 'page.orderBy=' + this._orderBy;
				hasParam = true;
			}
			url += (hasParam ? '&' : '?') + 'page.id=' + pageId + '&page.size=' + pageSize; 
			if(this.ajaxMode){
				this.ajaxLoad(null, url);
			}else{				
				self.location.href = url;
			}
			return false;
		};
		//加载显示
		Table.prototype.show = function(selector){
			if(!selector){
				selector = this._selector;
			}else{
				this._selector = selector;
			}
			if(!selector){
				logError("调用show()方法必须提供用于加载表格内容的jQuery选择器参数！");
				return;
			}
			//标题设置数组
			var array = this._titleSetting.split(/=|;/), actions = this._action, rowActions = this._rowAction, rowActions = this._rowAction;
			//标题设置数组长度，//行【操作栏】的操作列数，//是否有复选框
			var length = array.length, actionLength = rowActions.length, hasCheckBox = actions.length > 0;
			if(length == 0 || (length & 1) == 1){
				logError('表格对象传入的标题设置参数格式不正确，应该采用如下格式："name=姓名;age=年龄;sex=性别"');
				return;
			}
			var titleArray = [];	//标题栏
			for(var i=0; i < length; i++){
				titleArray.push( {"name" : array[i], "title" : array[++i]} );
			}
			length = titleArray.length;
			//包含序号列、操作列在内的所有列数，//是否有序号列
			var realSize = length + actionLength, hasRowNO = this.hasRowNumber;	
			var isPrefix = hasCheckBox || hasRowNO;
			if(isPrefix){
				realSize++;
			}
			var tableHtml = '<table id="' + Table._Id + this.index + '" class="ui-table">';
			////////////////////////////////////////////
			//表格标题
			tableHtml += '<caption>' + this._caption + '</caption>';
			///////////////////////////////////////////
			//表格列标题
			tableHtml += '<tr class="tr-head">';
			var tabName = this._name;
			if(hasCheckBox){	//如果有复选框
				tableHtml += '<th class="th-first"><input type="checkbox" value="0" onclick="' + tabName + '.selectAll(this)" />全选</th>';
			}else if(hasRowNO){	//如果无复选框+有序号
				tableHtml += '<th class="th-first">序号</th>'; 
			}
			for(var i = 0; i < length; i++){	//列标题
				tableHtml += '<th class="th-' + titleArray[i].name + '">' + titleArray[i].title + '</th>';
			}
			if(actionLength > 0){	//操作栏标题
				tableHtml += '<th class="th-action" colspan="'+ actionLength +'">' + this.actionName + '</th>';
			}
			tableHtml += "</tr>";
			//////////////////////////////////////////
			//表格正文内容
			/////////////////////////////////////////
			var dataObj = this._data;
			var isArray = jQuery.isArray(dataObj);
			if(!isArray){	//如果不是数组对象
				var objType = typeof(dataObj);
				if(objType == "object"){	//如果是单个对象，将其封装为数组对象
					dataObj = this._data = [dataObj];
					isArray = true;
				}else if(objType == "string"){
					this.tips = dataObj;
				}
			}
			if(isArray && dataObj.length > 0){	//如果是数组，并且有元素
				//记录数，是否是奇数列，复选框的checkbox的name
				var size = dataObj.length, isOdd = false, checkBox = Table._checkbox + this.index;	//记录数	
				for(var i=0; i < size; i++){
					tableHtml += '<tr class="' + ((isOdd = !isOdd) ? 'odd' : 'even') + '">';	//tr class奇偶判断
					if(isPrefix){
						tableHtml += '<td class="td-first">'
					}
					if(this.hasRowNumber){	//如果有序号
						tableHtml += (i + 1);
					}
					if(hasCheckBox){
						tableHtml += '<input name="' + checkBox + '" type="checkbox" value="' + i + '" />';
					}
					if(isPrefix){
						tableHtml += '</td>';
					}
					var obj = dataObj[i];	//单条数据
					var rowTemplates = temp = this._rowTemplate;
					var hasTemplate = "__hasP__" in rowTemplates;
					for(var j=0; j < length; j++){//循环列
						var name = titleArray[j].name;	//列名
						var text = null;	//表格内容
						var style = "";
						if(hasTemplate){	//如果有特殊模板
							var temp = rowTemplates[name];
							if(jQuery.isFunction(temp)){	//如果是函数
								text = temp(obj);
								if(text == null){	//如果有为null的值，一律转为空字符串
									text = "&nbsp;";
								}else if(jQuery.isArray(text)){	//如果设置了单元格的其他属性
									if(text[1]){
										style = " " + text[1] + " ";									
									}
								}
							}
						}
						if(text == null){	//如果没有特殊模板或特殊模板配置有误
							text = obj[name];	//取实际值
							if(text == null){//如果有为null的值，一律转为空格字符串
								text = "&nbsp;";
							}
						}
						if(j == 0){//如果有隐藏文本域，则全部放在有效列的第一列
							var rowHiddens = this._rowHidden;
							var hiddenSize = rowHiddens.length;
							if(hiddenSize > 0){
								for(var k=0; k < hiddenSize; k++){
									var hiddenName = rowHiddens[k];
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
							var action = rowActions[l];
							if(action.href){	//如果是链接操作
								var href = action.href.call(obj, obj);
								if(href != null){
									tableHtml += '<td class="td-action"><a href="' + href + '">' + action.name + '</a></td>';								
								}else{
									tableHtml += '<td class="td-action">-</td>';
								}
							}else if(action.callback){	//如果是按钮操作
								tableHtml += '<td class="td-action"><input class="ui-button" type="button" value="' + action.name + '" onclick="' + tabName + '.run('+ i +',' + l +')" /></td>';
							}
						}
					}
					tableHtml += '</tr>';				
				}
			}else {
				//如果是字符串、null、undefined、空数组等其他类型
				tableHtml += '<tr><td colspan="' + realSize+ '">'+ this.tips +'</td></tr>';		
			}
			tableHtml += '</table>';
			tableHtml += this.getTableWidget();
			if(hasCheckBox){
				//如果有复选框操作
				var div = '<div id="' + Table._Action + this.index + '" class="ui-table-action">';
				for(var x in actions){
					var action = actions[x];
					div += '<input class="ui-button" type="button" value="'+ action.name +'" onclick="' + tabName + '.RUN('+ x +')" />';
				}
				div += "</div>";
				tableHtml += div;
			}
			$(selector).html(tableHtml);
			if(checkBox && this.rowSelected){	//如果有复选框
				this.selectCurrentRow();
			}
			this.fixHead();
		};
	}	
}

// UI组件
var UI = {
	backTop : function(html){
		if(!html) html = '<a id="UI_BackTop" class="ui-backtop" title="返回顶部" >返回顶部</a>'; 
		$(html).appendTo(document.body).backTop();
	},
	table : function(title, titleSetting, pageObj){
		return new Table(title, titleSetting, pageObj);
	}
};

