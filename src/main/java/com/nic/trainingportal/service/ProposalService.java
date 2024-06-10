package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.ProposalDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class ProposalService {
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	@Autowired
	private ProposalDao proposaldao;
	
	public Map<String,Object>getProposal(Map<String,Object>map)
	{
		Map<String,Object>dataMap=new HashMap<String,Object>();
		try
		{
			int userId=this.getUserId(map);
			map.put("userId",userId);
			dataMap.put(map.get("key")+"Information",this.getUserDetails(map));
			dataMap.put("demographicProfile",getDemographicDetails(map));
			dataMap.put("instituteInfo",getTrainingInstituteDetails(map));
			return dataMap;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);
		
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
	
	public List<Map<String,Object>> getUserDetails(Map<String,Object>map)
	{
		try
		{
			String tableName="";
			/**
			 *  check login type
			 */
			if(map.get("key").toString().equalsIgnoreCase("etc"))
			{
				tableName="etc";
			}else if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				tableName="sird";
			}else
			{
				tableName="ministry";
			}
			
			String sql="select \"Name\" as \"sirdName\",contact_number as \"mobileNo\" ,email from"+" "+tableName+" "+" where"+ " "+map.get("key")+"_id='"+map.get("userId")+"'";
			return 	jdbctemplate.queryForList(sql);	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>> getDemographicDetails(Map<String,Object>map)
	
	{
		try
		{
			return proposaldao.getDemographicDetails(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>> getTrainingInstituteDetails(Map<String,Object>map)
	{
		try
		{
			return proposaldao.getTrainingInfo(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>>getFacultyDetails(Map<String,Object>map)
	{
		try
		{
			return proposaldao.getFacultyDetails(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>>getTrainingCalendarDetails(Map<String,Object>map)
	{
		try
		{
			return proposaldao.getTrainingCalendarDetails(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	
	public List<Map<String,Object>>getForwardProposal(String userType,String userName)
	{
		try
		{
			return proposaldao.getForwradProposal(userType,userName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateForwardProposal(Map<String,Object>map)
	{
		try
		{
			return proposaldao.updateForwradProposal(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int addProposal(Map<String,Object> map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addProposal(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

}
	
	public List<Map<String, Object>>getProposalNew(String userId) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.getProposal(userId);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public int addCombinedProposal(Map<String,Object>map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addCombinedProposal(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

}
	
	public int addForwardProposal(Map<String,Object>map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addForwardProposal(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

}
}
