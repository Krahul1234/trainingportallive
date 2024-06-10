package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class TrainingInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int addInformation(final Map<String, Object> map) {
		
		try
		{
			 int userId=this.getUserId(map);	
		 String insertqry = "insert into training_info (functional,building,number_of_permanent_faculty,number_of_contractual_faculty,"
				+ "number_of_other_staff,hostel_facility,number_of_seat,hall_number,hall_capacity,lab_number,"
				+ "lab_capacity,dining_number,dining_capacity,number_of_kitchens,auditorium_number,auditorium_capacity,Remarks,user_id,user_type)"
				+ " values('" + map.get("functional") + "','" + map.get("building")
				+ "'," + "'" + map.get("number_of_permanent_faculty") + "'," + "'"
				+ map.get("number_of_contractual_faculty") + "'," + "'" + map.get("number_of_other_staff") + "','"
				+ map.get("hostel_facility") + "','" + map.get("number_of_seat") + "','" + map.get("hall_number") + "',"
				+ "'" + map.get("hall_capacity") + "','" + map.get("lab_number") + "'," + "'" + map.get("lab_capacity")
				+ "','" + map.get("dining_number") + "'," + "'" + map.get("dining_capacity") + "','"
				+ map.get("number_of_kitchens") + "'" + ",'" + map.get("auditorium_number") + "','"
				+ map.get("auditorium_capacity") + "','" + map.get("Remarks") + "',"+userId+",'"+map.get("key").toString().toLowerCase()+"')";
		return jdbcTemplate.update(insertqry);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

	}

	public List<Map<String, Object>> getInformation( int pageSize, int pageNumber,int userId,String userType) {
		try {
			String sql = "select * from training_info where user_type='"+userType.toLowerCase()+"' and  user_id='"+userId+"' order by info_id desc  offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public int updateInformation(Map<String, Object> map) {
		try {
			String updateQry = "update training_info set remarks='"+map.get("Remarks")+"', functional='" + map.get("functional") + "',number_of_permanent_faculty=" + map.get("number_of_permanent_faculty")
					+ "," + "number_of_contractual_faculty='" + map.get("number_of_contractual_faculty") + "',"
					+ "number_of_other_staff='" + map.get("number_of_other_staff") + "',hostel_facility='"
					+ map.get("hostel_facility") + "'" + ",number_of_seat='" + map.get("number_of_seat")
					+ "',hall_number='" + map.get("hall_number") + "'" + ",hall_capacity='" + map.get("hall_capacity")
					+ "',lab_number='" + map.get("lab_number") + "'," + "lab_capacity='" + map.get("lab_capacity")
					+ "',dining_number='" + map.get("dining_number") + "'," + "dining_capacity='"
					+ map.get("dining_capacity") + "',number_of_kitchens='" + map.get("number_of_kitchens") + "',"
					+ "auditorium_number='" + map.get("auditorium_number") + "',auditorium_capacity='"
					+ map.get("auditorium_capacity") + "'," + "building='" + map.get("building") + "'"
					+ " where info_id='" + map.get("id") + "'";
			return jdbcTemplate.update(updateQry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(Map<String,Object>map)
	{
		try
		{
			String tableName="";
			/**
			 *  check login type
			 */
			if(map.get("key").toString().equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}else
			{
				tableName="loginmaster_ministry";
			}
			
			String sql="select"+" "+ map.get("key")+"_"+ "id  from"+" "+tableName+" "+"where username='"+map.get("username")+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key")+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getTrainingInfoById(String id) {
		try {
			String sql = "select * from training_info where info_id='"+id+"'";
			
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public int deleteTrainingInfoById(String id) {
		try {
			String sql = "delete from training_info where info_id='"+id+"'";
			
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(String userName,String userType)
	{
		try
		{
			String tableName="";
			/**
			 *  check login type
			 */
			if(userType.equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(userType.equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}else
			{
				tableName="loginmaster_ministry";
			}
			
			String sql="select"+" "+userType+"_"+ "id  from"+" "+tableName+" "+"where username='"+userName+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(userType+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getTrainingInfoCount(int userId,String userType)
	{
		try
		{
			String sql="select count(*) as count from training_info where user_type='"+userType.toLowerCase()+"' and  user_id='"+userId+"'";
			return jdbcTemplate.queryForObject(sql,Integer.class);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

}
