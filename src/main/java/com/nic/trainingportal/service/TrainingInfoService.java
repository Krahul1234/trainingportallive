package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.TrainingInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrainingInfoService {

	@Autowired
	private TrainingInfoDao TrainingInfoDao;
	
	@Autowired
	private JdbcTemplate jdbctemplate;

	public int addTrainingInfo(Map<String, Object> map) {
		try {
			return TrainingInfoDao.addInformation(map);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public List<Map<String, Object>> getAllFaculty( int pageSize, int pageNumber,int userId,String userType) {
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
		public List<Map<String, Object>> getTrainingInfoById(Integer id) {
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
		
		public List<Map<String, Object>> getAllEtc() {
		    return TrainingInfoDao.getAllEtc();
		   }

		public Map<String, Object> getAllCount() {
		    return TrainingInfoDao.getAllCount();
		}

		public List<Map<String, Object>> getAllSird() {
		    return TrainingInfoDao.getAllSird();
		}
		
		
		public Object getTrainingInfo(String userName, String userType) {
			 try {
		            /**
		             * get faculty
		             */
		            return TrainingInfoDao.getInformation(userName,userType);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return new ArrayList<Map<String, Object>>(0);
		    }
		
		public List<Map<String, Object>> getCalendar(String userName,String userType) {
	        try {
	            /**
	             * get faculty
	             */
	            return TrainingInfoDao.getCalendar(userName,userType);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
		
		
		public Object getAllCal() {
		     try {
		            /**
		             * get faculty
		             */
		            return TrainingInfoDao.getAllCal();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return new ArrayList<Map<String, Object>>(0);
		    }
		    public Object getAllInfo(Map<String, Object> map) {
		         try {
		                /**
		                 * get faculty
		                 */
		                return TrainingInfoDao.getAllInfo(map);
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return new ArrayList<Map<String, Object>>(0);
		        }
		    
		    
		    public  List<Map<String, Object>> calendarCount() {
		         try {
		                /**
		                 * get faculty
		                 */
		                return TrainingInfoDao.calendarCount();
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		            return new ArrayList<Map<String, Object>>(0);
		        }
		    
		    public String updateTranningProposal(Map<String,Object>map) {
		        try {
		            /**
		             * get faculty
		             */
		            return TrainingInfoDao.updateTranningProposal(map);
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "Not Updated";
		        }
		    }
		    
		    public String updateFundsProposal(Map<String, Object> map) {
				try {
		            /**
		             * get faculty
		             */
		            return TrainingInfoDao.updateFundsProposal(map);
		        } catch (Exception e) {
		            e.printStackTrace();
		            return "Not Updated";
		        }
			}
		   
}
