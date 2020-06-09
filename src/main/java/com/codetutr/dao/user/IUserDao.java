package com.codetutr.dao.user;

import java.util.List;

import com.codetutr.entity.User;

public interface IUserDao {

	User createUser(User user);

	// Update
	User updateUser(User user);

	// Delete
	boolean deleteUser(long guid);

	// Read All
	List<User> getAllUsers();

	// Read single using primary key
	User getUser(Long guid);

	// Read single using other parameter (not primary key)
	User getUserByUserName(String username);

	// Read single using other parameter (not primary key)
	boolean ismoreUsernameExists(String username);

	List<User> getUserByName(String name);

	void initiateDatabase();

}