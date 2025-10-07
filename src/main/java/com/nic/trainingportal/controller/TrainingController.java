package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.TrainingDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class TrainingController {
	@Autowired
	private JdbcTemplate jdbctemplate;
	@Autowired
	private HttpServletRequest httpservletrequest;
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	public TrainingDao trainingdao;
	@Autowired private JWT jwt;
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
			 int result=trainingService.addTrainingDetails(map);
			  dataMap.put(Literal.statusCode, result);

			if (result == 0) {
				dataMap.put(Literal.message, "Record not inserted");
			} else {
				dataMap.put(Literal.message, "Record inserted successfully");
			}
			dataMap.remove(Literal.statusCode);
			return dataMap;


		}catch (Exception e) {
			dataMap.put(Literal.status, Literal.errorCode);
			dataMap.put(Literal.message, "Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
	}
	
	@PostMapping(value = "GetAllTrainingDetails")
	public Map<String, Object> getAllTrainingDetails(@RequestHeader String token, @RequestBody Map<String,Object> maps) {
		Object pageSizeObj = maps.get("pageSize");
		Object pageNumberObj = maps.get("pageNumber");

		int pageSize = pageSizeObj != null ? Integer.parseInt(pageSizeObj.toString()) : 10; // default to 10
		int pageNumber = pageNumberObj != null ? Integer.parseInt(pageNumberObj.toString()) : 0; // default to 0

		String userType = (String) maps.get("userType");
		String financialYear = (String) maps.get("financialYear");
		String installmentType = (String) maps.get("installmentType");

		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		List<Map<String,Object>>totalRecord=new ArrayList<>();
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
			String userName = jwt.extractUsername(token);
			int i=(pageNumber*pageSize)+1;
			dataMap.put(Literal.status,Literal.successCode);
			int userId=trainingdao.getUserId(userName, userType);
			String userIdString = String.valueOf(userId);
			if((financialYear!=null && financialYear.length()!=0) && (installmentType!=null && installmentType.length()!=0))
			{
//			String sql="select financialyear As \"financialYear\",installmentno As \"installmentType\", totalduration As \"totalDuration\", enddate,training_id, entry_date AS \"proposedDate\",name AS \"facultyName\",training_venue AS \"venue\",training_subject AS \"trainingSubject\",number_of_trainees AS \"trainessNumber\",target_group AS \"targetGroup\" from training_final where usertype='"+userType.toLowerCase()+"' and  user_id='"+userId+"' and financialyear='"+financialYear+"' and installmentno='"+installmentType+"'";
			String	 sql = "SELECT financialyear AS \"financialYear\", " +
						"installmentno AS \"installmentType\", " +
						"totalduration AS \"totalDuration\", enddate, training_id, " +
						"entry_date AS \"proposedDate\", name AS \"facultyName\", " +
						"training_venue AS \"venue\", training_subject AS \"trainingSubject\", " +
						"number_of_trainees AS \"trainessNumber\", target_group AS \"targetGroup\" " +
						"FROM training_final " +
						"WHERE usertype = ? AND user_id = ? AND financialyear = ? AND installmentno = ?";

				 totalRecord = jdbctemplate.queryForList(
						sql,
						userType.toLowerCase(),
						 userIdString,
						financialYear,
						installmentType
				);
			for(Map<String,Object>map:totalRecord)
			{
				map.put("serialNo", i++);
			}
			}else
			{
				String sql = "SELECT financialyear AS \"financialYear\", " +
						"installmentno AS \"installmentType\", " +
						"totalduration AS \"totalDuration\", enddate, training_id, " +
						"entry_date AS \"proposedDate\", name AS \"facultyName\", " +
						"training_venue AS \"venue\", training_subject AS \"trainingSubject\", " +
						"number_of_trainees AS \"trainessNumber\", target_group AS \"targetGroup\" " +
						"FROM training_final " +
						"WHERE usertype = ? AND user_id = ?";

				totalRecord = jdbctemplate.queryForList(
						sql,
						userType.toLowerCase(),
						userIdString
				);
				for(Map<String,Object>map:totalRecord)
				{
					map.put("serialNo", i++);
				}
			}
			dataMap.put("Data",trainingservice.getAllTrainingDetails(pageSize,pageNumber,userName,userType,userIdString,financialYear,installmentType));
			dataMap.put("totalCount",trainingdao.getTrainingCalendarCount(userIdString,userType,financialYear,installmentType));
			dataMap.put("totalRecord",totalRecord);
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

			dataMap.put(Literal.status,Literal.successCode);
			int result =trainingservice.updateTrainingDetails(map);
			dataMap.put(Literal.statusCode, result);

			if (result == 0) {
				dataMap.put(Literal.status, Literal.badReuqest);
				dataMap.put(Literal.message, "Record not Updated");
			} else {
				dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.message, "Record Updated successfully");
			}
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
	
@PostMapping(value = Webhook.getCalendarInfoById)
	
	public Map<String,Object>getCalendarInfoById(@RequestBody Map<String,Integer> map)
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
			 	int idBig = Integer.parseInt(map.get("id").toString());
			    dataMap.put(Literal.status, Literal.successCode);
				dataMap.put(Literal.data, trainingservice.getCalendarInfoById(Integer.parseInt(map.get("id").toString())));
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

@PostMapping(value = Webhook.deleteCalendarInfoById)

public Map<String,Object>deleteDemographicById(@RequestBody Map<String,Object> map)

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
			dataMap.put(Literal.data, trainingservice.deleteCalendarInfoById(map.get("id").toString()));
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

@PostMapping("addForwardCombinedProposalNew")
public Map<String,Object> updateforwardProposalCombined(@RequestHeader String token,@RequestBody(required = false) Map<String,Object>map)

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.addForwardCombinedProposalNew(map));
		return dataMap;
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	return new HashMap<String,Object>(0);
	
}

