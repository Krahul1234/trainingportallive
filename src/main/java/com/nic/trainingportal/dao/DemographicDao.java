package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemographicDao {
	private static final Logger logger = LoggerFactory.getLogger(DemographicDao.class);
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	public int  addDemographicDetails(Map<String,Object>map)
	{
		try
		{
			int sird_id=this.getUserId(map);
			String sql="insert into demographic(total_population_state,total_rural_population,percent_rural_population,state_code,usertype,user_id) values(?,?,?,?,?,?)";
			return jdbctemplate.update(sql,map.get("totalPopulation"),map.get("totalRuralPopulation"),map.get("percentOfRural"),map.get("stateCode"),map.get("key").toString().toLowerCase(),sird_id);
			
		}catch(Exception e)
		{  /**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getDemographicDetails(int pageSize,int pageNumber,int userId,String userType)
	{
		try
		{
			String sql = "select * from demographic where user_id='"+userId+"' and usertype='"+userType.toLowerCase()+"' order by id offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
			/**
			 * print log
			 */
			logger.info("Get Demographic Details", sql);
			return jdbctemplate.queryForList(sql);
			
		}catch(Exception e)
		{
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateDemographicDetails(Map<String,Object>map)
	{
		try
		{
//		 String sql="update demographic set usertype='"+map.get("userType")+"',sird_id='"+map.get("sirdId")+"',total_population_state='"+map.get("totalPopulation")+"',total_rural_population='"+map.get("totalRuralPopulation")+"',percent_rural_population='"+map.get("percentOfRural")+"',state_code='"+map.get("stateCode")+"' where id='"+map.get("id")+"'";
//		 /**
//			 * print log
//			 */
//		 logger.info("Update Demographic Details", sql);
//		return jdbctemplate.update(sql);
		String sql="update demographic set total_population_state=?,total_rural_population=?,percent_rural_population=?,state_code=? where id=?";	
		return jdbctemplate.update(sql,map.get("totalPopulation"),map.get("totalRuralPopulation"),map.get("percentOfRural"),map.get("stateCode"),map.get("id"));
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
			
			return Integer.parseInt(jdbctemplate.queryForList(sql).get(0).get(map.get("key")+"_"+"id").toString());			
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
	public List<Map<String, Object>> getDemographicById(String id) {
		try {
			String sql = "SELECT total_population_state AS \"totalPopulation\", " +
		               "total_rural_population AS \"totalRuralPopulation\", " +
		               "percent_rural_population AS \"percentOfRural\", " +
		               "state_code AS \"stateCode\" " +
		               "FROM demographic WHERE id='" + id + "'";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql);
			
			return jdbctemplate.queryForList(sql);
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
	public int deleteDemographicById(String id) {
		try {
			String sql = "delete from demographic where id='"+id+"'";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql);
			
			return jdbctemplate.update(sql);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getDemographicCount()
	{
		try
		{
			String sql="select count(*) as count from demographic";
			return jdbctemplate.queryForObject(sql,Integer.class);
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
			
			return Integer.parseInt(jdbctemplate.queryForList(sql).get(0).get(userType+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public int getDemographicCount(int userId,String userType)
	{
		try
		{
			String sql="select count(*) as count from demographic where userType='"+userType.toLowerCase()+"' and  user_id='"+userId+"'";
			return jdbctemplate.queryForObject(sql,Integer.class);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>> getFinancialYear()
	{
		try
		{
			String sql="select fin_year from financial_year";
			return jdbctemplate.queryForList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}


}
