package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.SIRDDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	
	 public List<Map<String, Object>> sirdDetails(Map<String,Object>map) {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.sirdDetails(map);
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
		public List<Map<String, Object>> getSirdDetails(String userName,String view,String financialYear,String installmentType) {
			try {
				/**
				 * get faculty
				 */
				return sirdDao.getSirdDetails(userName,view,financialYear,installmentType);
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
		public List<Map<String, Object>> getSirdDetailsNonRecuring(String userName, String view, String financialYear) {
			try {
				/**
				 * get faculty
				 */
				return sirdDao.getSirdDetailsNonRecuring(userName,view,financialYear);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ArrayList<Map<String, Object>>(0);
		}
		
		
		public String updateInformationSirdEtc(Map<String,Object>map) {
	        try {
	            /**
	             * get faculty
	             */
	            return sirdDao.updateInformationSirdEtc(map);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Not Updated";
	        }
	    }

}
