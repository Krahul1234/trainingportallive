package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.TrainingDaoNirdpr;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingServiceNirdpr;
import com.nic.trainingportal.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class TrainingControllerNirdpr {
	
	
	/**
	 * create logger class object
	 */
	@Autowired
	private HttpServletRequest httpservletrequest;
	@Autowired
	private TrainingServiceNirdpr trainingservice;
	
	@Autowired
	public TrainingDaoNirdpr trainingdao;
	
	@Autowired
	public Utility utility;
	 private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);
	@PostMapping(value="addTrainingDetailsNirdpr")
	public Map<String, Object>addTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {

			/**
			 *  check token is valid or not
			 */
			if (utility.getHeaderValue(httpservletrequest)) {
				dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			}
			/**
			 * check Null
			 */
			// Validate 'proposedDate' (required and must be in yyyy-MM-dd format)
			if (Utility.checkNull(map.get("proposedDate"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Proposed Date");
				return dataMap;
			}
			String proposedDate = map.get("proposedDate").toString().trim();
			if (!proposedDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Proposed Date must be in YYYY-MM-DD format");
				return dataMap;
			}

// Validate 'endDate'
			if (Utility.checkNull(map.get("endDate"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide End Date");
				return dataMap;
			}
			String endDate = map.get("endDate").toString().trim();
			if (!endDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "End Date must be in YYYY-MM-DD format");
				return dataMap;
			}
			LocalDate proposed = LocalDate.parse(proposedDate);
			LocalDate end = LocalDate.parse(endDate);
			if (!end.isAfter(proposed)) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "End Date must be greater than Proposed Date");
				return dataMap;
			}
// Validate 'facultyName'
			if (Utility.checkNull(map.get("facultyName"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				return dataMap;
			}
			String facultyName = map.get("facultyName").toString().trim();
			if (!facultyName.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Faculty Name must contain only alphabets and spaces");
				return dataMap;
			}

// Validate 'venue'
			if (Utility.checkNull(map.get("venue"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Venue Details");
				return dataMap;
			}
			String venue = map.get("venue").toString().trim();
			if (!venue.matches("^[a-zA-Z0-9\\s,\\-]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Venue can contain letters, numbers, commas, and hyphens");
				return dataMap;
			}

// Validate 'trainingSubject'
			if (Utility.checkNull(map.get("trainingSubject"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Training Subject");
				return dataMap;
			}
			String trainingSubject = map.get("trainingSubject").toString().trim();
			if (!trainingSubject.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Training Subject must contain only alphabets and spaces");
				return dataMap;
			}

// Validate 'trainessNumber'
			if (Utility.checkNull(map.get("trainessNumber"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Number Of Trainees");
				return dataMap;
			}
			String trainessNumber = map.get("trainessNumber").toString().trim();
			if (!trainessNumber.matches("^\\d+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Number of Trainees must be a valid number");
				return dataMap;
			}

// Validate 'targetGroup'
			if (Utility.checkNull(map.get("targetGroup"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Target Group is required (e.g., Elected representatives of Panchayati Raj)");
				return dataMap;
			}
			String targetGroup = map.get("targetGroup").toString().trim();
			if (!targetGroup.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Target Group must contain only alphabets and spaces");
				return dataMap;
			}


			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put("StatusCode", trainingservice.addTrainingDetails(map));
			dataMap.remove(Literal.statusCode);
			dataMap.put(Literal.message, "Record Insert Successfully");
			return dataMap;


		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
		
		
}
	
	@GetMapping(value = "GetAllTrainingDetailsNirdpr")
	public Map<String, Object> getAllTrainingDetails(@RequestParam int pageSize,@RequestParam int pageNumber) {
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
//			int userId=trainingdao.getUserId(userName, userType);
			dataMap.put("Data",trainingservice.getAllTrainingDetails(pageSize,pageNumber));
			dataMap.put("totalCount",trainingdao.getTrainingCalendarCount());
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			return dataMap;
		}
	}
	
	@PostMapping(value ="updateTrainingDetailsNirdpr")
	public Map<String, Object> updateDeleteTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 *  check token is valid or not
			 */
			if (utility.getHeaderValue(httpservletrequest)) {
				dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
			}
			/**
			 * check Null
			 */
			// Proposed Date
			if (Utility.checkNull(map.get("proposedDate"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Proposed Date");
				return dataMap;
			}
			String proposedDate = map.get("proposedDate").toString().trim();
			if (!proposedDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Proposed Date must be in YYYY-MM-DD format");
				return dataMap;
			}

// Faculty Name
			if (Utility.checkNull(map.get("facultyName"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				return dataMap;
			}
			String facultyName = map.get("facultyName").toString().trim();
			if (!facultyName.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Faculty Name must contain only alphabets and spaces");
				return dataMap;
			}

// End Date
			if (Utility.checkNull(map.get("endDate"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide End Date");
				return dataMap;
			}
			String endDate = map.get("endDate").toString().trim();
			if (!endDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "End Date must be in YYYY-MM-DD format");
				return dataMap;
			}
			LocalDate proposed = LocalDate.parse(proposedDate);
			LocalDate end = LocalDate.parse(endDate);
			if (!end.isAfter(proposed)) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "End Date must be greater than Proposed Date");
				return dataMap;
			}
// Venue
			if (Utility.checkNull(map.get("venue"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Venue Details");
				return dataMap;
			}
			String venue = map.get("venue").toString().trim();
			if (!venue.matches("^[a-zA-Z0-9\\s,\\-]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Venue must contain only letters, numbers, spaces, commas, or hyphens");
				return dataMap;
			}

// Training Subject
			if (Utility.checkNull(map.get("trainingSubject"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Training Subject");
				return dataMap;
			}
			String trainingSubject = map.get("trainingSubject").toString().trim();
			if (!trainingSubject.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Training Subject must contain only alphabets and spaces");
				return dataMap;
			}

// Number of Trainees
			if (Utility.checkNull(map.get("trainessNumber"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Number Of Trainees");
				return dataMap;
			}
			String traineesNumber = map.get("trainessNumber").toString().trim();
			if (!traineesNumber.matches("^\\d+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Number of Trainees must be numeric only");
				return dataMap;
			}

// Target Group
			if (Utility.checkNull(map.get("targetGroup"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Target Group (e.g., Elected Representatives)");
				return dataMap;
			}
			String targetGroup = map.get("targetGroup").toString().trim();
			if (!targetGroup.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Target Group must contain only alphabets and spaces");
				return dataMap;
			}

// Coordinator Name
			if (Utility.checkNull(map.get("nameOfCoordintor"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Name Of Coordinator");
				return dataMap;
			}
			String coordinatorName = map.get("nameOfCoordintor").toString().trim();
			if (!coordinatorName.matches("^[a-zA-Z\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Coordinator Name must contain only alphabets and spaces");
				return dataMap;
			}

// Financial Year
			if (Utility.checkNull(map.get("financialYear"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Financial Year");
				return dataMap;
			}
			String financialYear = map.get("financialYear").toString().trim();
			if (!financialYear.matches("^\\d{4}-\\d{4}$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Financial Year must be in YYYY-YYYY format");
				return dataMap;
			}

// Installment Type
			if (Utility.checkNull(map.get("installmentType"))) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Kindly Provide Installment Type");
				return dataMap;
			}
			String installmentType = map.get("installmentType").toString().trim();
			if (!installmentType.matches("^[a-zA-Z0-9\\s]+$")) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Installment Type must be alphanumeric (e.g., 1st Installment)");
				return dataMap;
			}

			dataMap.put(Literal.status, Literal.successCode);
			dataMap.put("StatusCode", trainingservice.updateTrainingDetails(map));
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
	
@GetMapping(value ="getCalendarInfoByIdNirdpr")
	
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

@GetMapping(value ="deleteCalendatInfoNirdpr")

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
