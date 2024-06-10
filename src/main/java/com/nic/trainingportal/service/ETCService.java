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
	
	 public List<Map<String, Object>> getEtcDetails(int pageSize,int pageNumber) {
	        try {
	            /**
	             * get faculty
	             */
	            return etcDao.getEtcDetails(pageSize,pageNumber);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ArrayList<Map<String, Object>>(0);
	    }
	 
	 public int updateEtcDetails(Map<String,Object>map) {
	        try {
	            /**
	             * get faculty
	             */
	            return etcDao.updateEtcDetails(map);
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
		public List<Map<String, Object>> getEtcById(String id) {
			try {
				/**
				 * get faculty
				 */
				return etcDao.getEtcById(id);
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
		public int  deleteEtcById(String id) {
			try {
				/**
				 * get faculty
				 */
				return etcDao.deleteEtcById(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		
		public List<Map<String, Object>> getAllEtcs(String userName,String userTYpe) {
			try {
				/**
				 * get faculty
				 */
				return etcDao.getAllEtcs(userName,userTYpe);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ArrayList<Map<String, Object>>(0);
		}

}
