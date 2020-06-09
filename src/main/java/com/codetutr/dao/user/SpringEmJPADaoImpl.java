package com.codetutr.dao.user;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codetutr.entity.User;
import com.codetutr.utility.UtilityHelper;

@Repository
@Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
@Profile("SpringEmJPA")
public class SpringEmJPADaoImpl implements IUserDao {
	
	private final Logger logger = LoggerFactory.getLogger(SpringEmJPADaoImpl.class);

    @PersistenceContext//(unitName="entityManagerUnit")
    private EntityManager entityManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
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
		
		logger.debug("Initialization of database completed");
	}
	
	@Override
	public User createUser(User user) {
		entityManager.persist(user);
		return user;
	}

	@Override
	public User updateUser(User user) {
		User merged =  entityManager.merge(user);
		entityManager.flush();
		return merged;
	}

	@Override
	public boolean deleteUser(long guid) {
		if(null == Long.valueOf(guid)) {
			return false;
		}
		else {
			User user = entityManager.find(User.class, guid);
			entityManager.remove(user);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new LinkedList<>();
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<User> from = criteriaQuery.from(User.class);
		CriteriaQuery<Object> select = criteriaQuery.select(from);
		TypedQuery<Object> typedQuery = entityManager.createQuery(select);
		List<Object> result = typedQuery.getResultList();
		for (Object next : result) {
			User user = (User) next;
			user.setAuthorities(null);
			users.add(user);
		}
		
		return users;
	}

	@Override
	public User getUser(Long guid) {
		return entityManager.find(User.class, guid);
	}

	@Override
	public User getUserByUserName(String username) {
		
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE u.username = (:firstParam)", User.class)
							.setParameter("firstParam", username);
		try {
			return query.getSingleResult();
		} 
		catch(NoResultException e) {
			logger.error(this.getClass().getName(), UtilityHelper.getMethodName(new Object() {}), "User " + username + " could not be found in a database.");
			return null;
		}
	}

	@Override
	public boolean ismoreUsernameExists(String username) {
		return getUserByUserName(username) != null;
	}

	@Override
	public List<User> getUserByName(String name) {
		return entityManager.createQuery("SELECT u FROM User u WHERE u.firstName = ?1", User.class)
						.setParameter(1, name)
							.setFirstResult(0)
								.setMaxResults(10)
									.getResultList();
	}
}
