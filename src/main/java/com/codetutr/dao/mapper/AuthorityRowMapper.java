package com.codetutr.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.codetutr.entity.Authority;

public class AuthorityRowMapper implements RowMapper<Authority> {

	private final String columnLabelPrefix;
	
	public AuthorityRowMapper(String columnLabelPrefix) {
		this.columnLabelPrefix = columnLabelPrefix;
	}

	@Override
	public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
		Authority authority = new Authority();
		authority.setUid(rs.getLong(columnLabelPrefix + "uid"));
		authority.setRole(rs.getString(columnLabelPrefix + "role"));
        return authority;
	}
}
  