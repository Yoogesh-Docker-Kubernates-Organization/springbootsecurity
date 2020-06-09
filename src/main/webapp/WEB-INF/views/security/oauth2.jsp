<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link href="${pageContext.request.contextPath}/static/css/oauth2.css" rel="stylesheet" type="text/css">

<div id="js_oauth2-modal" class="oauth2-modal">
	<div class="oauth2-modal-content">
		<span class="close">&times;</span>
		<c:forEach var="provider" items="${urls_oauth2}">
			<div>
				<a href="${provider.value}">${provider.key}</a>
			</div>
		</c:forEach>
	</div>
</div>


<script type="text/javascript">

//Get the modal
var modal = document.getElementById('js_oauth2-modal');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}
</script>