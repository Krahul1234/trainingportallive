package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.nic.trainingportal.dao.TrainingDao;

@Service
public class TrainingService {
	
	@Autowired
	public TrainingDao trainingdao;
	
	/**
	 * add training details
	 * 
	 * @param map
	 * @return
	 */
	public int addTrainingDetails(Map<String, Object> map) {
		try {
			return trainingdao.addTrainingDetails(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get all training details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllTrainingDetails(int pageSize,@RequestParam int pageNumber,String userName,String userType,int userId) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getTrainingDetails(pageSize,pageNumber,userName,userType,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * update training details
	 * 
	 * @param map
	 * @return
	 */
	public int updateTrainingDetails(Map<String, Object> map) {
		try {
			/**
			 * update faculty
			 */
			return trainingdao.updateTrainingDetails(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public int deleteCalendarInfoById(String id) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.deleteCalendarInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * get all faculty details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCalendarInfoById(String id) {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getCalendarInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

}
