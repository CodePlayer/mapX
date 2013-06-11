
//存放已注册的button的关联对象
var nemedButtons = {
	"back" : '<input class="btn" type="button" value="返回" onclick="history.back()" />',
	"close" : '<input class="btn" type="button" value="关闭" onclick="closeWin()" />'
};

//返回指定命名注册的按钮html
function getNamedButton(name){
	return 	nemedButtons[name];
}
//返回跳转到指定URL的按钮html
function getForwardButton(value, url){
	return '<input class="btn" type="button" value="' + value + '" onclick="$.sendPost(\'' + url + '\')" />';
}
//返回执行指定函数的html
function getFunctionButton(value, functionName){
	return '<input class="btn" type="button" value="' + value + '" onclick="' + functionName + '" />';
}
