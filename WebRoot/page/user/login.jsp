<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">    
    <title>用户登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="css/ui.css" /> 
	<style type="text/css">
	.demo{height:800px; }
	</style>   
	</head>
  <body>
  <h2>用户登录</h2>
  <form id="loginForm" action="user/login.htm" method="post" >
  	用户名：<input id="username" name="j_username" type="text"><br/>
  	密码：<input id="password" name="j_password" type="password"><br/>
  	<input id="btnLogin" type="submit" value="登录">  	
  </form>
  <jsp:include page="/page/common/js.jsp"></jsp:include>
  <script type="text/javascript">
  	V.isRequire("#username=用户名;#password=密码");
  </script>
  </body>
</html>
