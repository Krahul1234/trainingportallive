package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.HashMap;
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
			/**
			 *  insert data into database
			 */
			return demographicdao.addDemographicDetails(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getDemographicDetails(int pageSize,int pageNumber,int userId,String userType)
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.getDemographicDetails(pageSize,pageNumber,userId,userType);
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
			/**
			 * Update data into database
			 */
			return demographicdao.updateDemographicDetails(map);	
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDemographicById(String id) {
		try {
			/**
			 * get faculty
			 */
			return demographicdao.getDemographicById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public int deleteDemographicById(String id) {
		try {
			/**
			 * get faculty
			 */
			return demographicdao.deleteDemographicById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getFinnancialYear()
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.getFinancialYear();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

}
