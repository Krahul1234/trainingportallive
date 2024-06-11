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
import org.springframework.web.bind.annotation.RequestParam;

import com.nic.trainingportal.controller.DemographicController;

@Component
public class TrainingDao {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(TrainingDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 * 
	 * @param map
	 * @return
	 */
	public int addTrainingDetails(final Map<String, Object> map) {
		try
		{
		int userId=this.getUserId(map);
		String sql = "insert into training_final  (entry_date,name,training_venue,training_subject,number_of_trainees,target_group,enddate,usertype,user_id) values('" + map.get("proposedDate") + "','" + map.get("facultyName")
				+ "','"+map.get("venue")+"','"+map.get("trainingSubject")+"','"+map.get("trainessNumber")+"','"+map.get("targetGroup")+"','"+map.get("endDate")+"','"+map.get("key").toString().toLowerCase()+"','"+userId+"')";
		/**
		 * print log
		 */
		logger.info("Add Training Details", sql);
		return jdbcTemplate.update(sql);
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
	public List<Map<String, Object>> getTrainingDetails(int pageSize,int pageNumber,String userName,String userType,int userId) {
		try {
			List<Map<String,Object>>dataList=new ArrayList<Map<String,Object>>();
			String sql = "select enddate,training_id, entry_date AS \"proposedDate\",name AS \"facultyName\",training_venue AS \"venue\",training_subject AS \"trainingSubject\",number_of_trainees AS \"trainessNumber\",target_group AS \"targetGroup\" from training_final where usertype='"+userType.toLowerCase()+"' and  user_id='"+userId+"' order by training_id desc  offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
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
	public int deleteTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "delete from training_final where id='" + map.get("id") + "'";
			/**
			 * print log
			 */
			logger.info("Delete Training Details",sql);
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			/**
			 *  print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int updateTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "update training_final set entry_date='"+map.get("proposedDate")+"',name='"+map.get("facultyName")+"',training_venue='"+map.get("venue")+"',training_subject='"+map.get("trainingSubject")+"',number_of_trainees='"+map.get("trainessNumber")+"',target_group='"+map.get("targetGroup")+"' where training_id='"+map.get("id")+"'";
			/**
			 * print log
			 */
			logger.info("Update Training Details", sql);
			return jdbcTemplate.update(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.info("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCalendarInfoById(String id) {
		try {
			String sql ="SELECT enddate AS \"endDate\", entry_date AS \"proposedDate\", " +
		             "name AS \"facultyName\", " +
		             "training_venue AS \"venue\", " +
		             "training_subject AS \"trainingSubject\", " +
		             "number_of_trainees AS \"trainessNumber\", " +
		             "target_group AS \"targetGroup\" " +
		             "FROM training_final " +
		             "WHERE training_id = '" + id + "'";
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
	public int deleteCalendarInfoById(String id) {
		try {
			String sql = "delete from training_final where training_id='"+id+"'";
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
	
	public int getfacultyCount()
	{
		try
		{
			String sql="select count(*) as count from faculty";
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
	
	public int getTrainingCalendarCount(int userId,String userType)
	{
		try
		{
			String sql="select count(*) as count from training_final where usertype='"+userType.toLowerCase()+"' and  user_id='"+userId+"'";
			return jdbcTemplate.queryForObject(sql,Integer.class);
		}catch(Exception e)
		{
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
			
			String sql="select"+" "+map.get("key")+"_"+ "id from"+" "+tableName+" "+"where username='"+map.get("userName")+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key")+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	

}