@PostMapping("GetAllCombinedListNew")
public Map<String,Object> GetAllCombinedList(@RequestHeader String token,@RequestBody Map<String,Object> map)

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.getAllCombinedList( map.get("userType").toString()));
		return dataMap;
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	return new HashMap<String,Object>(0);
	
}

@PostMapping(value = "getAggregatedProposalCounts")
public Map<String, Object> getAggregatedProposalCounts() {
    Map<String, Object> dataMap = new HashMap<String, Object>(6);
    try {
        dataMap.put(Literal.data, trainingservice.getAggregatedProposalCounts());
        return dataMap;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new HashMap<String, Object>(0);

}


@PostMapping(value ="getBackwardProposalForSirdNew")

public Map<String,Object>getBackwardProposalForSirdNew(@RequestBody Map<String,Object> map,@RequestHeader String token)

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
//		String userName = jwt.extractUsername(token);
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.getBackwardProposalForSirdNew(map.get("userType").toString(),map.get("userName").toString()));
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


@PostMapping(value ="getApprovedProposalForAsNew")

public Map<String,Object>getApprovedProposalForAsNew()

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
		 
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.getApprovedProposalForAsNew());
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


@GetMapping("GetTrainingCalendarWithCount")
public Map<String,Object> GetTrainingCalendarWithCount() 

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.GetTrainingCalendarWithCount());
		return dataMap;
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	return new HashMap<String,Object>(0);
	
}

@GetMapping("GetTrainingCalendarWithCountWithFinnancialYear")
public Map<String,Object> GetTrainingCalendarWithCountWithFinnancialYear(@RequestParam String date,@RequestParam int pageSize,@RequestParam int pageNumber) 

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		String sql1 = "SELECT count(*) AS count FROM training_final WHERE entry_date = ?";
		Map<String, Object> queryForMap = jdbctemplate.queryForMap(sql1, date);

		Object count = queryForMap.get("count");
        dataMap.put("count", count);
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.GetTrainingCalendarWithCountWithFinnancialYear(date,pageSize,pageNumber));
		return dataMap;
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	return new HashMap<String,Object>(0);
	
}


