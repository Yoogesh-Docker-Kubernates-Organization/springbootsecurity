<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="springform" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<header>
<title>Sign-Up</title>
<link href="${pageContext.request.contextPath}/static/css/sign-in.css" rel="stylesheet" type="text/css">
</header>

<body>
          <a href="sign-up?mylocale=en_US">English</a> |  <a href="sign-up?mylocale=fr">French</a>
<h1><spring:message code="signup.welcome.message"/></h1>


<div style="float:right;margin-top:-30px"> 
<p><a href="<c:url value='/sign-out'/>">signIn</a></p>
</div>


<springform:form  action="${pageContext.request.contextPath}/sign-up" modelAttribute="user" method="post">
<springform:errors class="error" path=""/> </p>
<%-- <springform:errors style="color:red;" path=""/> </p> --%>

	Username: <springform:input path="username"/> </br>
	password: <springform:password path="password"/> </br>
   FirstName: <springform:input path="firstName"/> </br>
    LastName: <springform:input path="lastName"/> </br>
	
	<input type ="submit" value="Create">
	
</springform:form> </br> </br> </br> 



<table>
       <tr>
              <td>ID</td>                                                       
              <td>UserName</td>
              <td>FirstName</td>
              <td>LastName</td>
<!--          <td>Role</td>  -->
      <tr>
              <td><c:out value="${user1.uid}"></c:out></td>
              <td><c:out value="${user1.username}"></c:out></td>
              <td><c:out value="${user1.firstName}"></c:out></td>
              <td><c:out value="${user1.lastName}"></c:out></td>  
<%--          <td><c:out value="${user1.Role}"></c:out></td> --%>
     </tr>
              

</table>






</body>
</html>