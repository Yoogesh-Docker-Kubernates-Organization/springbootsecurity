/************************************************************************************************
 * Connection
 ************************************************************************************************/

var connectionManager = null;

function ConnectionManager(webSocketEndPoint, headers){
	console.log("New Connection manager is created.....")
	this.webSocketEndPoint = webSocketEndPoint;
	this.headers = headers;
	this.client = null;
	this.subscriptions = [];
}


ConnectionManager.prototype.connect = function(){
	console.log("Connect Method is called");
	var wsSocket = new SockJS(this.webSocketEndPoint);
	this.client = Stomp.over(wsSocket);
	this.client.connect(this.headers, () => this.connectSuccess())
}


ConnectionManager.prototype.connectSuccess = function(){
		
	console.log("Total subscribers actively listening for the message : ", this.subscriptions.length);
	
	for(let i=0; i< this.subscriptions.length; i++){
		let subscription = this.subscriptions[i];
		let destination = subscription.inboundDestination;
		let callback = subscription.newMessageCallback;
		
		this.client.subscribe(destination, callback);
		
		console.log("******************* Subscriber: " + i + " ******************************************")
		console.log("Destination: ", destination);
		console.log("callBackFunction: ", callback);
		console.log("*************************************************************************************")
	}
}


ConnectionManager.prototype.send = function(url, request){
	this.client.send(url, this.headers, JSON.stringify(request));
}



const initializeSocketJS  = (contextpath, csrfTokenName, csrfTokenValue, username) => {
	
	
	const webSocketEndPoint = contextpath + WEB_SOCKET_ENDPOINT;
	
	//Setting a csrf token in a header
	let headers = [];
	headers[csrfTokenName] = csrfTokenValue;
	
	//Create a ConnectionManager Object
    connectionManager = new ConnectionManager(webSocketEndPoint, headers);
    
    //Add a Notification subscription to get missed messages
    connectionManager.addSubscription("/user" + WEB_SOCKET_QUEUE_NAME + WEB_SOCKET_NOTIFICATION, (message) => {
    	let response = JSON.parse(message.body);
    	document.getElementById('web_socket_new_message_notification').innerHTML = "New messages: <b>" + response.length + "</b>"; 
    });
    
    
    //This will notify to browser when someone is JOINED or LEFT(not implemented correctly) the chat room
    connectionManager.addSubscription(WEB_SOCKET_TOPIC_NAME + WEB_SOCKET_TOPIC_NOTIFICATION, (message) => {
    	let response = JSON.parse(message.body);
    	
    	if(!("Notification" in window)){
    		//Notification is not supported
    		return;
    	}
    	else if(Notification.permission === 'denied'){
    		//User doesn't want notification
    		return;
    	}
    	else if(Notification.permission !== 'granted'){
    		Notification.requestPermission();
    	}
    	
    	if(Notification.permission === 'granted'){
    		if(response.joined == true){
    			let notification = new Notification(response.senderUsername, {body: "IS ONLINE NOW"})
    		}
    		else{
    			let notification = new Notification(response.senderUsername, {body: "IS OFFLINE NOW"})
    		}
    	}
    });
};