@PostMapping("addForwardCombinedProposalNewNonRecurring")
public Map<String,Object> addForwardCombinedProposalNewNonRecurring(@RequestBody Map<String,Object>map) 

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		String stateNameQuery = "SELECT state_name FROM sird WHERE sird_id = ?";
		String stateName = jdbctemplate.queryForObject(
				stateNameQuery,
				new Object[]{Integer.parseInt(map.get("userid").toString())},
				String.class
		);
		dataMap.put("stateName", stateName);
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.addForwardCombinedProposalNewNonRecurring(map));
		return dataMap;
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return new HashMap<String,Object>(0);
	
}


@PostMapping("GetAllCombinedListNewNonRecuring")
public Map<String,Object> GetAllCombinedListNewNonRecuring(@RequestBody Map<String,Object>map)

{
	Map<String,Object> dataMap = new HashMap<>();
	try {
		
		if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				dataMap.put(Literal.statusCode, Literal.zero);

				return dataMap;
		 }
		
		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data,trainingservice.GetAllCombinedListNewNonRecuring((String) map.get("userType")));
		return dataMap;
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	return new HashMap<String,Object>(0);
	
}


@PostMapping(value ="getBackwardProposalForSirdNewNonRecurring")

public Map<String,Object>getBackwardProposalForSirdNewNonRecurring(@RequestBody Map<String,Object> map,@RequestHeader String token)

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
//		String userName = jwt.extractUsername(token);
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.getBackwardProposalForSirdNewNonRecurring(map.get("userType").toString(),map.get("userName").toString()));
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


@PostMapping(value ="getApprovedProposalForAsNewNonRecurring")

public Map<String,Object>getApprovedProposalForAsNewNonRecurring()

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
		 
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.getApprovedProposalForAsNewNonRecurring());
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


@PostMapping(value ="deleteUpdateTrainingCalendar")

public Map<String,Object>deleteUpdateTrainingCalendar(@RequestBody Map<String,Object>map)

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
		 
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.message, trainingservice.deleteUpdateTrainingCalendar(map));
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

@PostMapping(value ="getBackwardProposalForAllRecurring")

public Map<String,Object>getBackwardProposalForAllRecurring(@RequestBody Map<String,Object>map)

{
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try
	{
//		/**
//		 *  check token is valid or not
//		 */
		 if(utility.getHeaderValue(httpservletrequest))
		 {
			    dataMap.put(Literal.status, Literal.unauthorized);
				dataMap.put(Literal.message, "Your Token Is Expired");
				return dataMap;
		 }
		 
		    dataMap.put(Literal.status, Literal.successCode);
			dataMap.put(Literal.data, trainingservice.getBackwardProposalForAllRecurring((String) map.get("userType")));
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



@PostMapping (value ="getSanctionedAmountStatusRecurring")
public Map<String, Object> getSanctionedAmountStatusRecurring(@RequestBody Map<String, Object> map) {
	Map<String, Object> dataMap = new HashMap<String, Object>(6);
	try {
//     /**
//      *  check token is valid or not
//      */
		if (utility.getHeaderValue(httpservletrequest)) {
			dataMap.put(Literal.status, Literal.unauthorized);
			dataMap.put(Literal.message, "Your Token Is Expired");
			return dataMap;
		}

		dataMap.put(Literal.status, Literal.successCode);
		dataMap.put(Literal.data, trainingservice.getSanctionedAmountStatusRecurring(map.get("combinedProposalId").toString()));
		/**
		 * print log
		 */
		logger.info("Get All Faculty", dataMap);
		return dataMap;

	} catch (Exception e) {
		e.printStackTrace();
		dataMap.put(Literal.message, "Something Went Wrong");
		dataMap.put(Literal.status, Literal.errorCode);
		return dataMap;
	}
}
}




