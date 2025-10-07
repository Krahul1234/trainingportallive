package com.nic.trainingportal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrainingInfoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	public int addInformation(final Map<String, Object> map) {
		
		try
		{
			int userId = this.getUserId(map);
			if (userId!=0) {
				String insertQry = "INSERT INTO training_info (functional, building, number_of_permanent_faculty, number_of_contractual_faculty, "
						+ "number_of_other_staff, hostel_facility, number_of_seat, hall_number, hall_capacity, lab_number, "
						+ "lab_capacity, dining_number, dining_capacity, number_of_kitchens, auditorium_number, auditorium_capacity, "
						+ "Remarks, user_id, user_type) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				return jdbcTemplate.update(insertQry,
						map.get("functional"),
						map.get("building"),
						map.get("number_of_permanent_faculty"),
						map.get("number_of_contractual_faculty"),
						map.get("number_of_other_staff"),
						map.get("hostel_facility"),
						map.get("number_of_seat"),
						map.get("hall_number"),
						map.get("hall_capacity"),
						map.get("lab_number"),
						map.get("lab_capacity"),
						map.get("dining_number"),
						map.get("dining_capacity"),
						map.get("number_of_kitchens"),
						map.get("auditorium_number"),
						map.get("auditorium_capacity"),
						map.get("remarks"),
						userId,
						map.get("key").toString().toLowerCase()
				);
			}
			else {
				return 0;
			}

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

	}

	public List<Map<String, Object>> getInformation( int pageSize, int pageNumber,int userId,String userType) {
		try {
			int i=(pageNumber*pageSize)+1;
			List<Map<String,Object>>trainingList=new ArrayList<Map<String,Object>>();
			String sql = "select * from training_info where user_type = ? and user_id = ? order by info_id desc offset ? limit ?";
			int offset = pageNumber * pageSize;

			trainingList = jdbcTemplate.queryForList(sql, userType.toLowerCase(), userId, offset, pageSize);

			for(Map<String,Object>map:trainingList)
			{
				map.put("serialNo", i++);
			}
			return trainingList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public int updateInformation(Map<String, Object> map) {
		try {
			 int userIdint= getUserId(map);
			 String check="select user_id,user_type from training_info WHERE info_id=?";
			Map<String, Object> id = jdbcTemplate.queryForMap(check, map.get("id"));
			if (!map.get("key").toString().equalsIgnoreCase(id.get("user_type").toString())|| !(userIdint==Integer.parseInt( id.get("user_id").toString()))){
				return  0;
			}

			String updateQry = "UPDATE training_info SET remarks=?, functional=?, number_of_permanent_faculty=?, number_of_contractual_faculty=?, " +
					"number_of_other_staff=?, hostel_facility=?, number_of_seat=?, hall_number=?, hall_capacity=?, lab_number=?, lab_capacity=?, " +
					"dining_number=?, dining_capacity=?, number_of_kitchens=?, auditorium_number=?, auditorium_capacity=?, building=? WHERE info_id=?";

			return jdbcTemplate.update(updateQry,
					map.get("remarks"),
					map.get("functional"),
					map.get("number_of_permanent_faculty"),
					map.get("number_of_contractual_faculty"),
					map.get("number_of_other_staff"),
					map.get("hostel_facility"),
					map.get("number_of_seat"),
					map.get("hall_number"),
					map.get("hall_capacity"),
					map.get("lab_number"),
					map.get("lab_capacity"),
					map.get("dining_number"),
					map.get("dining_capacity"),
					map.get("number_of_kitchens"),
					map.get("auditorium_number"),
					map.get("auditorium_capacity"),
					map.get("building"),
					map.get("id")
			);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(Map<String,Object>map)
	{
		try
		{String tableName = "";
			String key = map.get("key").toString().toLowerCase();
			String username = map.get("username").toString();

			if (key.equals("etc")) {
				tableName = "loginmaster_etc";
			} else if (key.equals("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			String idColumn = key + "_id";
			String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE username = ?";

			Integer userId = jdbcTemplate.queryForObject(sql, new Object[] { username }, Integer.class);
			return userId;

		}catch(Exception e)
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
	public List<Map<String, Object>> getTrainingInfoById(Integer id) {
		try {
//			int idInt = Integer.parseInt(id);
			String sql = "select * from training_info where info_id = ?";
			return jdbcTemplate.queryForList(sql, id);

		} catch (Exception e) {
			/**
			 * print error log
			 */
			
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public int deleteTrainingInfoById(String id) {
		try {
			String sql = "delete from training_info where info_id=?";
			int idInt= Integer.parseInt(id);
			return jdbcTemplate.update(sql,idInt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(String userName,String userType)
	{
		try
		{String tableName = "";
			String idColumn = "";

			if (userType.equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
				idColumn = "etc_id";
			} else if (userType.equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
				idColumn = "sird_id";
			} else {
				tableName = "loginmaster_ministry";
				idColumn = "min_id";
			}

			String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE username = ?";

			List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userName);

			if (!result.isEmpty()) {
				return Integer.parseInt(result.get(0).get(idColumn).toString());
			} else {
				// Handle no record found scenario, maybe throw exception or return a default value
				throw new RuntimeException("User not found");
			}

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getTrainingInfoCount(int userId,String userType)
	{
		try
		{
			String sql = "select count(*) from training_info where user_type = ? and user_id = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { userType.toLowerCase(), userId }, Integer.class);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean exist(int userId,Map<String,Object>map)
	{
		try
		{
			String sql = "select * from training_info where user_type = ? and user_id = ?";
			List<Map<String, Object>> facultyList = jdbcTemplate.queryForList(sql, map.get("key"), userId);

			return facultyList != null && facultyList.size() != 0;
        }catch(Exception e)
		{
			e.printStackTrace();
			return true;
		}
	}
	
	public List<Map<String, Object>> getAllEtc() {
	       try {
	          String sql = "SELECT \"Name\", state_name, email FROM etc WHERE \"Name\" IS NOT NULL" ;
	          return jdbcTemplate.queryForList(sql);
	       } catch (Exception e) {
	          e.printStackTrace();
	       }
	       return new ArrayList<Map<String, Object>>(0);
	   }

	public Map<String, Object> getAllCount() {
	    try {
	       Map<String,Object> count = new HashMap<>();
	      String sqlEtc ="select count(*) from etc WHERE \"Name\" IS NOT NULL";
	    Integer etcCount = jdbcTemplate.queryForObject(sqlEtc, Integer.class);
	    String sqlSird ="select count(*) from sird WHERE \"Name\" IS NOT NULL";
	    Integer sirdCount =    jdbcTemplate.queryForObject(sqlSird, Integer.class);
	    count.put("etc",etcCount);
	    count.put("sird",sirdCount);
	    return  count;
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	    return new HashMap<String, Object>();
	}

	public List<Map<String, Object>> getAllSird() {
	    try {
	       String sql = "SELECT \"Name\", state_name, email FROM sird WHERE \"Name\" IS NOT NULL" ;
	       return jdbcTemplate.queryForList(sql);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	    return new ArrayList<Map<String, Object>>(0);
	}
	
	
	public List<Map<String, Object>> getCalendar(String userName,String userType){
        try {
             String userId = String.valueOf(this.getUserId(userName,userType));

			String sql = "SELECT tc.id, s.\"Name\" as userName, tc.lgdstatecode, tc.user_id, tc.institute_type, " +
					"tc.proposed_date_of_trg_prog, tc.name_of_faculty_course_coordinator, tc.venue_trg_prog, " +
					"tc.subject_of_trg_prog, tc.number_of_trainees_proposed, tc.target_group, tc.entry_date, " +
					"tc.entry_ip, tc.training_end_date, tc.user_type, " +
					"(SELECT COUNT(*) FROM training_final WHERE user_id = tc.user_id) AS count " +
					"FROM training_calendar tc " +
					"JOIN " + userType + " s ON s." + userType + "_id = tc.user_id " +
					"WHERE tc.user_id = ?";

			return jdbcTemplate.queryForList(sql, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public Object getInformation(String userName, String userType) {
        try
        {    Map<String,Object> map = new HashMap<>();
             int userId =this.getUserId(userName,userType);
			String sql1 = "SELECT s.\"Name\" AS username, ti.info_id, ti.functional, ti.building, ti.number_of_permanent_faculty, " +
					"ti.number_of_contractual_faculty, ti.number_of_other_staff, ti.hostel_facility, ti.number_of_seat, " +
					"ti.hall_number, ti.hall_capacity, ti.lab_number, ti.lab_capacity, ti.dining_number, ti.dining_capacity, " +
					"ti.number_of_kitchens, ti.auditorium_number, ti.auditorium_capacity, ti.remarks, ti.user_id, ti.user_type " +
					"FROM training_info ti JOIN " + userType + " s ON s." + userType + "_id = ti.user_id " +
					"WHERE ti.user_id = ? AND ti.user_type = ?";

			return jdbcTemplate.queryForList(sql1, userId, userType);


		}catch(Exception e)
        {
            e.printStackTrace();
        }
        return new HashMap<String,Object>(0);
    }
    
    
    public List<Map<String, Object>> getAllCal() {
  	  List<Map<String, Object>> allResults = new ArrayList<>();
      
      try {
         
      	
      	String sql ="select proposalid from final_proposal";
       List<Map<String, Object>> queryForList3 = jdbcTemplate.queryForList(sql);
       for (Map<String, Object> map : queryForList3) {

		   Object propsalId = map.get("proposalid");

		   String sql1 = "SELECT ROW_NUMBER() OVER (ORDER BY training_final_id) AS serial_number, " +
				   "training_final_id AS id, lgdstatecode AS lgdstatecode, user_id AS user_id, " +
				   "training_venue AS venue_trg_prog, fin_year AS financialyear, \"name\" AS userName, " +
				   "training_subject AS subject_of_trg_prog, number_of_trainees AS number_of_trainees_proposed, " +
				   "entry_date AS entry_date, entry_ip AS entry_ip, entry_by AS entry_by, counter AS counter, " +
				   "target_group AS target_group, usertype AS user_type, enddate AS training_end_date, " +
				   "nameofcordinator AS name_of_faculty_course_coordinator, proposalno AS proposalno, totalduration AS totalduration " +
				   "FROM training_final WHERE proposalno = ? ORDER BY training_final_id";

		   List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql1, propsalId);

		   for (Map<String, Object> map2 : queryForList) {
              if (map2.get("user_type").toString().equalsIgnoreCase("sird")) {
				  String sql2 = "SELECT \"Name\" AS userName, state_name FROM sird WHERE sird_id = ?";
				  Integer id = Integer.parseInt((String) map2.get("user_id"));
				  List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

				  for (Map<String, Object> map3 : queryForList2) {
                      Object object = map3.get("userName");
                      Object object2 = map3.get("state_name");

                      map2.put("userName", object);
                      map2.put("stateName", object2);
                  }

              } else {
				  String sql2 = "SELECT \"Name\" AS userName, state_name FROM etc WHERE etc_id = ?";
				  Integer id = Integer.parseInt((String) map2.get("user_id"));
				  List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

				  for (Map<String, Object> map3 : queryForList2) {
                      Object object = map3.get("userName");
                      Object object2 = map3.get("state_name");

                      map2.put("userName", object);
                      map2.put("stateName", object2);
                  }
              }
              allResults.add(map2);
          }
       }
      }
          
       catch (Exception e) {
          e.printStackTrace();
      }
      return allResults;
  
     
}
    public Object getAllInfo(Map<String, Object> maps) {
		try {
			int pageNumber = Integer.parseInt(maps.get("pageNumber").toString());
			int pageSize = Integer.parseInt(maps.get("pageSize").toString());
			int offset = pageNumber * pageSize;

			String sql1 = "SELECT ROW_NUMBER() OVER (ORDER BY training_info_id) AS serial_number, training_info_id, functional, building, number_of_permanent_faculty, number_of_contractual_faculty,"
					+ " number_of_other_staff, hostel_facility, number_of_seat, hall_number, hall_capacity, lab_number, "
					+ "lab_capacity, dining_number, dining_capacity, number_of_kitchens, auditorium_number, auditorium_capacity,"
					+ " remarks, user_id, UPPER(user_type) AS usertype FROM training_info OFFSET " + offset + " LIMIT " + pageSize;
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql1);
			for (Map<String, Object> map2 : queryForList) {
			
				if (map2.get("usertype").toString().equalsIgnoreCase("sird")) {
					String sql2 = "SELECT \"Name\" AS userName, state_name FROM sird WHERE sird_id = ?";
					Integer id = Integer.parseInt(map2.get("user_id").toString());
					List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

					for (Map<String, Object> map3 : queryForList2) {
						 Object object = map3.get("userName");
						 Object object2 = map3.get("state_name");
						
						 map2.put("userName", object);
						 map2.put("stateName", object2);
					}
					 
					
				} else {
					String sql2 = "SELECT \"Name\" AS userName, state_name FROM etc WHERE etc_id = ?";
					Integer id = Integer.parseInt(map2.get("user_id").toString());

					List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

					for (Map<String, Object> map3 : queryForList2) {
						 Object object = map3.get("userName");
						 Object object2 = map3.get("state_name");
						
						 map2.put("userName", object);
						 map2.put("stateName", object2);
					}
				}

			}
         
			return queryForList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<String, Object>(0);
	
}

    
    public int getTrainingInfoCount() {
        try {
            String sql = "select count(*) as count from training_info";
                    
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }public int getTrainingCalendarCount() {
        try {
            String sql = "select count(*) as count from training_Calendar";
                    
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
    public List<Map<String, Object>> calendarCount(){
		try {
			Map<String, Object> map = new HashMap<>();
			int i=1;
//			String sql1 = "select training_final_id, UPPER(usertype) as usertype,count(*) as count,user_id,fin_year as financialyear "
//					+ "from training_final group by training_final_id,usertype,user_id,fin_year  order by usertype desc";
			String sql1 = "select  UPPER(usertype) as user_type,count(*) as count,user_id  from training_final group by usertype,user_id  order by usertype desc";
			List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql1);
			for (Map<String, Object> map2 : queryForList) {
			
				if ( map2.get("user_type")!=null &&  map2.get("user_type").toString().equalsIgnoreCase("sird")) {
//					System.out.println(map2.get("training_final_id"));
					String sql2 = "SELECT \"Name\" AS userName, state_name FROM sird WHERE sird_id = ?";
					Integer id = Integer.parseInt((String) map2.get("user_id"));
					List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

					for (Map<String, Object> map3 : queryForList2) {
						 Object object = map3.get("userName");
						 Object object2 = map3.get("state_name");
						 map2.put("serial_number",i++);
						 map2.put("userName", object);
						 map2.put("stateName", object2);
						 map2.put("financialyear", "2024-2025");
					}
					 
					
				} else {
					String sql2 = "SELECT \"Name\" AS userName, state_name FROM etc WHERE etc_id = ?";
					Integer id = Integer.parseInt((String) map2.get("user_id"));
					List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList(sql2, id);

					for (Map<String, Object> map3 : queryForList2) {
						 Object object = map3.get("userName");
						 Object object2 = map3.get("state_name");
						 map2.put("serial_number",i++);
						 map2.put("userName", object);
						 map2.put("stateName", object2);
						 map2.put("financialyear", "2024-2025");
					}
				}

			}
     
			return queryForList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	
}
    
    public String updateTranningProposal(Map<String,Object>map) {
		try {
			int proposalIdint = Integer.parseInt(map.get("proposalId").toString());
			if(map.get("proposalType").toString().equalsIgnoreCase("recurring"))
			{
			@SuppressWarnings("unchecked")
			Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
				String updateQuery = "UPDATE final_proposal SET " +
						"functional = ?, " +
						"building = ?, " +
						"numberOfFacultyPermanent = ?, " +
						"numberOfFacultyContractual = ?, " +
						"numberOfOtherStaff = ?, " +
						"hostelFacility = ?, " +
						"numberOfSeatsInHostel = ?, " +
						"trainingHallsNumber = ?, " +
						"trainingHallsCapacity = ?, " +
						"computerLabNumber = ?, " +
						"computerLabCapacity = ?, " +
						"diningHallsNumber = ?, " +
						"diningHallsCapacity = ?, " +
						"numberOfKitchens = ?, " +
						"auditoriumOrCommitteeRoomNumber = ?, " +
						"auditoriumOrCommitteeRoomCapacity = ?, " +
						"instituteRemarks = ?, " +
						"TypeofGrantRequested = ? " +
						"WHERE proposalId = ?";

				jdbcTemplate.update(updateQuery,
						instituteInfo.get("functional"),
						instituteInfo.get("building"),
						instituteInfo.get("number_of_permanent_faculty"),
						instituteInfo.get("number_of_contractual_faculty"),
						instituteInfo.get("number_of_other_staff"),
						instituteInfo.get("hostel_facility"),
						instituteInfo.get("number_of_seat"),
						instituteInfo.get("hall_number"),
						instituteInfo.get("hall_capacity"),
						instituteInfo.get("lab_number"),
						instituteInfo.get("lab_capacity"),
						instituteInfo.get("dining_number"),
						instituteInfo.get("dining_capacity"),
						instituteInfo.get("number_of_kitchens"),
						instituteInfo.get("auditorium_number"),
						instituteInfo.get("auditorium_capacity"),
						instituteInfo.get("remarks"),
						instituteInfo.get("TypeofGrantRequested"),
						proposalIdint
				);

				return "Updated";
			}else
			{
				@SuppressWarnings("unchecked")
				Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
				String updateQuery = "UPDATE nonrecurring_proposal SET " +
						"functional = ?, " +
						"building = ?, " +
						"numberoffacultypermanent = ?, " +
						"numberoffacultycontractual = ?, " +
						"numberofotherstaff = ?, " +
						"hostelfacility = ?, " +
						"numberOfSeatsInHostel = ?, " +
						"trainingHallsNumber = ?, " +
						"trainingHallsCapacity = ?, " +
						"computerLabNumber = ?, " +
						"computerLabCapacity = ?, " +
						"diningHallsNumber = ?, " +
						"diningHallsCapacity = ?, " +
						"numberOfKitchens = ?, " +
						"auditoriumOrCommitteeRoomNumber = ?, " +
						"auditoriumOrCommitteeRoomCapacity = ?, " +
						"instituteRemarks = ?, " +
						"TypeofGrantRequested = ? " +
						"WHERE proposalId = ?";

				jdbcTemplate.update(updateQuery,
						instituteInfo.get("functional"),
						instituteInfo.get("building"),
						instituteInfo.get("number_of_permanent_faculty"),
						instituteInfo.get("number_of_contractual_faculty"),
						instituteInfo.get("number_of_other_staff"),
						instituteInfo.get("hostel_facility"),
						instituteInfo.get("number_of_seat"),
						instituteInfo.get("hall_number"),
						instituteInfo.get("hall_capacity"),
						instituteInfo.get("lab_number"),
						instituteInfo.get("lab_capacity"),
						instituteInfo.get("dining_number"),
						instituteInfo.get("dining_capacity"),
						instituteInfo.get("number_of_kitchens"),
						instituteInfo.get("auditorium_number"),
						instituteInfo.get("auditorium_capacity"),
						instituteInfo.get("remarks"),
						instituteInfo.get("TypeofGrantRequested"),
						proposalIdint
				);

				return "Updated";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}
	}
    
    public String  updateFundsProposal(Map<String, Object> map) {
		try {
			Object propsalId = map.get("proposalid");
			@SuppressWarnings("unchecked")
			Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
			BigDecimal fundsReleasedbyMord = new BigDecimal(String.valueOf(instituteInfo.get("fundsReleasedbyMord")));
	        BigDecimal otherResources = new BigDecimal(String.valueOf(instituteInfo.get("otherResources")));
	        BigDecimal mordAndStateShare = new BigDecimal(String.valueOf(instituteInfo.get("mordAndStateShare")));
	        BigDecimal fundsFromVariousSources = new BigDecimal(String.valueOf(instituteInfo.get("fundsFromVariousSources")));
			String updateQuery = "UPDATE final_proposal SET " +
					"fundsReleasedByMord = ?, " +
					"otherresources = ?, " +
					"mordAndStateShare = ?, " +
					"fundsFromVariousSources = ? " +
					"WHERE proposalId = ?";

			jdbcTemplate.update(updateQuery,
					fundsReleasedbyMord,
					otherResources,
					mordAndStateShare,
					fundsFromVariousSources,
					propsalId
			);

			return "Updated";
		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}
	}
}
