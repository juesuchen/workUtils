<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="js/third/jw/jw.css" />
<script type="text/javascript" src="js/third/jquery-1.7.2.js"></script>
<script type="text/javascript" src="js/third/jw/jw.js"></script>
<title>demo</title>
<script type="text/javascript">
	var dialog;
	function callback() {
		
	}
	function upload() {
		dialog = jw.dialog({iframe:'<%=basePath%>js/third/jcrop/upload.jsp?uid=juesu.png',max:false,width:838,height:450,onClose:callback});
	}
</script>
</head>
<body>
<div style="text-align: center;">
<button onclick="upload()">Upload</button> 
</div>

</body>
</html>