<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>上传脚本</title>
<link rel="stylesheet" type="text/css" href="css/all.css"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
			<!--javascript:location.href='servlet/UploadHandler'-->
</head>
<body style="background:#71AFA4">
<script type="text/javascript">
	function go_url(actn) {
		document.getElementsByName('operation').value = actn;
		document.mainForm.submit();
}
</script>
<div class="wrapper">
	<form name="mainForm" action="${pageContext.request.contextPath}/servlet/UploadHandler" enctype="multipart/form-data" method="post">
		<div class="file">
			<input type="file" name="script"><label class="tip">选择脚本</label>
		</div>
		<div class="file">
			<input type="submit" name="upload" value="上传">上传
		</div>
		<div class="file">
			<input type="submit" name="run" value="运行">运行
		</div>

		<hr style="margin:2px -20px;height:1px;border:none;border-top:1px dashed #0066CC;" />
	</form>
</div>

<script type="text/javascript">
// change tip content when have choosed file
	$(".file").on("change", "input[type='file']", function() {
		var filePath = $(this).val();
		var arr = filePath.split('\\');
		var fileName = arr[arr.length - 1];
		$(".tip").html(fileName);
		})
</script>

</body>
</html>
