package com.codetutr.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.codetutr.entity.User;

public class UserRowMapper implements RowMapper<User> {

	private final String columnLabelPrefix;
	
	public UserRowMapper(String columnLabelPrefix) {
		this.columnLabelPrefix = columnLabelPrefix;
	}

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		User user = new User();
		user.setUid(rs.getLong(columnLabelPrefix + "uid"));
		user.setUsername(rs.getString(columnLabelPrefix + "username"));
		user.setPassword(rs.getString(columnLabelPrefix + "password"));
		user.setFirstName(rs.getString(columnLabelPrefix + "firstName"));
		user.setLastName(rs.getString(columnLabelPrefix + "lastName"));
		user.setEnabled(rs.getBoolean(columnLabelPrefix + "enabled"));
        return user;
	}

}
  