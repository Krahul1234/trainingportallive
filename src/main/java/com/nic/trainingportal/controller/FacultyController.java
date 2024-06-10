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

import com.nic.trainingportal.dao.FacultyDao;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.FacultyService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/ao/trainingportal")
public class FacultyController {
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	public FacultyDao facultydao;
	
	@Autowired
	public Utility utility;
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);
	@Autowired
	public FacultyService facultyservice;
	/**
	 * Add Faculty details
	 * 
	 * @param map
	 * @return
	 */
	@PostMapping(value = Webhook.addFaculty)
	public Map<String, Object> addFaculty(@RequestBody Map<String, Object> map) {
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
		
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("name"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("postHeld"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Postheld Value");
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("scalePay"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Scale Pay Value");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("typeOfFaculty"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Value");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("remarks"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Remarks Value");
				return dataMap;
			}
			/**
			 * print log
			 */
			logger.info("Add Faculty Request Data", map);
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.statusCode, facultyservice.addFaculty(map));
			dataMap.put(Literal.message,"Record insert successfully");
			dataMap.remove(Literal.statusCode);
			return dataMap;

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			   dataMap.put(Literal.message,"Something Went Wrong");
	            dataMap.put(Literal.status,Literal.errorCode);
	            return dataMap;
		}
	}

	/**
	 * Get Faculty Details
	 * 
	 * @return
	 */
	@GetMapping(value =Webhook.getAllFaculty)
	public Map<String, Object> getAllFaculty(@RequestParam int pageSize,@RequestParam int pageNumber,@RequestParam String userName,@RequestParam String userType) {
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
			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, facultyservice.getAllFaculty(pageSize,pageNumber,userName,userType));
			int userId=facultydao.getUserId(userName,userType);
			dataMap.put("totalCount",facultydao.getFacultyCount(userId,userType));
			/**
			 * print log
			 */
			logger.info("Get All Faculty", dataMap);
			return dataMap;
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			dataMap.put(Literal.message,"Something Went Wrong");
            dataMap.put(Literal.status,Literal.errorCode);
            return dataMap;
		}
	}

	@PostMapping(value =Webhook.updateFaculty)
	public Map<String, Object> updateDeleteFaculty(@RequestBody Map<String, Object> map) {
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
			 }else
			 {
							/**
							 * check Null
							 */
							if (Utility.checkNull(map.get("name"))) {
								dataMap.put(Literal.status, Literal.badReuqest);
								dataMap.put(Literal.message, "Kindly Provide Faculty Name");
								return dataMap;
							}
							/**
							 * check Null
							 */
							if (Utility.checkNull(map.get("postHeld"))) {
								dataMap.put(Literal.status, Literal.badReuqest);
								dataMap.put(Literal.message, "Kindly Provide Postheld Value");
								return dataMap;
							}

							/**
							 * check Null
							 */
							if (Utility.checkNull(map.get("scalePay"))) {
								dataMap.put(Literal.status, Literal.badReuqest);
								dataMap.put(Literal.message, "Kindly Provide Scale Pay Value");
								return dataMap;
							}
							/**
							 * check Null
							 */
							if (Utility.checkNull(map.get("typeOfFaculty"))) {
								dataMap.put(Literal.status, Literal.badReuqest);
								dataMap.put(Literal.message, "Kindly Provide Value");
								return dataMap;
							}
							/**
							 * check Null
							 */
							if (Utility.checkNull(map.get("remarks"))) {
								dataMap.put(Literal.status, Literal.badReuqest);
								dataMap.put(Literal.message, "Kindly Provide Remarks Value");
								return dataMap;
							}
							/**
							 * print log
							 */
							logger.info("Update Faculty Details", map);
							
							dataMap.put(Literal.status,Literal.successCode);
							dataMap.put(Literal.statusCode, facultyservice.updateFacultyDetails(map));
							dataMap.put(Literal.message,"Record  Update successfully");
							dataMap.remove(Literal.statusCode);
							return dataMap;

			 }
			
		} catch (Exception e) {
			/**
			 * print error log
			 */
			    logger.error("An error occurred while doing something", e);
			    e.printStackTrace();
			    dataMap.put(Literal.message,"Something Went Wrong");
	            dataMap.put(Literal.status,Literal.errorCode);
	            return dataMap;
		}
	}
	
	@GetMapping(value = Webhook.getFacultyById)
	
	public Map<String,Object>getFacultyById(@RequestParam String id)
	
	{
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try
		{
			/**
			 *  check token is valid or not
			 */
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					return dataMap;
//			 }
			 
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data, facultyservice.getFacultyById(id));
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
	
	@GetMapping(value = Webhook.deleteFacultyById)

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
				dataMap.put(Literal.data, facultyservice.deleteFacultyById(id));
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
	

@PostMapping(value = Webhook.getFacultyName)
	
	public Map<String,Object>getFacultyName(@RequestBody Map<String,Object>map)
	
	{
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try
		{
			/**
			 *  check token is valid or not
			 */
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status, Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					return dataMap;
//			 }
			 
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data, facultyservice.getFacultyName(map));
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
