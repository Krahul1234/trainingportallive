package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.TrainingInfoDao;

@Service
public class TrainingInfoService {

	@Autowired
	private TrainingInfoDao TrainingInfoDao;

	public int addFaculty(Map<String, Object> map) {
		try {
			return TrainingInfoDao.addInformation(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Map<String, Object>> getAllFaculty() {
        try {
            /**
             * get faculty
             */
            return TrainingInfoDao.getInformation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
	 public int updateInformation(Map<String, Object> map) {
	        try {
	            /**
	             * update faculty
	             */
	            return TrainingInfoDao.updateInformation(map);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }

}
