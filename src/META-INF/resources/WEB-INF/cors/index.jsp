<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="format-detection" content="telephone=no" />
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=0" />
		<title>cors v${version}</title>
		<style type="text/css">
			body {
				font-size: 12px;
			}
			div {
				margin: 5px 0;
			}
		</style>
		<script type="text/javascript">
			function refresh() {
				var xhr = new XMLHttpRequest();
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4 && xhr.status == 200) { // 当状态变化时处理的事情
                    	var response = xhr.responseText; // 接收响应信息
                    	if (response == "success") {
                    		alert("刷新成功");
                    		window.location.reload();
                    	} else {
                    		alert("刷新失败");
                    	}
					}
				}
				xhr.open("POST", "${ctx}/cors/refresh", true);
				xhr.send();
			}
		</script>
	</head>
	<body>
		<c:forEach items="${allow_origins}" var="origin">
			<div>${allow_methods}&nbsp;${origin}&nbsp;${allow_credentials}</div>
		</c:forEach>
		<div>
			<button onclick="refresh()">刷新</button>
		</div>
	</body>
</html>