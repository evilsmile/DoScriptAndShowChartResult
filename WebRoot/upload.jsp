<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<title>统计脚本</title>
<link rel="stylesheet" type="text/css" href="css/all.css"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
			<!--javascript:location.href='servlet/UploadHandler'-->
</head>
<body style="background:#71AFA4">
<script type="text/javascript">
	function go_url(actn) {
		document.uploadForm.submit();
	}
</script>
<div class="wrapper">
	<form name="uploadForm" action="servlet/UploadHandler" enctype="multipart/form-data" method="post">
		<div class="file">
			<input type="file" name="script" id="upload_file"><label class="tip">选择脚本</label>
		</div>
		<div class="file">
			<input type="submit" name="upload" value="上传">上传
		</div>
	</form>
	<form name="runForm" action="servlet/RunHandler" method="post">
		<div class="file">
			<!-- run_file is consistent with upload_file-->
			<input type="hidden" id="runfile" name="run_filename" value="">
			<input type="submit" name="run" value="运行">运行
		</div>
	</form>
	<form name="listFileForm" action="servlet/ListFilesHandler" method="post">
		<div class="file">
			<input type="submit" name="list" value="列表">查看所有文件
		</div>
	</form>
	<hr style="margin:2px -20px;height:1px;border:none;border-top:1px dashed #0066CC;" />
</div>

<script type="text/javascript">
// change tip content when have choosed file
	$(".file").on("change", "input[type='file']", function() {
		var filePath = $(this).val();
		var arr = filePath.split('\\');
		var fileName = arr[arr.length - 1];
		$(".tip").html(fileName);
		$("#runfile").val(fileName);
		})
</script>

</body>
</html>
