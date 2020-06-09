<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!-- Custom theme -->
<link rel="stylesheet" href='<spring:theme code="css"/>' type="text/css"/>

<span style="float: right">
	<a href="${pageContext.request.contextPath}/${param.themeUrl}?theme=default" style="color:white;">default-theme</a> 
	| 
	<a href="${pageContext.request.contextPath}/${param.themeUrl}?theme=black" style="color:white;">black-theme</a> 
	| 
	<a href="${pageContext.request.contextPath}/${param.themeUrl}?theme=chocolate" style="color:white;">chocolate-theme </a>
</span>