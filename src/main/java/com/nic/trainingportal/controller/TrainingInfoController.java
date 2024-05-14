package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingInfoService;
import com.nic.trainingportal.utility.Utility;

@RestController
public class TrainingInfoController {

	@Autowired
	private TrainingInfoService trainingInfoService;

//	End point for 
//	insert operation
	@PostMapping(value = "addTrainingInfo")
	public Map<String, Object> addTrainingInfo(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			

			if (Utility.checkNull(map.get("info_id"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly provide info id");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			if (Utility.checkNull(map.get("functional"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide functional");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("building"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide building availabilty");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			
			if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly check number of permanent faculty member");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly check number of contractual faculty member");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_other_staff"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check the number of staff");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("hostel_facility"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check whether hostel facility is available or not");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_seat"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check the seat Availability");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of computer");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of computer");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			if (Utility.checkNull(map.get("hall_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of training hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("hall_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of training hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of dining hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of dining hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_kitchens"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of kitchens");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of auditorium");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of auditorium");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("Remarks"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide Remarks");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.statusCode, trainingInfoService.addFaculty(map));
			return dataMap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}
	
//	End point of 
//	Fetch Operation
    @GetMapping(value = "gettraininginfo")
	public Map<String, Object> getTrainingInfo() {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.data, trainingInfoService.getAllFaculty());
			dataMap.put(Literal.statusCode, 1);
			return dataMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}
    //End point 
    //for update 
    @PostMapping(value = "updatetraininginfo")
    public Map<String,Object> updateTrainingInfo(Map<String, Object> map) {
    	Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             * update faculty
             */

			if (Utility.checkNull(map.get("info_id"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly provide info id");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			if (Utility.checkNull(map.get("functional"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide functional");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("building"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide building availabilty");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			
			if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly check number of permanent faculty member");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly check number of contractual faculty member");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			
			if (Utility.checkNull(map.get("number_of_other_staff"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check the number of staff");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("hostel_facility"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check whether hostel facility is available or not");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_seat"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "check the seat Availability");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of computer");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("lab_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of computer");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			if (Utility.checkNull(map.get("hall_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of training hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("hall_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of training hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of dining hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("dining_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of dining hall");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("number_of_kitchens"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of kitchens");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_number"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the number of auditorium");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("auditorium_capacity"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide the capacity of auditorium");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			if (Utility.checkNull(map.get("Remarks"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "provide Remarks");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			dataMap.put("Status","Success");
			dataMap.put("StatusCode",trainingInfoService.updateInformation(map));
			return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
      return   new HashMap<String,Object>(0);
    }
}
