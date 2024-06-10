package com.nic.trainingportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.nic.trainingportal.dao.TrainingInfoDao;

@Service
public class TrainingInfoService {

	@Autowired
	private TrainingInfoDao TrainingInfoDao;

	public int addTrainingInfo(Map<String, Object> map) {
		try {
			return TrainingInfoDao.addInformation(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Map<String, Object>> getAllFaculty(@RequestParam int pageSize,@RequestParam int pageNumber,int userId,String userType) {
        try {
            /**
             * get faculty
             */
            return TrainingInfoDao.getInformation(pageSize,pageNumber,userId,userType);
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
	 
	 /**
		 * get all faculty details
		 * 
		 * @return
		 */
		public List<Map<String, Object>> getTrainingInfoById(String id) {
			try {
				/**
				 * get faculty
				 */
				return TrainingInfoDao.getTrainingInfoById(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ArrayList<Map<String, Object>>(0);
		}
		
		/**
		 * get all faculty details
		 * 
		 * @return
		 */
		public int deleteTrainingInfoById(String id) {
			try {
				/**
				 * get faculty
				 */
				return TrainingInfoDao.deleteTrainingInfoById(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}

}
