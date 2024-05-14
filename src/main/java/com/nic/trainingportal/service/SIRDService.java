package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.SIRDDao;

@Service
public class SIRDService {
	
	@Autowired
	private SIRDDao sirdDao;
	
	 public List<Map<String, Object>> getSirdDetails() {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.getSirdDetails();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ArrayList<Map<String, Object>>(0);
	    }
	
	
   


}
