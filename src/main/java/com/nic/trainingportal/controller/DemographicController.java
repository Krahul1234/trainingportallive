package com.nic.trainingportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import org.springframework.web.multipart.MultipartFile;

import com.nic.trainingportal.dao.DemographicDao;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.DemographicService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/ao/trainingportal")
public class DemographicController {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(DemographicController.class);
	@Autowired
	public DemographicService demographicservice;
	@Autowired
	private HttpServletRequest httpservletrequest;
	
	@Autowired
	public Utility utility;
	
	@Autowired
	private DemographicDao demographic;
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
//			/**
//			 *  check token is valid or not
//			 */
//			 if(utility.getHeaderValue(httpservletrequest))
//			 {
//				    dataMap.put(Literal.status,Literal.unauthorized);
//					dataMap.put(Literal.message, "Your Token Is Expired");
//					return dataMap;
//			 }
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalPopulation"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Total Population");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalRuralPopulation"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("percentOfRural"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("stateCode"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide  State Code value");
				return dataMap;
			}
		    /**
		     * Print log
		     */
			logger.info("Add Demographic Request Data", map);
			
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put(Literal.statusCode,demographicservice.addDemographicDetails(map));
			dataMap.put(Literal.message,"Record insert successfully");
			dataMap.remove(Literal.statusCode);
			return dataMap;

		} catch (Exception e) {
			/**
			 * print error log
			 */
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something went wrong");
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			return dataMap;
		}
		
	}
	/** get DemographicDetails
	 * @return
	 */
	@GetMapping(value =Webhook.getDemographicDetails)
	public Map<String,Object> getDemographicDetails(@RequestParam int pageSize,@RequestParam int pageNumber,@RequestParam String userName,@RequestParam String userType) 
	{
		Map<String,Object>map=new HashMap<String,Object>(4);
		try
		{ 
			/**
			 *  check token is valid or not
//			 */
//			if(utility.getHeaderValue(httpservletrequest))
//			 {
//				map.put(Literal.status,Literal.unauthorized);
//				map.put(Literal.message, "Your Token Is Expired");
//				return map;
//			 }
			map.put(Literal.status,Literal.successCode);
			int userId=demographic.getUserId(userName,userType);
			map.put(Literal.data,demographicservice.getDemographicDetails(pageSize,pageNumber,userId,userType));
			map.put("totalCount",demographic.getDemographicCount(userId,userType));
			/**
			 * Print log
			 */
			logger.info("Get Demographic Details", map);
			return map;
		
		}catch(Exception e)
		{
			map.put(Literal.status,Literal.errorCode);
			map.put(Literal.message,"Something Went Wrong");
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			return map;
		}
		
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
			 *  check token is valid or not
			 */
			if(utility.getHeaderValue(httpservletrequest))
			 {
				map.put(Literal.status,Literal.unauthorized);
				map.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			 }
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalPopulation"))) {
				dataMap.put(Literal.status,Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Total Population");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("totalRuralPopulation"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Total Rural Population");
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("percentOfRural"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Percent OF Rural");
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("stateCode"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide  State Code value");
				return dataMap;
			}
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put(Literal.statusCode,demographicservice.updateDemographicDetails(map));
			dataMap.put(Literal.message,"Record Updated Successfully");
			dataMap.remove(Literal.statusCode);
			return dataMap;

		} catch (Exception e) {
			/**
			 * print error log
			 */
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			return dataMap;
		}
	}


@GetMapping(value = Webhook.getDemographicById)
	
	public Map<String,Object>getDemographicById(@RequestParam String id)
	
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
				dataMap.put(Literal.data, demographicservice.getDemographicById(id));
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


@GetMapping(value = Webhook.deleteDemographicById)

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
			dataMap.put(Literal.data, demographicservice.deleteDemographicById(id));
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

@GetMapping(value = Webhook.getFinancialYear)
public Map<String,Object>getFinancialYear()

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
		 
		    dataMap.put(Literal.status, Literal.successCode);
		    dataMap.put("Data",demographicservice.getFinnancialYear());
			return dataMap;
		 
	}catch(Exception e)
	{
		e.printStackTrace();
		dataMap.put(Literal.message,"Something Went Wrong");
        dataMap.put(Literal.status,Literal.errorCode);
        return dataMap;
	}
}

@PostMapping(value = "/upload")
public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file, 
                         @RequestParam("newFileName") String newFileName,
                         @RequestParam("userName") String userName) {
    // Define the directory path where the file should be saved
    String directoryPath = "E:\\AreaOfficer_PDF";
    Map<String,Object>dataMap=new HashMap<String,Object>();
    LocalDate currentDate = LocalDate.now();
    try {
        // Create Path object for the upload directory
        Path uploadPath = Paths.get(directoryPath);
        // Ensure the upload directory exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // Define the target file path with the new file name
        Path filePath = uploadPath.resolve(newFileName+"_"+userName+"_"+currentDate +".pdf");

        // Save the file (overwrite if it already exists)
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        dataMap.put(Literal.status,Literal.successCode);
        dataMap.put(Literal.message,"File uploaded successfully");
        return dataMap;
    } catch (IOException e) {
        e.printStackTrace();
        dataMap.put(Literal.status,Literal.errorCode);
        dataMap.put(Literal.message,"File uploaded Unsuccessfully");
        dataMap.put("errorMessage",e.getMessage());
        return dataMap;
    }
}
@GetMapping(value = "file-path")
 public Map<String,Object> getFilePath(@RequestParam("fileName") String fileName,@RequestParam("date") String date,
		 @RequestParam("userName") String userName) {
	  Map<String,Object>dataMap=new HashMap<String,Object>();
	try
	{
	 String directoryPath = "E:\\AreaOfficer_PDF";
     Path filePath = Paths.get(directoryPath, fileName+"_"+userName+"_"+date);
     String fileNameNew = filePath.getFileName().toString();
     dataMap.put(Literal.status,Literal.successCode);
     dataMap.put("path","https://areaofficer.nic.in/AreaOfficer_PDF/"+fileNameNew+".pdf");
       return dataMap;
	}catch(Exception e)
	{
		e.printStackTrace();
		dataMap.put(Literal.status,Literal.errorCode);
		dataMap.put("errorMessage",e.getMessage());
		return dataMap;
		
	}
}

}
