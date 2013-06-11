<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>用户列表页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">	
  	<link rel="stylesheet" href="css/ui.css" type="text/css" />
  	</head>  
  <body>
 <form name="myForm" action="user/showList.htm" method="get">
 <input name="e.NAME_LK" type="text" /><input type="submit" value="提交" />
 </form>
<br />
<div id="table"></div>
<jsp:include flush="false" page="/page/common/js.jsp" />
<script type="text/javascript">
  var table = new Table("用户列表", "name=姓名;password=密码;age=年龄;gender=性别;remark=备注",  ${page});
  //table.init("用户列表", "name=姓名;age=年龄;gender=性别;password=密码;remark=备注",  ${page}); 
  table.rowDecode("gender", 1, "男", "女");
  table.rowDecode("remark", "Google", "谷歌", "Bing", "微软", "AOT");
  table.rowButton("点击", function(obj){
	  alert(this.name);	  
  });
  table.rowLink("查看", function(){
	  return "show.htm?user.id=" + this.id;	  
  })
  table.hasRowNumber = true;
  table.actionButton("全局操作", function(array){
	  for(var i in array){
		  alert(array[i].name);
	  }
  }, false);
  table.rowSelected = true;
  //table.link = "user/ajaxShowList.htm";
  table.ajaxMode = true;  
  table.ajaxLoad("#table", "user/ajaxShowList.htm");
  //table.show("#table");
  UI.backTop(); 
</script> 
</body>
</html>
