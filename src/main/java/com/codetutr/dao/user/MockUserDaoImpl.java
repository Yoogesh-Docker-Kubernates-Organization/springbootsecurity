package com.codetutr.dao.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.codetutr.entity.User;
import com.codetutr.populatorHelper.Converter;
import com.codetutr.populatorHelper.Converters;
import com.codetutr.utility.UtilityHelper;

@Profile("Mock")
@Repository
public class MockUserDaoImpl implements IUserDao {
	
	private final Logger logger = LoggerFactory.getLogger(MockUserDaoImpl.class);

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private Converter<User, User> userConverter;
	
	private static Map<Long, User> userTable = new HashMap<Long, User>();

	public MockUserDaoImpl() {
	}
	
	@Override
	public void initiateDatabase() {
		
		logger.debug("Initialization of database started");
		
		userTable.put(generateUID(), new User(generateUID(), "user@gmail.com",passwordEncoder.encode("1234"), "Yoogesh", "Sharma", true, UtilityHelper.getUserAuthList(null)));
		userTable.put(generateUID(), new User(generateUID(), "admin@gmail.com",passwordEncoder.encode("1234"), "Kristy", "Sharma", true, UtilityHelper.getAdminAuthList(null)));
		userTable.put(generateUID(), new User(generateUID(), "dba@gmail.com",passwordEncoder.encode("1234"), "Sushila", "Sapkota", true, UtilityHelper.getDbaAuthList(null)));
		
		logger.debug("Initialization of database completed"); 
	}

	@Override
	public User createUser(User user) {
		if(ismoreUsernameExists(user.getUsername())) {
			throw new  org.hibernate.exception.ConstraintViolationException("Username " + user.getUsername() + " already exists into the database", null, "INSERT INTO USER(username)");
		}
		user.setUid(generateUID());
		userTable.put(user.getUid(), user);
		return user;
	}


	@Override
	public User updateUser(User user) {
		if (user.getUid() <= 0) {
			System.out.println("*****PLEASE ENTER CORRECT USER ID*******");
			return new User(null, null, null, null, null, false, null);
		}
		User updatedUser = new User(user.getUid(), user.getUsername(), user.getPassword(), user.getFirstName(),
				user.getLastName(), false, user.getAuthorities());
		userTable.put(updatedUser.getUid(), updatedUser);
		return updatedUser;
	}

	@Override
	public boolean deleteUser(long guid) {
		boolean find = false;

		if (guid <= 0) {
			System.out.print("*****PLEASE ENTER CORRECT USER ID*******");
			return false;
		}
		Set<Entry<Long, User>> set = userTable.entrySet();
		Iterator<Entry<Long, User>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, User> me = i.next();
			if (me.getKey() == guid) {
				find = true;
				break;
			}
		}
		if (!find) {
			System.out.println("*****COULD NOT FIND ID IN A DATABASE*******");
			return false;
		} else {
			userTable.remove(guid);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		Collection<User> values = userTable.values();
		return Converters.convertAll(values, userConverter);

	}

	@Override
	public User getUser(Long guid) {
		boolean find = false;

		if (guid <= 0) {
			System.out.println("*****PLEASE ENTER CORRECT STUDENT ID*******");
			return new User(null, null, null, null, null, false, null);
		}

		Set<Entry<Long, User>> set = userTable.entrySet();
		Iterator<Entry<Long, User>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, User> me = i.next();
			if (me.getKey() == guid) {
				find = true;
				break;
			}
		}
		if (!find) {
			System.out.println("*****COULD NOT FIND ID IN A DATABASE*******");
			return new User(null, null, null, null, null, false, null);
		} else {
			return userTable.get(guid);
		}
	}

	@Override
	public User getUserByUserName(String username) {
		ArrayList<User> users = getAllUsersByUsername(username);
		if (users != null && users.size() == 1) {
			return users.get(0);
		} else {
			return null;
		}
	}

	// Read single using other parameter (not primary key)
	@Override
	public boolean ismoreUsernameExists(String username) {
		return !getAllUsersByUsername(username).isEmpty();
	}

	@Override
	public List<User> getUserByName(String name) {
		ArrayList<User> users = new ArrayList<User>();
		Set<Entry<Long, User>> set = userTable.entrySet();
		Iterator<Entry<Long, User>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, User> me = i.next();
			if (me.getValue().getFirstName().equals(name)) {
				users.add(userTable.get(me.getKey()));
			}
		}
		return users;
	}
	
	public ArrayList<User> getAllUsersByUsername(Object obj) {
		ArrayList<User> al = new ArrayList<User>();
		Set<Entry<Long, User>> set = userTable.entrySet();
		Iterator<Entry<Long, User>> i = set.iterator();
		while (i.hasNext()) {
			Entry<Long, User> me = i.next();
			if (me.getValue().getUsername().equals(obj)) {
				al.add(userTable.get(me.getKey()));
			}
		}
		return al;
	}
	
	private long generateUID() {
		return (long) userTable.size() + 1;
	}
}
