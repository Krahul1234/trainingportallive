package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.service.TrainingService;
import com.nic.trainingportal.utility.Utility;

@RestController
public class TrainingController {
	
	@Autowired
	public TrainingService trainingservice;
	
	@PostMapping(value="AddTraningDetails")
	public Map<String, Object>addTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("proposedDate"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Kindly Provide Proposed Date");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("facultyName"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Kindly Provide Faculty Name");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("venue"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Kindly Provide Venue Details");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainingSubject"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Kindly Provide Training Subject");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("trainessNumber"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Kindly Provide Number Of Trainees");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}
			
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("targetGroup"))) {
				dataMap.put("Status", "Error");
				dataMap.put("Message", "Target group Elected representative of Panchayati Raj");
				dataMap.put("StatusCode", 0);
				return dataMap;
			}
			
			dataMap.put("Status","Success");
			dataMap.put("StatusCode",trainingservice.addTrainingDetails(map));
			return dataMap;
	

		}catch(Exception e)
		{
			dataMap.put("Status","Error");
			dataMap.put("Message","Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
		
		
}
	
	@GetMapping(value = "GetAllTrainingDetails")
	public Map<String, Object> getAllTrainingDetails() {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			dataMap.put("Status", "Success");
			dataMap.put("Data",trainingservice.getAllTrainingDetails());
			dataMap.put("StatusCode", 1);
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>(0);
	}
	
	@GetMapping(value = "UpdateDeleteTrainingDetails")
	public Map<String, Object> updateDeleteTrainingDetails(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			dataMap.put("Status", "Success");
			dataMap.put("StatusCode",trainingservice.updateTrainingDetails(map));
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>(0);
	}
}
