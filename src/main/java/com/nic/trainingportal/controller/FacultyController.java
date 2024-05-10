package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.FacultyService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;

@RestController
public class FacultyController {

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
			 * check Null
			 */
			if (Utility.checkNull(map.get("name"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Faculty Name");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("postHeld"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Postheld Value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}

			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("scalePay"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Scale Pay Value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("permanent"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			/**
			 * check Null
			 */
			if (Utility.checkNull(map.get("remarks"))) {
				dataMap.put(Literal.status, Literal.error);
				dataMap.put(Literal.message, "Kindly Provide Remarks Value");
				dataMap.put(Literal.statusCode, Literal.zero);
				return dataMap;
			}
			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.statusCode, facultyservice.addFaculty(map));
			return dataMap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}

	/**
	 * Get Faculty Details
	 * 
	 * @return
	 */
	@GetMapping(value =Webhook.getAllFaculty)
	public Map<String, Object> getAllFaculty() {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			dataMap.put(Literal.status, Literal.success);
			dataMap.put(Literal.data, facultyservice.getAllFaculty());
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

	@GetMapping(value =Webhook.updateDeleteFaculty)
	public Map<String, Object> updateDeleteFaculty(@RequestBody Map<String, Object> map) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		try {
			if (Utility.checkNotNull(map.get("key"))) {
				if (map.get("key").toString().equalsIgnoreCase("update")) {
					dataMap.put(Literal.status,Literal.success);
					dataMap.put(Literal.statusCode, facultyservice.updateFacultyDetails(map));
					return dataMap;

				} else {
					dataMap.put(Literal.status,Literal.success);
					dataMap.put(Literal.statusCode, facultyservice.deleteFacultyDetails(map));
					return dataMap;
				}
			}

		} catch (Exception e) {
			/**
			 * print error
			 */
			e.printStackTrace();
		}
		/**
		 * return empty Hash Map
		 */
		return new HashMap<String, Object>(0);
	}

}
