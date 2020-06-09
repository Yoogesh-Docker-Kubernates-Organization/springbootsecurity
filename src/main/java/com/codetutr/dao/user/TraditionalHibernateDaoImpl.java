package com.codetutr.dao.user;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;
import com.codetutr.utility.UtilityHelper;

@Repository
@Transactional(transactionManager = "hibernateTransactionManager", propagation = Propagation.REQUIRED)
@Profile("Hibernate")
public class TraditionalHibernateDaoImpl implements IUserDao{
	
	 private final Logger logger = LoggerFactory.getLogger(TraditionalHibernateDaoImpl.class);

	/**
	 * Hibernate now conforms with the JPA specification to not allow flushing updates outside of a transaction boundary from Hibernate ORM 5.2. To restore 5.1 
	 * behavior, allowing flush operations outside of a transaction boundary, set [hibernate.allow_update_outside_transaction=true] in additional Properties.
	 * Otherwise you will get {@link no transaction is not in progress} exception.
	 */
	@Autowired
	private HibernateTemplate hibernateTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private SessionFactory sessionFactory;
    
    @Autowired
    public TraditionalHibernateDaoImpl(EntityManagerFactory entityManagerFactory) {
    	this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
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
		
		logger.debug("Initialization of database completed");
	}
	
	@Override
	public User createUser(User user) {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
			Long key = (Long) s.save(user);
		t.commit();
		s.close();
		return getUser(key);
	}

	@Override
	public User updateUser(User user) {
		User database = getUserByUserName(user.getUsername());
		if(null != database) {
			user.setUid(database.getUid());
			hibernateTemplate.update(user);
			return getUser(user.getUid());
		}
		return database;
	}

	@Override
	public boolean deleteUser(long guid) {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
			String hql = "delete User u Where u.uid = ?";
			Query<User> q = s.createQuery(hql);
			q.setParameter(0, guid);
			int rows = q.executeUpdate();
		t.commit();
		s.close();
		return rows > 0;
	}

	@Override
	public List<User> getAllUsers() {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
			String hql = "SELECT u from User u";
			Query<User> q = s.createQuery(hql);
			List<User> users = q.list();
			for (User next : users) {
				next.setAuthorities(null);
			}
		t.commit();
		s.close();
		return users;
	}

	@Override
	public User getUser(Long guid) {
		User user = hibernateTemplate.get(User.class, guid);
		if(null != user) {
			return getUserByUserName(user.getUsername());
		}
		return user;
	}

	@Override
	public User getUserByUserName(String username) {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
			Criteria cr = s.createCriteria (User.class );
			cr.add(Restrictions.eq("username", username ));
			User user = (User) cr.uniqueResult();
			if(null != user) {
				cr = s.createCriteria (Authority.class );
				cr.add(Restrictions.eq("user", user ));
				List<Authority> authorities = cr.list();
				user.setAuthorities(authorities);
			}
		t.commit();
		s.close();
		return user;
	}

	@Override
	public boolean ismoreUsernameExists(String username) {
		return !(getUserByUserName(username) == null);
	}

	@Override
	public List<User> getUserByName(String name) {
		Session s = sessionFactory.openSession();
		Transaction t = s.beginTransaction();
			Criteria cr = s.createCriteria (User.class );
			cr.add(Restrictions.eq("firstName", name ));
			List<User> users = cr.list();
			for (User next : users) {
				next.setAuthorities(null);
			}
		t.commit();
		s.close();
		return users;
	}
}