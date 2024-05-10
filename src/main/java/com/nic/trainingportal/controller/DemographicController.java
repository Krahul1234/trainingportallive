package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.DemographicService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

@RestController
public class DemographicController {
	
	@Autowired
	public DemographicService demographicservice;
	
	/**
	 * Add Demographic Details
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping(value =Webhook.addDemographicDetails)
	public Map<String, Object> addFaculty(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalPopulation"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Total Population");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalRuralPopulation"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("percentOfRural"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("stateCode"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide  State Code value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("userType"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide User Type");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("sirdId"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Sird Id");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.statusCode,demographicservice.addDemographicDetails(map));
			return dataMap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}
	
	@GetMapping(value =Webhook.getDemographicDetails)
	public Map<String,Object> getDemographicDetails() 
	{
		Map<String,Object>map=new HashMap<String,Object>(4);
		try
		{
			map.put("Data",demographicservice.getDemographicDetails());
			return map;
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);
		
	}
	
	/**
	 * Add Demographic Details
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping(value =Webhook.updateDemographicDetails)
	public Map<String, Object> updateDemographicDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalPopulation"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Total Population");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalRuralPopulation"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("percentOfRural"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("stateCode"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide  State Code value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("userType"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide User Type");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("sirdId"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Sird Id");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.statusCode,demographicservice.updateDemographicDetails(map));
			return dataMap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}


}
