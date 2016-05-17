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

		<title>My JSP 'index.jsp2' starting page</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
	</head>
	<body>
		<div id="console" style="height: 0px"></div>
		<div id="main" style="height: 400px"></div>
		<div id="main2" style="height: 400px"></div>
		<script src="js/echarts/source/echarts-all.js"></script>
		<script type="text/javascript">
			// 基于准备好的dom，初始化echarts图表
			var myChart = echarts.init(document.getElementById('main'), 'macarons');
		
			myChart.showLoading( {
				text : "Loading........"
			});
		
			var categories = [];
			var values = [];
		
			$.ajaxSettings.async = false;
		
			$.getJSON('${ctx}/DataAccessServlet', function(json) {
				categories = json.categories;
				values = json.values;
			});
		
			var option = {
				tooltip : {
					show : true
				},
				legend : {
					data : [ '销量' ]
				},
				xAxis : [ {
					type : 'category',
					data : categories
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : [ {
					'name' : '销量',
					'type' : 'bar',
					'data' : values
				} ]
			};
		
			myChart.setOption(option);
			myChart.hideLoading();
		</script>
	</body>
</html>
