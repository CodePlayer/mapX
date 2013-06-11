<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
  <head>
  	<base href="<%=basePath%>">
    <title>系统提示</title>    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" type="text/css"  href="css/error.css">
  </head>
<body>
<div class="wrapper">
	<h5>系统提示</h5>
	<div class="content">
		<div class="message">
			<div class="icon error">&nbsp;</div>
			<div class="text">对不起，找不到您指定的页面！
			请检查您的网址是否输入正确。</div>
		</div>
	</div>
	<div class="foot">
		<input id="btnBack" class="btn" type="button" value="返回"  onclick="history.back();"/>
	</div>
</div>
</body>
</html>
