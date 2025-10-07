package com.nic.trainingportal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FacultyDao {
	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(FacultyDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 * 
	 * @param map
	 * @return
	 */
	public int addFaculty(final Map<String, Object> map) {
		try
		{
		int instituteType=0;	
		if(map.get("key").toString().equalsIgnoreCase("sird"))
		{
			instituteType=1;
		}else if(map.get("key").toString().equalsIgnoreCase("etc"))
		{
			instituteType=2;
		}else
		{
			instituteType=3;
		}
		    int userId=this.getUserId(map);
		if (userId!=0){

			String sql="insert into faculty(name,post,pay_scale,type_of_faculty,remarks,user_id,institutetype,financialyear,installmentno)values(?,?,?,?,?,?,?,?,?)";
			return jdbcTemplate.update(sql,map.get("name"),map.get("postHeld"),map.get("scalePay"),map.get("typeOfFaculty"),map.get("remarks"),userId,instituteType,map.get("financialYear"),map.get("installmentType"));
		}
       else {
	return 0;
		}
		}catch(Exception e)
		{
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}

        return 0;
	}
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllFaculty(int pageSize,int pageNumber,String userName,String userType,String financialYear,String installmentType) {
		try {
			int i=(pageNumber*pageSize)+1;
			int instituteType=0;
			if(userType.equalsIgnoreCase("sird"))
			{
				instituteType=1;
			}else 
			{
				instituteType=2;
			}
			List<Map<String,Object>>dataList=new ArrayList<Map<String,Object>>();
			int userId=this.getUserId(userName,userType);
			String institue = String.valueOf(instituteType);
			if((financialYear!=null && financialYear.length()!=0)   && (installmentType!=null && installmentType.length()!=0))
			{

				String sql = "SELECT financialyear AS \"financialYear\", " +
						"installmentno AS \"installmentType\", " +
						"faculty_id, name, post AS \"postHeld\", " +
						"pay_scale AS \"scalePay\", type_of_faculty AS \"typeOfFaculty\", remarks " +
						"FROM faculty " +
						"WHERE institutetype = ? AND user_id = ? AND financialyear = ? AND installmentno = ? " +
						"ORDER BY faculty_id DESC OFFSET ? LIMIT ?";

				int offset = pageNumber * pageSize;

				dataList = jdbcTemplate.queryForList(
						sql,
						institue,
						userId,
						financialYear,
						installmentType,
						offset,
						pageSize
				);

				for(Map<String,Object>map:dataList)
			{
				map.put("serialNo",i++);
			}
			return dataList;
			}else
			{int offset = pageNumber * pageSize;

				String sql = "SELECT financialyear AS \"financialYear\", " +
						"installmentno AS \"installmentType\", " +
						"faculty_id, name, post AS \"postHeld\", " +
						"pay_scale AS \"scalePay\", type_of_faculty AS \"typeOfFaculty\", remarks " +
						"FROM faculty " +
						"WHERE institutetype = ? AND user_id = ? " +
						"ORDER BY faculty_id DESC OFFSET ? LIMIT ?";

				dataList = jdbcTemplate.queryForList(sql, institue, userId, offset, pageSize);

				for(Map<String,Object>map:dataList)
				{
					map.put("serialNo",i++);
				}
				return dataList;
			}
			
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * delete faculty details
	 * 
	 * @param map
	 * @return
	 */
	public int deleteFaculty(Map<String, Object> map) {
		try {
			String sql = "DELETE FROM faculty WHERE faculty_id = ?";
			logger.info("Delete Faculty: {}", sql);
			return jdbcTemplate.update(sql, map.get("faculty_id"));

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int updateFacultyDetails(Map<String, Object> map) {
		try {
			int userIdint= getUserId(map);
			String check="select user_id from faculty WHERE faculty_id=?";
			Map<String, Object> id = jdbcTemplate.queryForMap(check, map.get("id"));
			if (!(userIdint==Integer.parseInt( id.get("user_id").toString()))){
				return  0;
			}
			String sql = "UPDATE faculty SET " +
					"name = ?, post = ?, pay_scale = ?, type_of_faculty = ?, remarks = ?, " +
					"installmentno = ?, financialyear = ? " +
					"WHERE faculty_id = ?";

// Logging (with correct format)
			logger.info("Update Faculty SQL: {}", sql);

// Execute with parameters
			return jdbcTemplate.update(
					sql,
					map.get("name"),
					map.get("postHeld"),
					map.get("scalePay"),
					map.get("typeOfFaculty"),
					map.get("remarks"),
					map.get("installmentType"),
					map.get("financialYear"),
					map.get("id")
			);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(Map<String,Object>map)
	{
		try {
			String tableName = "";
			String userType = map.get("key").toString();

			// Determine the table name
			if (userType.equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (userType.equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			// Build query with placeholder
			String sql = "SELECT " + userType + "_id FROM " + tableName + " WHERE username = ?";

			// Execute safely with parameter
			List<Map<String, Object>> results = jdbcTemplate.queryForList(sql,map.get("username"));

			if (results.isEmpty()) {
				System.err.println("No user found with username: " + map.get("username") + " in table: " + tableName);
//				throw  new IndexOutOfBoundsException("NO USER FOUND"); // Or throw custom exception
				return 0;
			}
			String idColumn = userType + "_id";

			// Extract and return ID
			return Integer.parseInt(results.get(0).get(idColumn).toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();

		}
		return 0;
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getFacultyById(String id) {
		try {

			String sql = "SELECT financialyear AS \"financialYear\", " +
					"installmentno AS \"installmentType\", " +
					"name, " +
					"post AS \"postHeld\", " +
					"pay_scale AS \"scalePay\", " +
					"type_of_faculty AS \"typeOfFaculty\", " +
					"remarks " +
					"FROM faculty " +
					"WHERE faculty_id = ?";

// Print log
//			ObjectMapper objectMapper = new ObjectMapper();
//			JsonNode jsonNode = objectMapper.readTree(id);
//			int idInt = Integer.parseInt(jsonNode.get("id").asText());
			int idInt= Integer.parseInt(id.toString());
			logger.info("Get All Faculty with ID: {}", id);

			return jdbcTemplate.queryForList(sql, idInt);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public int deleteFacultyById(String id) {
		try {
			String sql = "DELETE FROM faculty WHERE faculty_id = ?";

/**
 * print log
 */
			logger.info("Delete Faculty with ID: {}", id);
          int idint = Integer.parseInt(id);
			return jdbcTemplate.update(sql, idint);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getFacultyName(Map<String,Object>map) {
		try {
			int userId=this.getUserId(map);
			int userType=0;
			if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				userType=1;
			}else
			{
				userType=2;
			}
             String userTypeString = String.valueOf(userType).toString();
			String sql = "SELECT name, faculty_id FROM faculty WHERE user_id = ? AND institutetype = ?";
			logger.info("Get All Faculty with user_id={} and institutetype={}", userId, userTypeString);

			return jdbcTemplate.queryForList(sql, userId, userTypeString);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	public int getFacultyCount(int userId, String userType, String financialYear, String installmentType)
	{
		try {
			int institutetype = 0;
			if (userType.equalsIgnoreCase("sird")) {
				institutetype = 1;
			} else {
				institutetype = 2;
			}
			String instituteTypeString = String.valueOf(institutetype);
			if ((financialYear != null && financialYear.length() != 0) &&
					(installmentType != null && installmentType.length() != 0)) {

				String sql = "SELECT COUNT(*) FROM faculty WHERE institutetype = ? AND user_id = ? AND financialyear = ? AND installmentno = ?";
				return jdbcTemplate.queryForObject(sql, Integer.class, instituteTypeString, userId, financialYear, installmentType);

			} else {

				String sql = "SELECT COUNT(*) FROM faculty WHERE institutetype = ? AND user_id = ?";
				return jdbcTemplate.queryForObject(sql, Integer.class, instituteTypeString, userId);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(String userName,String userType)
	{
		try
		{
			String tableName="";
			/**
			 *  check login type
			 */
			if(userType.equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(userType.equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}else
			{
				tableName="loginmaster_ministry";
			}

			String columnName = userType.toLowerCase() + "_id";
			String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

			return Integer.parseInt(jdbcTemplate.queryForList(sql, userName).get(0).get(columnName).toString());

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean exist(int userId)
	{
		try
		{
			String sql="select * from faculty where user_id='"+userId+"'";
			List<Map<String,Object>>facultyList=new ArrayList<Map<String,Object>>();
			facultyList=jdbcTemplate.queryForList(sql);
            return facultyList != null && facultyList.size() != 0;
        }catch(Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
	
	@Transactional
    public String deleteFaculty1(Map<String, Object> map) {
        try {
			String table =  map.get("table").toString();
			String deleteSql = null;
			int proposalIdint = (int)map.get("proposalid");
			if (table.equals("faculty")) {
				deleteSql = "delete from faculty_proposal_final where proposalno = ?";
			}else if (table.equals("training")) {
				deleteSql = "delete from training_calender_final where proposalno =  ?";
			}else {
				return "Not deleted";
			}
			jdbcTemplate.update(deleteSql,proposalIdint);
        } catch (Exception e) {
            String[] str =e.getMessage().split("\\[");
             System.out.println(str[0]); 
             e.printStackTrace();
            return "Not deleted";
        }
        
        return "delete Successfully";
    }
	
	@Transactional
    public String updateDeleteFaculty(Map<String, Object> map) {
        try {
			int userId = this.getUserId(map);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> facultyForm = (List<Map<String, Object>>) map.get("facultyForm");

// Secure DELETE query
			String deleteSql = "DELETE FROM faculty_proposal_final WHERE proposalno = ?";
			int propsalIdint = Integer.parseInt(map.get("proposalId").toString());
			jdbcTemplate.update(deleteSql, propsalIdint);

// Secure INSERT queries
			String insertFacultyQry = "INSERT INTO faculty_proposal_final "
					+ "(pay, name, post, permanent, remarks, proposalno, userId, financialyear, installmentno) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			for (Map<String, Object> facultyMap : facultyForm) {
				jdbcTemplate.update(
						insertFacultyQry,
						facultyMap.get("scalePay"),
						facultyMap.get("name"),
						facultyMap.get("postHeld"),
						facultyMap.get("typeOfFaculty"),
						facultyMap.get("remarks"),
						propsalIdint,
						userId,
						facultyMap.get("financialYear"),
						facultyMap.get("installmentType")
				);
			}

			return "Updated";

		} catch (Exception e) {
          return "Not Updated";
        }
       
    }
}
