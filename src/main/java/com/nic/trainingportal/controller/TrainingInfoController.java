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

import com.nic.trainingportal.dao.TrainingInfoDao;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingInfoService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ao/trainingportal")
public class TrainingInfoController {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(TrainingInfoController.class);
	@Autowired
	private TrainingInfoService trainingInfoService;
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	public Utility utility;
	@Autowired
	private TrainingInfoDao traininginfodao;
	@PostMapping(value = "addTrainingInfo")
	public Map<String, Object> addTrainingInfo(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status,Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					return dataMap;
//			 }

			if (Utility.checkNull(map.get("functional"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide functional");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("building"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide building availabilty");
				return dataMap;
			}

			
			if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly check number of permanent faculty member");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly check number of contractual faculty member");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_other_staff"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check the number of staff");
				return dataMap;
			}
			if (Utility.checkNull(map.get("hostel_facility"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check whether hostel facility is available or not");
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_seat"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check the seat Availability");
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of computer");
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of computer");
				return dataMap;
			}

			if (Utility.checkNull(map.get("hall_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of training hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("hall_capacity"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of training hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of dining hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of dining hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_kitchens"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of kitchens");
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of auditorium");
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of auditorium");
				return dataMap;
			}
			if (Utility.checkNull(map.get("Remarks"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "provide Remarks");
				return dataMap;
			}
            
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.statusCode, trainingInfoService.addTrainingInfo(map));
			dataMap.remove(Literal.statusCode);
			return dataMap;

		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			return dataMap;
		}
	}
	
    @GetMapping(value = "gettraininginfo")
	public Map<String, Object> getTrainingInfo(@RequestParam int pageSize,@RequestParam int pageNumber,@RequestParam String userName,@RequestParam String userType) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
			 if(utility.getHeaderValue(httpservletrequest))
			 {
				    dataMap.put(Literal.status,Literal.unauthorized);
					dataMap.put(Literal.message, "Your Token Is Expired");
					return dataMap;
			 }
			int userId=traininginfodao.getUserId(userName,userType);
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingInfoService.getAllFaculty(pageSize,pageNumber,userId,userType));
			dataMap.put("totalCount",traininginfodao.getTrainingInfoCount(userId,userType));
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			return dataMap;
		}
	}

    @PostMapping(value = "updatetraininginfo")
    public Map<String,Object> updateTrainingInfo(@RequestBody Map<String, Object> map) {
    	Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {

        	/**
			 *  check token is valid or not
			 */
			 if(utility.getHeaderValue(httpservletrequest))
			 {
				    dataMap.put(Literal.status,Literal.unauthorized);
					dataMap.put(Literal.message, "Your Token Is Expired");
					return dataMap;
			 }
			 
			if (Utility.checkNull(map.get("functional"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide functional");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("building"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide building availabilty");
				return dataMap;
			}

			
			if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly check number of permanent faculty member");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly check number of contractual faculty member");
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_other_staff"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check the number of staff");
				return dataMap;
			}
			if (Utility.checkNull(map.get("hostel_facility"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check whether hostel facility is available or not");
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_seat"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "check the seat Availability");
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of computer");
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_number"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of computer");
				return dataMap;
			}

			if (Utility.checkNull(map.get("hall_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of training hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("hall_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of training hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of dining hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_capacity"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of dining hall");
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_kitchens"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of kitchens");
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_number"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the number of auditorium");
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_capacity"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide the capacity of auditorium");
				return dataMap;
			}
			if (Utility.checkNull(map.get("Remarks"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "provide Remarks");
				return dataMap;
			}
            
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put("StatusCode",trainingInfoService.updateInformation(map));
			dataMap.remove(Literal.statusCode);
			return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			return dataMap;
        }
    }
    
    
@GetMapping(value = Webhook.getTrainingInfoById)
	
	public Map<String,Object>getDemographicById(@RequestParam String id)
	
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
				dataMap.put(Literal.data, trainingInfoService.getTrainingInfoById(id));
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

@GetMapping(value = Webhook.deleteTrainingInfoById)

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
			dataMap.put(Literal.data, trainingInfoService.deleteTrainingInfoById(id));
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
