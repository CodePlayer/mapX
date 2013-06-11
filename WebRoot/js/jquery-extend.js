/* ****************************************************************
 * 功能模块：扩展JQuery的静态方法、实例方法，以便于开发人员进行JS操作，尤其是表单验证。 
 * 作者：Ready
 * 时间：2012-05-31 
 ******************************************************************/
/*  设置jQuery ajax全局参数   */
//设置ajax请求以同步方式执行
jQuery.ajaxSetup({async:false});
jQuery("body").ajaxError(function(e, request, setObj, ex) { //ajax请求异常，弹出提示信息
			alert("发送请求到" + setObj.url + "失败：" + ex);
});

//打印或记录错误信息
function logError(msg){
	alert(msg);
}

//兼容IE6-8、无弹出提示框的关闭窗口函数
function closeWin(){
	window.opener = null;
	window.open("", "_self");
	window.close();
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
			var regObj = length == 0 ? /^\d+$/ : eval("/^\\d{" + length + "}$/");
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
			var regObj = eval(regStr);
			return regObj.test(str);
		},
		//直接返回Ajax请求返回的数据
		AJAX : function(url, args){
			var result;
			jQuery.post(url, args, function(data){
				result = data;
			});	
			return result;
		},
		//动态创建FORM，并以POST方式发送请求
		//如果URL为null，则默认发送至不带参数的当前页面URL
		sendPost : function(url, args ){
			url = url || self.location.href.split(/\?/)[0];
			var f = $('<form action="' + url + '" method="POST"></form>');
			if(args && typeof args == "object"){
				var $input;
				var value;
				for(var i in args){
					value = args[i];
					$input = $('<input name="' + i + '" type="hidden" />')
					if(value == null || value == undefined){
						value = "";
					}
					$input.val(value);//此处采用动态赋值，以防止特殊字符被转义
					f.append($input);
				}
			}
			f.appendTo(document.body);
			f.submit();
		},
		//动态创建FORM，并以GET方式发送请求
		//如果URL为null，则默认发送至不带参数的当前页面URL
		sendGet : function(){
			url = url || self.location.href.split(/\?/)[0];
			var f = $('<form action="' + url + '" method="GET"></form>');
			if(args && typeof args == "object"){
				var $input;	var value;
				for(var i in args){
					value = args[i];
					$input = $('<input name="' + i + '" type="hidden" />')
					if(value == null || value == undefined){
						value = "";
					}
					$input.val(value);//此处采用动态赋值，以防止特殊字符被转义
					f.append($input);
				}
			}
			f.appendTo(document.body);
			f.submit();
		}
	});
	//扩展jQuery实例方法
	jQuery.fn.extend({
		//非空验证，如果为空返回true
		isEmpty : function(msg) {
			var isEmpty = jQuery.isEmpty(msg);
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
			var notNumber = jQuery.isNumber();
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
			var regExp = eval(regStr);
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
function V(formId){
	var self = this;	
	this.count = 5;
	this.formId = formId || "form";
	this.isRegister = false; //是否已经向表单注册提交事件
	this.array = new Array();	//需要验证的对象数组
	this.msgMode = true;	//消息模式，如果为true，遇错则报错，如果为false，就全部校验，最后一起报错
	this.dateFormat;	//日期格式
	
	/**
	 * 非空验证器
	 * @param {String} str
	 * @memberOf {TypeName} 
	 */
	this.isRequire = function(str){
		if($.trim(str)){	//内容不为空
			if(!this.isRegister){	//如果未注册事件就注册事件
				$(this.formId).submit(this.validate);
				this.isRegister = true;
			}
			var strArray = str.split(";");
			for(x in strArray){				
				var sArray = strArray[x].split("=");				
				var sObj = {id:sArray[0], name:sArray[1], type : "isRequire"};				
				this.array.push(sObj);				
			}			
		}		
	};
	/**
	 * 整数、小数格式验证器
	 * @param {String} str
	 * @param {Boolean} integerOnly
	 */
	this.isNumber = function(str, integerOnly, canEmpty){
		if(integerOnly !== false){
			integerOnly = true;
		}
		canEmpty = canEmpty || false;
		if($.trim(str)){	//内容不为空
			if(!this.isRegister){	//如果未注册事件就注册事件
				$(this.formId).submit(this.validate);
				this.isRegister = true;
			}
			str = str.replace(/;/g, "=");
			var strArray = str.split("=");
			var length = strArray.length;
			if(length == 0 || length % 2 != 0){
				alert("ERROR：数字验证器表达式格式不正确！");
				return;
			}else{
				for(var i = 0; i < length; i = i+2){
					var sObj = {id : strArray[i], name:strArray[i+1], type:"isNumber", intOnly:integerOnly, "canEmpty" : canEmpty};				
					this.array.push(sObj);
				}
			}						
		}
	};
	/**
	 * 整数、小数格式验证器，如果为空，则不验证
	 * @param {Object} str
	 * @param {Object} integerOnly
	 * @memberOf {TypeName} 
	 */
	this.opNumber = function(str, integerOnly){
		this.isNumber(str, integerOnly, true);	
	};
	
	/**
	 * 日期、时间格式验证器
	 * @param {Object} str
	 * @param {Object} formatStr
	 * @memberOf {TypeName} 
	 */
	this.isDate = function(str, formatStr, canEmpty){	
		formatStr = formatStr || this.dateFormat || "yyyy-MM-dd";
		canEmpty = canEmpty || false;		
		if($.trim(str)){	//内容不为空
			if(!this.isRegister){	//如果未注册事件就注册事件
				$(this.formId).submit(this.validate);
				this.isRegister = true;
			}
			str = str.replace(/;/g, "=");
			var strArray = str.split("=");
			var length = strArray.length;
			if(length == 0 || length % 2 != 0){
				alert("ERROR：日期验证器表达式格式不正确！");
				return;
			}else{
				for(var i = 0; i < length; i = i + 2){
					var sObj = {id : strArray[i], name : strArray[i+1], type :"isDate", format:formatStr, "canEmpty" : canEmpty};
					this.array.push(sObj);
				}
			}
		}
	};
	this.opDate = function(str, formatStr){
		this.isDate(str, formatStr, true);
	};
	
	//验证器调度执行方法
	this.validate = function(event){		
		var globalMsg = "";	//全局提示
		var counter = 0;
		var $first;
		var notFirst = true;
		for(i in self.array){
			var item = self.array[i];
			var id = item.id;
			var type = item.type;
			var $id = $(id);
			var value = $.trim($id.val());
			var msg = "";
			if(type == "isRequire"){				
				if(!value){
					msg = "[" + item.name + "]不能为空！";
				}
			}else if(type == "isNumber"){
				var a = (!item.canEmpty || value);
				var b = !jQuery.isNumber(value, item.intOnly);
				if((!item.canEmpty || value) && !jQuery.isNumber(value, item.intOnly)){
					msg = "[" + item.name + "]格式不正确，必须为"+ (item.intOnly ? "整数" : "小数或整数") +"格式！";
				}
			}else if(type == "isDate"){
				if((!item.canEmpty || value) && !jQuery.isDate(value, item.format)){
					msg = "[" + item.name + "]格式不正确，必须为"+ item.format +"格式！";
				}
			}
			if(msg){
				if(self.msgMode){
					if(event) event.preventDefault();
					alert(msg);
					$id.focus();
					return false;
				}else{
					globalMsg += msg + "\n";
					counter++;
					if(notFirst){
						$first = $id;
						notFirst = false;
					}else if(counter == self.count){						
						if(event) event.preventDefault();
						alert(globalMsg);
						$first.focus();
						return false;
					}
				}
			}
		}
		if(!self.msgMode && globalMsg){
			if(event) event.preventDefault();			
			alert(globalMsg);
			$first.focus();
			return false;
		}
		return true;
	}
};
var V = new V();
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
