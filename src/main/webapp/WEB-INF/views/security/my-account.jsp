<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<link href="${pageContext.request.contextPath}/static/css/myAccount.css" rel="stylesheet" type="text/css">

<!-- jQuery Modal -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-modal/0.9.1/jquery.modal.min.css" />

<!-- For WebSocket -->
<script src="${pageContext.request.contextPath}/static/js/webSocket/chatConstant.js"></script>
<script src="${pageContext.request.contextPath}/static/js/webSocket/connection.js"></script>
<script src="${pageContext.request.contextPath}/static/js/webSocket/chat.js"></script>
<script src="${pageContext.request.contextPath}/static/js/webSocket/subscription.js"></script>

 <c:choose>
	<c:when test="${currentUserMode eq 'admin'}"><title>my-account-Admin</title></c:when>
	<c:when test="${currentUserMode eq 'dba'}"><title>my-account-DBA</title></c:when>
	<c:otherwise><title>my-account-User</title></c:otherwise>
 </c:choose>

 <jsp:include page="/WEB-INF/views/theme/theme-include-my-account.jsp">
 	<jsp:param name="themeUrl" value="${themeUrl}"/>
 </jsp:include>
 
 <meta content="_csrf_header" content="X-CSRF-TOKEN">
</head>
<body>

<!-- Display image -->
<img src="<c:url value="/static/images/Welcome.gif"/>" style="height:20%; width:20%;" />

<c:choose>
	<c:when test="${currentUserMode eq 'admin'}"><h3>****Admin Account****</h3></c:when>
	<c:when test="${currentUserMode eq 'dba'}"><h3>****DBA Account****</h3></c:when>
	<c:otherwise><h3>****Normal User Account****</h3></c:otherwise>
</c:choose>

<h3>Welcome, ${username}!</h3>

<c:if test="${currentUserMode eq 'admin' || currentUserMode eq 'dba'}">
	<a href="${pageContext.request.contextPath}/my-account-user"> Click here to go for User account page!!!</a> </br>
</c:if>
<c:if test="${currentUserMode eq 'dba' || (currentUserMode eq 'user' && userAuthority eq 'dba') || (currentUserMode eq 'user' && userAuthority eq 'admin')}">
	<a href="${pageContext.request.contextPath}/my-account-admin"> Click here to go for Admin account page!!! </a> </br>
</c:if>
<c:if test="${(currentUserMode eq 'user' && userAuthority eq 'dba') || (currentUserMode eq 'admin' && userAuthority eq 'dba')}">
	<a href="${pageContext.request.contextPath}/my-account-dba"> Click here to go for DBA account page!!! </a> </br>
</c:if>

<a href="${pageContext.request.contextPath}/${themeUrl}"> Go back to Home!!! </a>


<h3>Method label Access Check</h3>
<table class="myAccount-table">
<tr>
	<td><a href="${pageContext.request.contextPath}/secure/dba"><span style="color:white">DBA</span></a></td>
	<td><a href="${pageContext.request.contextPath}/secure/admin"><span style="color:white">ADMIN</span></a></td>
	<td><a href="${pageContext.request.contextPath}/secure/user"><span style="color:white">USER</span></a></td>
	<td><a href="${pageContext.request.contextPath}/secure/authenticated"><span style="color:white">AUTHENTICATED</span></a></td>
	<td><a href="${pageContext.request.contextPath}/secure/editPermission"><span style="color:white">EDIT_PERMISSION</span></a></td>
</tr>
</table>

<h3>Others</h3>
<table class="myAccount-table">
<tr>
	<td><a href="${pageContext.request.contextPath}/secure/events-preFilter"><span style="color:white">PRE_FILTER_EVENTS</span></a></td>
	<td><a href="${pageContext.request.contextPath}/secure/events-postFilter"><span style="color:white">POST_FILTER_EVENTS</span></a></td>
	<td><a href="${pageContext.request.contextPath}/sessions"><span style="color:white">ACTIVE_SESSIONS</span></a></td>
	<td><a href="${pageContext.request.contextPath}/stateMachine"><span style="color:white">STATE_MACHINE</span></a></td>
	<td><a href="${pageContext.request.contextPath}/v2/api-docs"><span style="color:white">SWAGGER_API_DOCUMENT</span></a></td>
	<td><a href="${pageContext.request.contextPath}/swagger-ui.html"><span style="color:white">SWAGGER_HTML</span></a></td>
</tr>
<tr>
	<td><a href="${pageContext.request.contextPath}/forwardRequestViaFilter"><span style="color:white">FORWARD_REQUEST_VIA_FILTER</span></a></td>
	<td><a href="${pageContext.request.contextPath}/restclient"><span style="color:white">GET_ALL_USERS_USING_REST_API</span></a></td>
	<td><a href="${pageContext.request.contextPath}/actuator"><span style="color:white">ACTUATOR</span></a></td>
	<td><a href="#socketModal" rel="modal:open"><span style="color:white">CHAT</span></a></td>
</tr>
</table>

<c:if test="${currentUserMode eq 'dba'}">
	<form action='${pageContext.request.contextPath}/impersonate_As_USER' method='GET' style="margin-top:10px;" class="myform">
	    <input type="text" name="username" value="user@gmail.com"/>
	    <input type="submit" value="Impersonate As User"/>
	</form>
</c:if>


<form action='${pageContext.request.contextPath}/logout' method='POST' style="margin-top:10px;" class="myform">
	<input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
    <input name="LOGOUT" type="submit" value="Logout"/>
</form>


<!-- Socket Modal -->
<div id="socketModal" class="modal">
   <%@include file="socket.jsp"%>
</div>
</body>

<script> 
	window.onload = () => initializeSocketJS("${pageContext.request.contextPath}", "${_csrf.parameterName}", "${_csrf.token}", "${username}");
</script>
</html>

