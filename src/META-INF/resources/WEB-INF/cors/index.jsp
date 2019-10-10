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
	</head>
	<body>
		<c:forEach items="${allow_origins}" var="origin">
			<div>${allow_methods}&nbsp;${origin}&nbsp;${allow_credentials}</div>
		</c:forEach>
	</body>
</html>