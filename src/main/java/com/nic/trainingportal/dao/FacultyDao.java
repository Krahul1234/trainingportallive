package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		String sql = "insert into faculty (name,post,pay_scale,type_of_faculty,remarks) values('" + map.get("name")
				+ "','"+map.get("postHeld")+"',"+map.get("scalePay")+",'"+map.get("permanent")+"','"+map.get("remarks")+"')";
		return jdbcTemplate.update(sql);

	}
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllFaculty() {
		try {
			String sql = "select * from faculty";
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
			String sql = "delete from faculty where faculty_id='" + map.get("faculty_id") + "'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateFacultyDetails(Map<String, Object> map) {
		try {
			String sql = "update faculty set name='"+map.get("name")+"',post='"+map.get("postHeld")+"',pay_scale="+map.get("scalePay")+",type_of_faculty='"+map.get("permanent")+"',remarks='"+map.get("remarks")+"' where faculty_id='"+map.get("faculty_id")+"'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
