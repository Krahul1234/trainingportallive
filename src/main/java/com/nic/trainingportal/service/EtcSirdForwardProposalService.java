package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.EtcSirdForwardProposalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EtcSirdForwardProposalService {
	
	@Autowired
	private EtcSirdForwardProposalDao etcsirddao;
	
	public List<Map<String, Object>> getforwardEtcProposal(String userName,String userType,String installmentType,String financialYear) {
		try {
			/**
			 * get faculty
			 */
			return etcsirddao.getforwardEtcProposal(userName,userType,installmentType,financialYear);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public String addForwardProposalNew(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.addForwardProposalNew(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Forwarded";
	}
	
	
	public String updateForwardProposalNew(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.updateForwardProposalNew(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "Not Forwarded";
	}
	
	public List<Map<String,Object>> getForwardProposalForSird(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getForwardProposalForSird(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>> getForwardProposalForEtc(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getForwardProposalForEtc(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>> getBackwardProposalForEtc(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getBackwardProposalForEtc(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String, Object>> getforwardEtcProposalNonRecuring(String userName,String userType) {
		try {
			/**
			 * get faculty
			 */
			return etcsirddao.getforwardEtcProposalNonRecuring(userName,userType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public String addForwardProposalNonRecurring(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.addForwardProposalNonRecurring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "1";
	}
	
	
	public int updateForwardProposalNewNonRecurring(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.updateForwardProposalNewNonRecurring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<Map<String,Object>> getForwardProposalForSirdNonRecurring(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getForwardProposalForSirdNonRecurring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	public List<Map<String,Object>> getForwardProposalForEtcNonRecurring(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getForwardProposalForEtcNonRecurring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	public List<Map<String,Object>> getBackwardProposalForEtcNonRecuring(Map<String,Object>map)
	{
		try
		{
			return  etcsirddao.getBackwardProposalForEtcNonRecuring(map);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

}
