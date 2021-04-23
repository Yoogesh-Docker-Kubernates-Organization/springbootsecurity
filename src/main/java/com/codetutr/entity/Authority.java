package com.codetutr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="AUTHORITIES")
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="uid", nullable=false, length=50)
	private Long uid;

	@ManyToOne
	@JoinColumn(name = "fk_uid", nullable = false)
	private User user;

	@Column(name = "role", nullable = false, length = 50)
	private String role;

	public Authority() {
		super();
	}

	public Authority(Long uid, User user, String role) {
		super();
		this.uid = uid;
		this.user = user;
		this.role = role;
	}
	

	/**
	 * @return the uid
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}


	@Override
	public String toString() {
		return "Authority [uid=" + uid + ", user=" + user + ", role=" + role + "]";
	}

}
