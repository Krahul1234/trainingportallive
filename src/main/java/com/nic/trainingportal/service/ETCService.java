package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.ETCDao;
import com.nic.trainingportal.dao.SIRDDao;

@Service
public class ETCService {
	
	@Autowired
	private ETCDao etcDao;
	
	 public List<Map<String, Object>> getEtcDetails() {
	        try {
	            /**
	             * get faculty
	             */
	            return etcDao.getEtcDetails();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ArrayList<Map<String, Object>>(0);
	    }

}
