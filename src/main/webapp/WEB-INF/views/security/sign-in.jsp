<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
<link href="${pageContext.request.contextPath}/static/css/common.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/static/css/sign-in.css" rel="stylesheet" type="text/css">
<title>Login Page</title>

</head>
 <!-- Display the image -->
<img src="<c:url value="/static/images/Spring1.png"/>" style="max-height:60px;"/>

<h3>Login with Username and Password_testing</h3> </br>

<c:if test="${param.error != null}">
	<p class="error">Login failed. Check that your username and password are correct.</p>
</c:if>


<%
//    String error = request.getParameter("error");
//    if(error !=null)
//    {
%>	 
<!-- <label style="color:red;">Login failed!! Check that your username and password are correct!!</label> -->
<%  
//    }
%>
	
<div class="signInFormDiv">
	<form action='${pageContext.request.contextPath}/do-sign-in' method='POST'>
		 <label for="fname">USERNAME</label>
	    <input type="text" id="fname" name="username" placeholder="Enter username" value=''>
	    
		 <label for="fname">PASSWORD</label>
	    <input type="password" id="fname" name="password" placeholder="Enter password">
	    
	    <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" /> <!-- This must be provided -->
	    
	    <input type='checkbox' name='remember-me' /> Remember Me
	    <!-- <input type='checkbox' name='_spring_security_remember_me' checked="checked" /> -->
	    
	    <input type="submit" value="Submit">
	</form>
</div>


</br>
 <div > 
 <p><a href="<c:url value='/sign-up'/>">SignUp</a></p>
 </div>
 
 
  <!-- OAuth2 Logic Started -->
 <div > 
 	<p><a href="<c:url value='/oauth_login'/>">OAuth2</a></p>
 </div>
 
  <c:if test="${urls_oauth2 != null}">
  
	 <!-- We Just need this file at compile time and need not refresh data every-time the file loads since these values are kind of static so that we prefer include directive over include action tag -->
	 <%@include  file = "oauth2.jsp" %>
	 
     <%-- <jsp:include page="oauth2.jsp" /> --%>
  </c:if>
 <!-- OAuth2 Logic Finished -->
  
</body>
</html>

