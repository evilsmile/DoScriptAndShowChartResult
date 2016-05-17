<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<title>脚本上传</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/servlet/UploadHandler" enctype="multipart/form-data" method="post">
		Upload User: <input type="text" name="username"><br/>
		Upload file: <input type="file" name="file1"><br/>
		<input type="submit" value="Upload">
	</form>
</body>
</html>
