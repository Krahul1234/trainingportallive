package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.DemographicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	public List<Map<String,Object>>getDemographicDetails(int pageSize,int pageNumber,int userId,String userType,String userName)
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.getDemographicDetails(pageSize,pageNumber,userId,userType,userName);
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
	
	
	public List<String>GetProposalInstallment(String userType,String userName,String financialYear)
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.GetProposalInstallment(userType,userName,financialYear);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public int GetStateCode(String userType,String userName)
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.GetStateCode(userType,userName);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<String>GetProposalInstallmentNonRecurring(String userType,String userName)
	{
		try
		{
			/**
			 *  get data from database
			 */
			return demographicdao.GetProposalInstallmentNonRecurring(userType,userName);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public List<Map<String, Object>> getDemographicFinal() {
        try {
            return demographicdao.getDemographicFinal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getProposalVsApproval() {
        try {
            return demographicdao.getProposalVsApproval();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
    
    
    public List<Map<String,Object>> sirdSanction() {
		// TODO Auto-generated method stub
		try {
			return demographicdao.sirdSanction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public List<Map<String,Object>> etcSanction() {
		try {
			return demographicdao.etcSanction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	public List<Map<String, Object>> proVsAppCount() {
        // TODO Auto-generated method stub
        try {
            return demographicdao.proVsAppCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
	
	public List<Map<String, Object>> proposalApproval() {
        try {
            return demographicdao.proposalApproval();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
	
	public String updateDemographicProposal(Map<String,Object>map) {
		// TODO Auto-generated method stub
		try {
			return demographicdao.updateDemographicProposal(map);
		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}
	}
	public List<Map<String, Object>> remarksReport(Map<String,Object>map) {
		// TODO Auto-generated method stub
		try {
			return demographicdao.remarksReport(map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String, Object>>(0);
		}
	}
	
}
