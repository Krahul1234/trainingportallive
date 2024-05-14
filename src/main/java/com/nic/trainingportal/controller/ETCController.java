package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.ETCService;

@RestController
public class ETCController {
	
	@Autowired
	private ETCService etcService;

//	End point for 
//	fetch operation
	 @GetMapping(value ="getetcdetails")
	    public Map<String, Object> getsirddetails() {
	        Map<String, Object> dataMap = new HashMap<String, Object>(6);
	        try {
	            dataMap.put(Literal.status, Literal.success);
	            dataMap.put(Literal.data, etcService.getEtcDetails());
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
}
