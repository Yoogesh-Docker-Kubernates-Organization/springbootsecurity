package com.codetutr.feature.webSockets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleMessageDaoImpl {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static Map<Long, SimpleMessage> simpleMessageTable = new HashMap<>();

	public SimpleMessageDaoImpl() {
		//simpleMessageTable.put(1L, new SimpleMessage(1L, "dba@gmail.com", "admin@gmail.com", "Hi admin How are you?", new Date(), true));
	}

	@PrePersist
	public void oncreate() {
	 System.out.println("Pre-persist method is executed.....");
	}
	
	@Async  //Since this is async method. the response you will get null in a return response at swagger even though the messaged is saved
	public SimpleMessage createSimpleMessageWithAsyncCall(SimpleMessage message) {
		return createSimpleMessage(message);
	}
	
	
	public SimpleMessage createSimpleMessage(SimpleMessage message) {
		message.setUid((long) simpleMessageTable.size() + 1);
		simpleMessageTable.put(message.getUid(), message);
		return message;
	}

	public SimpleMessage updateSimpleMessage(SimpleMessage message) {
		if (message.getUid() <= 0) {
			System.out.println("*****PLEASE ENTER CORRECT MESSAGE ID*******");
			return new SimpleMessage(null, null, null, null, null, false);
		}
		SimpleMessage updatedMessage = new SimpleMessage(message.getUid(), message.getSenderUsername(),message.getRecieverUsername(), message.getMessage(), message.getReceivedDate(),message.getIsreplied());
		simpleMessageTable.put(updatedMessage.getUid(), updatedMessage);
		return updatedMessage;
	}

	public boolean deleteMessage(long guid) {
		boolean find = false;

		if (guid <= 0) {
			System.out.print("*****PLEASE ENTER CORRECT MESSAGE ID*******");
			return false;
		}
		Set<Entry<Long, SimpleMessage>> set = simpleMessageTable.entrySet();
		Iterator<Entry<Long, SimpleMessage>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, SimpleMessage> me = i.next();
			if (me.getKey() == guid) {
				find = true;
				break;
			}
		}
		if (!find) {
			System.out.println("*****COULD NOT FIND ID IN A DATABASE*******");
			return false;
		} else {
			simpleMessageTable.remove(guid);
			return true;
		}
	}

	public List<SimpleMessage> getAllMessages() {
		return new ArrayList<SimpleMessage>(simpleMessageTable.values());

	}

	public SimpleMessage getMessageByMessageId(Long guid) {
		boolean find = false;

		if (guid <= 0) {
			System.out.println("*****PLEASE ENTER CORRECT MESSAGE ID*******");
			return new SimpleMessage(null, null, null, null, null, false);
		}

		Set<Entry<Long, SimpleMessage>> set = simpleMessageTable.entrySet();
		Iterator<Entry<Long, SimpleMessage>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, SimpleMessage> me = i.next();
			if (me.getKey() == guid) {
				find = true;
				break;
			}
		}
		if (!find) {
			System.out.println("*****COULD NOT FIND ID IN A DATABASE*******");
			return new SimpleMessage(null, null, null, null, null, false);
		} else {
			return simpleMessageTable.get(guid);
		}
	}


	
	public ArrayList<SimpleMessage> getAllMessagesByReceiversUsername(Object obj) {
		ArrayList<SimpleMessage> al = new ArrayList<>();
		Set<Entry<Long, SimpleMessage>> set = simpleMessageTable.entrySet();
		Iterator<Entry<Long, SimpleMessage>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, SimpleMessage> me = i.next();
			if (me.getValue().getRecieverUsername().equals(obj)) {
				al.add(simpleMessageTable.get(me.getKey()));
			}
		}
		return al;
	}
}
