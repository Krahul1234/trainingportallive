package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.ETCDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.ETCService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class ETCController {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(ETCController.class);
	@Autowired
	public Utility utility;
	@Autowired
	private ETCService etcService;
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	private ETCDao etcdao;
	
	@Autowired
	private JWT jwt;
	
	 @GetMapping(value ="GetEtcDetails")
	    public Map<String, Object> getEtcdetails(@RequestParam int pageSize,@RequestParam int  pageNumber) {
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
				 
	            dataMap.put(Literal.status,Literal.successCode);
	            dataMap.put(Literal.data, etcService.getEtcDetails(pageSize,pageNumber));
	            /**
				  * print log
				  */
				 logger.info("Get Etc Details", dataMap);
	            return dataMap;
	        } catch (Exception e) {
	            e.printStackTrace();
	            dataMap.put("message","Something Went Wrong");
	            dataMap.put(Literal.status,Literal.errorCode);
	            return dataMap;
	        }
}
	 
	 @PostMapping(value ="updateEtcDetails")
	    public Map<String, Object> updateEtcDetails(@RequestBody Map<String,Object>map) {
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
				 /**
				  *  check null
				  */
	             if(Utility.checkNull(map.get("name")))
	             {
	            	    dataMap.put(Literal.status,Literal.badReuqest);
						dataMap.put(Literal.message, "Name value is null");
						return dataMap;
	             }
	             /**
				  *  check null
				  */
	             if(Utility.checkNull(map.get("mobileNo")))
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Mobile No  value is null");
						return dataMap;
	             }else if(! map.get("mobileNo").toString().matches("^[0-9]{10}$")) 
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Please Enter Valid Mobile No");
						return dataMap;
	             }
	             
	             if(Utility.checkNotNull(map.get("alternateMobile")))
	             {
	              if(! map.get("alternateMobile").toString().matches("^[0-9]{10}$"))
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Mobile No  value is null");
						return dataMap;
	             }
	             }
	             if(Utility.checkNull(map.get("emailId")))
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Email Id is Null");
						return dataMap;
	             }else if(! map.get("emailId").toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
	             {
	            	    dataMap.put(Literal.status, Literal.badReuqest);
						dataMap.put(Literal.message, "Please Enter Valid Email Id");
						return dataMap;
	             }
	             dataMap.put(Literal.status, Literal.successCode);
	 			 dataMap.put(Literal.statusCode, etcService.updateEtcDetails(map));
	 			 dataMap.put(Literal.message,"Record insert successfully");
	 			 dataMap.remove(Literal.statusCode);
	 			 return dataMap;
	        } catch (Exception e) {
	            e.printStackTrace();
	            dataMap.put(Literal.message,"Something Went Wrong");
	            dataMap.put(Literal.status,Literal.errorCode);
	            return dataMap;
	        }
}
	 
	 @GetMapping(value = Webhook.getEtcById)
		
		public Map<String,Object>getEtcById(@RequestParam String id)
		
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
					dataMap.put(Literal.data, etcService.getEtcById(id));
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
	 
	 @GetMapping(value = Webhook.deleteEtcById)
		
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
					dataMap.put(Literal.data, etcService.deleteEtcById(id));
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
	 
	 @GetMapping(value = Webhook.getAllEtcs)
		
		public Map<String,Object>getAllEtcs(@RequestHeader String token,@RequestParam String userType)
		
		{
		    int demand=0;
		    List<Map<String,Object>>proposal=new ArrayList<Map<String,Object>>();
			Map<String, Object> dataMap = new HashMap<String, Object>(6);
			try
			{
//				/**
//				 *  check token is valid or not
//				 */
				 if(utility.getHeaderValue(httpservletrequest))
				 {
					    dataMap.put(Literal.status, Literal.unauthorized);
						dataMap.put(Literal.message, "Your Token Is Expired");
						return dataMap;
				 }
				String userName = jwt.extractUsername(token);


				dataMap.put(Literal.status, Literal.successCode);
				    proposal=etcService.getAllEtcs(userName,userType);
				    Iterator<Map<String, Object>> iterator = proposal.iterator();
				    while (iterator.hasNext()) {
				        Map<String, Object> map = iterator.next();
				        if (map.get("demand") != null) {
				            demand = Integer.parseInt(map.get("demand").toString());
				            iterator.remove();  // Safe removal
				        }
				    }
					dataMap.put(Literal.data, proposal);
					dataMap.put("demand", demand);
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
	 
	 
	 @PostMapping(value ="GetAllEtcAddToCombined")
		
		public Map<String,Object>GetAllEtcAddToCombined(@RequestHeader String token,@RequestBody Map<String,Object>map)
		
		{
			  Map<String, Object> dataMap = new HashMap<String, Object>(6);
			    List<Map<String,Object>>proposal=new ArrayList<Map<String,Object>>();
			try
			{
//				/**
//				 *  check token is valid or not
//				 */
				 if(utility.getHeaderValue(httpservletrequest))
				 {
					    dataMap.put(Literal.status, Literal.unauthorized);
						dataMap.put(Literal.message, "Your Token Is Expired");
						return dataMap;
				 }
				String userName = jwt.extractUsername(token);


				dataMap.put(Literal.status, Literal.successCode);
				    proposal= etcService.GetAllEtcAddToCombined(userName,map.get("userType").toString(),map.get("installmentType").toString(),map.get("financialYear").toString());
				    Iterator<Map<String, Object>> iterator = proposal.iterator();
				    while (iterator.hasNext()) {
				        Map<String, Object> maps = iterator.next();
				        if (maps.get("demand") != null) {
				        	String totalDemandStr = maps.get("demand").toString();
						    BigDecimal demand = new BigDecimal(totalDemandStr);
//				            demand = Integer.parseInt(map.get("demand").toString());
						    dataMap.put("demand",demand);
				            iterator.remove();  // Safe removal
				        }
				    }
				    dataMap.put(Literal.data, proposal);
//					dataMap.put("demand", demand);
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
	 
	 
	 @PostMapping(value ="GetAllEtcAddToCombinedNonRecuring")
		
		public Map<String,Object>GetAllEtcAddToCombinedNonRecuring(@RequestHeader String token, @RequestBody Map<String,Object>maps)
		
		{
			  Map<String, Object> dataMap = new HashMap<String, Object>(6);
			try
			{
//				/**
//				 *  check token is valid or not
//				 */
				 if(utility.getHeaderValue(httpservletrequest))
				 {
					    dataMap.put(Literal.status, Literal.unauthorized);
						dataMap.put(Literal.message, "Your Token Is Expired");
						return dataMap;
				 }
//				String userName = jwt.extractUsername(token);

				dataMap.put(Literal.status, Literal.successCode);
				    dataMap.put("Data", etcService.GetAllEtcAddToCombinedNonRecuring(maps.get("userName").toString(),maps.get("userType").toString(),maps.get("financialYear").toString()));
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
