package com.codetutr.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;

public class UtilityHelper {

	private UtilityHelper() {
		super();
	}

	public static List<Authority> getUserAuthList(User user) {
		Authority userAuth = new Authority();
		userAuth.setUser(user);
		userAuth.setRole("ROLE_USER");
		List<Authority> userAuthList = new ArrayList<>();
		userAuthList.add(userAuth);
		return userAuthList;
	}

	public static List<Authority> getAdminAuthList(User user) {
		Authority adminAuth = new Authority();
		adminAuth.setUser(user);
		adminAuth.setRole("ROLE_ADMIN");
		List<Authority> adminAuthList = new ArrayList<>();
		adminAuthList.add(adminAuth);
		return adminAuthList;
	}
	
	public static List<Authority> getDbaAuthList(User user) {
		Authority dbaAuth = new Authority();
		dbaAuth.setUser(user);
		dbaAuth.setRole("ROLE_DBA");
		List<Authority> dbaAuthList = new ArrayList<>();
		dbaAuthList.add(dbaAuth);
		return dbaAuthList;
	}

	
	public static Long generateLong() {
		long lowerLimit = 123456712L;
		long upperLimit = 234567892L;
		Long value = UUID.randomUUID().getMostSignificantBits() + (lowerLimit+((long)(new Random().nextDouble()*(upperLimit-lowerLimit))));
		if (value < 0) {
			return Math.abs(value);
		}
		return value;
	}
	
	public static String generateFullUUIDWithOutDashes()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static List<Event> addEventsIntoList(List<Event> events) {
		events.add(new Event("dba@gmail.com", ""));
		events.add(new Event("admin@gmail.com", "dba@gmail.com"));
		events.add(new Event("user@gmail.com", "dba@gmail.com"));
		events.add(new Event("unknown@gmail.com", "admin@gmail.com"));
		return events;
	}

	public static String getMethodName(Object object) {
		return object.getClass().getEnclosingMethod().getName();
	}
}
