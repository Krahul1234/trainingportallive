package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.dao.TrainingDao;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ao/trainingportal")
public class TrainingController {
	/**
	 * create logger class object
	 */
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	public TrainingDao trainingdao;
	
	@Autowired
	public Utility utility;
	 private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);
	@Autowired
	public TrainingService trainingservice;
	@PostMapping(value=Webhook.addTraningDetails)
	public Map<String, Object>addTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					return dataMap;
//			 }
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("proposedDate"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Proposed Date");
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("endDate"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide endDate");
				return dataMap;
			}
			

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("facultyName"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("venue"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Venue Details");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainingSubject"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Training Subject");
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainessNumber"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Number Of Trainees");
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("targetGroup"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Target group Elected representative of Panchayati Raj");
				return dataMap;
			}
			
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("StatusCode",trainingservice.addTrainingDetails(map));
			dataMap.remove(Literal.statusCode);
			dataMap.put(Literal.message,"Record Insert Successfully");
			return dataMap;
	

		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
		
		
}
	
	@GetMapping(value = "GetAllTrainingDetails")
	public Map<String, Object> getAllTrainingDetails(@RequestParam int pageSize,@RequestParam int pageNumber,@RequestParam String userName,@RequestParam String userType) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
			 if(utility.getHeaderValue(httpservletrequest))
			 {
				    dataMap.put(Literal.status, Literal.unauthorized);
					dataMap.put(Literal.message, "Your Token Is Expired");
					return dataMap;
			 }
			dataMap.put(Literal.status,Literal.successCode);
			int userId=trainingdao.getUserId(userName, userType);
			dataMap.put("Data",trainingservice.getAllTrainingDetails(pageSize,pageNumber,userName,userType,userId));
			dataMap.put("totalCount",trainingdao.getTrainingCalendarCount(userId,userType));
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			return dataMap;
		}
	}
	
	@PostMapping(value =Webhook.updateTrainingDetails)
	public Map<String, Object> updateDeleteTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
			 if(utility.getHeaderValue(httpservletrequest))
			 {
				    dataMap.put(Literal.status, Literal.unauthorized);
					dataMap.put(Literal.message, "Your Token Is Expired");
					return dataMap;
			 }
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("proposedDate"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Proposed Date");
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("facultyName"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("venue"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Venue Details");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainingSubject"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Training Subject");
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainessNumber"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Number Of Trainees");
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("targetGroup"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Target group Elected representative of Panchayati Raj");
				return dataMap;
			}
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("StatusCode",trainingservice.updateTrainingDetails(map));
			dataMap.remove(Literal.statusCode);
			return dataMap;
	

		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
	}
	
@GetMapping(value = Webhook.getCalendarInfoById)
	
	public Map<String,Object>getCalendarInfoById(@RequestParam String id)
	
	{
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try
		{
			/**
			 *  check token is valid or not
			 */
			 if(utility.getHeaderValue(httpservletrequest))
			 {
				    dataMap.put(Literal.status, Literal.unauthorized);
					dataMap.put(Literal.message, "Your Token Is Expired");
					return dataMap;
			 }
			 
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data, trainingservice.getCalendarInfoById(id));
				/**
				 * print log
				 */
				logger.info("Get All Faculty", dataMap);
				return dataMap;
			 
		}catch(Exception e)
		{
			e.printStackTrace();
			dataMap.put(Literal.message,"Something Went Wrong");
            dataMap.put(Literal.status,Literal.errorCode);
            return dataMap;
		}
	}

@GetMapping(value = Webhook.deleteCalendarInfoById)

public Map<String,Object>deleteDemographicById(@RequestParam String id)

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
		/**
		 *  check token is valid or not
		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
		 
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.deleteCalendarInfoById(id));
			/**
			 * print log
			 */
			logger.info("Get All Faculty", dataMap);
			return dataMap;
		 
	}catch(Exception e)
	{
		e.printStackTrace();
		dataMap.put(Literal.message,"Something Went Wrong");
        dataMap.put(Literal.status,Literal.errorCode);
        return dataMap;
	}
}
}
