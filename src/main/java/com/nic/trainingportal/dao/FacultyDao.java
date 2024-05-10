package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FacultyDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 * 
	 * @param map
	 * @return
	 */
	public int addFaculty(final Map<String, Object> map) {
		String id=UUID.randomUUID().toString();
		String sql = "insert into faculty (name,post,pay_scale,type_of_faculty,remarks) values('" + map.get("name")
				+ "','"+map.get("postHeld")+"',"+Integer.parseInt(map.get("scalePay").toString())+",'"+map.get("permanent")+"','"+map.get("remarks")+"')";
		return jdbcTemplate.update(sql);

	}

	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllFaculty() {
		try {
			String sql = "select faculty_id,name,post,pay_scale,type_of_faculty,remarks,institutetype from \"faculty\"";
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * delete faculty details
	 * 
	 * @param map
	 * @return
	 */
	public int deleteFaculty(Map<String, Object> map) {
		try {
			String sql = "delete from \"users\" where id='" + map.get("id") + "'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateFacultyDetails(Map<String, Object> map) {
		try {
			String sql = "update \"users\" set scalePay='" + map.get("scalePay") + "' where id='"+map.get("id")+"'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
