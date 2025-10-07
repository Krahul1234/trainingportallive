package com.nic.trainingportal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SIRDDao {

	@Autowired
	private JdbcTemplate jdbctemplate;

	/** Add SIRD Details
     * @param map
     * @return
     */
	public List<Map<String,Object>>getSirdDetails(Map<String,Object>map)
    {
        try
        {
        	String tableName="";
			/**
			 *  check login type
			 */
			if(map.get("key").toString().equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}
			String sql = "SELECT id FROM " + tableName + " WHERE username = ?";
			int id = Integer.parseInt(jdbctemplate.queryForList(sql, map.get("username")).get(0).get("sird_id").toString());
			List<Map<String, Object>> list = jdbctemplate.queryForList(
					"SELECT \"Name\", contact, email FROM " + tableName + " WHERE id = ?", id
			);
			Map<String, Object> map1 = jdbctemplate.queryForList(
					"SELECT total_population_state, total_rural_population, percent_rural_population FROM demographic WHERE sird_id = ?", id).get(0);
			list.add(map1);
            return list;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }


	public List<Map<String,Object>>sirdDetails(Map<String,Object>map)
	{
		try
		{
			int userId=this.getUserId(map);

			String tableName="";
			/**
			 *  check login type
			 */
			if(map.get("key").toString().equalsIgnoreCase("etc"))
			{
				tableName="etc";
			}else if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				tableName="sird";
			}else
			{
				tableName="loginmaster_ministry";
			}
			String sql="select"+" "+map.get("key")+"_id as id "+" ,state_name AS \"stateName\",\"Name\"  AS \"name\" ,contact_number AS \"mobileNo\",email As \"emailId\" , address,nameofdirector AS \"nameOfDirector\" ,alternatemobileno As \"alternateMobileNo\",alternateemailid as \"alternateEmailId\"  from"+" "+tableName+" "+ "where"+" " +map.get("key")+"_"+"id ='"+userId+"'";

			return jdbctemplate.queryForList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}

	/** Add SIRD Details
     * @param map
     * @return
     */
	public int updateSirdDetails(Map<String,Object>map)
    {
        try
        {String key = map.get("key").toString().toLowerCase();

// Whitelist allowed table names to prevent injection via `key`
			if (!key.equals("etc") && !key.equals("sird")) {
				throw new IllegalArgumentException("Invalid key");
			}

			String sql = "UPDATE " + key + " SET " +
					"\"Name\" = ?, " +
					"contact_number = ?, " +
					"email = ?, " +
					"address = ?, " +
					"nameofdirector = ?, " +
					"alternatemobileno = ?, " +
					"alternateemailid = ? " +
					"WHERE " + key + "_id = ?";

			return jdbctemplate.update(sql,
					map.get("name"),
					map.get("mobileNo"),
					map.get("emailId"),
					map.get("address"),
					map.get("nameOfDirector"),
					map.get("alternateMobileNo"),
					map.get("alternateEmailId"),
					map.get("id")
			);

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
	public List<Map<String, Object>> getSirdById(String id) {
		try {
			String sql = "SELECT * FROM sird WHERE sird_id = ?";
			return jdbctemplate.queryForList(sql, id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

	/**
	 * Get All Faculty Details
	 *
	 * @return
	 */
	public int deleteSirdById(String id) {
		try {
			String sql = "delete from sird where sird_id=?";
			return jdbctemplate.update(sql,id);
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
	public List<Map<String, Object>> getSirdDetails(String userName,String view,String financialYear,String installmentType) {
		try {
			if (view != null && view.equals("Combined") && !financialYear.equals("ALL") && !installmentType.equals("ALL")) {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT status, financialyear AS \"financialYear\", installmentno AS \"installmentType\", " +
						"proposalid, proposaldate, proposal_no, proposedtotal, proposaldate, user_id, usertype, backwarded " +
						"FROM final_proposal WHERE usertype = 'sird' AND user_id = ? AND status = ? AND financialyear = ? " +
						"AND installmentno = ? AND combined = 'false'";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(
						proposalSql, sirdId, "Forward to combined", financialYear, installmentType);

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}
				return proposallist;

			} else if (view != null && view.equals("Combined") && financialYear.equals("ALL") && installmentType.equals("ALL")) {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId =  jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT status, financialyear AS \"financialYear\", installmentno AS \"installmentType\", " +
						"proposalid, proposaldate, proposal_no, proposedtotal, proposaldate, user_id, usertype, backwarded " +
						"FROM final_proposal WHERE usertype = 'sird' AND user_id = ? AND status = ? AND combined = 'false'";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(
						proposalSql, sirdId, "Forward to combined");

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}
				return proposallist;

			} else {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT forwarded, status, financialyear AS \"financialYear\", installmentno AS \"installmentType\", " +
						"proposalid, proposaldate, proposal_no, proposedtotal, proposaldate, user_id, usertype, backwarded " +
						"FROM final_proposal WHERE usertype = 'sird' AND user_id = ?";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(proposalSql, sirdId);

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}
				return proposallist;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);


	}

	public int getUserId(Map<String,Object>map) {
		try {
			String tableName = "";
			/**
			 *  check login type
			 */
			if (map.get("key").toString().equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (map.get("key").toString().equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			String key = map.get("key").toString().toLowerCase(); // assuming column like etc_id or sird_id

			// Whitelist check to prevent misuse
			if (!key.equals("etc") && !key.equals("sird") && !key.equals("ministry")) {
				throw new IllegalArgumentException("Invalid key");
			}

			String sql = "SELECT " + key + "_id FROM " + tableName + " WHERE username = ?";
			return Integer.parseInt(jdbctemplate.queryForList(sql, map.get("userName"))
					.get(0)
					.get(key + "_id")
					.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public List<Map<String, Object>> getSirdDetailsNonRecuring(String userName, String view, String financialYear) {
		try {
			if (view != null && view.equals("Combined") && !financialYear.equals("ALL")) {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT status, financialyear AS \"financialYear\", proposalid, proposaldate, proposal_no, " +
						"proposaldate, user_id, usertype, backwarded " +
						"FROM nonrecurring_proposal WHERE usertype = 'sird' AND user_id = ? AND status = ? AND financialyear = ? AND combined = ?";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(
						proposalSql, sirdId, "Forward to combined", financialYear, false
				);

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}

				return proposallist;

			} else if (view != null && view.equals("Combined") && financialYear.equals("ALL")) {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT status, financialyear AS \"financialYear\", proposalid, proposaldate, proposal_no, " +
						"proposaldate, user_id, usertype, backwarded " +
						"FROM nonrecurring_proposal WHERE usertype = 'sird' AND user_id = ? AND status = ? AND combined = ?";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(
						proposalSql, sirdId, "Forward to combined", false
				);

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}

				return proposallist;

			} else {
				String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
				Object sirdId = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

				String sql2 = "SELECT \"Name\" AS name FROM sird WHERE sird_id = ?";
				String name = jdbctemplate.queryForList(sql2, sirdId).get(0).get("name").toString();

				String proposalSql = "SELECT forwarded, status, financialyear AS \"financialYear\", proposalid, proposaldate, " +
						"proposal_no, proposaldate, user_id, usertype, backwarded " +
						"FROM nonrecurring_proposal WHERE usertype = 'sird' AND user_id = ?";
				List<Map<String, Object>> proposallist = jdbctemplate.queryForList(
						proposalSql, sirdId
				);

				for (Map<String, Object> map : proposallist) {
					map.put("name", name);
					map.put("serialNo", 1);
				}

				return proposallist;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);

	}
	public String updateInformationSirdEtc(Map<String,Object>map) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> sirdInformation = (Map<String, Object>) map.get("sirdInformation");

			 int proposalIdString= Integer.parseInt(map.get("proposalId").toString());
			String updateQuery;
			Object[] params = new Object[] {
					sirdInformation.get("name"),
					sirdInformation.get("nameOfDirector"),
					sirdInformation.get("address"),
					sirdInformation.get("alternateMobileNo"),
					sirdInformation.get("mobileNo"),
					sirdInformation.get("emailId"),
					proposalIdString
			};

			if (map.get("proposalType").toString().equalsIgnoreCase("recurring")) {

				updateQuery = "UPDATE final_proposal SET " +
						"sirdname = ?, nameofdirector = ?, address = ?, telephoneno = ?, mobileno = ?, email = ? " +
						"WHERE proposalId = ?";
			} else {
				updateQuery = "UPDATE nonrecurring_proposal SET " +
						"sirdname = ?, nameofdirector = ?, address = ?, telephoneno = ?, mobileno = ?, email = ? " +
						"WHERE proposalId = ?";
			}

			jdbctemplate.update(updateQuery, params);
			return "Updated";

		} catch (Exception e) {
			e.printStackTrace();
			return "Not Updated";
		}

	}

}
