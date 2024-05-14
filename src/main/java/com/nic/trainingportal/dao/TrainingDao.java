package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainingDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 * 
	 * @param map
	 * @return
	 */
	public int addTrainingDetails(final Map<String, Object> map) {
		String sql = "insert into training_final  (entry_date,name,training_venue,training_subject,number_of_trainees,target_group) values('" + map.get("proposedDate") + "','" + map.get("facultyName")
				+ "','"+map.get("venue")+"','"+map.get("trainingSubject")+"',"+map.get("trainessNumber")+",'"+map.get("targetGroup")+"')";
		return jdbcTemplate.update(sql);

	}

	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getTrainingDetails() {
		try {
			String sql = "select * from training_final";
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
	public int deleteTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "delete from training_final where id='" + map.get("id") + "'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "update training_final set entry_date='"+map.get("proposedDate")+"',name='"+map.get("facultyName")+"',training_venue='"+map.get("venue")+"',training_subject='"+map.get("trainingSubject")+"',number_of_trainees="+map.get("trainessNumber")+",target_group='"+map.get("targetGroup")+"' where training_id='"+map.get("training_id")+"'";
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
