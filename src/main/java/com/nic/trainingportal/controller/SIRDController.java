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

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.SIRDService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/ao/trainingportal")
public class SIRDController {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(SIRDController.class);
	@Autowired
	public Utility utility;
	@Autowired
	private HttpServletRequest httpservletrequest;
	@Autowired
	private SIRDService sirdService;
	 @GetMapping(value ="SirdDetails")
	    public Map<String, Object> sirddetails(@RequestBody Map<String,Object>map) {
	        Map<String, Object> dataMap = new HashMap<String, Object>(6);
	        try {
	        	/**
				 *  check token is valid or not
				 */
//				 if(utility.getHeaderValue(httpservletrequest))
//				 {
//					    dataMap.put(Literal.status, Literal.error);
//						dataMap.put(Literal.message, "Your Token Is Expired");
//						dataMap.put(Literal.statusCode, Literal.zero);
//						return dataMap;
//				 }
				 
	            dataMap.put(Literal.status, Literal.success);
	            dataMap.put(Literal.data, sirdService.getSirdDetails(map));
	            dataMap.put(Literal.statusCode, 1);
	            logger.info("Get Sird Details", dataMap);
	            return dataMap;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        /**
	         * return empty Hash Map
	         */
	        return new HashMap<String, Object>(0);
}
	 
	 @GetMapping(value ="GetSirdDetails")
	    public Map<String, Object> getsirddetails(@RequestParam int pageSize,@RequestParam int pageNumber) {
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
				 
	            dataMap.put(Literal.status, Literal.successCode);
	            dataMap.put(Literal.data, sirdService.sirdDetails(pageSize,pageNumber));
	            logger.info("Get Sird Details", dataMap);
	            return dataMap;
	        } catch (Exception e) {
	            e.printStackTrace();
	            dataMap.put(Literal.status,Literal.errorCode);
				dataMap.put("message","Something Went Wrong");
				return dataMap;
	        }
}
	 
	 @PostMapping(value ="updateSirdDetails")
	    public Map<String, Object> updateEtcDetails(@RequestBody Map<String,Object>map) {
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
				  *  check null
				  */
	             if(Utility.checkNull(map.get("name")))
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Name value is null");
						return dataMap;
	             }
	             /**
				  *  check null
				  */
	             if(Utility.checkNull(map.get("mobileNo")))
	             {
	            	    dataMap.put(Literal.status,  Literal.badReuqest);
						dataMap.put(Literal.message, "Mobile No  value is null");
						return dataMap;
	             }else if(! map.get("mobileNo").toString().matches("^[0-9]{10}$")) 
	             {
	            	    dataMap.put(Literal.status,  Literal.badReuqest);
						dataMap.put(Literal.message, "Please Enter Valid Mobile No");
						return dataMap;
	             }
	             
	             if(Utility.checkNotNull(map.get("alternateMobile")))
	             {
	              if(! map.get("alternateMobile").toString().matches("^[0-9]{10}$"))
	             {
	            	    dataMap.put(Literal.status,  Literal.badReuqest);
						dataMap.put(Literal.message, "Mobile No  value is null");
						return dataMap;
	             }
	             }
	             if(Utility.checkNull(map.get("emailId")))
	             {
	            	    dataMap.put(Literal.status,  Literal.badReuqest);
						dataMap.put(Literal.message, "Email Id is Null");
						return dataMap;
	             }else if(! map.get("emailId").toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
	             {
	            	    dataMap.put(Literal.status,  Literal.badReuqest);
						dataMap.put(Literal.message, "Please Enter Valid Email Id");
						return dataMap;
	             }
	             if(sirdService.updateSirdDetails(map)==0)
	             {
	            	 dataMap.put(Literal.status,Literal.errorCode);
	 				dataMap.put(Literal.message,"Record  Update Unsuccessfully");
	 				return dataMap;
	             }
	             dataMap.put(Literal.status, Literal.successCode);
	 			 dataMap.put(Literal.statusCode, sirdService.updateSirdDetails(map));
	 			 dataMap.remove(Literal.statusCode);
	 			 return dataMap;
	        } catch (Exception e) {
	            e.printStackTrace();
	            dataMap.put(Literal.status,Literal.errorCode);
				dataMap.put(Literal.message,"Something Went Wrong");
				return dataMap;
	        }
}
	 
	 @GetMapping(value = Webhook.getSirdById)
		
		public Map<String,Object>getSirdById(@RequestParam String id)
		
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
					dataMap.put(Literal.data, sirdService.getSirdById(id));
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
	 
	 @GetMapping(value = Webhook.deleteSirdById)
		
		public Map<String,Object>deleteEtcById(@RequestParam String id)
		
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
					dataMap.put(Literal.data, sirdService.deleteSirdById(id));
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
	 
	 
	 @GetMapping(value = Webhook.getSirdDetails)
		
		public Map<String,Object>getSirdDetails(@RequestParam String userName)
		
		{
			Map<String, Object> dataMap = new HashMap<String, Object>(6);
			try
			{
				/**
				 *  check token is valid or not
				 */
//				 if(utility.getHeaderValue(httpservletrequest))
//				 {
//					    dataMap.put(Literal.status, Literal.unauthorized);
//						dataMap.put(Literal.message, "Your Token Is Expired");
//						return dataMap;
//				 }
				 
				    dataMap.put(Literal.status, Literal.successCode);
					dataMap.put(Literal.data, sirdService.getSirdDetails(userName));
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

	
	