/*
 * 功能说明：JS公共工具方法
 * 此JS文件依赖于JQuery，在引入此文件前，必须先引入JQuery文件
 * 所有方法的使用说明参见方法的相关注释
 *  作者：Ready
 *  时间：2012-05-21
 */

/**
 * 验证指定字符串是否是日期格式<br />
 * 如果验证通过则返回true
 * @param {String} str 指定字符串
 * @param {String} format 指定日期格式，不区分大小写。如："yyyy-mm-dd"、"yyyyMM"、"yyyy"。无此参数，默认为"yyyymmdd"
 * @return {Boolean} 
 */
function isDate(str, format){
	str = $.trim(str);	//内部去空格，如需变动，请自行修改
	if(!str)	//如果为null、""、空格字符串则返回false
		return false;
	format = format || "yyyymmdd";	//没有format参数，默认为yyyymmdd格式
	format = format.toLowerCase();
	var regStr = format.replace(/yyyy/,"(19\\d{2}|2\\d{3})");	//1900-2999，如需改动范文，请自行修改
	regStr = regStr.replace(/mm/,"(0[1-9]|1[0-2])");	//01-12
	regStr = regStr.replace(/dd/,"(0[1-9]|[1-2]\\d|3[0-1])");	//01-31，验证相对宽松，如需严格验证，请自行修改
	regStr = "/^"+ regStr +"$/"
	var reg = eval(regStr);
	return reg.test(str);
}

/**
 * 判断对象是否为空，如果为null、空字符串、空格字符串，则返回true
 * @param {String} str
 * @return {Boolean} 
 */
function isEmpty(str){
	return !$.trim(str);
}

/**
 * 执行ajax操作
 * @param {String} url 请求路径
 * @param {Object} param 请求参数(支持字符串格式或json对象格式)<br />
 * 字符串格式例如:"a=hello&b=12&c=yes" <br />
 * json格式例如:{a:"hello", b:12, c:"yes"}
 * @return {String} 
 */
function ajax(url, param){
	var result;
	$.post(url,param,function(data){
		result = data;
	});	
	return result;
}
/**
 * 验证指定字符串是否为整数或小数形式,如果是，则返回true
 * @param {String} str 指定字符串
 * @param {Boolean} intOnly 如果为true，则只验证是否为整数；为false，则验证是否为小数形式；默认为true
 * @return {Boolean} 
 */
function isNumber(str, intOnly){
	str = $.trim(str);
	if(intOnly != false)
		intOnly = true;	
	var regObj = intOnly ? /^\d+$/ : /^\d+(\.\d+)?$/;
	return regObj.test(str);		
}

//设置Ajax请求以同步方式执行
$.ajaxSetup({async:false});
$("body").ajaxError(function(e, request, setObj, ex) { //ajax请求异常，弹出提示信息
			alert("发送请求到" + setObj.url + "失败：" + ex);
});

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
	this.msgMode = false;	//消息模式，如果为true，遇错则报错，如果为false，就全部校验，最后一起报错
	this.dateFormat;	//日期格式
	this.after;	//回调函数，执行完验证继续执行的函数
	
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
		if(integerOnly != false){
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
				for(var i = 0; i < length; i = i+2){
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
				if((!item.canEmpty || value) && !isNumber(value, item.intOnly)){
					msg = "[" + item.name + "]格式不正确，必须为"+ (item.intOnly ? "整数" : "小数或整数") +"格式！";
				}
			}else if(type == "isDate"){
				if((!item.canEmpty || value) && !isDate(value, item.format)){
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
		if($.isFunction(self.after) && self.after() == false){
			if(event) event.preventDefault();
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