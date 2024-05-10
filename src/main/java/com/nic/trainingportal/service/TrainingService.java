package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Map<String, Object>> getAllTrainingDetails() {
		try {
			/**
			 * get faculty
			 */
			return trainingdao.getTrainingDetails();
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
	 * delete training details
	 * 
	 * @param map
	 * @return
	 */
	public int deleteTrainingDetails(Map<String, Object> map) {
		try {
			/**
			 * delete faculty
			 */
			return trainingdao.deleteTrainingDetails(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
