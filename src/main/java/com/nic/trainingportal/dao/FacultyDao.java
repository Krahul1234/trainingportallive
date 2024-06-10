package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FacultyDao {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(FacultyDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 * 
	 * @param map
	 * @return
	 */
	public int addFaculty(final Map<String, Object> map) {
		try
		{
		int instituteType=0;	
		if(map.get("key").toString().equalsIgnoreCase("sird"))
		{
			instituteType=1;
		}else if(map.get("key").toString().equalsIgnoreCase("etc"))
		{
			instituteType=2;
		}else
		{
			instituteType=3;
		}
		    int userId=this.getUserId(map);	
			String sql="insert into faculty(name,post,pay_scale,type_of_faculty,remarks,user_id,institutetype)values(?,?,?,?,?,?,?)";
			return jdbcTemplate.update(sql,map.get("name"),map.get("postHeld"),map.get("scalePay"),map.get("typeOfFaculty"),map.get("remarks"),userId,instituteType);
		}catch(Exception e)
		{
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
        return 0;
	}
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllFaculty(int pageSize,int pageNumber,String userName,String userType) {
		try {
			int instituteType=0;
			if(userType.equalsIgnoreCase("sird"))
			{
				instituteType=1;
			}else 
			{
				instituteType=2;
			}
			List<Map<String,Object>>dataList=new ArrayList<Map<String,Object>>();
			int userId=this.getUserId(userName,userType);
			String sql = "select faculty_id, name,post as \"postHeld\", pay_scale AS \"scalePay\",type_of_faculty AS \"typeOfFaculty\",remarks   from faculty where institutetype='"+instituteType+"' and  user_id='"+userId+"'order by faculty_id desc  offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
			dataList=jdbcTemplate.queryForList(sql);
			return dataList;
			
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
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
			/**
			 * print log
			 */
			logger.info("Delete  Faculty", sql);
			
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int updateFacultyDetails(Map<String, Object> map) {
		try {
			String sql = "update faculty set name='"+map.get("name")+"',post='"+map.get("postHeld")+"',pay_scale='"+map.get("scalePay")+"',type_of_faculty='"+map.get("typeOfFaculty")+"',remarks='"+map.get("remarks")+"' where faculty_id='"+map.get("id")+"'";
			/**
			 * print log
			 */
			logger.info("update  Faculty", sql);
			
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
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
	public List<Map<String, Object>> getFacultyById(String id) {
		try {
			String sql = "SELECT name, " +
		             "post AS \"postHeld\", " +
		             "pay_scale AS \"scalePay\", " +
		             "type_of_faculty AS \"typeOfFaculty\", " +
		             "remarks " +
		             "FROM faculty " +
		             "WHERE faculty_id='" + id + "'";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql);
			
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public int deleteFacultyById(String id) {
		try {
			String sql = "delete from faculty where faculty_id='"+id+"'";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql);
			
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getFacultyName(Map<String,Object>map) {
		try {
			int userId=this.getUserId(map);
			int userType=0;
			if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				userType=1;
			}else
			{
				userType=2;
			}
				
			String sql ="select name,faculty_id from faculty where user_id='"+userId+"' and institutetype='"+userType+"'";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql);
			
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public int getFacultyCount(int userId,String userType)
	{
		try
		{
			int institutetype=0;
			if(userType.equalsIgnoreCase("sird"))
			{
				institutetype=1;
			}else
			{
				institutetype=2;
			}
			String sql="select count(*) as count from faculty where institutetype='"+institutetype+"' and  user_id='"+userId+"'";
			return jdbcTemplate.queryForObject(sql,Integer.class);
		}catch(Exception e)
		{
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
	

}
