package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.dao.ProposalDao;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.ProposalService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ao/trainingportal")
public class ProposalController {
	
	@Autowired
	public Utility utility;
	
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	private ProposalService proposalservice;
	
	@Autowired
	private ProposalDao dao;
	
	/** get DemographicDetails
	 * @return
	 */
	@PostMapping(value =Webhook.getProposal)
	public Map<String,Object> getDemographicDetails(@RequestBody Map<String,Object>map) 
	{
		Map<String,Object>dataMap=new HashMap<String,Object>(4);
		try
		{ 
			/**
			 *  check token is valid or not
			 */
			if(utility.getHeaderValue(httpservletrequest))
			 {
				dataMap.put(Literal.status,Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			 }
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("Data",proposalservice.getProposal(map));
			return dataMap;
		
		}catch(Exception e)
		{
			map.put(Literal.status,Literal.errorCode);
			map.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return map;
		}
		
	}
	
	/** get DemographicDetails
	 * @return
	 */
	@PostMapping(value =Webhook.forwardProposal)
	public Map<String,Object>forwardPorposal(@RequestBody Map<String,Object>map) 
	{
		Map<String,Object>dataMap=new HashMap<String,Object>(4);
		try
		{ 
//			/**
//			 *  check token is valid or not
//			 */
//			if(utility.getHeaderValue(httpservletrequest))
//			 {
//				dataMap.put(Literal.status,Literal.unauthorized);
//				dataMap.put(Literal.message, "Your Token Is Expired");
//				return dataMap;
//			 }
			
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put(Literal.data, proposalservice.addForwardProposal(map));
	
			return dataMap;
		
		}catch(Exception e)
		{
			map.put(Literal.status,Literal.errorCode);
			map.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return map;
		}
		
	
	}
	
	
	@GetMapping(value =Webhook.getForwardProposal)
	public Map<String,Object> getForwardProposal(@RequestParam String userType,@RequestParam String userName) 
	{
		Map<String,Object>dataMap=new HashMap<String,Object>(4);
		try
		{ 
			/**
			 *  check token is valid or not
			 */
//			if(utility.getHeaderValue(httpservletrequest))
//			 {
//				dataMap.put(Literal.status,Literal.unauthorized);
//				dataMap.put(Literal.message, "Your Token Is Expired");
//				return dataMap;
//			 }
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("Data",proposalservice.getForwardProposal(userType,userName));
			return dataMap;
		
		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
		
	}
	
	@GetMapping(value =Webhook.updateForwardProposal)
	public Map<String,Object> updateForwardProposal(@RequestBody Map<String,Object>map) 
	{
		Map<String,Object>dataMap=new HashMap<String,Object>(4);
		try
		{ 
			/**
			 *  check token is valid or not
			 */
			if(utility.getHeaderValue(httpservletrequest))
			 {
				dataMap.put(Literal.status,Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			 }
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("Data",proposalservice.updateForwardProposal(map));
			return dataMap;
		
		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
		
	}
	
	@PostMapping("addProposalNew")
	private Map<String,Object> addProposal(@RequestBody Map<String,Object> map) {
		Map<String,Object> dataMap = new HashMap<>();
		try {
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					dataMap.put(Literal.statusCode, Literal.zero);
//					
//					return dataMap;
//			 }
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data,proposalservice.addProposal(map));
				return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);	
	}
	@GetMapping("getAllProposal")
	public Map<String,Object> getProposal(@RequestParam String userId) 
	
	{
		Map<String,Object> dataMap = new HashMap<>();
		try {
			
//			if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					dataMap.put(Literal.statusCode, Literal.zero);
//					
//					return dataMap;
//			 }
			
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data,proposalservice.getProposalNew(userId));
			return dataMap;
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new HashMap<String,Object>(0);
		
	}
	
	
	@PostMapping(value=Webhook.addCombinedProposal)
	private Map<String,Object> addCombinedProposal(@RequestBody Map<String,Object>map) {
		Map<String,Object> dataMap = new HashMap<>();
		try {
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					dataMap.put(Literal.statusCode, Literal.zero);
//					
//					return dataMap;
//			 }
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data,proposalservice.addCombinedProposal(map));
				return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);	
	}
	
	
	@GetMapping("getCombinedProposal")
	public Map<String,Object> getCombinedProposal(@RequestParam String userType,@RequestParam String userName) 
	
	{
		Map<String,Object> dataMap = new HashMap<>();
		try {
			
//			if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					dataMap.put(Literal.statusCode, Literal.zero);
//					
//					return dataMap;
//			 }
			
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data,proposalservice.getCombinedProposal(userType,userName));
			return dataMap;
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return new HashMap<String,Object>(0);
		
	}
		
	
}

