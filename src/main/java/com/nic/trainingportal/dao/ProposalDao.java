package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProposalDao {
	
	@Autowired
	
	private JdbcTemplate jdbcTemplate;
	
	public List<Map<String,Object>> getDemographicDetails(Map<String,Object>map)
	{
		try
		{
			String sql="SELECT total_population_state AS \"totalPopulation\", " +
		               "total_rural_population AS \"totalRuralPopulation\", " +
		               "percent_rural_population AS \"percentOfRural\", " +
		               "state_code AS \"stateCode\" " +
		               "FROM demographic WHERE user_id='" + map.get("userId") + "'";
			
			return jdbcTemplate.queryForList(sql);
		}catch(Exception e)
		{ e.printStackTrace();
			
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>>getTrainingInfo(Map<String,Object>map)
	{
		try
		{
			String sql="select * from training_info where user_id='"+map.get("userId")+"'";
			return jdbcTemplate.queryForList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>>getFacultyDetails(Map<String,Object>map)
	{
		try
		{
			Map<String,Object>countMap=new HashMap<String,Object>();
			List<Map<String,Object>>facultyList=new ArrayList<Map<String,Object>>();
			String sql="SELECT name, " +
		             "post AS \"postHeld\", " +
		             "pay_scale AS \"scalePay\", " +
		             "type_of_faculty AS \"typeOfFaculty\", " +
		             "remarks " +
		             "FROM faculty " +
		             "WHERE user_id='" + map.get("userId") + "'";
			
			facultyList= jdbcTemplate.queryForList(sql);
			
			countMap.put("facultyCount",getFacultyCount());
			 facultyList.add(countMap);
			 return facultyList;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public List<Map<String,Object>>getTrainingCalendarDetails(Map<String,Object>map)
	{
		try
		{
			Map<String,Object>countMap=new HashMap<String,Object>();
			List<Map<String,Object>>trainingCalendarList=new ArrayList<Map<String,Object>>();
			String sql="SELECT entry_date AS \"proposedDate\", " +
		             "name AS \"facultyName\", " +
		             "training_venue AS \"venue\", " +
		             "training_subject AS \"trainingSubject\", " +
		             "number_of_trainees AS \"trainessNumber\", " +
		             "target_group AS \"targetGroup\" " +
		             "FROM training_final " +
		             "WHERE user_id = '" + map.get("userId") + "'";
			
			trainingCalendarList= jdbcTemplate.queryForList(sql);
			countMap.put("trainingCalendarCount",getTrainingCalendarCount());
			trainingCalendarList.add(countMap);
			return trainingCalendarList;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int getFacultyCount()
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
	
	public int getTrainingCalendarCount()
	{
		try
		{
			String sql="select count(*) as count from training_final";
			
			return jdbcTemplate.queryForObject(sql,Integer.class);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
//	public int addProposalNew(Map<String,Object>map)
//	{
//	 try
//	 {
//		 int userId=this.getUserId(map);
//		  
//	 }catch(Exception e)
//	 {
//		 
//	 }
//	}
	
	public int  addProposal(String lowerDesignation,String upperDesignation,Map<String,Object>map)
	{
		try
		{
			String sql="insert into forward_proposal(user_type,remarks,upper_designation,lower_designation) values('"+lowerDesignation+"','"+map.get("remarks")+"','"+upperDesignation+"','"+lowerDesignation+"')";
			return jdbcTemplate.update(sql);
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
			}else
			{
				tableName="loginmaster_ministry";
			}
			
			String sql="select"+" "+map.get("key")+"_"+ "id from"+" "+tableName+" "+"where username='"+map.get("username")+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key")+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getForwradProposal(String userType,String userName)
	{
		try
		{
			List<Map<String,Object>>datalist=new ArrayList<Map<String,Object>>();
			Map<String,Object>idmap=new HashMap<String,Object>();
			String lowerDesignation="";
			if(userType.equals("sird"))
			{
				lowerDesignation="sird";
			}else if(userType.equals("so"))
			{
				lowerDesignation="so";
			}else if(userType.equals("us"))
			{
				lowerDesignation="us";
			}else if(userType.equals("ds"))
			{
				lowerDesignation="ds";
			}else
			{
				lowerDesignation="etc";
			}
			String sql="select remarks,combined_proposal_no from forward_proposal where lower_designation='"+lowerDesignation+"'";
			String combinedNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_no").toString();
			String sqlNew="select userid,etcids from combined_proposal where combined_proposal_id='"+combinedNo+"'";
			List<Map<String,Object>>list=jdbcTemplate.queryForList(sqlNew);
			int i=1;
			for(Map<String,Object>datamap:list)
			{
				
				idmap.put("sirdId",datamap.get("userid"));
				String arr[]=datamap.get("etcids").toString().split(",");
				
				for(String str:arr)
				{
					idmap.put("etcId"+i++, str);
				}
				
				
			}
			
			datalist= jdbcTemplate.queryForList(sql);
			datalist.add(idmap);
			return datalist;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public int updateForwradProposal(Map<String,Object>map)
	{
		try
		{
			String lowerDesignation="";
			String upperDesignation="";
			if(map.get("key").toString().equals("sird"))
			{
				lowerDesignation="etc";
			}else if(map.get("key").toString().equals("so"))
			{
				lowerDesignation="sird";
			}else if(map.get("key").toString().equals("us"))
			{
				lowerDesignation="so";
			}else if(map.get("key").toString().equals("ds"))
			{
				lowerDesignation="us";
			}else
			{
				lowerDesignation="ds";
			}
			String sql="update forward_proposal set remarks='"+map.get("remarks")+"' where lower_designation='"+lowerDesignation+"' and remarks='"+"pending"+"'";
			return jdbcTemplate.update(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int addProposal(Map<String, Object> map) {
		try {
			int user_id = this.getUserId2(map);

			Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");
			Map<String, Object> sirdInformation = (Map<String, Object>) map.get("sirdInformation");
			Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
		    List<Map<String,Object>> facultyForm = (List<Map<String, Object>>) map.get("facultyForm");
		    List<Map<String,Object>>  trainingCalender = (List<Map<String, Object>>) map.get("trainingCalender");
			Map<String, Object> actualExpenditure = (Map<String, Object>) ((Map<String, Object>) map
					.get("expenditureDetail")).get("actualExpenditureIncurredInLastFy");
			Map<String, Object> proposedExpenditure = (Map<String, Object>) ((Map<String, Object>) map
					.get("expenditureDetail")).get("proposedExpenditureToBeIncurredForTheFY");

			String insertQry = "INSERT INTO final_proposal (totalPopulation, totalRuralPopulation, percentRuralPopulation, sirdName, financialYear, nameOfDirector, address, telephoneNo, mobileNo, email, functional, building, numberOfFacultyPermanent, numberOfFacultyContractual, numberOfOtherStaff, hostelFacility, numberOfSeatsInHostel, trainingHallsNumber, trainingHallsCapacity, computerLabNumber, computerLabCapacity, diningHallsNumber, diningHallsCapacity, numberOfKitchens, auditoriumOrCommitteeRoomNumber, auditoriumOrCommitteeRoomCapacity, instituteRemarks, TypeofGrantRequested, fundsReleasedByMord, otherresources, mordAndStateShare, fundsFromVariousSources, actualTraining, actualMeal, actualTravel, actualPurchase, actualSupply, actualOffice, actualNonTeaching, actualSalaryOfCoreFaculty, actualTotal, proposedTraining, proposedMeal, proposedTravel, proposedPurchase, proposedSupply, proposedOffice, proposedNonTeaching, proposedSalaryOfCoreFaculty, proposedTotal, usertype, proposal_no, user_id, totaldemand) " +
				    "VALUES (" + demographicProfile.get("totalPopulation") + ", " +
				    demographicProfile.get("totalRuralPopulation") + ", " +
				    demographicProfile.get("percentOfRural") + ", '" + sirdInformation.get("sirdName") + "', '" +
				    sirdInformation.get("financialYear") + "', '" + sirdInformation.get("nameOfDirector") + "', '" +
				    sirdInformation.get("address") + "', '" + sirdInformation.get("telephoneNo") + "', '" +
				    sirdInformation.get("mobileNo") + "', '" + sirdInformation.get("email") + "', '" +
				    instituteInfo.get("functional") + "', '" + instituteInfo.get("building") + "', " +
				    instituteInfo.get("number_of_permanent_faculty") + ", " +
				    instituteInfo.get("number_of_contractual_faculty") + ", " +
				    instituteInfo.get("number_of_other_staff") + ", " +
				    instituteInfo.get("hostel_facility") + ", " +
				    instituteInfo.get("number_of_seat") + ", " +
				    instituteInfo.get("hall_number") + ", " +
				    instituteInfo.get("hall_capacity") + ", " +
				    instituteInfo.get("lab_number") + ", " +
				    instituteInfo.get("lab_capacity") + ", " +
				    instituteInfo.get("dining_number") + ", " +
				    instituteInfo.get("dining_capacity") + ", " +
				    instituteInfo.get("number_of_kitchens") + ", " +
				    instituteInfo.get("auditorium_number") + ", " +
				    instituteInfo.get("auditorium_capacity") + ", '" +
				    instituteInfo.get("remarks") + "', '" +
				    instituteInfo.get("TypeofGrantRequested") + "', '" +
				    instituteInfo.get("fundsReleasedbyMord") + "', " +
				    instituteInfo.get("otherResources") + ", " +
				    instituteInfo.get("mordAndStateShare") + ", " +
				    instituteInfo.get("fundsFromVariousSources") + ", " +
				    actualExpenditure.get("actualTraining") + ", " +
				    actualExpenditure.get("actualMeal") + ", " +
				    actualExpenditure.get("actualTravel") + ", " +
				    actualExpenditure.get("actualPurchase") + ", " +
				    actualExpenditure.get("actualSupply") + ", " +
				    actualExpenditure.get("actualOffice") + ", " +
				    actualExpenditure.get("actualNonTeaching") + ", " +
				    actualExpenditure.get("actualSalaryOfCoreFaculty") + ", " +
				    actualExpenditure.get("actualTotal") + ", " +
				    proposedExpenditure.get("proposedTraining") + ", " +
				    proposedExpenditure.get("proposedMeal") + ", " +
				    proposedExpenditure.get("proposedTravel") + ", " +
				    proposedExpenditure.get("proposedPurchase") + ", " +
				    proposedExpenditure.get("proposedSupply") + ", " +
				    proposedExpenditure.get("proposedOffice") + ", " +
				    proposedExpenditure.get("proposedNonTeaching") + ", " +
				    proposedExpenditure.get("proposedSalaryOfCoreFaculty") + ", " +
				    proposedExpenditure.get("proposedTotal") + ", '" +
				    map.get("key") + "', " +
				    proposedExpenditure.get("proposalNo") + ", '" +
				    user_id + "', " +
				    (Integer.parseInt(actualExpenditure.get("actualTotal").toString()) +
				    Integer.parseInt(proposedExpenditure.get("proposedTotal").toString())) + ")";

				jdbcTemplate.update(insertQry);
			
			String sql = "SELECT proposalid from final_proposal order by proposalid desc limit 1";
			
			String proposalId=jdbcTemplate.queryForList(sql).get(0).get("proposalId").toString();

			for(Map<String,Object>facultyMap : facultyForm)
			{
			String insertFacultyQry = "INSERT INTO faculty_proposal_final (pay, name, post, permanent, remarks, proposalno,userId) "
					+ "VALUES (" + facultyMap.get("scalePay") + ", '" + facultyMap.get("name") + "', '"
					+ facultyMap.get("postHeld") + "', '" + facultyMap.get("typeOfFaculty") + "', '"
					+ facultyMap.get("remarks") + "', '" + proposalId + "','"+user_id + "')";
			jdbcTemplate.update(insertFacultyQry);
			}
			for(Map<String,Object>trainingCalenderMap : trainingCalender) {
			String insertCalenderQry = "INSERT INTO training_calender_final (traningStartDate,nameOfFaculty, placeOfTraining, subjectOfTraining, noOfTrainees, targetGroup, proposalno, userId) "
					+ "VALUES ('" + trainingCalenderMap.get("proposedDate") + "', '" + trainingCalenderMap.get("facultyName") + "', '"
					+ trainingCalenderMap.get("venue") + "', '" + trainingCalenderMap.get("trainingSubject")
					+ "', " + trainingCalenderMap.get("trainessNumber") + ", '" + trainingCalenderMap.get("targetGroup") + "', "
					+ proposalId + ",'"+user_id + "')";

			jdbcTemplate.update(insertCalenderQry);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	private int getUserId2(Map<String, Object> map) {
		try

		{
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

			String sql = "select" + " " + map.get("key") + "_" + "id  from" + " " + tableName + " " + "where username='"
					+ map.get("username") + "'";

			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key") + "_" + "id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Map<String,Object>> getProposal(String userId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>>proposaldata=new ArrayList<Map<String,Object>>();
		 Map<String, Object> facultyForm = new HashMap<String, Object>();
		 Map<String, Object> trainingCalendar = new HashMap<String, Object>();
		
				String proposalsql = "select * from final_proposal where  user_id='"+userId+"'";
				List<Map<String,Object>>proposalList = jdbcTemplate.queryForList(proposalsql);
				
				for(Map<String,Object>etcProposal:proposalList)
				{
					// Demographic Profile Map
				    Map<String, Object> demographic1 = new HashMap<String, Object>();
				    Map<String, Object> demographic = new HashMap<String, Object>();
				    demographic1.put("totalPopulation", etcProposal.get("totalPopulation"));
				    demographic1.put("totalRuralPopulation", etcProposal.get("totalRuralPopulation"));
				    demographic1.put("percentRuralPopulation", etcProposal.get("percentRuralPopulation"));
				    
				    demographic.put("demographicProfile", demographic1);

				    // SIRD Information Map
				    Map<String, Object> sirdInformation1 = new HashMap<String, Object>();
				    Map<String, Object> sirdInformation = new HashMap<String, Object>();
				    sirdInformation1.put("sirdName", etcProposal.get("sirdName"));
				    sirdInformation1.put("financialYear", etcProposal.get("financialYear"));
				    sirdInformation1.put("nameOfDirector", etcProposal.get("nameOfDirector"));
				    sirdInformation1.put("address", etcProposal.get("address"));
				    sirdInformation1.put("telephoneNo", etcProposal.get("telephoneNo"));
				    sirdInformation1.put("mobileNo", etcProposal.get("mobileNo"));
				    sirdInformation1.put("email", etcProposal.get("email"));
				    
				    sirdInformation.put("sirdInformation", sirdInformation1);

				    // Institute Info Map
				    Map<String, Object> instituteInfo = new HashMap<String, Object>();
				    Map<String, Object> instituteInfo1 = new HashMap<String, Object>();
				    instituteInfo.put("functional", etcProposal.get("functional"));
				    instituteInfo.put("building", etcProposal.get("building"));
				    instituteInfo.put("numberOfFacultyPermanent", etcProposal.get("numberOfFacultyPermanent"));
				    instituteInfo.put("numberOfFacultyContractual", etcProposal.get("numberOfFacultyContractual"));
				    instituteInfo.put("numberOfOtherStaff", etcProposal.get("numberOfOtherStaff"));
				    instituteInfo.put("hostelFacility", etcProposal.get("hostelFacility"));
				    instituteInfo.put("numberOfSeatsInHostel", etcProposal.get("numberOfSeatsInHostel"));
				    instituteInfo.put("trainingHallsNumber", etcProposal.get("trainingHallsNumber"));
				    instituteInfo.put("trainingHallsCapacity", etcProposal.get("trainingHallsCapacity"));
				    instituteInfo.put("computerLabNumber", etcProposal.get("computerLabNumber"));
				    instituteInfo.put("computerLabCapacity", etcProposal.get("computerLabCapacity"));
				    instituteInfo.put("diningHallsNumber", etcProposal.get("diningHallsNumber"));
				    instituteInfo.put("diningHallsCapacity", etcProposal.get("diningHallsCapacity"));
				    instituteInfo.put("numberOfKitchens", etcProposal.get("numberOfKitchens"));
				    instituteInfo.put("auditoriumOrCommitteeRoomNumber", etcProposal.get("auditoriumOrCommitteeRoomNumber"));
				    instituteInfo.put("auditoriumOrCommitteeRoomCapacity", etcProposal.get("auditoriumOrCommitteeRoomCapacity"));
				    instituteInfo.put("remarks", etcProposal.get("remarks"));
				    instituteInfo.put("TypeofGrantRequested", etcProposal.get("TypeofGrantRequested"));
				    instituteInfo.put("fundsReleasedByMord", etcProposal.get("fundsReleasedByMord"));
				    instituteInfo.put("otherResources", etcProposal.get("otherResources"));
				    instituteInfo.put("mordAndStateShare", etcProposal.get("mordAndStateShare"));
				    instituteInfo.put("fundsFromVariousSources", etcProposal.get("fundsFromVariousSources"));

				    instituteInfo1.put("instituteInfo", instituteInfo);
				    // Expenditure Detail Map
				    Map<String, Object> expenditureDetail = new HashMap<String, Object>();
				    // Actual Expenditure Incurred in Last FY
				    Map<String, Object> actualExpenditureIncurredInLastFy = new HashMap<String, Object>();
				    Map<String, Object> actualExpenditureIncurredInLastFy1 = new HashMap<String, Object>();
				    actualExpenditureIncurredInLastFy.put("actualTraining", etcProposal.get("actualTraining"));
				    actualExpenditureIncurredInLastFy.put("actualMeal", etcProposal.get("actualMeal"));
				    actualExpenditureIncurredInLastFy.put("actualTravel", etcProposal.get("actualTravel"));
				    actualExpenditureIncurredInLastFy.put("actualPurchase", etcProposal.get("actualPurchase"));
				    actualExpenditureIncurredInLastFy.put("actualSupply", etcProposal.get("actualSupply"));
				    actualExpenditureIncurredInLastFy.put("actualOffice", etcProposal.get("actualOffice"));
				    actualExpenditureIncurredInLastFy.put("actualNonTeaching", etcProposal.get("actualNonTeaching"));
				    actualExpenditureIncurredInLastFy.put("actualSalaryOfCoreFaculty", etcProposal.get("actualSalaryOfCoreFaculty"));
				    actualExpenditureIncurredInLastFy.put("actualTotal", etcProposal.get("actualTotal"));
//				    actualExpenditureIncurredInLastFy1.put("actualExpenditureIncurredInLastFy", actualExpenditureIncurredInLastFy);
				    // Proposed Expenditure To Be Incurred for the FY
				    Map<String, Object> proposedExpenditureToBeIncurredForTheFY = new HashMap<String, Object>();
				    Map<String, Object> proposedExpenditureToBeIncurredForTheFY1 = new HashMap<String, Object>();
				    proposedExpenditureToBeIncurredForTheFY.put("proposedTraining", etcProposal.get("proposedTraining"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedMeal", etcProposal.get("proposedMeal"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedTravel", etcProposal.get("proposedTravel"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedPurchase", etcProposal.get("proposedPurchase"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedSupply", etcProposal.get("proposedSupply"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedOffice", etcProposal.get("proposedOffice"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedNonTeaching", etcProposal.get("proposedNonTeaching"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedSalaryOfCoreFaculty", etcProposal.get("proposedSalaryOfCoreFaculty"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposedTotal", etcProposal.get("proposedTotal"));
				    proposedExpenditureToBeIncurredForTheFY.put("proposalNo", etcProposal.get("proposalNo"));
//				    proposedExpenditureToBeIncurredForTheFY1.put("proposedExpenditureToBeIncurredForTheFY", proposedExpenditureToBeIncurredForTheFY);
				    
				    expenditureDetail.put("actualExpenditureIncurredInLastFy", actualExpenditureIncurredInLastFy);
				    expenditureDetail.put("proposedExpenditureToBeIncurredForTheFY",  proposedExpenditureToBeIncurredForTheFY);
				    actualExpenditureIncurredInLastFy1.put("expenditureDetail", expenditureDetail);
				    // Add the maps to a parent map or process them as needed
				    proposaldata.add(demographic);
				    proposaldata.add(instituteInfo1);
				    proposaldata.add(actualExpenditureIncurredInLastFy1);
				    proposaldata.add(sirdInformation);
				    
				    
					
				}
				String facultysql = "select * from faculty_proposal_final where  userId='"+userId+"'";
				List<Map<String,Object>> facultyList = jdbcTemplate.queryForList(facultysql);
				facultyForm.put("facultyForm",facultyList);
				
				proposaldata.add(facultyForm);
				
				String trainingSql = "select * from training_calender_final where  userId='"+userId+"'";
				List<Map<String,Object>> trainingList = jdbcTemplate.queryForList(trainingSql);
				trainingCalendar.put("trainingForm",trainingList);
				proposaldata.add(trainingCalendar);
				
				return proposaldata;
				
	}
	
	
	public int addCombinedProposal(Map<String,Object>map)
	{
		try
		{
			StringBuilder id=new StringBuilder();
			StringBuilder ids=new StringBuilder();
			boolean flag=true;
			int userId=this.getUserId(map);
			String sql1="select state_code from sird where sird_id='"+userId+"'";
			String stateCode=jdbcTemplate.queryForList(sql1).get(0).get("state_code").toString();
			String sql2="select etc_id from etc where state_code='"+stateCode+"'";
			List<Map<String,Object>> etcId = jdbcTemplate.queryForList(sql2);
			
			for(Map<String,Object> etcPropasal:etcId ) {
				if(flag)
				{
					id.append("'"+etcPropasal.get("etc_id").toString()+"'");
					ids.append(""+etcPropasal.get("etc_id").toString()+"");
					flag=false;
				}else
				{
					id.append(",'"+etcPropasal.get("etc_id").toString()+"'");
					ids.append(","+etcPropasal.get("etc_id").toString()+"");
				}
			}
			
			
			String proposal="select totaldemand, proposaldate,proposalid,user_id,usertype from final_proposal where usertype='etc' and user_id in("+id.toString()+")";
			
			List<Map<String,Object>>proposalList=jdbcTemplate.queryForList(proposal);
			
			for(Map<String,Object>proposalmap:proposalList)
			{
				String insert="insert into combined_proposal (proposalno,userid,usertype,proposalid) values("+proposalmap.get("proposal_no")+",'"+proposalmap.get("user_id")+"','"+proposalmap.get("usertype")+"','"+proposalmap.get("proposalid")+"')";
				jdbcTemplate.update(insert);
			}
			
			String proposalNew="select proposal_no, user_id,usertype,proposalid from final_proposal where usertype='sird' and user_id='"+userId+"'";
			
			List<Map<String,Object>>proposalListNew=jdbcTemplate.queryForList(proposalNew);
			
			for(Map<String,Object>proposalNewMap:proposalListNew)
			{
				String insert="insert into combined_proposal (proposalno,userid,usertype,proposalid,etcids,status) values("+proposalNewMap.get("proposal_no")+",'"+proposalNewMap.get("user_id")+"','"+proposalNewMap.get("usertype")+"','"+proposalNewMap.get("proposalid")+"','"+ids+"','"+"Generated"+"')";
				jdbcTemplate.update(insert);
			}
			
			return 1;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int addForwardProposal(Map<String,Object>map)
	{
		try
		{
			String combined_proposal_no="";
			
			String upperDesignation="";
			String lowerDesignation="";
			if(map.get("key").toString().equals("sird"))
			{
				lowerDesignation="sird";
				upperDesignation="so";
				String sql1="select sird_id from loginmaster_sird where username='"+map.get("username")+"'";
				int userId=Integer.parseInt(jdbcTemplate.queryForList(sql1).get(0).get("sird_id").toString());
				String sql="select combined_proposal_id from combined_proposal where userid='"+userId+"'";
				String proposalNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_id").toString();
				jdbcTemplate.update("insert into forward_proposal (lower_designation,upper_designation,user_type,user_id,remarks,combined_proposal_no) values('"+lowerDesignation+"','"+upperDesignation+"','"+map.get("key")+"','"+userId+"','"+map.get("remarks")+"','"+proposalNo+"')");
			}else if(map.get("key").toString().equals("so"))
			{
				String sql1="select min_id from loginmaster_ministry where username='"+map.get("username")+"'";
				int userId=Integer.parseInt(jdbcTemplate.queryForList(sql1).get(0).get("min_id").toString());
				lowerDesignation="so";
				upperDesignation="us";
				String sql="select combined_proposal_no from forward_proposal where lower_designation='"+"sird"+"' ";
				String proposalNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_no").toString();
				jdbcTemplate.update("insert into forward_proposal (lower_designation,upper_designation,user_type,user_id,remarks,combined_proposal_no) values('"+lowerDesignation+"','"+upperDesignation+"','"+map.get("key")+"','"+userId+"','"+map.get("remarks")+"','"+proposalNo+"')");
			}else if(map.get("key").toString().equals("us"))
			{
				String sql1="select min_id from loginmaster_ministry where username='"+map.get("username")+"'";
				int userId=Integer.parseInt(jdbcTemplate.queryForList(sql1).get(0).get("min_id").toString());
				lowerDesignation="us";
				upperDesignation="ds";
				String sql="select combined_proposal_no from forward_proposal where lower_designation='"+"sird"+"' ";
				String proposalNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_no").toString();
				jdbcTemplate.update("insert into forward_proposal (lower_designation,upper_designation,user_type,user_id,remarks,combined_proposal_no) values('"+lowerDesignation+"','"+upperDesignation+"','"+map.get("key")+"','"+userId+"','"+map.get("remarks")+"','"+proposalNo+"')");
			}else if(map.get("key").toString().equals("ds"))
			{
				String sql1="select min_id from loginmaster_ministry where username='"+map.get("username")+"'";
				int userId=Integer.parseInt(jdbcTemplate.queryForList(sql1).get(0).get("min_id").toString());
				lowerDesignation="ds";
				upperDesignation="as";
				String sql="select combined_proposal_no from forward_proposal where lower_designation='"+"sird"+"' ";
				String proposalNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_no").toString();
				jdbcTemplate.update("insert into forward_proposal (lower_designation,upper_designation,user_type,user_id,remarks,combined_proposal_no) values('"+lowerDesignation+"','"+upperDesignation+"','"+map.get("key")+"','"+userId+"','"+map.get("remarks")+"','"+proposalNo+"')");
			}else
			{
				lowerDesignation="etc";
				upperDesignation="sird";
				String sql1="select etc_id from loginmaster_etc where username='"+map.get("username")+"'";
				int userId=Integer.parseInt(jdbcTemplate.queryForList(sql1).get(0).get("etc_id").toString());
				String sql="select combined_proposal_no from forward_proposal where lower_designation='"+"sird"+"' ";
				String proposalNo=jdbcTemplate.queryForList(sql).get(0).get("combined_proposal_no").toString();
				jdbcTemplate.update("insert into forward_proposal (lower_designation,upper_designation,user_type,user_id,remarks,combined_proposal_no) values('"+lowerDesignation+"','"+upperDesignation+"','"+map.get("key")+"','"+userId+"','"+map.get("remarks")+"','"+proposalNo+"')");
			}
			return 1;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId3(Map<String,Object>map)
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
			}else if (map.get("key").toString().equalsIgnoreCase("sird")) {
				tableName="loginmaster_sird";
			}else
			{
				tableName="loginmaster_ministry";
			}
			
			String sql="select"+" "+map.get("key")+"_"+ "id from"+" "+tableName+" "+"where username='"+map.get("username")+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key")+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String,Object>>getCombinedProposal(String userType,String userName)
	{
		try
		{
			int userId=this.getUserId(userType,userName);
			String sql="select combined_proposal_id,proposaldate,status from combined_proposal where userid='"+userId+"'";
			return jdbcTemplate.queryForList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	
	
	public int getUserId(String userType,String userName)
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
			
			String sql="select"+" "+userType+"_"+ "id  from"+" "+tableName+" "+"where username='"+userName+"'";
			
			return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(userType+"_"+"id").toString());			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}


}

