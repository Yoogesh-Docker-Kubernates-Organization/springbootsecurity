<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link href="${pageContext.request.contextPath}/static/css/socket.css" rel="stylesheet" type="text/css">

<script src="${pageContext.request.contextPath}/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="${pageContext.request.contextPath}/webjars/stomp-websocket/2.3.3/stomp.min.js"></script>

<!-- First pop-up -->
 <div class="mySocket_firstStep">
    <input type="text" name="username" placeholder="Enter the Username you want to message to .........." id="firststep_receiverUsername"/>
    <input type="submit" value="Send" onclick="openChatWindow()"/>
    <a href="#socket-modal-id-start-chat" rel="modal:open" id="firststep_openchatwindow" style="display:none;"></a>
 </div>


<!-- Second pop-up -->
<div id="socket-modal-id-start-chat" class="modal" style="height:auto;">
	<div class="mySocket_secondStep">
	    Chat-with : <span id="secondstep_headline"></span>  &nbsp;
	    <span id="web_socket_new_message_notification" style="color:red; float:right;"></span>
	    </br></br>
	    
	    
	    <div id="secondstep_sentMessage" style='background-color:#d7e0d2;'></div>
	    
	    <input type="hidden" id="secondstep_senderUsername" value="${username}">
	    <input type="hidden" id="secondstep_receiverUsername" value="">
	    
	    <input type="text" name="message" id="secondstep_message" placeholder="Enter messages...."/>
	    <input type="submit" value="Send" onclick="sendMessage()"/>
	 </div>
 </div>





 