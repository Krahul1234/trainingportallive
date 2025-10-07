package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.TrainingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainingService {
	
	@Autowired
	public TrainingDao trainingdao;
	
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
	public List<Map<String, Object>> getAllTrainingDetails(int pageSize,@RequestParam int pageNumber,String userName,String userType,String userId,String financialYear,String installmentType) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getTrainingDetails(pageSize,pageNumber,userName,userType,userId,financialYear,installmentType);
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
	public List<Map<String, Object>> getCalendarInfoById(int id) {
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
	
	
	public String  addForwardCombinedProposalNew(Map<String,Object>map)
	{
		try
		{
			return  trainingdao.addForwardCombinedProposalNew(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Forwarded";
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
	
	public List<Map<String, Object>> getBackwardProposalForSirdNew(String userType,String userName) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getBackwardProposalForSirdNew(userType,userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String, Object>> getApprovedProposalForAsNew() {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getApprovedProposalForAsNew();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String,Object>> GetTrainingCalendarWithCount()
	{
		try
		{
			return  trainingdao.GetTrainingCalendarWithCount();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	public List<Map<String,Object>> GetTrainingCalendarWithCountWithFinnancialYear(String date,int pageSize,int pageNumber)
	{
		try
		{
			return  trainingdao.GetTrainingCalendarWithCountWithFinnancialYear(date,pageSize,pageNumber);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	public String addForwardCombinedProposalNewNonRecurring(Map<String,Object>map)
	{
		try
		{
			return  trainingdao.addForwardCombinedProposalNewNonRecurring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "0";
	}
	
	
	public List<Map<String,Object>> GetAllCombinedListNewNonRecuring(String userType)
	{
		try
		{
			return  trainingdao.GetAllCombinedListNewNonRecuring(userType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	public List<Map<String, Object>> getBackwardProposalForSirdNewNonRecurring(String userType,String userName) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getBackwardProposalForSirdNewNonRecurring(userType,userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String, Object>> getApprovedProposalForAsNewNonRecurring() {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getApprovedProposalForAsNewNonRecurring();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public String deleteUpdateTrainingCalendar(Map<String,Object>map) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.deleteUpdateTrainingCalendar(map);
		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}
	}
	
	
	public List<Map<String, Object>>getBackwardProposalForAllRecurring(String userType) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getBackwardProposalForAllRecurring(userType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String, Object>>getSanctionedAmountStatusRecurring(String combinedProposalId) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getSanctionedAmountStatusRecurring(combinedProposalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

}
