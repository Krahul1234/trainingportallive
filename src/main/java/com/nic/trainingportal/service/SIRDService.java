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
	
	 public List<Map<String, Object>> getSirdDetails(Map<String,Object>map) {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.getSirdDetails(map);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ArrayList<Map<String, Object>>(0);
	    }
	
	
	 public List<Map<String, Object>> sirdDetails(int pageSize,int pageNumber) {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.sirdDetails(pageSize,pageNumber);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return new ArrayList<Map<String, Object>>(0);
	    }

	 
	 public int updateSirdDetails(Map<String,Object>map) {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.updateSirdDetails(map);
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
		public List<Map<String, Object>> getSirdById(String id) {
			try {
				/**
				 * get faculty
				 */
				return sirdDao.getSirdById(id);
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
		public int  deleteSirdById(String id) {
			try {
				/**
				 * get faculty
				 */
				return sirdDao.deleteSirdById(id);
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
		public List<Map<String, Object>> getSirdDetails(String userName) {
			try {
				/**
				 * get faculty
				 */
				return sirdDao.getSirdDetails(userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ArrayList<Map<String, Object>>(0);
		}

}
