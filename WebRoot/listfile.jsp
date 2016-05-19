<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>Scripts ON Server</title>
<link rel="stylesheet" type="text/css" href="css/all.css"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
</head>
<body>
<script type="text/javascript">
	// remove start '[' and end ']'
	$(document).ready(function() {
		var fileList = '<%= request.getAttribute("files") %>';
		fileList = fileList.substring(1, fileList.length - 1);
	
		var files = fileList.split(",");
		for (var i in files) {
			file = files[i].trim();
			var $item = $("<span>" + file + "</span>&nbsp<div class=\"file runbtn\"><input type=\"button\" value=\"" + file + "\">Run</div><br/>");
			$("div#filelist_container").append($item);
		}
		$("input").click(function() {
				$("input#run_file").val($(this).val());
				document.runForm.submit();
			});
	})
</script>

<form name="runForm" action="/servlet/RunHandler" method="post">
<div id="filelist_container">
	<!-- "name" should be "run_filename" so that RunHandler could use -->
	<input type="hidden" id="run_file" name="run_filename" value="">
</div>
</form>

</body>
</html>
