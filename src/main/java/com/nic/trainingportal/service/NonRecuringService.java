package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.NonRecuringDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NonRecuringService {
	@Autowired
	private NonRecuringDao nonrecuring;
	public String addRecurringProposal(Map<String, Object> map) {
		try
		{
		    /**
		     *  insert data into database
		     */
		    return nonrecuring.addRecurringProposal(map);

		}catch(Exception e)
		{
		    e.printStackTrace();
		}
		return "0";
		  }
	
	
	public List<Map<String, Object>> getNonRecurringProposal(String proposalId) {
		// TODO Auto-generated method stub
		try
		{
		    /**
		     *  insert data into database
		     */
		    return nonrecuring.getNonRecurringProposal(proposalId);

		}catch(Exception e)
		{
		    e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
		}
	
	public String updateNonRecurringProposal(Map<String, Object> map) {
		try
		{
		    /**
		     *  insert data into database
		     */
		    return nonrecuring.updateNonRecurringProposal(map);

		}catch(Exception e)
		{
		    e.printStackTrace();
		}
		return "0";
		  }
	}
