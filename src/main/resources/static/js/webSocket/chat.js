/************************************************************************************************
 * When First send Button is clicked
 ************************************************************************************************/

const openChatWindow = () => {
	
	let baseUrl = document.getElementById('base_url').value;
	let username = document.getElementById('firststep_receiverUsername').value;
	let currentUser = document.getElementById('secondstep_senderUsername').value;
	let response = {};
	let xhr = new XMLHttpRequest();
	xhr.withCredentials = true;
	xhr.addEventListener("readystatechange", function() {
	  if(this.readyState === 4 && this.status === 200) 
	  {
		  responseData = JSON.parse(this.responseText);
		  response = responseData.data;
		  document.getElementById('firststep_receiverUsername').value = '';
		  document.getElementById('secondstep_headline').innerHTML = "<b>" + response.username + "</b>";
		  document.getElementById('secondstep_receiverUsername').value = response.username;
	      document.getElementById('firststep_openchatwindow').click();
	      
	      
	      //Add a subscription of chat messages
	      connectionManager.addSubscription("/user" + WEB_SOCKET_QUEUE_NAME + "/" + response.username, getInboundMessageEvent);
	      
	      
	      //Start a connection
	      connectionManager.connect();
	      
	      setTimeout(function() {
	    	  connectionManager.send(REGISTER_MESSAGE_URL, {"senderUsername": currentUser, "joined" : true});
	    	  connectionManager.send(SEND_MESSAGE_URL + currentUser.split("@")[0], {});
	    	}, 1000);
	  }
	});
	
	if(baseUrl.includes("localhost")){
		console.log("BaseUrl is local.....");
		xhr.open("GET", "http://localhost:8888/springbootsecurity/dispatcher/api/user?username=" + username);
	}
	else{
		console.log("BaseUrl is AWS Cloud.....");
		xhr.open("GET", baseUrl + "/springbootsecurity/api/user?username=" + username);
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();		
};



/************************************************************************************************
 * When Return or Enter key is pressed
 ************************************************************************************************/

$(document).on('keypress', e => {
	 let username = document.getElementById('firststep_receiverUsername').value;
	 let message = document.getElementById('secondstep_message').value;
	 
	 if(e.which == 13){
		 if (!(typeof message == undefined|| message === '')){
			 sendMessage();
		 }
		 else if(!(typeof username == undefined|| username === '')){
			 openChatWindow(); 
		 }
	 }
	 return true;
});



/************************************************************************************************
 * When Second send Button is clicked
 ************************************************************************************************/

const sendMessage = () => {
	const senderUsername = document.getElementById('secondstep_senderUsername').value;
	const recieverUsername =  document.getElementById('secondstep_receiverUsername').value;
	const message = document.getElementById('secondstep_message').value;
    const receivedDate = new Date();
    
	if(typeof message == undefined|| message === ''){
		alert('Please Enter a message!!');
		return false;
	}
	else {
		
		//Create a SimpleMessage object
		var request = {
				senderUsername,
				recieverUsername,
				message,
				receivedDate
		};
		
		//Send a message
		connectionManager.send(SEND_MESSAGE_URL + recieverUsername.split("@")[0], request);
		
		
		document.getElementById('secondstep_message').value = '';
		document.getElementById('secondstep_message').focus();
	}
}