/************************************************************************************************
 * Subscription for the message
 ************************************************************************************************/


ConnectionManager.prototype.addSubscription = function(inboundDestination, newMessageCallback){
	this.subscriptions.push({
		"inboundDestination" : inboundDestination,
		"newMessageCallback" : newMessageCallback
	});
}


function getInboundMessageEvent(message){
	var received = JSON.parse(message.body);
	

	if(received.isreplied == true){
		document.getElementById('secondstep_sentMessage').innerHTML += "<span style='color:green;'>" + received.message + "</span></br>";
	}
	else{
		document.getElementById('secondstep_sentMessage').innerHTML += "<span style='color:white;'>" + received.message + "</span></br>";
	}
	
}



