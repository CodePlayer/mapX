<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="easymapping.util.SessionUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">    
    <title>用户退出</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="css/ui.css" /> 
	<style type="text/css">
	.demo{height:800px; }
	</style>   
	</head>
  <body>
  <h2>用户退出</h2>
  <%SessionUtil.removeUser(); %>
  </body>
</html>
