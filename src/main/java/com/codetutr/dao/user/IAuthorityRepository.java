package com.codetutr.dao.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codetutr.entity.Authority;
import com.codetutr.entity.User;

public interface IAuthorityRepository extends JpaRepository<Authority, Long> {
	
	public List<Authority> findByUser(User user);
}
