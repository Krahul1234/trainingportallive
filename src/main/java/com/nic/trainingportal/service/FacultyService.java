package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.FacultyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FacultyService {
	@Autowired
	public FacultyDao facultydao;

	/**
	 * add faculty details
	 * 
	 * @param map
	 * @return
	 */
	public int addFaculty(Map<String, Object> map) {
		try {
			/**
			 * add data into database
			 */
			return facultydao.addFaculty(map);

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
	public List<Map<String, Object>> getAllFaculty(int pageSize,int pageNumber,String userName,String userType,String financialYear,String installmentType) {
		try {
			
			/**
			 * get faculty
			 */
			return facultydao.getAllFaculty(pageSize,pageNumber,userName,userType,financialYear,installmentType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * update faculty
	 * 
	 * @param map
	 * @return
	 */
	public int updateFacultyDetails(Map<String, Object> map) {
		try {
			/**
			 * update faculty
			 */
			return facultydao.updateFacultyDetails(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * delete faculty
	 * 
	 * @param map
	 * @return
	 */
	public int deleteFacultyDetails(Map<String, Object> map) {
		try {
			/**
			 * delete faculty
			 */
			return facultydao.deleteFaculty(map);

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
	public List<Map<String, Object>> getFacultyById(String id) {
		try {
			/**
			 * get faculty
			 */
			return facultydao.getFacultyById(id);
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
	public int deleteFacultyById(String id) {
		try {
			/**
			 * get faculty
			 */
			return facultydao.deleteFacultyById(id);
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
	public List<Map<String, Object>> getFacultyName(Map<String,Object>map) {
		try {
			/**
			 * get faculty
			 */
			return facultydao.getFacultyName(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public String updateDeleteFaculty(Map<String,Object> map) {
        try
        {
            /**
             *  insert data into database
             */
            return facultydao.updateDeleteFaculty(map);
            
        }catch(Exception e)
        {
            e.printStackTrace();
            return "Not Updated";
        }

}

}
