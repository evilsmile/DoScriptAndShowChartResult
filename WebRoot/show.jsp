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

		<title>统计结果展示</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="${ctx}/js/jquery-1.12.3.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/all.css"/>
	</head>
	<body>
		<div id="options" class="wrapper">
			<label for="echart_style">图表类型:</label><select id="echart_style" onChange="showDiffEchartsSetting()">
				<option value="line">折线图</option>
				<option value="bar">饼状图</option>
			</select><br/>
			<label for="legend">标题:</label><input type="text" id="legend"><br/>

			<input type="button" id="showBtn" onclick="showData()" value="展示" class="file">
		</div>
		<div id="main" style="height: 400px"></div>
		<script src="js/echarts/source/echarts-all.js"></script>
		<script type="text/javascript">
			function showDiffEchartsSetting() {

			}

			function showData() {
				$('#options').fadeOut("slow");

				// 基于准备好的dom，初始化echarts图表
				var myChart = echarts.init(document.getElementById('main'), 'macarons');

				var style = $('#echart_style option:selected').val();
				var legend = $('#legend').val();
			
				myChart.showLoading( {
					text : "Loading........"
				});
			
			
				$.ajaxSettings.async = false;
				var strJsonReply = '<%=request.getAttribute("json")%>';
	
				var jsonReply = eval('(' + strJsonReply + ')');

				var option;
				if (style == "line" || style == "bar") {
					var categories = [];
					var values = [];
					categories = jsonReply.keys;
					values = jsonReply.values;
				
					var lineOption = {
						tooltip : {
							show : true
						},
						legend : {
							data : [ legend ]
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
							'type' : style,
							'data' : values
						} ]
					};
					option = lineOption;
				}
			
				myChart.setOption(option);
				myChart.hideLoading();
			}
		</script>
	</body>
</html>
