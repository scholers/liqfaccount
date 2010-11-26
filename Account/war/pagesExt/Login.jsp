<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
     <script type="text/javascript" src="<%=basePath%>/pagesExt/extjs3.3/adapter/ext/ext-base.js"></script>
     <script type="text/javascript" src="<%=basePath%>/pagesExt/extjs3.3/ext-all.js"></script>
	 <link rel="stylesheet" type="text/css" href="<%=basePath%>/pagesExt/extjs3.3/resources/css/ext-all.css">
	<script type="text/javascript" src="<%=basePath%>/pagesExt/Login.js"></script>

  </head>
       
  <body>
  <div id="loginForm"></div>
  </body>
</html>
