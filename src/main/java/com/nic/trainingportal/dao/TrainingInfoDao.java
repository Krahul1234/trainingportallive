package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TrainingInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int addInformation(final Map<String, Object> map) {
		
		try
		{
		String insertqry = "insert into \"training_info\" (info_id,functional,building,number_of_permanent_faculty,number_of_contractual_faculty,"
				+ "number_of_other_staff,hostel_facility,number_of_seat,hall_number,hall_capacity,lab_number,"
				+ "lab_capacity,dining_number,dining_capacity,number_of_kitchens,auditorium_number,auditorium_capacity,Remarks)"
				+ " values('" + map.get("info_id") + "'," + "'" + map.get("functional") + "','" + map.get("building")
				+ "'," + "'" + map.get("number_of_permanent_faculty") + "'," + "'"
				+ map.get("number_of_contractual_faculty") + "'," + "'" + map.get("number_of_other_staff") + "','"
				+ map.get("hostel_facility") + "','" + map.get("number_of_seat") + "','" + map.get("hall_number") + "',"
				+ "'" + map.get("hall_capacity") + "','" + map.get("lab_number") + "'," + "'" + map.get("lab_capacity")
				+ "','" + map.get("dining_number") + "'," + "'" + map.get("dining_capacity") + "','"
				+ map.get("number_of_kitchens") + "'" + ",'" + map.get("auditorium_number") + "','"
				+ map.get("auditorium_capacity") + "','" + map.get("Remarks") + "')";
		return jdbcTemplate.update(insertqry);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

	}

	public List<Map<String, Object>> getInformation() {
		try {
			String selectqry = "select * from training_info";
			return jdbcTemplate.queryForList(selectqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public int updateInformation(Map<String, Object> map) {
		try {
			String updateQry = "update training_info set functional='" + map.get("functional") + "',building='"
					+ map.get("building") + "',number_of_permanent_faculty=" + map.get("number_of_permanent_faculty")
					+ "," + "number_of_contractual_faculty='" + map.get("number_of_contractual_faculty") + "',"
					+ "number_of_other_staff='" + map.get("number_of_other_staff") + "',hostel_facility='"
					+ map.get("hostel_facility") + "'" + ",number_of_seat='" + map.get("number_of_seat")
					+ "',hall_number='" + map.get("hall_number") + "'" + ",hall_capacity='" + map.get("hall_capacity")
					+ "',lab_number='" + map.get("lab_number") + "'," + "lab_capacity='" + map.get("lab_capacity")
					+ "',dining_number='" + map.get("dining_number") + "'," + "dining_capacity='"
					+ map.get("dining_capacity") + "',number_of_kitchens='" + map.get("number_of_kitchens") + "',"
					+ "auditorium_number='" + map.get("auditorium_number") + "',auditorium_capacity='"
					+ map.get("auditorium_capacity") + "'," + "building='" + map.get("building") + "'"
					+ " where info_id='" + map.get("info_id") + "'";
			return jdbcTemplate.update(updateQry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
