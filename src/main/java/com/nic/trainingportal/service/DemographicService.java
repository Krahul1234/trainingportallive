package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.DemographicDao;

@Service
public class DemographicService {
	
	@Autowired
	public DemographicDao demographicdao;
	public int addDemographicDetails(Map<String,Object>map)
	{
		try
		{
			return demographicdao.addDemographicDetails(map);
			
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
			return demographicdao.getDemographicDetails();
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
			return demographicdao.updateDemographicDetails(map);	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

}
