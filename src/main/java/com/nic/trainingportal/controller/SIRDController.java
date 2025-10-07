package com.nic.trainingportal.controller;

import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.SIRDService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class SIRDController {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(SIRDController.class);
	@Autowired
	public Utility utility;

	@Autowired private JWT jwt;
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
				 if(utility.getHeaderValue(httpservletrequest))
				 {
					    dataMap.put(Literal.status, Literal.unauthorized);
						dataMap.put(Literal.message, "Your Token Is Expired");
						dataMap.put(Literal.statusCode, Literal.zero);
						return dataMap;
				 }
				 
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

	@PostMapping(value ="getProfileDetails")
	public Map<String, Object> getsirddetails(@RequestBody Map<String,Object>map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
//	        	/**
//				 *  check token is valid or not
//				 */
			if(utility.getHeaderValue(httpservletrequest))
			{
				dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			}

			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, sirdService.sirdDetails(map));
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
//	             /**
//				  *  check null
//				  */
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
				if(Utility.checkNull(map.get("alternateEmailId")))
				{
					dataMap.put(Literal.status,  Literal.badReuqest);
					dataMap.put(Literal.message, "Alternate EmailId Id is Null");
					return dataMap;
				}else if(! map.get("alternateEmailId").toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
				{
					dataMap.put(Literal.status,  Literal.badReuqest);
					dataMap.put(Literal.message, "Please Enter Valid Email Id");
					return dataMap;
				}if(Utility.checkNull(map.get("address")))
				{
					dataMap.put(Literal.status,  Literal.badReuqest);
					dataMap.put(Literal.message, "Address is Null");
					return dataMap;
				}if(Utility.checkNull(map.get("nameOfDirector")))
				{
					dataMap.put(Literal.status,  Literal.badReuqest);
					dataMap.put(Literal.message, "Name Of Director is Null");
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
	 
	 
	 @PostMapping(value = Webhook.getSirdDetails)
		public Map<String,Object>getSirdDetailsWithProposal(@RequestHeader String token,
				@RequestBody(required = false) Map<String ,Object>maps)
		
		{

			String userName = maps != null && maps.get("userName") != null ? maps.get("userName").toString() : "";
			String view = maps != null && maps.get("view") != null ? maps.get("view").toString() : "";
			String financialYear = maps != null && maps.get("financialYear") != null ? maps.get("financialYear").toString() : "";
			String installmentType = maps != null && maps.get("installmentType") != null ? maps.get("installmentType").toString() : "";

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
//				String userName = jwt.extractUsername(token);
				    dataMap.put(Literal.status, Literal.successCode);
					dataMap.put(Literal.data, sirdService.getSirdDetails(userName,view,financialYear,installmentType));
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
	 
	 
	 @PostMapping(value ="getSirdDetailsNonRecuring")
		
		public Map<String,Object>getSirdDetailsNonRecuring(@RequestHeader String token,@RequestBody
														  Map<String,Object> map)
		
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
				String financialYear = map.get("financialYear") != null ? map.get("financialYear").toString() : null;
				String view = map.get("view") != null ? map.get("view").toString() : null;


				dataMap.put(Literal.status, Literal.successCode);
					dataMap.put(Literal.data, sirdService.getSirdDetailsNonRecuring(map.get("userName").toString(),view,financialYear));
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
	 
	 
	 @PostMapping(value ="updateInformationSirdEtc")
		
		public Map<String,Object>updateInformationSirdEtc(@RequestBody Map<String,Object>map)
		
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
					dataMap.put(Literal.message, sirdService.updateInformationSirdEtc(map));
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

	
	