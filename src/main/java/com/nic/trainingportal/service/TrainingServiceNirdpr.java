package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.TrainingDaoNirdpr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainingServiceNirdpr {
	
	@Autowired
	public TrainingDaoNirdpr trainingdao;
	
	/**
	 * add training details
	 * 
	 * @param map
	 * @return
	 */
	public int addTrainingDetails(Map<String, Object> map) {
		try {
			return trainingdao.addTrainingDetails(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get all training details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllTrainingDetails(int pageSize,@RequestParam int pageNumber) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getTrainingDetails(pageSize,pageNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * update training details
	 * 
	 * @param map
	 * @return
	 */
	public int updateTrainingDetails(Map<String, Object> map) {
		try {
			/**
			 * update faculty
			 */
			return trainingdao.updateTrainingDetails(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public int deleteCalendarInfoById(String id) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.deleteCalendarInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCalendarInfoById(String id) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getCalendarInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public List<Map<String, Object>> getCalendar(String userName,String userType) {
        try {
            /**
             * get faculty
             */
            return trainingdao.getCalendar(userName,userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
	
	
	public int addForwardCombinedProposalNew(Map<String,Object>map)
	{
		try
		{
			return  trainingdao.addForwardCombinedProposalNew(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>> getAllCombinedList(String userType)
	{
		try
		{
			return  trainingdao.getAllCombinedDetails(userType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAggregatedProposalCounts() {
        try {
              return trainingdao.getAggregatedProposalCounts();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (List<Map<String, Object>>) new  HashMap<String,Object>();
        
    }
	

}
