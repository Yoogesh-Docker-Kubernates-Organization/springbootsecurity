package com.codetutr.dao.user;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codetutr.dao.mapper.AuthorityRowMapper;
import com.codetutr.dao.mapper.UserRowMapper;
import com.codetutr.entity.Authority;
import com.codetutr.entity.User;
import com.codetutr.utility.UtilityHelper;

@Profile("SpringJdbc")
@Repository
@Transactional(transactionManager = "springJdbcTransactionManager", propagation = Propagation.REQUIRED)
public class SpringJdbcUserDaoImpl implements IUserDao {
	
	private final Logger logger = LoggerFactory.getLogger(SpringJdbcUserDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void initiateDatabase() {
		
		logger.debug("Initialization of database started");
		
		createUser(new User(UtilityHelper.generateLong(), "user@gmail.com",passwordEncoder.encode("1234"), "Yoogesh", "Sharma", true, UtilityHelper.getUserAuthList(null)));
		createUser(new User(UtilityHelper.generateLong(), "admin@gmail.com",passwordEncoder.encode("1234"), "Kristy", "Sharma", true, UtilityHelper.getAdminAuthList(null)));
		createUser(new User(UtilityHelper.generateLong(), "dba@gmail.com",passwordEncoder.encode("1234"), "Sushila", "Sapkota", true, UtilityHelper.getDbaAuthList(null)));
		
		logger.debug("Initialization of database completed");
	}
	
	@Override
	public User createUser(User user) {
		Long uid = UtilityHelper.generateLong();
		Object[]  params = {uid, user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.isEnabled()};
        jdbcTemplate.update("INSERT INTO USERS (uid, username, password, firstName, lastName, enabled) VALUES(?,?,?,?,?,?)", params);
        
		for (Authority next : user.getAuthorities()) {
			jdbcTemplate.update("INSERT INTO AUTHORITIES (uid, fk_uid, role) VALUES(?,?,?)", new Object[] {UtilityHelper.generateLong(), uid, next.getRole()});
		}
		
		return getUserByUserName(user.getUsername());
	}

	@Override
	public User updateUser(User user) {
        Object[]  params = {user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.isEnabled(), user.getUid()};                         
        jdbcTemplate.update("UPDATE USERS SET username = ?, password = ?, firstName = ?, lastName = ?, enabled= ?  where uid = ?", params);
        
        jdbcTemplate.update("DELETE FROM AUTHORITIES WHERE fk_uid = ?", new Object[] {user.getUid()});
        
        for (Authority next : user.getAuthorities()) {
        	jdbcTemplate.update("INSERT INTO AUTHORITIES (uid, fk_uid, role) VALUES(?,?,?)", new Object[] {UtilityHelper.generateLong(), user.getUid(), next.getRole()});
        }
        return getUserByUserName(user.getUsername());
	}

	@Override
	public boolean deleteUser(long uid) {
		int affectedRows = jdbcTemplate.update("DELETE FROM USERS WHERE uid = ?", new Object[] {uid});
		return affectedRows > 0;
	}

	@Override
	public List<User> getAllUsers() {
		return jdbcTemplate.query("SELECT * FROM USERS", new UserRowMapper("USERS."));
	}

	@Override
	public User getUser(Long uid) {
		User user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE uid=?", new UserRowMapper("USERS."), uid);
		if(null != user.getUid()) {
			user.setAuthorities(getAuthorityFromAuthoritiesTable(user.getUid()));
		}
		return user;
	}

	@Override
	public User getUserByUserName(String username) {
		User user = jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE username=?", new UserRowMapper("USERS."), username);
		if(null != user.getUid()) {
			user.setAuthorities(getAuthorityFromAuthoritiesTable(user.getUid()));
		}
		return user;
	}

	@Override
	public boolean ismoreUsernameExists(String username) {
		List<Map<String, Object>> users = jdbcTemplate.queryForList("SELECT * FROM USERS WHERE username=?", new Object[] {username});
		return !users.isEmpty();
	}

	@Override
	public List<User> getUserByName(String firstName) {
		return jdbcTemplate.query("SELECT * FROM USERS WHERE firstName=?", new UserRowMapper("USERS."), firstName);
	}
	
	private List<Authority> getAuthorityFromAuthoritiesTable(Long uid) {
		return jdbcTemplate.query("SELECT * FROM AUTHORITIES WHERE fk_uid=?", new AuthorityRowMapper("AUTHORITIES."), uid);
	}
	
}
