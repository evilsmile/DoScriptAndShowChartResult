<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>


<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">
	<title>结果</title>
	<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/all.css"/>
</head>

<body>
	<div class="wrapper">
		<p id="result_wrapper" style="display:none">
			结果: <label id="result_tip"/>
		</p>
		<p id="error_wrapper" style="display:none">
			原因: <label id="err_tip"/>
		</p>
	</div>
	<script type="text/javascript">
		var error = '<%=request.getAttribute("error")%>';
		var result = '<%=request.getAttribute("result")%>';

		if (error != null && error != "") {
			$("#error_wrapper").show();
			$("#err_tip").text(error);
		}
		if (result != null && result != "") {
			$("#result_wrapper").show();
			$("#result_tip").text(result);
		}
	</script>
</body>
</html>
