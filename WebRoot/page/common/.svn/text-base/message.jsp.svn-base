<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="easymapping.util.X"%>
<%@page import="easymapping.util.btn.Button"%>
<%@page import="easymapping.core.LogicException"%>
<%@page import="easymapping.core.Messager"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	Exception e = (Exception)request.getAttribute("exception");
	String msg = null;
	Button[] buttons = null;
	String icon = null;
	if(e != null){
		msg = e.getMessage();
		icon = Messager.ERROR;
		LogicException.log.error(msg, e);
		buttons = new Button[]{ Button.BACK, Button.CLOSE };
	}else{
		Messager messager = (Messager)request.getAttribute("messager");
		if(messager != null){
			icon = messager.getIcon();
			buttons = messager.getButtons();
			msg = messager.getMessage();
		}
	}
	if(X.isEmpty(msg)){
		msg = "系统错误，请与系统管理员联系！";
	}
%>
<!DOCTYPE html>
<html>
  <head>
  	<base href="<%=basePath%>" target="_self">
    <title>系统提示</title>    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" type="text/css"  href="css/error.css">
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-extend.js"></script>
	<script type="text/javascript" src="js/message.js"></script>
  </head>
<body>
<div class="wrapper">
	<h5>系统提示</h5>
	<div class="content">
		<div class="message">
			<div class="icon <%=icon %>">&nbsp;</div>
			<div class="text"><%=msg %></div>
		</div>
	</div>
	<div class="foot">
		<% 
			int length;
			if(buttons != null && (length = buttons.length) > 0){
				out.println("<script type=\"text/javascript\">");	
				for(int i = 0; i < length; i++){
					out.println("document.write(" + buttons[i].toJsCode() + ");");	
				}
				out.println("</script>");
			}
		 %>
	</div>
</div>
</body>
</html>