package com.nic.trainingportal.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DemographicDao {
	private static final Logger logger = LoggerFactory.getLogger(DemographicDao.class);
	@Autowired
	private JdbcTemplate jdbctemplate;

	public int addDemographicDetails(Map<String, Object> map) {
		try {
			int sird_id = this.getUserId(map);
			if(sird_id!=0) {
				String sql = "insert into demographic(total_population_state,total_rural_population,percent_rural_population,state_code,usertype,user_id) values(?,?,?,?,?,?)";
				return jdbctemplate.update(sql, map.get("totalPopulation"), map.get("totalRuralPopulation"),
						map.get("percentOfRural"), map.get("stateCode"), map.get("key").toString().toLowerCase(), sird_id);
			}
			else {
				return 0;
			}
		} catch (Exception e) { 
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
			return 0;
		}
	}

	public List<Map<String, Object>> getDemographicDetails(int pageSize, int pageNumber, int userId, String userType,
			String userName) {
		try {
			int stateCode = this.GetStateCode(userType, userName);

			int offset = pageNumber * pageSize;

			String sql = "SELECT * FROM demographic WHERE state_code = ? ORDER BY id OFFSET " + offset + " LIMIT " + pageSize;

			logger.info("Get Demographic Details SQL: {}", sql);
			return jdbctemplate.queryForList(sql, stateCode);


		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public int updateDemographicDetails(Map<String, Object> map) {
		try {
//		 String sql="update demographic set usertype='"+map.get("userType")+"',sird_id='"+map.get("sirdId")+"',total_population_state='"+map.get("totalPopulation")+"',total_rural_population='"+map.get("totalRuralPopulation")+"',percent_rural_population='"+map.get("percentOfRural")+"',state_code='"+map.get("stateCode")+"' where id='"+map.get("id")+"'";
//		 /**
//			 * print log
//			 */
//		 logger.info("Update Demographic Details", sql);
//		return jdbctemplate.update(sql);
			String sql = "update demographic set total_population_state=?,total_rural_population=?,percent_rural_population=?,state_code=? where id=?";
			return jdbctemplate.update(sql, map.get("totalPopulation"), map.get("totalRuralPopulation"),
					map.get("percentOfRural"), map.get("stateCode"), map.get("id"));
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int getUserId(Map<String, Object> map) {
		try {
			String tableName = "";
			/**
			 * check login type
			 */
			if (map.get("key").toString().equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (map.get("key").toString().equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			// 1. Whitelist allowed table names and keys
			Set<String> allowedTables = Set.of("user", "admin", "faculty");  // <-- customize
			Set<String> allowedKeys = Set.of("user", "faculty", "officer");

			String key = map.get("key").toString();
			String userName = map.get("userName").toString();

			if (!allowedTables.contains(tableName) || !allowedKeys.contains(key)) {
				throw new IllegalArgumentException("Invalid table or key");
			}

			String column = key + "_id";
			String sql = "SELECT " + column + " FROM " + tableName + " WHERE username = ?";
			List<Map<String, Object>> result = jdbctemplate.queryForList(sql, userName);

			if (result.isEmpty()) {
				throw new NoSuchElementException("No user found with username: " + userName);
			}

			return Integer.parseInt(result.get(0).get(column).toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDemographicById(String id) {
		try {
			int idInt = Integer.parseInt(id);
			String sql = "SELECT total_population_state AS \"totalPopulation\", " +
					"total_rural_population AS \"totalRuralPopulation\", " +
					"percent_rural_population AS \"percentOfRural\", " +
					"state_code AS \"stateCode\" " +
					"FROM demographic WHERE id = ?";

			logger.info("Get All Faculty: {}", sql);

			return jdbctemplate.queryForList(sql, idInt);

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
	public int deleteDemographicById(String id) {
		try {
			String sql = "delete from demographic where id= ? ";
			/**
			 * print log
			 */
			logger.info("Get All Faculty", sql,id);
			int idInt= Integer.parseInt(id);
			return jdbctemplate.update(sql,idInt);
		} catch (Exception e) {
			/**
			 * print error log
			 */
			logger.error("An error occurred while doing something", e);
			e.printStackTrace();
		}
		return 0;
	}

	public int getDemographicCount() {
		try {
			String sql = "select count(*) as count from demographic";
			return jdbctemplate.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getUserId(String userName, String userType) {
		try {
			String tableName = "";
			/**
			 * check login type
			 */
			if (userType.equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (userType.equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				userType = "min";
				tableName = "loginmaster_ministry";
			}


			String columnName = userType + "_id";
			String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

			List<Map<String, Object>> result = jdbctemplate.queryForList(sql, userName);

			if (result.isEmpty()) {
				throw new NoSuchElementException("No result for username: " + userName);
			}

			return Integer.parseInt(result.get(0).get(columnName).toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getDemographicCount(int userId, String userType) {
		try {
			String id = String.valueOf(userId);
			String sql = "SELECT count(*) AS count FROM demographic WHERE userType = ? AND user_id = ?";
			return jdbctemplate.queryForObject(sql, Integer.class, userType.toLowerCase(), id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Map<String, Object>> getFinancialYear() {
		try {
			String sql = "select fin_year from financial_year";
			return jdbctemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public boolean exist(Map<String, Object> map) {
		try {
			String sql = "SELECT * FROM demographic WHERE state_code = ?";
			List<Map<String, Object>> facultyList = jdbctemplate.queryForList(sql, map.get("stateCode"));

			return facultyList != null && facultyList.size() != 0;
        } catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	public List<String> GetProposalInstallment(String userType, String userName,String financialYear) {
		try {
			List<String> installmentList = new ArrayList<String>();
			installmentList.add("1st");
			installmentList.add("2nd");
			installmentList.add("3rd");
			installmentList.add("4th");
			int userId = this.getUserId(userName, userType);

			String sql = "SELECT installmentno, usertype, user_id FROM final_proposal " +
					"WHERE user_id = ? AND usertype = ? AND financialyear = ?";

			List<Map<String, Object>> proposallist = jdbctemplate.queryForList(sql, userId, userType, financialYear);


			if(proposallist!=null && proposallist.size()!=0)
			{
			for(Map<String,Object>map:proposallist)
			{
						installmentList.remove(map.get("installmentno"));
					}
			}
			return installmentList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public int GetStateCode(String userType, String userName) {
		try {
			int userId = this.getUserId(userName, userType);
			// Whitelist allowed user types and corresponding table names


			String tableName = userType;
			String columnName = userType + "_id";
			String sql = "SELECT state_code FROM " + tableName + " WHERE " + columnName + " = ?";

			List<Map<String, Object>> result = jdbctemplate.queryForList(sql, userId);

			if (result.isEmpty()) {
				throw new NoSuchElementException("No record found for userId: " + userId);
			}

			int stateCode = Integer.parseInt(result.get(0).get("state_code").toString());

			return stateCode;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<String> GetProposalInstallmentNonRecurring(String userType, String userName) {
		try {
			List<String> installmentList = new ArrayList<String>();
			installmentList.add("1st");
//			installmentList.add("2nd");
//			installmentList.add("3rd");
//			installmentList.add("4th");
			int userId = this.getUserId(userName, userType);

			String sql = "SELECT proposal_no FROM nonrecurring_proposal WHERE user_id = ? AND usertype = ?";
			List<Map<String, Object>> proposallist = jdbctemplate.queryForList(sql, userId, userType);


			if (proposallist != null && proposallist.size() != 0) {
				for (Map<String, Object> map : proposallist) {
					if (map.get("proposal_no") != null) {
						installmentList.remove(map.get("proposal_no"));
					}
				}
			}
			return installmentList;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public List<Map<String, Object>> getDemographicFinal() {
		try {
			String sql = "select * from demographic_profile_final";
			return jdbctemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public List<Map<String, Object>> getProposalVsApproval() {
		try {
			String sql = "select * from trn_proposalvsapproval";
			return jdbctemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	public List<Map<String, Object>> sirdSanction() {

		List<Map<String, Object>> dataList = new ArrayList<>();

		try {

			List<Map<String, Object>> state = new ArrayList<>();
			String sirdSql = "select state_name,\"Name\",state_code,sird_id,user_type from sird";
			List<Map<String, Object>> queryForSird = jdbctemplate.queryForList(sirdSql);
			// code for sird
			for (Map<String, Object> sirdMap : queryForSird) {
				Map<String, Object> stateName = new HashMap<>();
				String sirdName = (String) sirdMap.get("Name");
				String stateNameValue = (String) sirdMap.get("state_name");
				stateName.put("StateName", stateNameValue);
				stateName.put("name_of_etcs", "null");

				String sql = "SELECT proposedtotal, proposalid, financialyear FROM final_proposal WHERE usertype = ? AND user_id = ?";
				Object id = sirdMap.get("sird_id");
				String idString = sirdMap.get("sird_id").toString();
				List<Map<String, Object>> sirdDemandList = jdbctemplate.queryForList(sql, "sird", id);

				String sirdNonRec = "SELECT tovffundstotal AS proposedtotal, proposalid, financialyear " +
						"FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ?";

				List<Map<String, Object>> sirdDemandListN = jdbctemplate.queryForList(sirdNonRec, "sird", id);

				sirdDemandList.addAll(sirdDemandListN);
				Map<String, Object> sirdDetails = new HashMap<>();
				stateName.put("sird_name", sirdName);
				stateName.put("stateCode", sirdMap.get("state_code"));
				stateName.put("previousYearDemand", "0");
				stateName.put("previousYearSanction", "0");
				String trainingCount = "SELECT count(*) FROM training_final WHERE usertype = ? AND user_id = ?";
				int traineeCount = jdbctemplate.queryForObject(trainingCount, Integer.class, "sird",idString);

				stateName.put("traning_center_sird", traineeCount);

				if (sirdDemandList == null || sirdDemandList.size() == 0) {
					stateName.put("Curent_total_demand_sird", 0);
					stateName.put("Current_sanction_sird", 0);

				} else {

					for (Map<String, Object> demandMap : sirdDemandList) {

						if (demandMap.get("proposalid") != null) {
							String combinedProposal = "SELECT combined_proposal_id, proposaltype FROM combined_proposal " +
									"WHERE proposalid = ? AND combinedstatus IS NULL";
							String proposalID = demandMap.get("proposalid").toString();
							List<Map<String, Object>> queryForPropsal = jdbctemplate.queryForList(combinedProposal, proposalID);


							if (!queryForPropsal.isEmpty()
									&& queryForPropsal.get(0).get("combined_proposal_id") != null) {
								if (queryForPropsal.get(0).get("proposaltype") != null) {
									stateName.put("proposalType", "Non Reccuring");
								} else {
									stateName.put("proposalType", "Reccuring");
								}
								String status = "SELECT status FROM remarks WHERE combined_proposal_id = ?";
								Object combinedProposalId = queryForPropsal.get(0).get("combined_proposal_id");

								List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(status, combinedProposalId);

								for (Map<String, Object> map : queryForCombined) {
									if (map.get(status) == "approved") {
										stateName.put("isApproved", "YES");
									} else {
										stateName.put("isApproved", "NO");
									}
								}
								
							} else {
								stateName.put("isApproved", "NO");
							}
						}

						String financialYear = (String) demandMap.get("financialYear");
						stateName.put("financialyear", financialYear);
						BigDecimal proposedTotal = new BigDecimal(String.valueOf(demandMap.get("proposedtotal")));
						stateName.put("Curent_total_demand_sird", proposedTotal);
						String sacntion = "SELECT proposedtotalsanction FROM proposal_sanction WHERE proposalid = ?";
						int propsalIdint = (int) demandMap.get("proposalid");
						List<Map<String, Object>> queryForList1 = jdbctemplate.queryForList(sacntion, propsalIdint);

						if (queryForList1 != null && queryForList1.size() != 0) {
							stateName.put("Current_sanction_sird", queryForList1.get(0).get("proposedtotalsanction"));
						}
					}
				}

				dataList.add(stateName);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	public List<Map<String, Object>> etcSanction() {
		List<Map<String, Object>> dataList = new ArrayList<>();

		String etcSql = "select \"Name\",etc_id,user_type,state_name,state_code,name_of_etcs from etc";
		List<Map<String, Object>> queryForEtc = jdbctemplate.queryForList(etcSql);
		for (Map<String, Object> etcMap : queryForEtc) {
			Map<String, Object> stateName = new HashMap<>();
			String sirdName = (String) etcMap.get("Name");
			String stateNameValue = (String) etcMap.get("state_name");
			stateName.put("StateName", stateNameValue);
			stateName.put("name_of_etcs", etcMap.get("name_of_etcs"));

			stateName.put("previousYearDemand", "0");
			stateName.put("previousYearSanction", "0");
			String sirddemand = "SELECT proposedtotal, proposalid, financialyear FROM final_proposal " +
					"WHERE usertype = ? AND user_id = ?";

			List<Map<String, Object>> sirdDemandList = jdbctemplate.queryForList(sirddemand, "etc", etcMap.get("etc_id"));

			String sirdNonRec = "SELECT tovffundstotal AS proposedtotal, proposalid, financialyear " +
					"FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ?";

			List<Map<String, Object>> sirdDemandListN = jdbctemplate.queryForList(sirdNonRec, "etc", etcMap.get("etc_id"));

			sirdDemandList.addAll(sirdDemandListN);
			Map<String, Object> sirdDetails = new HashMap<>();
			stateName.put("etc_name", sirdName);
			stateName.put("stateCode", etcMap.get("state_code"));

			String trainingCount = "SELECT count(*) FROM training_final WHERE usertype = ? AND user_id = ?";
			int traineeCount = jdbctemplate.queryForObject(trainingCount, Integer.class, "etc", etcMap.get("etc_id"));

			stateName.put("traning_center", traineeCount);

//   			if(sirdDemandList==null ||sirdDemandList.size()==0)
//   			{
//   				stateName.put("Curent_total_demand", 0);
//   				stateName.put("Current_sanction",0);
//   				stateName.put("financialyear", null);
//   			}else
//   			{

			for (Map<String, Object> demandMap : sirdDemandList) {

				if (demandMap.get("proposalid") != null) {
					String combinedProposal = "SELECT combined_proposal_id, proposaltype FROM combined_proposal " +
							"WHERE proposalid = ? AND combinedstatus IS NULL";
					String propsalId = demandMap.get("proposalid").toString();
					List<Map<String, Object>> queryForPropsal = jdbctemplate.queryForList(combinedProposal, propsalId);


					if (!queryForPropsal.isEmpty() && queryForPropsal.get(0).get("combined_proposal_id") != null) {
						if (queryForPropsal.get(0).get("proposaltype") != null) {
							stateName.put("proposalType", "Non Reccuring");
						} else {
							stateName.put("proposalType", "Reccuring");
						}
						String status = "SELECT status FROM remarks WHERE combined_proposal_id = ?";
						Object combinedProposalId = queryForPropsal.get(0).get("combined_proposal_id");

						List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(status, combinedProposalId);

						for (Map<String, Object> map : queryForCombined) {
							if (map.get(status) == "approved") {
								stateName.put("isApproved", "YES");
							} else {
								stateName.put("isApproved", "NO");
							}
						}
						
					} else {
						stateName.put("isApproved", "NO");
					}
				}

				String financialYear = (String) demandMap.get("financialYear");
				stateName.put("financialyear", financialYear);
				BigDecimal proposedTotal = new BigDecimal(String.valueOf(demandMap.get("proposedtotal")));
				stateName.put("Curent_total_demand_sird", proposedTotal);
				String sacntion = "SELECT proposedtotalsanction FROM proposal_sanction WHERE proposalid = ?";
//				System.out.println(sacntion); // This will now just print the SQL with '?'
				int propsalIdint = (int)demandMap.get("proposalid");
				List<Map<String, Object>> queryForList1 = jdbctemplate.queryForList(sacntion, propsalIdint);

				if (queryForList1 != null && queryForList1.size() != 0) {
					stateName.put("Current_sanction_sird", queryForList1.get(0).get("proposedtotalsanction"));
				}
			}
//   			}

			dataList.add(stateName);

		}

		// TODO Auto-generated method stub
		return dataList;
	}

	public List<Map<String, Object>> proVsAppCount() {List<Map<String, Object>> proposalList = new ArrayList<>();
    Set<String> addedStates = new HashSet<>();

    String comb = "select combined_proposal_id, proposaltype, userid from combined_proposal  where combinedstatus != 'discard' or combinedstatus IS NULL";
    List<Map<String, Object>> queryForCombine = jdbctemplate.queryForList(comb);
   

    for (Map<String, Object> map : queryForCombine) {
        Map<String, Object> proposalMap = new HashMap<>();
       proposalMap.put("combined_proposal_id", map.get("combined_proposal_id"));

		String sird = "SELECT state_name, state_code, sird_id FROM sird WHERE sird_id = ?";
		Map<String, Object> queryForSird = jdbctemplate.queryForMap(sird, map.get("userid"));


		String statusCheck = "SELECT status, combined_proposal_id FROM remarks " +
				"WHERE combined_proposal_id = ? AND status = 'approved' AND upperdesignation = 'MORD(Deputy Secretary)'";
		int combined = (int) map.get("combined_proposal_id");
		List<Map<String, Object>> queryForStatus = jdbctemplate.queryForList(statusCheck, combined);

		String statusCount = "select count(*) from remarks where status = 'approved' and upperdesignation = 'MORD(Deputy Secretary)'";

        Integer queryForObject = jdbctemplate.queryForObject(statusCount, Integer.class);
        proposalMap.put("approvedProposalCount", "null");

        String proposalType = (map.get("proposaltype") == null) ? "recurring" : "non recurring";
        proposalMap.put("propoaltype", proposalType);
        proposalMap.put("stateName", queryForSird.get("state_name"));
        proposalMap.put("stateCode", queryForSird.get("state_code"));
		String baseCombCount = "SELECT count(*) FROM combined_proposal WHERE userid = ? AND (combinedstatus != 'discard' OR combinedstatus IS NULL)";
		 proposalType = proposalType.toLowerCase(); // normalize if needed
		String combCount;
		Object sirdId = queryForSird.get("sird_id");

		if ("non recurring".equals(proposalType)) {
			combCount = baseCombCount + " AND proposaltype = 'nonrecurring'";
		} else {
			combCount = baseCombCount + " AND proposaltype IS NULL";
		}

		Integer queryForCount = jdbctemplate.queryForObject(combCount, Integer.class, sirdId);

		proposalMap.put("CombineProposalCount", queryForCount);
        if (!queryForStatus.isEmpty()) {
            queryForStatus.get(0).get(getDemographicCount());
			String ConinedReverse = "SELECT * FROM combined_proposal WHERE combined_proposal_id = ?";
			Object combinedProposalId = queryForStatus.get(0).get("combined_proposal_id");

			Map<String, Object> queryForCombined = jdbctemplate.queryForMap(ConinedReverse, combinedProposalId);


			proposalMap.put("isApproved", "Yes");
        } else {
            proposalMap.put("isApproved", "No");
        }
        proposalList.add(proposalMap);
    }
    return proposalList;}


	public List<Map<String, Object>> proposalApproval() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> proposalList = new ArrayList<>();

		String proposal = "select * from final_proposal";
		List<Map<String, Object>> queryForPropsal = jdbctemplate.queryForList(proposal);

		String proposalNon = "select * from nonrecurring_proposal";
		List<Map<String, Object>> queryForNonPropsal = jdbctemplate.queryForList(proposalNon);

		String etc = "select * from etc";
		List<Map<String, Object>> queryForEtcs = jdbctemplate.queryForList(etc);
		String sird = "select * from sird";
		List<Map<String, Object>> queryForSirds = jdbctemplate.queryForList(sird);

		for (Map<String, Object> mapEtc : queryForEtcs) {
			Map<String, Object> proposalMap = new HashMap<>();
			proposalMap.put("etcId", mapEtc.get("etc_id"));
			proposalMap.put("name", mapEtc.get("name_of_etcs"));
			proposalMap.put("etcName", mapEtc.get("Name"));
			proposalMap.put("stateName", mapEtc.get("state_name"));
			proposalMap.put("stateCode", mapEtc.get("state_code"));

			for (Map<String, Object> map : queryForPropsal) {
				if (map.get("user_id")== mapEtc.get("etc_id") && map.get("usertype").equals("etc")) {
					proposalMap.put("installmentNo", map.get("installmentno"));
					proposalMap.put("recurringProposalNo", map.get("proposalid"));
					if (!map.get("status").equals("Backward to etc") ||!map.get("status").equals("Generated") ) {
						proposalMap.put("forwardedRecurring", "Yes");
					}

					String etcProposal = "select count(*) from final_proposal where usertype = 'etc' and user_id = '"+map.get("user_id")+"'";
					proposalMap.put("proposalCountRecurring", jdbctemplate.queryForObject(etcProposal, Integer.class));
					Integer queryForObject = jdbctemplate.queryForObject(etcProposal, Integer.class);
					if (queryForObject!=0) {
						proposalMap.put("proposalType", "recurring");
					}

					proposalMap.put("previousSanction", "0");
					proposalMap.put("previousExpenditure", "0");
					String approve = "SELECT * FROM combined_proposal WHERE etcsproposalid LIKE '%"+map.get("proposalid")+"%' and (combinedstatus!='discard' or combinedstatus is null) order by combined_proposal_Id desc";

					List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(approve);

					if (!queryForCombined.isEmpty()) {
						String remark = "select count(*) from remarks where combined_proposal_id = '"+queryForCombined.get(0).get("combined_proposal_id")+"' and status = 'approved' and upperdesignation = 'MORD(Deputy Secretary)'";

					}else {

					}
// proposalList.add(proposalMap);
				}
			}
			if (!queryForNonPropsal.isEmpty()) {
				for (Map<String, Object> map2 : queryForNonPropsal) {
					if (map2.get("user_id")== mapEtc.get("etc_id") && map2.get("usertype").equals("etc")) {
						proposalMap.put("NonRecurringProposalNo", map2.get("proposalid") );
						if (!map2.get("status").equals("Backward to etc") ||!map2.get("status").equals("Generated") ) {
							proposalMap.put("forwardedNonRecurring", "Yes");
						}
						String etcProposal = "select count(*) from nonrecurring_proposal where usertype = 'etc' and user_id = '"+map2.get("user_id")+"'";
						proposalMap.put("proposalCountNonRecurring", jdbctemplate.queryForObject(etcProposal, Integer.class));

						Integer queryForObject = jdbctemplate.queryForObject(etcProposal, Integer.class);
						if (queryForObject!=0) {

							try {
								if (proposalMap.get("proposalType").equals("recurring")) {
									String proposalT = (String) proposalMap.get("proposalType");
									proposalT = proposalT+ " & non recurring";
									proposalMap.put("proposalType", proposalT);
								}else {
									proposalMap.put("proposalType", "non recurring");
								}
							} catch (Exception NullPointerException) {
								System.out.println(map2.get("user_id"));
								System.out.println(proposalMap.get("proposalType"));
							}

						}

						String approve = "SELECT * FROM combined_proposal WHERE etcsproposalid LIKE '%"+map2.get("proposalid")+"%' and (combinedstatus!='discard' or combinedstatus is null) order by combined_proposal_Id desc";

						List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(approve);

						if (!queryForCombined.isEmpty()) {
							String remark = "select count(*) from remarks_non_recurring where combined_proposal_id = '"+queryForCombined.get(0).get("combined_proposal_id")+"' and status = 'approved' and upperdesignation = 'MORD(Deputy Secretary)' ";

// proposalMap.put("approvedCount", jdbctemplate.queryForObject(remark, Integer.class));
						}else {
// proposalMap.put("approvedCount", "0");
						}
					}

				}}
			proposalList.add(proposalMap);
		}
		for (Map<String, Object> mapSird : queryForSirds) {
			Map<String, Object> proposalMap = new HashMap<>();
			proposalMap.put("sirdId", mapSird.get("sird_id"));
			proposalMap.put("sirdName", mapSird.get("Name"));
			proposalMap.put("stateName", mapSird.get("state_name"));
			proposalMap.put("stateCode", mapSird.get("state_code"));
			for (Map<String, Object> map : queryForPropsal) {
				if (map.get("user_id")==mapSird.get("sird_id") && map.get("usertype").equals("sird")) {
					proposalMap.put("installmentNo", map.get("installmentno"));
					proposalMap.put("recurringProposalNo", map.get("proposalid"));
					String etcProposal = "select count(*) from final_proposal where usertype = 'sird' and user_id = '"+map.get("user_id")+"'";
					proposalMap.put("proposalCountRecurring", jdbctemplate.queryForObject(etcProposal, Integer.class));
					proposalMap.put("previousSanction", "0");
					proposalMap.put("previousExpenditure", "0");

					Integer queryForObject = jdbctemplate.queryForObject(etcProposal, Integer.class);
					if (queryForObject!=0) {
						proposalMap.put("proposalType", "recurring");
					}
					String approve = "SELECT * FROM combined_proposal WHERE proposalid = '"+map.get("proposalid")+"' and (combinedstatus!='discard' or combinedstatus is null) order by combined_proposal_Id desc";
					List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(approve);
//
					if (!queryForCombined.isEmpty()) {
						proposalMap.put("combinedIdRecurring", queryForCombined.get(0).get("combined_proposal_id"));
						String remark = "select count(*) from remarks where combined_proposal_id = '"+queryForCombined.get(0).get("combined_proposal_id")+"' and status = 'approved' and upperdesignation = 'MORD(Deputy Secretary)'";
						proposalMap.put("approvedCountRecurring", jdbctemplate.queryForObject(remark, Integer.class));
						proposalMap.put("combinedRecurring", "Yes");
					}else {
						proposalMap.put("approvedCountRecurring", "0");
					}
// proposalList.add(proposalMap);
				}
			}
			if (!queryForNonPropsal.isEmpty()) {
				for (Map<String, Object> map2 : queryForNonPropsal) {
					if (map2.get("user_id")==mapSird.get("sird_id") && map2.get("usertype").equals("sird")) {
						String etcProposal = "select count(*) from nonrecurring_proposal where usertype = 'sird' and user_id = '"+map2.get("user_id")+"'";
						proposalMap.put("proposalCountNonRecurring", jdbctemplate.queryForObject(etcProposal, Integer.class));
						proposalMap.put("NonRecurringProposalNo", map2.get("proposalid"));
						Integer queryForObject = jdbctemplate.queryForObject(etcProposal, Integer.class);
						if (queryForObject!=0) {

							if (proposalMap.get("proposalType")!=null && proposalMap.get("proposalType").equals("recurring")) {
								String proposalT = (String) proposalMap.get("proposalType");
								proposalT = proposalT+ " & non recurring";
								proposalMap.put("proposalType", proposalT);
							}else {
								proposalMap.put("proposalType", "non recurring");
							}
						}
						String approve = "SELECT * FROM combined_proposal WHERE proposalid = '"+map2.get("proposalid")+"' and (combinedstatus!='discard' or combinedstatus is null) order by combined_proposal_Id desc";

						List<Map<String, Object>> queryForCombined = jdbctemplate.queryForList(approve);

						if (!queryForCombined.isEmpty()) {
							proposalMap.put("combinedIdNonRecurring", queryForCombined.get(0).get("combined_proposal_id"));
							String remark = "select count(*) from remarks_non_recurring where combined_proposal_id = '"+queryForCombined.get(0).get("combined_proposal_id")+"' and status = 'approved' and upperdesignation = 'MORD(Deputy Secretary)' ";
							proposalMap.put("approvedCountNonRecurring", jdbctemplate.queryForObject(remark, Integer.class));
							proposalMap.put("combinedNonRecurring", "Yes");
						}else {
							proposalMap.put("approvedCountNonRecurring", "0");

						}

					}

				}}
			proposalList.add(proposalMap);

		}
		return proposalList;
	}
	
	public String updateDemographicProposal(Map<String,Object>map) {
		try {
			if(map.get("proposalType").toString().equalsIgnoreCase("recurring"))
			{
			@SuppressWarnings("unchecked")
			Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");
				String sql = "UPDATE final_proposal SET totalpopulation = ?, totalruralpopulation = ?, percentruralpopulation = ? WHERE proposalid = ?";

				Object totalPopulation = demographicProfile.get("total_population_state");
				Object totalRural = demographicProfile.get("total_rural_population");
				Object percentRural = demographicProfile.get("percent_rural_population");
				int proposalIdint = Integer.parseInt(map.get("proposalId").toString());

				jdbctemplate.update(sql, totalPopulation, totalRural, percentRural, proposalIdint);

				return "Updated";
			}else
			{
				@SuppressWarnings("unchecked")
				Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");
				String sql = "UPDATE nonrecurring_proposal SET totalpopulation = ?, totalruralpopulation = ?, percentofrural = ? WHERE proposalid = ?";

				Object totalPopulation = demographicProfile.get("total_population_state");
				Object totalRuralPopulation = demographicProfile.get("total_rural_population");
				Object percentOfRural = demographicProfile.get("percent_rural_population");
				int propsalIdint = Integer.parseInt(map.get("proposalId").toString());

				jdbctemplate.update(sql, totalPopulation, totalRuralPopulation, percentOfRural, propsalIdint);

				return "Updated";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}
	}
	
	
	public List<Map<String, Object>> remarksReport(Map<String,Object>map) {
		
		List<String> remarksList = new ArrayList<>();
		try {
//			String remarkSQL = "select remarks from remarks where combined_proposal_id ='"++"'";
			}
		catch (Exception e) {
			e.printStackTrace();
			
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	

}
