package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.ProposalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		int userIdInt= Integer.parseInt(map.get("userId").toString());
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
			
			String sql="select \"Name\" as \"sirdName\",contact_number as \"mobileNo\" ,email from"+" "+tableName+" "+" where"+ " "+map.get("key")+"_id= ?";
			return 	jdbctemplate.queryForList(sql, userIdInt);
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
	
	public String addProposal(Map<String,Object> map) {
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
		return "Record Not Saved";

}
	
	public List<Map<String, Object>>getProposalNew(String proposalId) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.getProposal(proposalId);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public String addCombinedProposal(Map<String,Object>map) {
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
		return "0";

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
	
	public List<Map<String, Object>>getCombinedProposal(String userType,String userName) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.getCombinedProposal(userType,userName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public int checkProposal(String userType,String userName) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.checkProposal(userType,userName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int addSantioned(Map<String,Object> map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addSantioned(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

}
	
	
	public Map<String,Object> getUpperLevel(String userType) {
		try
		{
			Map<String,Object>levelMap=new HashMap<String,Object>();
			if(userType.equals("sird"))
			{
				levelMap.put("key", "so");
				levelMap.put("value", "MORD(Section Officer)");
			}else if(userType.equals("MORD(Section Officer)"))
			{
				levelMap.put("key", "us");
				levelMap.put("value", "MORD(Under Secretary)");
			}else if(userType.equals("MORD(Under Secretary)"))
			{
				levelMap.put("key", "ds");
				levelMap.put("value", "MORD(Deputy Secretary)");
			}else if(userType.equalsIgnoreCase("MORD(Deputy Secretary)"))
			{
				levelMap.put("key", "as");
				levelMap.put("value", "MORD(Additional Secretary)");
				levelMap.put("value1","Sird");
			}
			else
			{
				levelMap.put("key", "as");
				levelMap.put("value", "MORD(Additional Secretary)");
			}
		   return levelMap;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);

}
	
	public int updateCombinedProposalStatus(String proposalNo,String userType)
	{
		try
		{
			return   proposaldao.updateCombinedProposalStatus(proposalNo,userType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>> getAllCombinedList(String userType,String approved)
	{
		try
		{
			return  proposaldao.getAllCombinedDetails(userType,approved);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateforwardProposalCombined(String combinedProposalId,String remarks,String status,String approved)
	{
		try
		{
			return  proposaldao.updateforwardProposalCombined(combinedProposalId,remarks,status,approved);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>> GetCombinedListBySird(String userType,String userName)
	{
		try
		{
			return  proposaldao.getCombinedDetailsBySird(userType,userName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>> GetAllEtcsByCombinedProposalId(String combinedProposalId)
	{
		try
		{
			return  proposaldao.GetAllEtcsByCombinedProposalId(combinedProposalId);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String, Object>> getSirdDetailsByCombinedId(String combinedProposalId) {
		try {
			/**
			 * get faculty
			 */
			return proposaldao.getSirdDetailsbyCombinedId(combinedProposalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public List<Map<String, Object>> checkCombinedProposal(String userName,String userType) {
		try {
			/**
			 * get faculty
			 */
			return proposaldao.checkCombinedProposal(userName,userType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String, Object>> BackwardCombinedProposalByDs(String userName,String userType) {
		try {
			/**
			 * get faculty
			 */
			return proposaldao.BackwardCombinedProposalByDs(userName,userType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public Map<String,Object> getCombinedPropoalBoolean(String userType,String userName,String proposalType) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.getCombinedPropoalBoolean(userType,userName,proposalType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>();
	}
	
	
	public int updateforwardProposalCombinedByDs(String combinedProposalId,String remarks,String status)
	{
		try
		{
			return  proposaldao.updateforwardProposalCombinedByDs(combinedProposalId,remarks,status);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public String updateProposal(Map<String,Object> map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.updateProposal(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Update";

}
	
	public String addCombinedProposalNew(Map<String,Object>map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addCombinedProposalNew(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Generate Combined";

}
	
	public List<Map<String,Object>> GetRemarks(String proposalId,String userName,String userType,String backwarded,String financialYear,String installmentType)
	{
		try
		{
			return  proposaldao.GetRemarks(proposalId,userName,userType,backwarded,financialYear,installmentType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateSantioned(Map<String,Object> map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.updateSanctioned(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

}
	
	
	public List<Map<String, Object>> proposalCountRec( String status,String userType) {
        try {
              return proposaldao.proposalCountRec(status,userType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (List<Map<String, Object>>) new  HashMap<String,Object>();
        
    }
    public List<Map<String, Object>> proposalCountNonRec( String status,String userType) {
        try {
              return proposaldao.proposalCountNonRec(status,userType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (List<Map<String, Object>>) new  HashMap<String,Object>();
        
    }
    
    public String addCombinedProposalNewNonRecuring(Map<String,Object>map) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.addCombinedProposalNewNonRecuring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Success";

}
    
    public Map<String,Object> getCombinedPropoalBooleanNonRecurring(String userType,String userName,String proposalType) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.getCombinedPropoalBooleanNonRecurring(userType,userName,proposalType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);
	}
    
    
    public List<Map<String, Object>> getProposal1() {
        try {
              return proposaldao.getPropopsal1();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (List<Map<String, Object>>) new  HashMap<String,Object>();
        
    }
    public List<Map<String, Object>> getNonProposal1() {
        try {
              return proposaldao.getNonPropopsal1();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (List<Map<String, Object>>) new  HashMap<String,Object>();
        
    }
    
    public List<Map<String,Object>> GetCombinedListBySirdNonRecurring(String userType,String userName)
	{
		try
		{
			return  proposaldao.getCombinedDetailsBySirdNonRecurring(userType,userName);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
    
    
    public List<Map<String,Object>> GetAllCombinedListNonRecurring(String userType,String approved)
	{
		try
		{
			return  proposaldao.GetAllCombinedListNonRecurring(userType,approved);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
    
    public int updateforwardProposalCombinedNonRecurring(String combinedProposalId,String remarks,String status,String approved)
	{
		try
		{
			return  proposaldao.updateforwardProposalCombinedNonRecurring(combinedProposalId,remarks,status,approved);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
    
    
    public String checkSanctionAmountExist(String proposalId) {
		try
		{
			/**
			 *  insert data into database
			 */
			return proposaldao.checkSanctionAmountExist(proposalId);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
    
    
    public List<Map<String,Object>> GetAllEtcsByCombinedProposalIdNonRecurring(String combinedProposalId)
	{
		try
		{
			return  proposaldao.GetAllEtcsByCombinedProposalIdNonRecurring(combinedProposalId);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
    
    public List<Map<String, Object>> getSirdDetailsByCombinedIdNonRecurring(String combinedProposalId) {
		try {
			/**
			 * get faculty
			 */
			return proposaldao.getSirdDetailsbyCombinedIdNonRecurring(combinedProposalId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
    
    
    public Map<String, Object> proposalCount( String userName,String userType) {
        try {
               return proposaldao.proposalCount(userName,userType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
        
    }
    
    
    public List<Map<String,Object>> GetRemarksNonRecurring(String proposalId,String userName,String userType,String backwarded,
														   String financialYear)
	{
		try
		{
			return  proposaldao.GetRemarksNonRecurring(proposalId,userName,userType,backwarded,financialYear);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

}

