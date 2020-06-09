package com.codetutr.dao.user;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codetutr.entity.User;

public interface IUserRepository extends JpaRepository<User, Long>{
	
	public User findUserByUid(Long guid);
	public User findUserByUsername(String username);
	public List<User> findUserByFirstName(String firstName);
	public List<User> findUserByUsernameContains(String username);
	
	
	/**
	 * 
	 * Query Using JPQL language ( Make sure you cannot create a table using JPQL)
	 */
	@Query("from User")
	public List<User> findAllUsersJPQL(Pageable pageable);
	
	@Query("select u.firstName, u.lastName from User u where u.username = 'admin@gmail.com'")
	public List<Object[]> findAllPartialResultsJPQL();
	
	@Query("select u.username, u.firstName from User u where u.lastName=:firstParam")
	public List<Object[]> findUserByLastNameJPQL(@Param("firstParam") String lastName);
	
	@Modifying
	@Query("delete from User where firstName=:firstParam")
	public void deleteUserByFirstNameJPQL(@Param("firstParam") String firstName);
	
	
	/**
	 * using Native Query (Make sure you can create a table using Native)
	 */
	@Query(value = "SELECT * FROM USERS", nativeQuery = true)
	public List<User> findAllUsersNATIVE(Pageable pageable);
}
