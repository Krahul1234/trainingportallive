package com.nic.trainingportal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NonRecuringDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Transactional
	public String addRecurringProposal(Map<String, Object> map) {
		try {
			int user_id = this.getUserId(map);

			Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");
			Map<String, Object> EtcInformation = (Map<String, Object>) map.get("EtcInformation");
			Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
			Map<String, Object>  freshProposalCivilWork = (Map<String, Object>) map.get("freshProposalCivilWork");
			Map<String, Object>  officerEquipmentAndOthers = (Map<String, Object>) map.get("officerEquipmentAndOthers");
			Map<String, Object> uploadFile = (Map<String, Object>) map.get("uploadDocuments");

			String sql2 = "SELECT financialyear FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ?";
			List<Map<String, Object>> installment = jdbcTemplate.queryForList(sql2, map.get("key"), user_id);


			if(installment!=null && installment.size()!=0)
			{
				for(Map<String,Object>map1:installment)
				{
					if(map1.get("financialyear").equals(EtcInformation.get("financialYear").toString()))
					{
						return "Proposal For This Financial Year Already Submitted";

					}
				}
			}

			String insertQry = "INSERT INTO nonrecurring_proposal (" +
					"totalPopulation, totalRuralPopulation, percentOfRural, sirdName, financialYear, nameOfDirector, address, telephoneNo, mobileNo, email, functional, building, " +
					"numberOfFacultyPermanent, numberOfFacultyContractual, numberOfOtherStaff, hostelFacility, numberOfSeatsInHostel, trainingHallsNumber, trainingHallsCapacity, " +
					"computerLabNumber, computerLabCapacity, diningHallsNumber, diningHallsCapacity, numberOfKitchens, auditoriumOrCommitteeRoomNumber, auditoriumOrCommitteeRoomCapacity, " +
					"instituteRemarks, TypeofGrantRequested, fundsReleasedByMord, otherresources, mordAndStateShare, fundsFromVariousSources, " +
					"constructionUnit, constructionCost, constructionFunds, constructionRemark, renovationUnit, renovationCost, renovationFunds, renovationRemark, " +
					"crUnitTotal, crCostTotal, crFundsTotal, teachingUnit, teachingCost, teachingFund, teachingRemark, officeUnitProposed, officeCost, officeFunds, officeRemark, " +
					"vechileUnit, vechileCost, vechileFunds, vechileRemark, furnitureUnit, furnitureCost, furnitureFunds, furnitureRemark, " +
					"tovfUnitTotal, tovfCastTotal, tovfFundsTotal, usertype, proposal_no, user_id, departmentalInstitute) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

			Object[] params = new Object[]{
					demographicProfile.get("totalPopulation"),demographicProfile.get("totalRuralPopulation"),
					demographicProfile.get("percentOfRural"),EtcInformation.get("sirdName").toString(),
					EtcInformation.get("financialYear").toString(),EtcInformation.get("nameOfDirector").toString(),
					EtcInformation.get("address").toString(),EtcInformation.get("telephoneNo").toString(),
					EtcInformation.get("mobileNo").toString(),EtcInformation.get("email").toString(),
					instituteInfo.get("functional"),instituteInfo.get("building").toString(),
					instituteInfo.get("number_of_permanent_faculty"),instituteInfo.get("number_of_contractual_faculty"),
					instituteInfo.get("number_of_other_staff"),instituteInfo.get("hostel_facility"),
					instituteInfo.get("number_of_seat"),instituteInfo.get("hall_number"),
					instituteInfo.get("hall_capacity"),instituteInfo.get("lab_number"),
					instituteInfo.get("lab_capacity"),instituteInfo.get("dining_number"),
					instituteInfo.get("dining_capacity"),instituteInfo.get("number_of_kitchens"),instituteInfo.get("auditorium_number"),instituteInfo.get("auditorium_capacity"),instituteInfo.get("remarks").toString(),instituteInfo.get("TypeofGrantRequested").toString(),instituteInfo.get("fundsReleasedbyMord"),
					instituteInfo.get("otherResources"),instituteInfo.get("mordAndStateShare"),
					instituteInfo.get("fundsFromVariousSources"), Integer.parseInt(freshProposalCivilWork.get("constructionUnit").toString()),
					Integer.parseInt(freshProposalCivilWork.get("constructionCost").toString()),Integer.parseInt(freshProposalCivilWork.get("constructionFunds").toString()),
					freshProposalCivilWork.get("constructionRemark").toString(),Integer.parseInt(freshProposalCivilWork.get("renovationUnit").toString()),
					Integer.parseInt(freshProposalCivilWork.get("renovationCost").toString()),Integer.parseInt(freshProposalCivilWork.get("renovationFunds").toString()),
					freshProposalCivilWork.get("renovationRemark").toString(),freshProposalCivilWork.get("crUnitTotal"),
					freshProposalCivilWork.get("crCostTotal"),freshProposalCivilWork.get("crFundsTotal"),Integer.parseInt(officerEquipmentAndOthers.get("teachingUnit").toString()),
					Integer.parseInt(officerEquipmentAndOthers.get("teachingCost").toString()),Integer.parseInt(officerEquipmentAndOthers.get("teachingFund").toString()),
					officerEquipmentAndOthers.get("teachingRemark").toString(),Integer.parseInt(officerEquipmentAndOthers.get("officeUnitProposed").toString()),
					Integer.parseInt(officerEquipmentAndOthers.get("officeCost").toString()),Integer.parseInt(officerEquipmentAndOthers.get("officeFunds").toString()),officerEquipmentAndOthers.get("officeRemark").toString(),
					Integer.parseInt(officerEquipmentAndOthers.get("vechileUnit").toString()),Integer.parseInt(officerEquipmentAndOthers.get("vechileCost").toString()),
					Integer.parseInt(officerEquipmentAndOthers.get("vechileFunds").toString()),officerEquipmentAndOthers.get("vechileRemark").toString(),
					Integer.parseInt(officerEquipmentAndOthers.get("furnitureUnit").toString()),Integer.parseInt(officerEquipmentAndOthers.get("furnitureCost").toString()),
					Integer.parseInt(officerEquipmentAndOthers.get("furnitureFunds").toString()),officerEquipmentAndOthers.get("furnitureRemark").toString(),
					officerEquipmentAndOthers.get("tovfUnitTotal"),officerEquipmentAndOthers.get("tovfCastTotal"),
					officerEquipmentAndOthers.get("tovfFundsTotal"),map.get("key").toString(),
					instituteInfo.get("proposalNo"),
					user_id,
					EtcInformation.get("departmentalInstitute").toString()
			};
//			System.out.println("Insert Params: " + Arrays.toString(params));

			jdbcTemplate.update(insertQry, params);


			String sql = "SELECT proposalid from nonrecurring_proposal order by proposalid desc limit 1";

			String proposalId=jdbcTemplate.queryForList(sql).get(0).get("proposalid").toString();
			int proposalIdInt = Integer.parseInt(proposalId);

			String uploadFileProposal = "INSERT INTO upload_file_nonrecurring (" +
					"tpFileName, costestimats, itemdetails, sitemapplans, auditreports, utcertificates, " +
					"detailinterests, workagreements, latestannualreports, utcertificatecentralgrants, " +
					"proposalinformations, auditreportsigneds, detailsinterestpreviousyeargrants, " +
					"photographcompletes, progressreports, proposalid) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			Object[] params1 = new Object[]{
					uploadFile.get("tpFileName"),
					uploadFile.get("costestimats"),
					uploadFile.get("itemdetails"),
					uploadFile.get("sitemapplans"),
					uploadFile.get("auditreports"),
					uploadFile.get("utcertificates"),
					uploadFile.get("detailinterests"),
					uploadFile.get("workagreements"),
					uploadFile.get("latestannualreports"),
					uploadFile.get("utcertificatecentralgrants"),
					uploadFile.get("proposalinformations"),
					uploadFile.get("auditreportsigneds"),
					uploadFile.get("detailsinterestpreviousyeargrants"),
					uploadFile.get("photographcompletes"),
					uploadFile.get("progressreports"),
					proposalIdInt
			};

			jdbcTemplate.update(uploadFileProposal, params1);

		}catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}
	
	public int getUserId(Map<String,Object>map)
	{
		try
		{
			String tableName = "";

			// Determine the correct table name based on user type
			if (map.get("key").toString().equalsIgnoreCase("etc")) {
				tableName = "loginmaster_etc";
			} else if (map.get("key").toString().equalsIgnoreCase("sird")) {
				tableName = "loginmaster_sird";
			} else {
				tableName = "loginmaster_ministry";
			}

			String columnName = map.get("key") + "_id";
			String username = map.get("username").toString();

			// Use a parameterized query to prevent SQL injection
			String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

			return Integer.parseInt(
					jdbcTemplate.queryForList(sql, new Object[]{username})
							.get(0)
							.get(columnName)
							.toString()
			);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public List<Map<String, Object>> getNonRecurringProposal( String proposalId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>>nonRecurringProposaldata=new ArrayList<Map<String,Object>>();
		try {

			String sql="select * from nonrecurring_proposal where proposalid=?";
			int proposalIdInt = Integer.parseInt(proposalId);
			List<Map<String,Object>>proposalList = jdbcTemplate.queryForList(sql,proposalIdInt);
			
			for(Map<String,Object>nonRecurringProposal:proposalList) {
				Map<String, Object> demographicProfile1 = new HashMap<String, Object>();
				Map<String, Object> EtcInformation1 = new HashMap<String, Object>();
				Map<String, Object> instituteInfo1 = new HashMap<String, Object>();
				Map<String, Object>  freshProposalCivilWork1 = new HashMap<String, Object>();
				Map<String, Object>  officerEquipmentAndOthers1 = new HashMap<String, Object>();

				
				demographicProfile1.put("totalPopulation", nonRecurringProposal.get("totalPopulation"));
				demographicProfile1.put("totalRuralPopulation", nonRecurringProposal.get("totalRuralPopulation"));
				demographicProfile1.put("percentOfRural", nonRecurringProposal.get("percentOfRural"));
			
				EtcInformation1.put("sirdName", nonRecurringProposal.get("sirdName"));
				EtcInformation1.put("financialYear", nonRecurringProposal.get("financialYear"));
				EtcInformation1.put("nameOfDirector", nonRecurringProposal.get("nameOfDirector"));
				EtcInformation1.put("address", nonRecurringProposal.get("address"));
				EtcInformation1.put("telephoneNo", nonRecurringProposal.get("telephoneNo"));
				EtcInformation1.put("mobileNo", nonRecurringProposal.get("mobileNo"));
				EtcInformation1.put("email", nonRecurringProposal.get("email"));
				EtcInformation1.put("departmentalInstitute", nonRecurringProposal.get("departmentalInstitute"));
				
				instituteInfo1.put("functional", nonRecurringProposal.get("functional"));
				instituteInfo1.put("building", nonRecurringProposal.get("building"));
				instituteInfo1.put("number_of_permanent_faculty", nonRecurringProposal.get("numberoffacultypermanent"));
				instituteInfo1.put("number_of_contractual_faculty", nonRecurringProposal.get("numberoffacultycontractual"));
				instituteInfo1.put("number_of_other_staff", nonRecurringProposal.get("numberofotherstaff"));
				instituteInfo1.put("hostel_facility", nonRecurringProposal.get("hostelfacility"));
				instituteInfo1.put("number_of_seat", nonRecurringProposal.get("numberOfSeatsInHostel"));
				instituteInfo1.put("hall_number", nonRecurringProposal.get("trainingHallsNumber"));
				instituteInfo1.put("hall_capacity", nonRecurringProposal.get("trainingHallsCapacity"));
				instituteInfo1.put("lab_number", nonRecurringProposal.get("computerLabNumber"));
				instituteInfo1.put("lab_capacity", nonRecurringProposal.get("computerLabCapacity"));
				instituteInfo1.put("dining_number", nonRecurringProposal.get("diningHallsNumber"));
				instituteInfo1.put("dining_capacity", nonRecurringProposal.get("dininghallscapacity"));
				instituteInfo1.put("number_of_kitchens", nonRecurringProposal.get("numberofkitchens"));
				instituteInfo1.put("auditorium_number", nonRecurringProposal.get("auditoriumOrCommitteeRoomNumber"));
				instituteInfo1.put("auditorium_capacity", nonRecurringProposal.get("auditoriumOrCommitteeRoomCapacity"));
				instituteInfo1.put("remarks", nonRecurringProposal.get("instituteRemarks"));
				instituteInfo1.put("TypeofGrantRequested", nonRecurringProposal.get("TypeofGrantRequested"));
				instituteInfo1.put("fundsReleasedbyMord", nonRecurringProposal.get("fundsReleasedbyMord"));
				instituteInfo1.put("otherResources", nonRecurringProposal.get("otherResources"));
				instituteInfo1.put("mordAndStateShare", nonRecurringProposal.get("mordAndStateShare"));
				instituteInfo1.put("fundsFromVariousSources", nonRecurringProposal.get("fundsFromVariousSources"));
				instituteInfo1.put("proposalNo", nonRecurringProposal.get("proposal_no"));

				
				
				
				freshProposalCivilWork1.put("constructionUnit", nonRecurringProposal.get("constructionUnit"));
				freshProposalCivilWork1.put("constructionCost", nonRecurringProposal.get("constructionCost"));
				freshProposalCivilWork1.put("constructionFunds", nonRecurringProposal.get("constructionFunds"));
				freshProposalCivilWork1.put("constructionRemark", nonRecurringProposal.get("constructionRemark"));
				freshProposalCivilWork1.put("renovationUnit", nonRecurringProposal.get("renovationUnit"));
				freshProposalCivilWork1.put("renovationCost", nonRecurringProposal.get("renovationCost"));
				freshProposalCivilWork1.put("renovationFunds", nonRecurringProposal.get("renovationFunds"));
				freshProposalCivilWork1.put("renovationRemark", nonRecurringProposal.get("renovationRemark"));
				freshProposalCivilWork1.put("crUnitTotal", nonRecurringProposal.get("crUnitTotal"));
				freshProposalCivilWork1.put("crCostTotal", nonRecurringProposal.get("crCostTotal"));
				freshProposalCivilWork1.put("crFundsTotal", nonRecurringProposal.get("crFundsTotal"));

				
				
				officerEquipmentAndOthers1.put("teachingUnit", nonRecurringProposal.get("teachingUnit"));
				officerEquipmentAndOthers1.put("teachingCost", nonRecurringProposal.get("teachingCost"));
				officerEquipmentAndOthers1.put("teachingFund", nonRecurringProposal.get("teachingFund"));
				officerEquipmentAndOthers1.put("teachingRemark", nonRecurringProposal.get("teachingRemark"));
				officerEquipmentAndOthers1.put("officeUnitProposed", nonRecurringProposal.get("officeUnitProposed"));
				officerEquipmentAndOthers1.put("officeCost", nonRecurringProposal.get("officeCost"));
				officerEquipmentAndOthers1.put("officeFunds", nonRecurringProposal.get("officeFunds"));
				officerEquipmentAndOthers1.put("officeRemark", nonRecurringProposal.get("officeRemark"));
				officerEquipmentAndOthers1.put("vechileUnit", nonRecurringProposal.get("vechileUnit"));
				officerEquipmentAndOthers1.put("vechileCost", nonRecurringProposal.get("vechileCost"));
				officerEquipmentAndOthers1.put("vechileFunds", nonRecurringProposal.get("vechileFunds"));
				officerEquipmentAndOthers1.put("vechileRemark", nonRecurringProposal.get("vechileRemark"));
				officerEquipmentAndOthers1.put("furnitureUnit", nonRecurringProposal.get("furnitureUnit"));
				officerEquipmentAndOthers1.put("furnitureCost", nonRecurringProposal.get("furnitureCost"));
				officerEquipmentAndOthers1.put("furnitureFunds", nonRecurringProposal.get("furnitureFunds"));
				officerEquipmentAndOthers1.put("furnitureRemark", nonRecurringProposal.get("furnitureRemark"));
				officerEquipmentAndOthers1.put("tovfUnitTotal", nonRecurringProposal.get("tovfUnitTotal"));
				officerEquipmentAndOthers1.put("tovfCastTotal", nonRecurringProposal.get("tovfCastTotal"));
				officerEquipmentAndOthers1.put("tovfFundsTotal", nonRecurringProposal.get("tovfFundsTotal"));



				Map<String, Object> demographicProfile = new HashMap<String, Object>();
				Map<String, Object> EtcInformation = new HashMap<String, Object>();
				Map<String, Object> instituteInfo = new HashMap<String, Object>();
				Map<String, Object>  freshProposalCivilWork = new HashMap<String, Object>();
				Map<String, Object>  officerEquipmentAndOthers = new HashMap<String, Object>();

				
				demographicProfile.put("demographicProfile", demographicProfile1);
				EtcInformation.put("EtcInformation", EtcInformation1);
				instituteInfo.put("instituteInfo", instituteInfo1);
				freshProposalCivilWork.put("freshProposalCivilWork", freshProposalCivilWork1);
				officerEquipmentAndOthers.put("officerEquipmentAndOthers", officerEquipmentAndOthers1);

				Map<String, Object> uploadFile = new HashMap<String, Object>();
				String uploadSql = "SELECT * FROM upload_file_nonrecurring WHERE proposalid = ?";
				Map<String, Object> uploadList = jdbcTemplate.queryForMap(uploadSql, proposalIdInt);

				uploadList.remove("id");
				uploadList.remove("proposalid");
				uploadFile.put("uploadFile",uploadList);

				nonRecurringProposaldata.add(demographicProfile);
				nonRecurringProposaldata.add(EtcInformation);
				nonRecurringProposaldata.add(instituteInfo);
				nonRecurringProposaldata.add(freshProposalCivilWork);
				nonRecurringProposaldata.add(officerEquipmentAndOthers);
				nonRecurringProposaldata.add(uploadFile);

				return nonRecurringProposaldata;
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	@Transactional
	public String updateNonRecurringProposal(Map<String, Object> map) {
		try {
			Map<String, Object>  freshProposalCivilWork = (Map<String, Object>) map.get("freshProposalCivilWork");
			Map<String, Object>  officerEquipmentAndOthers = (Map<String, Object>) map.get("officerEquipmentAndOthers");
			Map<String, Object> uploadFile = (Map<String, Object>) map.get("uploadDocuments");
			  int proposalIdint = Integer.parseInt(map.get("proposalId").toString());

			String updateQry = "UPDATE nonrecurring_proposal SET " +
					"constructionUnit = ?, constructionCost = ?, constructionFunds = ?, constructionRemark = ?, " +
					"renovationUnit = ?, renovationCost = ?, renovationFunds = ?, renovationRemark = ?, " +
					"crUnitTotal = ?, crCostTotal = ?, crFundsTotal = ?, " +
					"teachingUnit = ?, teachingCost = ?, teachingFund = ?, teachingRemark = ?, " +
					"officeUnitProposed = ?, officeCost = ?, officeFunds = ?, officeRemark = ?, " +
					"vechileUnit = ?, vechileCost = ?, vechileFunds = ?, vechileRemark = ?, " +
					"furnitureUnit = ?, furnitureCost = ?, furnitureFunds = ?, furnitureRemark = ?, " +
					"tovfUnitTotal = ?, tovfCastTotal = ?, tovfFundsTotal = ? " +
					"WHERE proposalid = ?";

			Object[] params = {
					freshProposalCivilWork.get("constructionUnit"),
					freshProposalCivilWork.get("constructionCost"),
					freshProposalCivilWork.get("constructionFunds"),
					freshProposalCivilWork.get("constructionRemark"),

					freshProposalCivilWork.get("renovationUnit"),
					freshProposalCivilWork.get("renovationCost"),
					freshProposalCivilWork.get("renovationFunds"),
					freshProposalCivilWork.get("renovationRemark"),

					freshProposalCivilWork.get("crUnitTotal"),
					freshProposalCivilWork.get("crCostTotal"),
					freshProposalCivilWork.get("crFundsTotal"),

					officerEquipmentAndOthers.get("teachingUnit"),
					officerEquipmentAndOthers.get("teachingCost"),
					officerEquipmentAndOthers.get("teachingFund"),
					officerEquipmentAndOthers.get("teachingRemark"),

					officerEquipmentAndOthers.get("officeUnitProposed"),
					officerEquipmentAndOthers.get("officeCost"),
					officerEquipmentAndOthers.get("officeFunds"),
					officerEquipmentAndOthers.get("officeRemark"),

					officerEquipmentAndOthers.get("vechileUnit"),
					officerEquipmentAndOthers.get("vechileCost"),
					officerEquipmentAndOthers.get("vechileFunds"),
					officerEquipmentAndOthers.get("vechileRemark"),

					officerEquipmentAndOthers.get("furnitureUnit"),
					officerEquipmentAndOthers.get("furnitureCost"),
					officerEquipmentAndOthers.get("furnitureFunds"),
					officerEquipmentAndOthers.get("furnitureRemark"),

					officerEquipmentAndOthers.get("tovfUnitTotal"),
					officerEquipmentAndOthers.get("tovfCastTotal"),
					officerEquipmentAndOthers.get("tovfFundsTotal"),

					proposalIdint
			};

			jdbcTemplate.update(updateQry, params);


			String updateFileProposal = "UPDATE upload_file_nonrecurring SET " +
					"tpFileName = ?, " +
					"costestimats = ?, " +
					"itemdetails = ?, " +
					"sitemapplans = ?, " +
					"auditreports = ?, " +
					"utcertificates = ?, " +
					"detailinterests = ?, " +
					"workagreements = ?, " +
					"latestannualreports = ?, " +
					"utcertificatecentralgrants = ?, " +
					"proposalinformations = ?, " +
					"auditreportsigneds = ?, " +
					"detailsinterestpreviousyeargrants = ?, " +
					"photographcompletes = ?, " +
					"progressreports = ? " +
					"WHERE proposalid = ?";

			Object[] params1 = new Object[] {
					uploadFile.get("tpFileName"),
					uploadFile.get("costestimats"),
					uploadFile.get("itemdetails"),
					uploadFile.get("sitemapplans"),
					uploadFile.get("auditreports"),
					uploadFile.get("utcertificates"),
					uploadFile.get("detailinterests"),
					uploadFile.get("workagreements"),
					uploadFile.get("latestannualreports"),
					uploadFile.get("utcertificatecentralgrants"),
					uploadFile.get("proposalinformations"),
					uploadFile.get("auditreportsigneds"),
					uploadFile.get("detailsinterestpreviousyeargrants"),
					uploadFile.get("photographcompletes"),
					uploadFile.get("progressreports"),
					proposalIdint
			};

			jdbcTemplate.update(updateFileProposal, params1);

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "1";
	}

}
