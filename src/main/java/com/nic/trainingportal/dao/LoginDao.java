package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	public List<Map<String,Object>>getLoginDetails(Map<String,Object>map)
	{
		String tableName="";
		try
		{
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
			
			String sql="select username,user_password from"+" "+tableName+" "+"where username='"+map.get("username")+"'and user_password='"+map.get("password")+"'";
			
			return jdbctemplate.queryForList(sql);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

}
