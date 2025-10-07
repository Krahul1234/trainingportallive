package com.nic.trainingportal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrainingDaoNirdpr {

	/**
	 * create logger class object
	 */
	 private static final Logger logger = LoggerFactory.getLogger(TrainingDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Add Faculty Details
	 *
	 * @param map
	 * @return
	 */
	public int addTrainingDetails(final Map<String, Object> map) {
		try
		{
//		int userId=this.getUserId(map);
			String sql = "insert into training_final_nirdpr " +
					"(entry_date, name, training_venue, training_subject, number_of_trainees, target_group, enddate, sessionplan, fundedby, uploadfile) " +
					"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			logger.info("Add Training Details: {}", sql);

			return jdbcTemplate.update(sql,
					map.get("proposedDate"),
					map.get("facultyName"),
					map.get("venue"),
					map.get("trainingSubject"),
					map.get("trainessNumber"),
					map.get("targetGroup"),
					map.get("endDate"),
					map.get("sessionPlan"),
					map.get("fundedBy"),
					map.get("uploadFile")
			);

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
	public List<Map<String, Object>> getTrainingDetails(int pageSize,int pageNumber) {
		try {
			List<Map<String,Object>>dataList=new ArrayList<Map<String,Object>>();
			String sql = "select fundedby as \"fundedBy\", uploadfile as \"uploadFile\", sessionplan as \"sessionPlan\", enddate, training_id, entry_date AS \"proposedDate\", name AS \"facultyName\", training_venue AS \"venue\", training_subject AS \"trainingSubject\", number_of_trainees AS \"trainessNumber\", target_group AS \"targetGroup\" " +
					"from training_final_nirdpr order by training_id desc offset ? limit ?";

			dataList = jdbcTemplate.queryForList(sql, pageNumber * pageSize, pageSize);


			return dataList;

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
	public int deleteTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "delete from training_final_nirdpr where id = ?";
			logger.info("Delete Training Details", sql);
			return jdbcTemplate.update(sql, map.get("id"));

		} catch (Exception e) {
			/**
			 *  print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int updateTrainingDetails(Map<String, Object> map) {
		try {
			String sql = "update training_final_nirdpr set enddate = ?, entry_date = ?, name = ?, training_venue = ?, training_subject = ?, number_of_trainees = ?, target_group = ?, sessionplan = ?, uploadfile = ?, fundedby = ? where training_id = ?";
			logger.info("Update Training Details", sql);
			return jdbcTemplate.update(sql,
					map.get("endDate"),
					map.get("proposedDate"),
					map.get("facultyName"),
					map.get("venue"),
					map.get("trainingSubject"),
					map.get("trainessNumber"),
					map.get("targetGroup"),
					map.get("sessionPlan"),
					map.get("uploadFile"),
					map.get("fundedBy"),
					map.get("id")
			);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.info("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Get All Faculty Details
	 *
	 * @return
	 */
	public List<Map<String, Object>> getCalendarInfoById(String id) {
		try {
			String sql = "SELECT uploadfile AS \"uploadFile\", fundedby AS \"fundedBy\", sessionplan AS \"sessionPlan\", enddate AS \"endDate\", entry_date AS \"proposedDate\", " +
					"name AS \"facultyName\", training_venue AS \"venue\", training_subject AS \"trainingSubject\", number_of_trainees AS \"trainessNumber\", target_group AS \"targetGroup\" " +
					"FROM training_final_nirdpr WHERE training_id = ?";

			logger.info("Get All Faculty", sql);

			return jdbcTemplate.queryForList(sql, id);

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
	public int deleteCalendarInfoById(String id) {
		try {
			String sql = "delete from training_final_nirdpr where training_id = ?";
			logger.info("Delete Training Details", sql);
			return jdbcTemplate.update(sql, id);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int getfacultyCount()
	{
		try
		{
			String sql="select count(*) as count from faculty";
			return jdbcTemplate.queryForObject(sql,Integer.class);
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
			String tableName = "";
			if (userType.equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (userType.equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			String columnName = userType.toLowerCase() + "_id"; // assuming column names are lowercase

			String sql = "select " + columnName + " from " + tableName + " where username = ?";

			List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userName);

			if (result.isEmpty()) {
				throw new RuntimeException("User not found");
			}

			return Integer.parseInt(result.get(0).get(columnName).toString());

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public int getTrainingCalendarCount()
	{
		try
		{
			String sql="select count(*) as count from training_final_nirdpr";
			return jdbcTemplate.queryForObject(sql,Integer.class);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}


	public int getUserId(Map<String,Object>map)
	{
		try
		{
			String tableName = "";
			String key = map.get("key").toString().toLowerCase();

			if (key.equals("etc")) {
				tableName = "loginmaster_etc";
			} else if (key.equals("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			String columnName = key + "_id"; // e.g., etc_id, sird_id, ministry_id
			String sql = "select " + columnName + " from " + tableName + " where username = ?";

			List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, map.get("userName"));

			if (result.isEmpty()) {
				throw new RuntimeException("User not found");
			}

			return Integer.parseInt(result.get(0).get(columnName).toString());

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
			String sql = "select * from training_final where user_id = ?";
			List<Map<String, Object>> facultyList = jdbcTemplate.queryForList(sql, userId);

			return facultyList != null && facultyList.size() != 0;
        }catch(Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}


	public Map<String,Object>  getTrainingCalenderCount(int userName,String userType)
    {
		try
        {   Map<String, Object> map = new HashMap<>();

// Get sird_id securely
			String sql1 = "select sird_id from loginmaster_sird where username = ?";
			String sird_id = jdbcTemplate.queryForObject(sql1, new Object[] { userName }, String.class);

// Get name securely
			String sqlName = "select name from sird where sird_id = ?";
			String name = jdbcTemplate.queryForObject(sqlName, new Object[] { sird_id }, String.class);

// Get count securely
			String sqlCount = "select count(*) from training_calendar where user_type = ? and user_id = ?";
			Integer count = jdbcTemplate.queryForObject(sqlCount, new Object[] { userType.toLowerCase(), sird_id }, Integer.class);

// Put count with key as the fetched name
			map.put(name, count);

		}catch(Exception e)
        {
            e.printStackTrace();
        }
        return new HashMap<String,Object>(0);
    }
    public List<Map<String, Object>> getCalendar(String userName,String userType){
        try {
			String sql = "select * from training_calendar where user_type = ? and user_id = ?";
			return jdbcTemplate.queryForList(sql, userType.toLowerCase(), userName);

		} catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public int addForwardCombinedProposalNew(Map<String,Object>map)
    {
    	try
    	{String userType = map.get("userType").toString();
			int proposalIdint = (int)map.get("proposalId");
			String remarks = map.get("remarks").toString();
			String userId = map.get("userid").toString();

			String lowerDesignation = "";
			String upperDesignation = "";
			String nextStatus = "";

			switch (userType.toLowerCase()) {
				case "sird":
					lowerDesignation = "sird";
					upperDesignation = "MORD(Section Officer)";
					nextStatus = "pending at MORD(Section Officer)";
					break;
				case "mord(section officer)":
					lowerDesignation = "MORD(Section Officer)";
					upperDesignation = "MORD(Under Secretary)";
					nextStatus = "pending at MORD(Under Secretary)";
					break;
				case "mord(under secretary)":
					lowerDesignation = "MORD(Under Secretary)";
					upperDesignation = "MORD(Deputy Secretary)";
					nextStatus = "pending at MORD(Deputy Secretary)";
					break;
				case "mord(deputy secretary)":
					lowerDesignation = "MORD(Deputy Secretary)";
					upperDesignation = "MORD(Additional Secretary)";
					nextStatus = "pending at MORD(Additional Secretary)";
					break;
				default:
					// Approve case
					String sqlApprove = "update remarks set forwarded=true, status=? where combined_proposal_id=? and status=?";
					jdbcTemplate.update(sqlApprove, "approved", proposalIdint, "pending at MORD(Additional Secretary)");
					return 1;
			}

// Insert remark for next user
			String sqlInsert = "insert into remarks (combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid) values (?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sqlInsert, proposalIdint, nextStatus, remarks, lowerDesignation, upperDesignation, userId);

// Update current remark forwarded = true
			String sqlUpdate = "update remarks set forwarded=true where combined_proposal_id=? and status=?";
			jdbcTemplate.update(sqlUpdate, proposalIdint, nextStatus.replace("pending", "pending at").replace("pending at", "").trim().replace("at ", "pending at "));


		}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return 1;
    }


    public List<Map<String,Object>>getAllCombinedDetails(String userType)
	{
		try
		{
            String status = "pending at " + userType;
            String sql = "select combined_proposal_id, sirdid, status, remarks, forwarded from remarks where status = ?";
            return jdbcTemplate.queryForList(sql, status);

        }catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

    public List<Map<String, Object>> getAggregatedProposalCounts() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            String sqlRecurring = "SELECT count(*) as count, financialyear, sirdname, UPPER(usertype) as usertype FROM final_proposal GROUP BY financialyear, sirdname, usertype";
            List<Map<String, Object>> recurringList = jdbcTemplate.queryForList(sqlRecurring);

            String sqlNonRecurring = "SELECT count(*) as count, financialyear, sirdname, UPPER(usertype) as usertype FROM nonrecurring_proposal GROUP BY financialyear, sirdname, usertype";
            List<Map<String, Object>> nonRecurringList = jdbcTemplate.queryForList(sqlNonRecurring);

            Map<String, Map<String, Object>> aggregatedMap = new HashMap<>();

            // Processing recurring list
            for (Map<String, Object> map : recurringList) {
                String key = map.get("sirdname") + "-" + map.get("usertype") + "-" + map.get("financialyear");
                Map<String, Object> aggregatedData = aggregatedMap.getOrDefault(key, new HashMap<>());
                aggregatedData.put("sirdname", map.get("sirdname"));
                aggregatedData.put("usertype", map.get("usertype"));
                aggregatedData.put("financialyear", map.get("financialyear"));
                aggregatedData.put("recurringCount", map.get("count"));
                aggregatedData.put("nonrecurringCount", aggregatedData.getOrDefault("nonrecurringCount", 0));
                aggregatedMap.put(key, aggregatedData);
            }

            // Processing non-recurring list
            for (Map<String, Object> map : nonRecurringList) {
                String key = map.get("sirdname") + "-" + map.get("usertype") + "-" + map.get("financialyear");
                Map<String, Object> aggregatedData = aggregatedMap.getOrDefault(key, new HashMap<>());
                aggregatedData.put("sirdname", map.get("sirdname"));
                aggregatedData.put("usertype", map.get("usertype"));
                aggregatedData.put("financialyear", map.get("financialyear"));
                aggregatedData.put("nonrecurringCount", map.get("count"));
                aggregatedData.put("recurringCount", aggregatedData.getOrDefault("recurringCount", 0));
                aggregatedMap.put(key, aggregatedData);
            }

            resultList.addAll(aggregatedMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

}
