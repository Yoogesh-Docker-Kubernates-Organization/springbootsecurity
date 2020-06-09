package com.codetutr.dao.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codetutr.entity.User;
import com.codetutr.utility.UtilityHelper;

@Profile("SpringDataJPA")
@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
public class SpringDataJpaUserDaoImpl implements IUserDao {
	
	private final Logger logger = LoggerFactory.getLogger(SpringDataJpaUserDaoImpl.class);

	private IUserRepository userRepository;
	private IAuthorityRepository authorityRepository;
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public SpringDataJpaUserDaoImpl(IUserRepository userRepository, IAuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.passwordEncoder = passwordEncoder;
	}
	

	@Override
	public void initiateDatabase() {
		
		logger.debug("Initialization of database started");
		
		User user = new User(null, "user@gmail.com",passwordEncoder.encode("1234"), "Yoogesh", "Sharma", true, null);
		user.setAuthorities(UtilityHelper.getUserAuthList(user));
		createUser(user);
		
		User admin = new User(null, "admin@gmail.com",passwordEncoder.encode("1234"), "Kristy", "Sharma", true, null);
		admin.setAuthorities(UtilityHelper.getAdminAuthList(admin));
		createUser(admin);
		
		User dba = new User(null, "dba@gmail.com",passwordEncoder.encode("1234"), "Sushila", "Sapkota", true, null);
		dba.setAuthorities(UtilityHelper.getDbaAuthList(dba));
		createUser(dba);
		
		logger.debug("Inserting initial database completed. Total {} profile found. First List of these profile is : {}",  userRepository.count(), getAllUsers());
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean deleteUser(long guid) {
		if(userRepository.existsById(guid)) {
			userRepository.deleteById(guid);
			return true;
		}
		return false;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		//returns only two results in a first page which is sorted in accending order by username
		Pageable pageable = PageRequest.of(0, 2, Direction.ASC, "firstName");
		Page<User> result = userRepository.findAll(pageable);   // List<User> result = userRepository.findAllUsersJPQL(pageable);
		for (User next : result) {
			users.add(next);
		}
		return users;
	}

	@Override
	public User getUser(Long guid) {
		return userRepository.findUserByUid(guid);
	}

	@Override
	public User getUserByUserName(String username) {
		User user = userRepository.findUserByUsername(username);
		user.setAuthorities(authorityRepository.findByUser(user));
		return user;
	}

	@Override
	public boolean ismoreUsernameExists(String username) {
		return userRepository.findUserByUsernameContains(username).size() > 0;
	}

	@Override
	public List<User> getUserByName(String name) {
		return userRepository.findUserByFirstName(name);
	}

}
