package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemographicDao {
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	public int  addDemographicDetails(Map<String,Object>map)
	{
		try
		{
			String sql="insert into demographic (usertype,sird_id,total_population_state,total_rural_population,percent_rural_population,state_code)"
					+ "values('"+map.get("userType")+"','"+map.get("sirdId")+"',"+map.get("totalPopulation")+","+map.get("totalRuralPopulation")+","+map.get("percentOfRural")+","+map.get("stateCode")+")";
			
			return jdbctemplate.update(sql);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getDemographicDetails()
	{
		try
		{
			String sql="select * from demographic";
			return jdbctemplate.queryForList(sql);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateDemographicDetails(Map<String,Object>map)
	{
		try
		{
		 String sql="update demographic set usertype='"+map.get("userType")+"',sird_id='"+map.get("sirdId")+"',total_population_state="+map.get("totalPopulation")+",total_rural_population="+map.get("totalRuralPopulation")+",percent_rural_population="+map.get("percentOfRural")+",state_code="+map.get("stateCode")+" where id='"+map.get("id")+"'";
			return jdbctemplate.update(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

}
