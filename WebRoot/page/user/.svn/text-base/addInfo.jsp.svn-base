<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>添加用户页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">	
  	<link rel="stylesheet" href="css/ui.css" type="text/css" />
  	</head>  
  <body>
<h1>添加用户页面</h1>
<div id="user">
	<form action="user/addInfo.htm" method="post">
	姓名：<input id="name" name="item.name" type="text" /><br/>
	密码：<input id="password" name="item.password" type="text" /><br/>
	年龄：<input id="age" name="item.age" type="text" /><br/>
	性别：<select id="gender" name="item.gender">
		<option value=""></option>
		<option value="1">男</option>
		<option value="0">女</option>
	</select><br/>
	备注：<input id="remark" name="item.remark" type="text" /><br/>
	<input id="btn" type="submit" value="添加" /> 
	</form>
</div>
<jsp:include flush="false" page="/page/common/js.jsp" />
<script type="text/javascript">
V.isRequire("#name=姓名;#password=密码");
V.isNumber("#age=年龄");
V.isRequire("#gender=性别;#remark=备注");
</script>
  </body>
</html>
