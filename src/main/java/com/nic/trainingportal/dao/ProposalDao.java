package com.nic.trainingportal.dao;

import com.nic.trainingportal.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProposalDao {

    @Autowired
    private ErrorLogService error;

    @Autowired

    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDemographicDetails(Map<String, Object> map) {
        try {
            int stateCode = this.GetStateCode(map.get("key").toString(), map.get("userName").toString());

            String sql = "SELECT total_population_state AS \"totalPopulation\", " +
                    "total_rural_population AS \"totalRuralPopulation\", " +
                    "percent_rural_population AS \"percentOfRural\", " +
                    "state_code AS \"stateCode\" " +
                    "FROM demographic WHERE state_code = ?";

            return jdbcTemplate.queryForList(sql, stateCode);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getTrainingInfo(Map<String, Object> map) {
        try {

            String sql = "SELECT * FROM training_info WHERE user_id = ? AND user_type = ?";
            return jdbcTemplate.queryForList(sql, map.get("userId"), map.get("key"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getFacultyDetails(Map<String, Object> map) {
        try {
            Map<String, Object> countMap = new HashMap<>();
            List<Map<String, Object>> facultyList;

            String sql = "SELECT name, " +
                    "post AS \"postHeld\", " +
                    "pay_scale AS \"scalePay\", " +
                    "type_of_faculty AS \"typeOfFaculty\", " +
                    "remarks " +
                    "FROM faculty " +
                    "WHERE user_id = ?";

            facultyList = jdbcTemplate.queryForList(sql, map.get("userId"));

            countMap.put("facultyCount", getFacultyCount());
            facultyList.add(countMap);
            return facultyList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getTrainingCalendarDetails(Map<String, Object> map) {
        try {
            Map<String, Object> countMap = new HashMap<String, Object>();
            List<Map<String, Object>> trainingCalendarList = new ArrayList<Map<String, Object>>();

            String sql = "SELECT entry_date AS \"proposedDate\", " +
                    "name AS \"facultyName\", " +
                    "training_venue AS \"venue\", " +
                    "training_subject AS \"trainingSubject\", " +
                    "number_of_trainees AS \"trainessNumber\", " +
                    "target_group AS \"targetGroup\" " +
                    "FROM training_final " +
                    "WHERE user_id = ?";
            String userId = map.get("userId").toString();
            trainingCalendarList = jdbcTemplate.queryForList(sql, userId);
            countMap.put("trainingCalendarCount", getTrainingCalendarCount());
            trainingCalendarList.add(countMap);
            return trainingCalendarList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public int getFacultyCount() {
        try {
            String sql = "select count(*) as count from faculty";

            return jdbcTemplate.queryForObject(sql, Integer.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTrainingCalendarCount() {
        try {
            String sql = "select count(*) as count from training_final";

            return jdbcTemplate.queryForObject(sql, Integer.class);

        } catch (Exception e) {
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

    public int addProposal(String lowerDesignation, String upperDesignation, Map<String, Object> map) {
        try {
            String sql = "INSERT INTO forward_proposal(user_type, remarks, upper_designation, lower_designation) " +
                    "VALUES (?, ?, ?, ?)";

            return jdbcTemplate.update(sql,
                    lowerDesignation,
                    map.get("remarks"),
                    upperDesignation,
                    lowerDesignation);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return 0;
    }

    public int getUserId(Map<String, Object> map) {
        try {
            String tableName = "";
            String userType = map.get("key").toString().toLowerCase();
            String username = map.get("username").toString();

// Determine table name based on login type
            if (userType.equals("etc")) {
                tableName = "loginmaster_etc";
            } else if (userType.equals("sird")) {
                tableName = "loginmaster_sird";
            } else {
                tableName = "loginmaster_ministry";
            }

            String columnName = userType + "_id";

// Note: table and column names can't be parameterized — validate them beforehand (done above)
            String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, username);

// Ensure result is not empty
            if (!result.isEmpty()) {
                return Integer.parseInt(result.get(0).get(columnName).toString());
            } else {
                throw new RuntimeException("No user found for username: " + username);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getForwradProposal(String userType, String userName) {
        try {
            List<Map<String, Object>> datalist = new ArrayList<>();
            Map<String, Object> idmap = new HashMap<>();
            String lowerDesignation;

            if (userType.equals("sird")) {
                lowerDesignation = "sird";
            } else if (userType.equals("so")) {
                lowerDesignation = "so";
            } else if (userType.equals("us")) {
                lowerDesignation = "us";
            } else if (userType.equals("ds")) {
                lowerDesignation = "ds";
            } else {
                lowerDesignation = "etc";
            }

// Safe query with parameter for lowerDesignation
            String sql = "SELECT remarks, combined_proposal_no FROM forward_proposal WHERE lower_designation = ?";
            List<Map<String, Object>> forwardList = jdbcTemplate.queryForList(sql, lowerDesignation);

            if (forwardList.isEmpty()) {
                return Collections.emptyList();  // or handle as needed
            }

            String combinedNo = forwardList.get(0).get("combined_proposal_no").toString();

// Safe query with parameter for combinedNo
            String sqlNew = "SELECT userid, etcids FROM combined_proposal WHERE combined_proposal_id = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlNew, combinedNo);

            int i = 1;
            for (Map<String, Object> datamap : list) {
                idmap.put("sirdId", datamap.get("userid"));
                String[] arr = datamap.get("etcids").toString().split(",");
                for (String str : arr) {
                    idmap.put("etcId" + i++, str);
                }
            }

// Optionally re-use or copy forwardList
            datalist = forwardList;
            datalist.add(idmap);

            return datalist;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public int updateForwradProposal(Map<String, Object> map) {
        try {
            String lowerDesignation = "";
            if (map.get("key").toString().equals("sird")) {
                lowerDesignation = "etc";
            } else if (map.get("key").toString().equals("so")) {
                lowerDesignation = "sird";
            } else if (map.get("key").toString().equals("us")) {
                lowerDesignation = "so";
            } else if (map.get("key").toString().equals("ds")) {
                lowerDesignation = "us";
            } else {
                lowerDesignation = "ds";
            }

            String sql = "UPDATE forward_proposal SET status = ? WHERE lower_designation = ?";
            return jdbcTemplate.update(sql, map.get("status"), lowerDesignation);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public String addProposal(Map<String, Object> map) {
        try {
            int user_id = this.getUserId2(map);
            BigDecimal actualSalaryOfCoreFaculty = BigDecimal.ZERO;
            BigDecimal proposedSalaryOfCoreFaculty = BigDecimal.ZERO;
            if (map.get("userType") != null) {

                if (map.get("userType").toString().equals("MORD(Section Officer)")) {
                    String sanction = "select * from proposal_sanction  where proposalid=?";
                    int proposalIdint = (int) map.get("proposalid");
                    List<Map<String, Object>> sanctionList = jdbcTemplate.queryForList(sanction, proposalIdint);
                    if (sanctionList == null || sanctionList.size() == 0) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> actualEligible = (Map<String, Object>) ((Map<String, Object>) map
                                .get("expenditureDetail")).get("actualEligible");

                        @SuppressWarnings("unchecked")
                        Map<String, Object> extraKey = (Map<String, Object>) map.get("expenditureDetail");
                        BigDecimal unspentBalance = new BigDecimal(String.valueOf(extraKey.get("unspentBalance")));
                        BigDecimal trench = new BigDecimal(String.valueOf(extraKey.get("trench")));
                        BigDecimal netPaybleAmount = new BigDecimal(String.valueOf(extraKey.get("netPaybleAmount")));
                        @SuppressWarnings("unchecked")
                        Map<String, Object> proposedSanction = (Map<String, Object>) ((Map<String, Object>) map
                                .get("expenditureDetail")).get("proposedSanction");

                        String sanctionInsert = "INSERT INTO proposal_sanction (" +
                                "actualtrainingeligible, actualmealeligible, actualtraveleligible, actualpurchaseeligible, " +
                                "actualsupplyeligible, actualofficeeligible, actualnonteachingeligible, actualsalaryofcorefaculty, actualeligibletotaleligible, " +
                                "proposedtrainingsanction, proposedmealsanction, proposedtravelsanction, proposedpurchasesanction, proposedsupplysanction, " +
                                "proposedofficesanction, proposednonteachingsanction, proposedsalaryofcorefacultysanction, proposedtotalsanction, " +
                                "proposalid, unspentbalance, trench, netpaybleamount, usertype, userid) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                        jdbcTemplate.update(sanctionInsert,
                                actualEligible.get("actualTrainingEligible"),
                                actualEligible.get("actualMealEligible"),
                                actualEligible.get("actualTravelEligible"),
                                actualEligible.get("actualPurchaseEligible"),
                                actualEligible.get("actualSupplyEligible"),
                                actualEligible.get("actualOfficeEligible"),
                                actualEligible.get("actualNonTeachingEligible"),
                                actualEligible.get("actualSalaryOfCoreFaculty"),
                                actualEligible.get("actualEligibleTotalEligible"),
                                proposedSanction.get("proposedTrainingSanction"),
                                proposedSanction.get("proposedMealSanction"),
                                proposedSanction.get("proposedTravelSanction"),
                                proposedSanction.get("proposedPurchaseSanction"),
                                proposedSanction.get("proposedSupplySanction"),
                                proposedSanction.get("proposedOfficeSanction"),
                                proposedSanction.get("proposedNonTeachingSanction"),
                                proposedSanction.get("proposedSalaryOfCoreFacultySanction"),
                                proposedSanction.get("proposedTotalSanction"),
                                proposalIdint,
                                unspentBalance,
                                trench,
                                netPaybleAmount,
                                map.get("key"),
                                user_id
                        );

                    } else {
                        return "Record Already Exist With This Proposal Id";
                    }
                }
            } else {

                Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");
                Map<String, Object> sirdInformation = (Map<String, Object>) map.get("sirdInformation");
                Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
                BigDecimal fundsReleasedbyMord = new BigDecimal(String.valueOf(instituteInfo.get("fundsReleasedbyMord")));
                BigDecimal otherResources = new BigDecimal(String.valueOf(instituteInfo.get("otherResources")));
                BigDecimal mordAndStateShare = new BigDecimal(String.valueOf(instituteInfo.get("mordAndStateShare")));
                BigDecimal fundsFromVariousSources = new BigDecimal(String.valueOf(instituteInfo.get("fundsFromVariousSources")));
                List<Map<String, Object>> facultyForm = (List<Map<String, Object>>) map.get("facultyForm");
                List<Map<String, Object>> trainingCalender = (List<Map<String, Object>>) map.get("trainingCalender");
                Map<String, Object> actualExpenditure = (Map<String, Object>) ((Map<String, Object>) map
                        .get("expenditureDetail")).get("actualExpenditureIncurredInLastFy");
                Map<String, Object> proposedExpenditure = (Map<String, Object>) ((Map<String, Object>) map
                        .get("expenditureDetail")).get("proposedExpenditureToBeIncurredForTheFY");

                BigDecimal actualTraining = new BigDecimal(String.valueOf(actualExpenditure.get("actualTraining")));
                BigDecimal actualMeal = new BigDecimal(String.valueOf(actualExpenditure.get("actualMeal")));
                BigDecimal actualTravel = new BigDecimal(String.valueOf(actualExpenditure.get("actualTravel")));
                BigDecimal actualPurchase = new BigDecimal(String.valueOf(actualExpenditure.get("actualPurchase")));
                BigDecimal actualSupply = new BigDecimal(String.valueOf(actualExpenditure.get("actualSupply")));
                BigDecimal actualOffice = new BigDecimal(String.valueOf(actualExpenditure.get("actualOffice")));
                BigDecimal actualNonTeaching = new BigDecimal(String.valueOf(actualExpenditure.get("actualNonTeaching")));

                if (actualExpenditure.get("actualSalaryOfCoreFaculty") != null) {
                    actualSalaryOfCoreFaculty = new BigDecimal(String.valueOf(actualExpenditure.get("actualSalaryOfCoreFaculty")));
                }
                BigDecimal actualTotal = new BigDecimal(String.valueOf(actualExpenditure.get("actualTotal")));
                BigDecimal proposedTraining = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTraining")));
                BigDecimal proposedMeal = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedMeal")));
                BigDecimal proposedTravel = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTravel")));
                BigDecimal proposedPurchase = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedPurchase")));
                BigDecimal proposedSupply = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedSupply")));
                BigDecimal proposedOffice = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedOffice")));
                BigDecimal proposedNonTeaching = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedNonTeaching")));

                if (proposedExpenditure.get("proposedSalaryOfCoreFaculty") != null) {
                    proposedSalaryOfCoreFaculty = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedSalaryOfCoreFaculty")));
                }
                BigDecimal proposedTotal = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTotal")));


                Map<String, Object> uploadFile = (Map<String, Object>) map.get("uploadFile");

                String sql2 = "SELECT installmentno, financialyear FROM final_proposal WHERE usertype = ? AND user_id = ?";
                List<Map<String, Object>> installment = jdbcTemplate.queryForList(sql2, map.get("key"), user_id);


                if (installment != null && installment.size() != 0) {
                    for (Map<String, Object> map1 : installment) {
                        if (map1.get("installmentno").equals(instituteInfo.get("proposalNo").toString()) && map1.get("financialYear").equals(sirdInformation.get("financialYear"))) {
                            return "Proposal for this installment already Submitted";

                        }
                    }
                }

                BigDecimal totalExpenditure = actualTotal.add(proposedTotal);

                String insertQry = "INSERT INTO final_proposal (" +
                        "totalPopulation, totalRuralPopulation, percentRuralPopulation, sirdName, financialYear, nameOfDirector, address, telephoneNo, mobileNo, email, " +
                        "functional, building, numberOfFacultyPermanent, numberOfFacultyContractual, numberOfOtherStaff, hostelFacility, numberOfSeatsInHostel, " +
                        "trainingHallsNumber, trainingHallsCapacity, computerLabNumber, computerLabCapacity, diningHallsNumber, diningHallsCapacity, numberOfKitchens, " +
                        "auditoriumOrCommitteeRoomNumber, auditoriumOrCommitteeRoomCapacity, instituteRemarks, TypeofGrantRequested, fundsReleasedByMord, otherresources, " +
                        "mordAndStateShare, fundsFromVariousSources, actualTraining, actualMeal, actualTravel, actualPurchase, actualSupply, actualOffice, actualNonTeaching, " +
                        "actualSalaryOfCoreFaculty, actualTotal, proposedTraining, proposedMeal, proposedTravel, proposedPurchase, proposedSupply, proposedOffice, " +
                        "proposedNonTeaching, proposedSalaryOfCoreFaculty, proposedTotal, usertype, installmentno, user_id, totaldemand, certified" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                jdbcTemplate.update(insertQry,
                        demographicProfile.get("totalPopulation"),
                        demographicProfile.get("totalRuralPopulation"),
                        demographicProfile.get("percentOfRural"),
                        sirdInformation.get("sirdName"),
                        sirdInformation.get("financialYear"),
                        sirdInformation.get("nameOfDirector"),
                        sirdInformation.get("address"),
                        sirdInformation.get("telephoneNo"),
                        sirdInformation.get("mobileNo"),
                        sirdInformation.get("email"),
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
                        fundsReleasedbyMord,
                        otherResources,
                        mordAndStateShare,
                        fundsFromVariousSources,
                        actualTraining,
                        actualMeal,
                        actualTravel,
                        actualPurchase,
                        actualSupply,
                        actualOffice,
                        actualNonTeaching,
                        actualSalaryOfCoreFaculty,
                        actualTotal,
                        proposedTraining,
                        proposedMeal,
                        proposedTravel,
                        proposedPurchase,
                        proposedSupply,
                        proposedOffice,
                        proposedNonTeaching,
                        proposedSalaryOfCoreFaculty,
                        proposedTotal,
                        map.get("key"),
                        instituteInfo.get("proposalNo"),
                        user_id,
                        totalExpenditure,
                        map.get("certified")
                );


                String sql = "SELECT proposalid from final_proposal order by proposalid desc limit 1";

                String proposalId = jdbcTemplate.queryForList(sql).get(0).get("proposalId").toString();
                int proposalIdInt = Integer.parseInt(proposalId);

                for (Map<String, Object> facultyMap : facultyForm) {
                    String insertFacultyQry = "INSERT INTO faculty_proposal_final " +
                            "(pay, name, post, permanent, remarks, proposalno, userId, financialyear, installmentno) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertFacultyQry,
                            facultyMap.get("scalePay"),
                            facultyMap.get("name"),
                            facultyMap.get("postHeld"),
                            facultyMap.get("typeOfFaculty"),
                            facultyMap.get("remarks"),
                            proposalIdInt,
                            user_id,
                            sirdInformation.get("financialYear"),
                            instituteInfo.get("proposalNo")
                    );

                }
                for (Map<String, Object> trainingCalenderMap : trainingCalender) {
                    String insertCalenderQry = "INSERT INTO training_calender_final " +
                            "(traningStartDate, nameOfFaculty, placeOfTraining, subjectOfTraining, noOfTrainees, targetGroup, " +
                            "proposalno, userId, totalduration, trainingenddate, financialyear, installmentno) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    String proposedDateStr = trainingCalenderMap.get("proposedDate").toString();
                    LocalDate localDate = LocalDate.parse(proposedDateStr, dateFormatter);
                    LocalDateTime localStartDate = localDate.atStartOfDay(); // sets time to 00:00:00


                    String proposedDateEnd = trainingCalenderMap.get("enddate").toString();
                    LocalDate localEndDate = LocalDate.parse(proposedDateEnd, dateFormatter);
                    LocalDateTime localEndtDate = localEndDate.atStartOfDay();

                    int totalDuration = (int) trainingCalenderMap.get("totalDuration");


                    jdbcTemplate.update(insertCalenderQry,
                            localStartDate,
                            trainingCalenderMap.get("facultyName"),
                            trainingCalenderMap.get("venue"),
                            trainingCalenderMap.get("trainingSubject"),
                            trainingCalenderMap.get("trainessNumber"),
                            trainingCalenderMap.get("targetGroup"),
                            proposalIdInt,
                            user_id,
                            totalDuration,
                            localEndtDate,
                            sirdInformation.get("financialYear"),
                            instituteInfo.get("proposalNo")
                    );

                }
                String uploadFileProposal = "INSERT INTO upload_file (" +
                        "utilzationcertificate, participantstrained, attendancesheet, auditreportofthelastfY, " +
                        "certificatefromauditor, undertakingfromheadOfsird, noembezzlement, continuationorderOfcorefacultymembers, " +
                        "user_id, proposalid, remittancefilename, expenditurebillfilename) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(uploadFileProposal,
                        uploadFile.get("utilzationCertificate"),
                        uploadFile.get("participantsTrained"),
                        uploadFile.get("attendanceSheet"),
                        uploadFile.get("auditReportofthelastFY"),
                        uploadFile.get("certificateFromAuditor"),
                        uploadFile.get("underTakingFromHeadOfSIRD"),
                        uploadFile.get("noEmbezzlement"),
                        uploadFile.get("ContinuationOrderOfCoreFacultyMembers"),
                        user_id,
                        proposalId,
                        uploadFile.get("remittanceFileName"),
                        uploadFile.get("expenditureBillFileName")
                );

            }

        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "Add Proposal");
            return "Record Not Saved";
        }
        return "Record Saved";
    }

    private int getUserId2(Map<String, Object> map) {
        try {
            String tableName = "";
            String userType = map.get("key").toString().toLowerCase(); // normalize casing

// Check login type
            if (userType.equals("etc")) {
                tableName = "loginmaster_etc";
            } else if (userType.equals("sird")) {
                tableName = "loginmaster_sird";
            } else {
                tableName = "loginmaster_ministry";
            }

            String columnName = userType + "_id";
            String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, map.get("username"));
            if (!result.isEmpty() && result.get(0).get(columnName) != null) {
                return Integer.parseInt(result.get(0).get(columnName).toString());
            } else {
                throw new RuntimeException("User not found or invalid credentials.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getProposal(String userId) {
        // TODO Auto-generated method stub
        int i = 1;
        int j = 1;
        List<Map<String, Object>> proposaldata = new ArrayList<Map<String, Object>>();
        Map<String, Object> facultyForm = new HashMap<String, Object>();
        Map<String, Object> trainingCalendar = new HashMap<String, Object>();
        Map<String, Object> expenditureDetail = new HashMap<String, Object>();

        Map<String, Object> actualEligible = new HashMap<String, Object>();
        Map<String, Object> proposedSanction = new HashMap<String, Object>();


        List<Map<String, Object>> sanctionMap = new ArrayList<Map<String, Object>>();
        String sql = "SELECT " +
                "actualtrainingeligible AS \"actualTrainingEligible\", " +
                "actualmealeligible AS \"actualMealEligible\", " +
                "actualtraveleligible AS \"actualTravelEligible\", " +
                "actualpurchaseeligible AS \"actualPurchaseEligible\", " +
                "actualsupplyeligible AS \"actualSupplyEligible\", " +
                "actualofficeeligible AS \"actualOfficeEligible\", " +
                "actualnonteachingeligible AS \"actualNonTeachingEligible\", " +
                "actualsalaryofcorefaculty AS \"actualSalaryOfCoreFaculty\", " +
                "actualeligibletotaleligible AS \"actualEligibleTotalEligible\", " +
                "proposedtrainingsanction AS \"proposedTrainingSanction\", " +
                "proposedmealsanction AS \"proposedMealSanction\", " +
                "proposedtravelsanction AS \"proposedTravelSanction\", " +
                "proposedpurchasesanction AS \"proposedPurchaseSanction\", " +
                "proposedsupplysanction AS \"proposedSupplySanction\", " +
                "proposedofficesanction AS \"proposedOfficeSanction\", " +
                "proposednonteachingsanction AS \"proposedNonTeachingSanction\", " +
                "proposedsalaryofcorefacultysanction AS \"proposedSalaryOfCoreFacultySanction\", " +
                "proposedtotalsanction AS \"proposedTotalSanction\", " +
                "unspentbalance AS \"unspentBalance\", " +
                "trench, netpaybleamount AS \"netPaybleAmount\" " +
                "FROM proposal_sanction WHERE proposalid = ?";
        int proposalInt = Integer.parseInt(userId);
        sanctionMap = jdbcTemplate.queryForList(sql, proposalInt);

        if (sanctionMap != null && sanctionMap.size() != 0) {
            for (Map<String, Object> sanction : sanctionMap) {
                actualEligible.put("actualTrainingEligible", sanction.get("actualTrainingEligible"));
                actualEligible.put("actualMealEligible", sanction.get("actualMealEligible"));
                actualEligible.put("actualTravelEligible", sanction.get("actualTravelEligible"));
                actualEligible.put("actualPurchaseEligible", sanction.get("actualPurchaseEligible"));
                actualEligible.put("actualSupplyEligible", sanction.get("actualSupplyEligible"));
                actualEligible.put("actualOfficeEligible", sanction.get("actualOfficeEligible"));
                actualEligible.put("actualNonTeachingEligible", sanction.get("actualNonTeachingEligible"));
                actualEligible.put("actualSalaryOfCoreFaculty", sanction.get("actualSalaryOfCoreFaculty"));
                actualEligible.put("actualEligibleTotalEligible", sanction.get("actualEligibleTotalEligible"));

                proposedSanction.put("proposedTrainingSanction", sanction.get("proposedTrainingSanction"));
                proposedSanction.put("proposedMealSanction", sanction.get("proposedMealSanction"));
                proposedSanction.put("proposedTravelSanction", sanction.get("proposedTravelSanction"));
                proposedSanction.put("proposedPurchaseSanction", sanction.get("proposedPurchaseSanction"));
                proposedSanction.put("proposedSupplySanction", sanction.get("proposedSupplySanction"));
                proposedSanction.put("proposedOfficeSanction", sanction.get("proposedOfficeSanction"));
                proposedSanction.put("proposedNonTeachingSanction", sanction.get("proposedNonTeachingSanction"));
                proposedSanction.put("proposedSalaryOfCoreFacultySanction", sanction.get("proposedSalaryOfCoreFacultySanction"));
                proposedSanction.put("proposedTotalSanction", sanction.get("proposedTotalSanction"));
                expenditureDetail.put("unspentBalance", sanction.get("unspentBalance"));
                expenditureDetail.put("trench", sanction.get("trench"));
                expenditureDetail.put("netPaybleAmount", sanction.get("netPaybleAmount"));


            }
        }

        String proposalsql = "select * from final_proposal where proposalid = ?";
        List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposalsql, proposalInt);


        for (Map<String, Object> etcProposal : proposalList) {
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
            instituteInfo.put("remarks", etcProposal.get("instituteRemarks"));
            instituteInfo.put("TypeofGrantRequested", etcProposal.get("TypeofGrantRequested"));
            instituteInfo.put("fundsReleasedByMord", etcProposal.get("fundsReleasedByMord"));
            instituteInfo.put("otherResources", etcProposal.get("otherResources"));
            instituteInfo.put("mordAndStateShare", etcProposal.get("mordAndStateShare"));
            instituteInfo.put("fundsFromVariousSources", etcProposal.get("fundsFromVariousSources"));
            instituteInfo.put("proposalno", etcProposal.get("installmentno"));

            instituteInfo1.put("instituteInfo", instituteInfo);
            // Expenditure Detail Map
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
            expenditureDetail.put("proposedExpenditureToBeIncurredForTheFY", proposedExpenditureToBeIncurredForTheFY);
            expenditureDetail.put("actualEligible", actualEligible);
            expenditureDetail.put("proposedSanction", proposedSanction);

            actualExpenditureIncurredInLastFy1.put("expenditureDetail", expenditureDetail);
            // Add the maps to a parent map or process them as needed
            proposaldata.add(demographic);
            proposaldata.add(instituteInfo1);
            proposaldata.add(actualExpenditureIncurredInLastFy1);
            proposaldata.add(sirdInformation);


        }
        String facultysql = "SELECT financialyear As \"financialYear\", installmentno As \"installmentType\", faculty_id, name, " +
                "post AS \"postHeld\", " +
                "pay AS \"scalePay\", " +
                "permanent AS \"typeOfFaculty\", " +
                "remarks " +
                "FROM faculty_proposal_final " +
                "WHERE proposalno = ?";
        List<Map<String, Object>> facultyList = jdbcTemplate.queryForList(facultysql, proposalInt);


        for (Map<String, Object> map : facultyList) {
            map.put("serialNo", i++);
        }
        facultyForm.put("facultyForm", facultyList);

        proposaldata.add(facultyForm);

        String trainingSql = "SELECT financialyear As \"financialYear\", installmentno As \"installmentType\", totalduration AS \"totalDuration\", trainingid, trainingenddate AS \"enddate\", traningstartdate AS \"proposedDate\", " +
                "nameoffaculty AS \"facultyName\", " +
                "placeoftraining AS \"venue\", " +
                "subjectoftraining AS \"trainingSubject\", " +
                "nooftrainees AS \"trainessNumber\", " +
                "targetgroup AS \"targetGroup\" " +
                "FROM training_calender_final " +
                "WHERE proposalno = ?";

        List<Map<String, Object>> trainingList = jdbcTemplate.queryForList(trainingSql, proposalInt);

        for (Map<String, Object> map : trainingList) {
            map.put("serialNo", j++);
        }
        trainingCalendar.put("trainingForm", trainingList);
        proposaldata.add(trainingCalendar);

        Map<String, Object> uploadFile = new HashMap<String, Object>();
        String uploadSql = "select * from upload_file where proposalid = ?";
        List<Map<String, Object>> uploadList = jdbcTemplate.queryForList(uploadSql, userId);

        if (uploadList == null || uploadList.size() == 0) {
            uploadFile.put("uploadFile", new HashMap<String, Object>(0));
        } else {
            uploadFile.put("uploadFile", uploadList.get(0));
        }
        proposaldata.add(uploadFile);


        return proposaldata;

    }


    public String addCombinedProposal(Map<String, Object> map) {
        try {
            StringBuilder id = new StringBuilder();
            StringBuilder ids = new StringBuilder();
            StringBuilder sirdProposalids = new StringBuilder();
            StringBuilder proposalids = new StringBuilder();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            int userId = this.getUserId(map);

            String proposalSird = "select proposal_no, user_id, usertype, proposalid, installmentno from final_proposal where usertype = ? and user_id = ?";
            List<Map<String, Object>> sirdList = jdbcTemplate.queryForList(proposalSird, "sird", userId);

            if (sirdList == null || sirdList.isEmpty()) {
                return "Please Create Sird Proposal First";
            }

            String sql1 = "select state_code from sird where sird_id = ?";
            String stateCode = jdbcTemplate.queryForList(sql1, userId).get(0).get("state_code").toString();

            String sql2 = "select etc_id from etc where state_code = ?";
            List<Map<String, Object>> etcId = jdbcTemplate.queryForList(sql2, stateCode);

            // Build list of etc_ids for IN clause and a plain comma separated string for insert
            List<String> etcIdList = new ArrayList<>();
            for (Map<String, Object> etcPropasal : etcId) {
                etcIdList.add(etcPropasal.get("etc_id").toString());
                if (flag) {
                    ids.append(etcPropasal.get("etc_id").toString());
                    flag = false;
                } else {
                    ids.append(",").append(etcPropasal.get("etc_id").toString());
                }
            }

            if (etcIdList.isEmpty()) {
                return "No ETC Ids found";
            }

            // Create placeholders for the IN clause
            String placeholders = String.join(",", Collections.nCopies(etcIdList.size(), "?"));
            String proposalSql = "select proposalid, user_id, usertype from final_proposal where usertype = ? and user_id in (" + placeholders + ")";

            // Prepare parameters: first param "etc" plus the etc IDs
            List<Object> proposalParams = new ArrayList<>();
            proposalParams.add("etc");
            proposalParams.addAll(etcIdList);

            List<Map<String, Object>> proposallist = jdbcTemplate.queryForList(proposalSql, proposalParams.toArray());

            for (Map<String, Object> etcMap : proposallist) {
                if (flag1) {
                    proposalids.append(etcMap.get("proposalid").toString());
                    flag1 = false;
                } else {
                    proposalids.append(",").append(etcMap.get("proposalid").toString());
                }
            }

            String proposalNew = "select proposal_no, user_id, usertype, proposalid from final_proposal where usertype = ? and user_id = ?";
            List<Map<String, Object>> proposalListNew = jdbcTemplate.queryForList(proposalNew, "sird", userId);

            for (Map<String, Object> proposalNewMap : proposalListNew) {
                if (flag2) {
                    sirdProposalids.append(proposalNewMap.get("proposalid").toString());
                    flag2 = false;
                } else {
                    sirdProposalids.append(",").append(proposalNewMap.get("proposalid").toString());
                }
            }

            String insert = "insert into combined_proposal (userid, usertype, proposalid, etcids, status, etcsproposalid) values (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insert, userId, map.get("key"), sirdProposalids.toString(), ids.toString(), "Generated", proposalids.toString());

            return "1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public int addForwardProposal(Map<String, Object> map) {
        try {
            String combined_proposal_no = "";

            String upperDesignation = "";
            String lowerDesignation = "";
            int userId = 0;
            String proposalNo = "";

            if (map.get("key").toString().equals("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "so";
                String sql1 = "select sird_id from loginmaster_sird where username = ?";
                userId = Integer.parseInt(jdbcTemplate.queryForList(sql1, map.get("username")).get(0).get("sird_id").toString());

                String sql = "select combined_proposal_id from combined_proposal where userid = ?";
                proposalNo = jdbcTemplate.queryForList(sql, userId).get(0).get("combined_proposal_id").toString();

                String insertSql = "insert into forward_proposal (lower_designation, upper_designation, user_type, user_id, remarks, combined_proposal_no, status) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, lowerDesignation, upperDesignation, map.get("key"), userId, map.get("remarks"),
                        proposalNo, "pending at so level");

            } else if (map.get("key").toString().equals("so")) {
                String sql1 = "select min_id from loginmaster_ministry where username = ?";
                userId = Integer.parseInt(jdbcTemplate.queryForList(sql1, map.get("username")).get(0).get("min_id").toString());

                lowerDesignation = "so";
                upperDesignation = "us";

                String sql = "select combined_proposal_no from forward_proposal where lower_designation = ?";
                proposalNo = jdbcTemplate.queryForList(sql, "sird").get(0).get("combined_proposal_no").toString();

                String insertSql = "insert into forward_proposal (lower_designation, upper_designation, user_type, user_id, remarks, combined_proposal_no, status) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, lowerDesignation, upperDesignation, map.get("key"), userId, map.get("remarks"),
                        proposalNo, "pending at us level");

            } else if (map.get("key").toString().equals("us")) {
                String sql1 = "select min_id from loginmaster_ministry where username = ?";
                userId = Integer.parseInt(jdbcTemplate.queryForList(sql1, map.get("username")).get(0).get("min_id").toString());

                lowerDesignation = "us";
                upperDesignation = "ds";

                String sql = "select combined_proposal_no from forward_proposal where lower_designation = ?";
                proposalNo = jdbcTemplate.queryForList(sql, "sird").get(0).get("combined_proposal_no").toString();

                String insertSql = "insert into forward_proposal (lower_designation, upper_designation, user_type, user_id, remarks, combined_proposal_no, status) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, lowerDesignation, upperDesignation, map.get("key"), userId, map.get("remarks"),
                        proposalNo, "pending at ds level");

            } else if (map.get("key").toString().equals("ds")) {
                String sql1 = "select min_id from loginmaster_ministry where username = ?";
                userId = Integer.parseInt(jdbcTemplate.queryForList(sql1, map.get("username")).get(0).get("min_id").toString());

                lowerDesignation = "ds";
                upperDesignation = "as";

                String sql = "select combined_proposal_no from forward_proposal where lower_designation = ?";
                proposalNo = jdbcTemplate.queryForList(sql, "sird").get(0).get("combined_proposal_no").toString();

                String insertSql = "insert into forward_proposal (lower_designation, upper_designation, user_type, user_id, remarks, combined_proposal_no, status) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, lowerDesignation, upperDesignation, map.get("key"), userId, map.get("remarks"),
                        proposalNo, "pending at as level");

            } else {
                lowerDesignation = "etc";
                upperDesignation = "sird";
                String sql1 = "select etc_id from loginmaster_etc where username = ?";
                userId = Integer.parseInt(jdbcTemplate.queryForList(sql1, map.get("username")).get(0).get("etc_id").toString());

                String sql = "select combined_proposal_no from forward_proposal where lower_designation = ?";
                proposalNo = jdbcTemplate.queryForList(sql, "sird").get(0).get("combined_proposal_no").toString();

                String insertSql = "insert into forward_proposal (lower_designation, upper_designation, user_type, user_id, remarks, combined_proposal_no, status) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(insertSql, lowerDesignation, upperDesignation, map.get("key"), userId, map.get("remarks"),
                        proposalNo, "pending at sird level");
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserId3(Map<String, Object> map) {
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

            String sql = "select" + " " + map.get("key") + "_" + "id from" + " " + tableName + " " + "where username='" + map.get("username") + "'";

            return Integer.parseInt(jdbcTemplate.queryForList(sql).get(0).get(map.get("key") + "_" + "id").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getCombinedProposal(String userType, String userName) {
        try {
            int userId = this.getUserId(userType, userName);
            String sql = "select combined_proposal_id, proposaldate, status from combined_proposal where userid = ?";
            return jdbcTemplate.queryForList(sql, userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public int getUserId(String userType, String userName) {
        try {
            Map<String, Object> map = new HashMap<>();
            String tableName = "";
            /**
             *  check login type
             */
            if (userType.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
            } else if (userType.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
            } else {
                tableName = "loginmaster_ministry";
            }


            String idColumn = userType + "_id";
            String sql = "select " + idColumn + " from " + tableName + " where username = ?";

            return Integer.parseInt(
                    jdbcTemplate.queryForList(sql, userName)
                            .get(0)
                            .get(idColumn)
                            .toString()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int checkProposal(String userType, String userName) {
        try {
            int userId = this.getUserId(userType, userName);
            String sql = "select count(*) as count from final_proposal where user_id = ? and usertype = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int addSantioned(Map<String, Object> map) {
        try {
//			int user_id = this.getUserId2(map);
            @SuppressWarnings("unchecked")
            Map<String, Object> actualEligible = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("actualEligible");
            @SuppressWarnings("unchecked")
            Map<String, Object> proposedSanction = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("proposedSanction");
            String insertQry = "INSERT INTO proposal_sanction (" +
                    "actualtrainingeligible, actualmealeligible, actualtraveleligible, actualpurchaseeligible, " +
                    "actualsupplyeligible, actualofficeeligible, actualnonteachingeligible, actualsalaryofcorefaculty, actualeligibletotaleligible, " +
                    "proposedtrainingsanction, proposedmealsanction, proposedtravelsanction, proposedpurchasesanction, proposedsupplysanction, " +
                    "proposedofficesanction, proposednonteachingsanction, proposedsalaryofcorefacultysanction, proposedtotalsanction) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(insertQry,
                    actualEligible.get("actualTrainingEligible"),
                    actualEligible.get("actualMealEligible"),
                    actualEligible.get("actualTravelEligible"),
                    actualEligible.get("actualPurchaseEligible"),
                    actualEligible.get("actualSupplyEligible"),
                    actualEligible.get("actualOfficeEligible"),
                    actualEligible.get("actualNonTeachingEligible"),
                    actualEligible.get("actualSalaryOfCoreFaculty"),
                    actualEligible.get("actualEligibleTotalEligible"),
                    proposedSanction.get("proposedTrainingSanction"),
                    proposedSanction.get("proposedMealSanction"),
                    proposedSanction.get("proposedTravelSanction"),
                    proposedSanction.get("proposedPurchaseSanction"),
                    proposedSanction.get("proposedSupplySanction"),
                    proposedSanction.get("proposedOfficeSanction"),
                    proposedSanction.get("proposedNonTeachingSanction"),
                    proposedSanction.get("proposedSalaryOfCoreFacultySanction"),
                    proposedSanction.get("proposedTotalSanction"));


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public int updateCombinedProposalStatus(String proposalNo, String userType) {
        try {
            String lowerDesignation = "";
            String upperDesignation = "";
            if (userType.equalsIgnoreCase("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "so";
            } else if (userType.equalsIgnoreCase("so")) {
                lowerDesignation = "so";
                upperDesignation = "ds";
            } else {
                lowerDesignation = "ds";
                upperDesignation = "as";
            }

            String sql = "select status from forward_proposal where combined_proposal_no = ? and lower_designation = ? and upper_designation = ?";
            String status = jdbcTemplate.queryForList(sql, proposalNo, lowerDesignation, upperDesignation).get(0).get("status").toString();

            String updateSql = "update combined_proposal set status = ? where proposal_no = ?";
            return jdbcTemplate.update(updateSql, status, proposalNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getAllCombinedDetails(String userType, String approved) {
        try {
            String sql = "select combined_proposal_id, userid, proposalid, etcsproposalid, status, remarks from combined_proposal " +
                    "where status = ? and (proposaltype is null or proposaltype != ?)";

            return jdbcTemplate.queryForList(sql, "pending at " + userType, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public int updateforwardProposalCombined(String combinedProposalId, String remarks, String status, String approved) {
        try {
            String lowerDesignation = "";
            String upperDesignation = "";

            if (status.equalsIgnoreCase("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "MORD(Section Officer)";
            } else if (status.equalsIgnoreCase("MORD(Section Officer)")) {
                lowerDesignation = "MORD(Section Officer)";
                upperDesignation = "MORD(Under Secretary)";
            } else if (status.equalsIgnoreCase("MORD(Deputy Secretary)")) {
                lowerDesignation = "MORD(Under Secretary)";
                upperDesignation = "MORD(Deputy Secretary)";
            } else {
                lowerDesignation = "MORD(Deputy Secretary)";
                upperDesignation = "MORD(Additional Secretary)";
            }

// Insert remarks with parameters to avoid SQL injection
            String insertQuery = "INSERT INTO remarks (combined_proposal_id, remarks, lowerdesignation, upperdesignation) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery, combinedProposalId, remarks, lowerDesignation, upperDesignation);

            if (approved != null) {
                String updateSql = "UPDATE combined_proposal SET forwarded = true, remarks = ?, status = ? WHERE combined_proposal_id = ?";
                return jdbcTemplate.update(updateSql, remarks, "Approved", combinedProposalId);
            } else {
                String updateSql = "UPDATE combined_proposal SET forwarded = true, remarks = ?, status = ? WHERE combined_proposal_id = ?";
                String statusStr = "pending at " + status;
                return jdbcTemplate.update(updateSql, remarks, statusStr, combinedProposalId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Map<String, Object>> getCombinedDetailsBySird(String userType, String userName) {
        try {
            int userId = this.getUserId(userType, userName);

            String sqlState = "select state_name from sird where sird_id = ?";
            String stateName = jdbcTemplate.queryForList(sqlState, userId).get(0).get("state_name").toString();

            String sql = "select financialyear As \"financialYear\", installmentno As \"installmentType\", combined_proposal_id, userid, proposalid, etcsproposalid, status, remarks, forwarded " +
                    "from combined_proposal " +
                    "where userid = ? and usertype = ? and (proposaltype is null or proposaltype != ?) and (combinedstatus is null or combinedstatus != ?)";

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId, userType, "nonrecurring", "discard");

            int serialNo = 1;
            for (Map<String, Object> map : list) {
                map.put("stateName", stateName);
                map.put("serialNo", serialNo++);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> GetAllEtcsByCombinedProposalId(String combinedProposalId) {
        try {
            int i = 1;

            Map<String, Object> etcMaps = new HashMap<>();
            List<Map<String, Object>> proposallist = new ArrayList<>();

// Get etcsproposalid string safely
            String sql = "select etcsproposalid from combined_proposal where combined_proposal_id = ?";
            String etcsProposalIdStr = jdbcTemplate.queryForList(sql, Integer.parseInt(combinedProposalId)).get(0).get("etcsproposalid").toString();

            String[] etcid = etcsProposalIdStr.split(",");
            List<Integer> etcidList = Arrays.stream(etcid)
                    .map(String::trim)
                    .map(Integer::parseInt) // Convert to Integer for type matching
                    .collect(Collectors.toList());

// Create dynamic placeholders: "?, ?, ?, ..."
            String placeholders = String.join(", ", Collections.nCopies(etcidList.size(), "?"));

// Final SQL query
            String proposalSql = "SELECT financialyear AS \"financialYear\", installmentno AS \"installmentType\", " +
                    "proposal_no, proposedtotal, proposaldate, user_id, proposalid, usertype, backwarded " +
                    "FROM final_proposal WHERE proposalid IN (" + placeholders + ")";

// Execute query
            proposallist = jdbcTemplate.queryForList(proposalSql, etcidList.toArray());

            boolean flag1 = true;

            for (Map<String, Object> map : proposallist) {
                if (flag1) {
                    BigDecimal totalDemand = new BigDecimal(map.get("proposedtotal").toString());
                    etcMaps.put("demand", totalDemand);
                    flag1 = false;
                } else {
                    BigDecimal etcDemand = new BigDecimal(etcMaps.get("demand").toString());
                    BigDecimal proposalTotalDemand = new BigDecimal(map.get("proposedtotal").toString());
                    BigDecimal newDemand = etcDemand.add(proposalTotalDemand);
                    etcMaps.put("demand", newDemand);
                }

                // Query etc name using parameterized query
                String etcSql = "select \"Name\" as name from etc where etc_id = ?";
                Integer id = Integer.parseInt(map.get("user_id").toString());
                String name = jdbcTemplate.queryForList(etcSql, id).get(0).get("name").toString();

                map.put("Name", name);
                map.put("serialNo", i++);
            }

            if (!etcMaps.isEmpty()) {
                proposallist.add(etcMaps);
            }

            return proposallist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getSirdDetailsbyCombinedId(String combinedProposalId) {
        try {
            String sql = "select proposalid, userid from combined_proposal where combined_proposal_id = ?";
            int combinedProposalIdInt = Integer.parseInt(combinedProposalId.toString());
            List<Map<String, Object>> combinedList = jdbcTemplate.queryForList(sql, combinedProposalIdInt);

            if (combinedList.isEmpty()) {
                return Collections.emptyList();
            }

            int userId = (int) combinedList.get(0).get("userid");
            int proposalId = Integer.parseInt(combinedList.get(0).get("proposalid").toString());

            String sql2 = "select \"Name\" as name from sird where sird_id = ?";
            String name = jdbcTemplate.queryForList(sql2, userId).get(0).get("name").toString();

            String proposalSql = "select financialyear As \"financialYear\", installmentno As \"installmentType\", proposalid, proposaldate, proposal_no, proposedtotal, user_id, usertype, backwarded " +
                    "from final_proposal " +
                    "where usertype = 'sird' and user_id = ? and proposalid = ?";
            List<Map<String, Object>> proposallist = jdbcTemplate.queryForList(proposalSql, userId, proposalId);

            for (Map<String, Object> map : proposallist) {
                map.put("name", name);
                map.put("serialNo", 1);
            }

            return proposallist;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> checkCombinedProposal(String userName, String userType) {
        try {
            int userId = this.getUserId(userName, userType);
            String sql = "select proposalid from final_proposal where user_id='" + userId + "' and usertype='" + "sird" + "'";


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> BackwardCombinedProposalByDs(String userName, String userType) {
        try {
            int userId = this.getUserId(userType, userName);
            String sql = "SELECT combined_proposal_id, remarks, status FROM combined_proposal WHERE userid = ? AND status = ?";
            return jdbcTemplate.queryForList(sql, userId, "Backward to sird");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public Map<String, Object> getCombinedPropoalBoolean(String userType, String userName, String proposalType) {
        try {
            boolean backward = false;
            boolean forward = false;
            Map<String, Object> map = new HashMap<>();

            int userId = this.getUserId(userType, userName);

// Parameterized query for remarks
            String remarksSql = "select * from remarks where status = ? and sirdid = ?";
            List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(remarksSql, "backward to sird", userId);
            if (remarksList != null && !remarksList.isEmpty()) {
                backward = true;
            }

            if (proposalType.equalsIgnoreCase("recurring")) {
                String sql = "select combined_proposal_id, proposaldate, status from combined_proposal where userid = ? and proposaltype is null and (combinedstatus is null or combinedstatus != ?)";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId, "discard");
                if (list != null && !list.isEmpty()) {
                    forward = true;
                }
            } else {
                String sql = "select combined_proposal_id, proposaldate, status from combined_proposal where userid = ? and proposaltype = ? and (combinedstatus is null or combinedstatus != ?)";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId, "nonrecurring", "discard");
                if (list != null && !list.isEmpty()) {
                    forward = true;
                }
            }

            map.put("backward", backward);
            map.put("forward", forward);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>();
    }

    public int updateforwardProposalCombinedByDs(String combinedProposalId, String remarks, String status) {
        try {
            String proposalSql = "select proposalid, etcsproposalid from combined_proposal where combined_proposal_id = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(proposalSql, combinedProposalId);

            for (Map<String, Object> map : list) {
                String updateMain = "update final_proposal set backwarded = true where proposalid = ?";
                int propsalIdint = (int) map.get("proposalid");
                jdbcTemplate.update(updateMain, propsalIdint);

                if (map.get("etcsproposalid") != null) {
                    String etcproposal = map.get("etcsproposalid").toString();
                    if (etcproposal.contains(",")) {
                        String[] str = etcproposal.split(",");
                        String updateEtc = "update final_proposal set backwarded = true where proposalid = ?";
                        for (String proposalid : str) {
                            jdbcTemplate.update(updateEtc, proposalid.trim());
                        }
                    } else {
                        String updateSingleEtc = "update final_proposal set backwarded = true where proposalid = ?";
                        jdbcTemplate.update(updateSingleEtc, map.get("etcsproposalid"));
                    }
                }
            }

            String combinedUpdate = "update combined_proposal set forwarded = true, remarks = ?, status = ? where combined_proposal_id = ?";
            String statusText = "Backward to " + status;
            return jdbcTemplate.update(combinedUpdate, remarks, statusText, combinedProposalId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    public String addCombinedProposalNew(Map<String, Object> map) {
        try {
            StringBuilder proposalId = new StringBuilder();
            StringBuilder ids = new StringBuilder();
            StringBuilder sirdProposalids = new StringBuilder();
            StringBuilder proposalids = new StringBuilder();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            int userId1 = this.getUserId(map);
            String userId = String.valueOf(this.getUserId(map));

            String remarksSql = "select * from remarks where status = ? and sirdid = ? and financialyear = ? and installmentno = ?";
            List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(remarksSql,
                    "backward to sird", userId, map.get("financialYear"), map.get("installmentType"));
            if (remarksList != null && !remarksList.isEmpty()) {
                String updateRemarksSql = "update remarks set status = ? where sirdid = ? and status = ? and financialyear = ?";
                jdbcTemplate.update(updateRemarksSql, "create again combined proposal", userId, "backward to sird", map.get("financialYear"));
            }

            String combinedProposalCheckSql = "select * from combined_proposal where userid = ? and proposaltype is null and (combinedstatus is null or combinedstatus != ?) and installmentno = ? and financialYear = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(combinedProposalCheckSql,
                    userId1, "discard", map.get("installmentType"), map.get("financialYear"));

            if (list != null && !list.isEmpty()) {
                return "Combined Proposal Exist";
            }

            String proposalSirdSql = "select proposal_no, user_id, usertype, proposalid, installmentno from final_proposal where usertype = ? and user_id = ? and financialyear = ? and installmentno = ?";
            List<Map<String, Object>> sirdList = jdbcTemplate.queryForList(proposalSirdSql, "sird", userId1, map.get("financialYear"), map.get("installmentType"));

            if (sirdList == null || sirdList.isEmpty()) {
                return "Please Create Sird Proposal First";
            }

            String stateName = map.get("stateName").toString();

            if (stateName.equalsIgnoreCase("Bihar") || stateName.equalsIgnoreCase("Goa") ||
                    stateName.equalsIgnoreCase("Gujarat") || stateName.equalsIgnoreCase("Jharkhand") ||
                    stateName.equalsIgnoreCase("Sikkim") || stateName.equalsIgnoreCase("Tripura") ||
                    stateName.equalsIgnoreCase("Karnataka") || stateName.equalsIgnoreCase("Arunachal Pradesh") ||
                    stateName.equalsIgnoreCase("Haryana")) {
//                String forwardSql = "select proposalid from forward_proposal where sird_id = ? and status = ? and financialyear = ? and installmentno = ?";
//                List<Map<String, Object>> forwardList = jdbcTemplate.queryForList(forwardSql, userId1, "Add to combined", map.get("financialYear"), map.get("installmentType"));
//                if (forwardList == null || forwardList.isEmpty()) {
                proposalId.append("0");
                ids.append("0");
                proposalids.append("0");
//                }
//                else{
//                    for (Map<String, Object> forwardMap : forwardList) {
//                        if (flag) {
//                            proposalId.append("'").append(forwardMap.get("proposalid").toString()).append("'");
//                            flag = false;
//                        } else {
//                            proposalId.append(",'").append(forwardMap.get("proposalid").toString()).append("'");
//                        }
//                    }
//                    String proposalSql = "select proposal_no, user_id, userType, proposalid from final_proposal where proposalid in (" + proposalId.toString() + ")";
//                    List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposalSql);
//
//                    for (Map<String, Object> proposalMap : proposalList) {
//                        if (flag1) {
//                            ids.append(proposalMap.get("user_id").toString());
//                            flag1 = false;
//                        } else {
//                            ids.append(",").append(proposalMap.get("user_id").toString());
//                        }
//
//                        if (flag2) {
//                            proposalids.append(proposalMap.get("proposalid").toString());
//                            flag2 = false;
//                        } else {
//                            proposalids.append(",").append(proposalMap.get("proposalid").toString());
//                        }
//                    }
//
//                }

                String proposalNewSql = "select proposal_no, user_id, usertype, proposalid from final_proposal where usertype = ? and user_id = ? and financialyear = ? and installmentno = ?";
                List<Map<String, Object>> proposalListNew = jdbcTemplate.queryForList(proposalNewSql, "sird", userId1, map.get("financialYear"), map.get("installmentType"));

                for (Map<String, Object> proposalNewMap : proposalListNew) {
                    if (flag3) {
                        sirdProposalids.append(proposalNewMap.get("proposalid").toString());
                        flag3 = false;
                    } else {
                        sirdProposalids.append(",").append(proposalNewMap.get("proposalid").toString());
                    }
                }

            } else {
                String forwardSql = "select proposalid from forward_proposal where sird_id = ? and status = ? and financialyear = ? and installmentno = ?";
                List<Map<String, Object>> forwardList = jdbcTemplate.queryForList(forwardSql, userId1, "Add to combined", map.get("financialYear"), map.get("installmentType"));

                if (forwardList == null || forwardList.isEmpty()) {
                    return "Please Create Etc Proposal First";
                }

                for (Map<String, Object> forwardMap : forwardList) {
                    if (flag) {
                        proposalId.append("'").append(forwardMap.get("proposalid").toString()).append("'");
                        flag = false;
                    } else {
                        proposalId.append(",'").append(forwardMap.get("proposalid").toString()).append("'");
                    }
                }

                String proposalSql = "select proposal_no, user_id, userType, proposalid from final_proposal where proposalid in (" + proposalId + ")";
                List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposalSql);

                for (Map<String, Object> proposalMap : proposalList) {
                    if (flag1) {
                        ids.append(proposalMap.get("user_id").toString());
                        flag1 = false;
                    } else {
                        ids.append(",").append(proposalMap.get("user_id").toString());
                    }

                    if (flag2) {
                        proposalids.append(proposalMap.get("proposalid").toString());
                        flag2 = false;
                    } else {
                        proposalids.append(",").append(proposalMap.get("proposalid").toString());
                    }
                }

                String proposalNewSql = "select proposal_no, user_id, usertype, proposalid from final_proposal where usertype = ? and user_id = ? and financialyear = ? and installmentno = ?";
                List<Map<String, Object>> proposalListNew = jdbcTemplate.queryForList(proposalNewSql, "sird", userId1, map.get("financialYear"), map.get("installmentType"));

                for (Map<String, Object> proposalNewMap : proposalListNew) {
                    if (flag3) {
                        sirdProposalids.append(proposalNewMap.get("proposalid").toString());
                        flag3 = false;
                    } else {
                        sirdProposalids.append(",").append(proposalNewMap.get("proposalid").toString());
                    }
                }
            }

            String insertSql = "insert into combined_proposal (userid, usertype, proposalid, etcids, status, etcsproposalid, financialyear, installmentno) values (?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, userId1, map.get("key"), sirdProposalids.toString(), ids.toString(), "Generated", proposalids.toString(), map.get("financialYear"), map.get("installmentType"));

            String updateEtcSql = "update forward_proposal set combined = ? where sird_id = ? and financialyear = ? and installmentno = ?";
            jdbcTemplate.update(updateEtcSql, true, userId1, map.get("financialYear"), map.get("installmentType"));

            String updateSirdSql = "update final_proposal set combined = ? where user_id = ? and financialyear = ? and installmentno = ?";
            jdbcTemplate.update(updateSirdSql, true, userId1, map.get("financialYear"), map.get("installmentType"));

        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "addCombinedProposalNew");
            return "Not Generate Combined";
        }
        return "Generate Combined";
    }


    public List<Map<String, Object>> GetRemarks(String proposalId, String userName, String userType, String backwarded,
                                                String financialYear, String installmentType) {
        try {
            boolean flag = true;
            StringBuilder str = new StringBuilder();
            int proposalIdint = Integer.parseInt(proposalId);
            if (backwarded != null && backwarded.equalsIgnoreCase("backwarded")) {
                int i = 1;
                List<Map<String, Object>> remarks = new ArrayList<Map<String, Object>>();
                String sql = "select status,combined_proposal_id as combinedproposalid,remarks,lowerdesignation as fromuser,upperdesignation as touser,forwardeddate from remarks where combined_proposal_id = ? order by id";
                remarks = jdbcTemplate.queryForList(sql, proposalIdint);

                for (Map<String, Object> remarksMap : remarks) {
                    remarksMap.put("serialNo", i++);
                }
                return remarks;
            } else {
                int i = 1;
                int userId = 0;
                if (userType.equalsIgnoreCase("sird")) {
                    userId = this.getUserId(userType, userName);
                } else {
                    String sql = "select sirdid from remarks where combined_proposal_id = ?";
                    List<Map<String, Object>> listremarks = new ArrayList<Map<String, Object>>();
                    listremarks = jdbcTemplate.queryForList(sql, proposalIdint);
                    if (listremarks != null && listremarks.size() != 0) {
                        userId = Integer.parseInt(listremarks.get(0).get("sirdid").toString());
                    }
                }

                List<Map<String, Object>> remarks = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> CombinedProposal = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> previousRemarksList = new ArrayList<Map<String, Object>>();

                String sql = "select status,combined_proposal_id as combinedproposalid,remarks,lowerdesignation as fromuser,upperdesignation as touser,forwardeddate from remarks where combined_proposal_id = ? order by id";

                remarks = jdbcTemplate.queryForList(sql, proposalIdint);

                for (Map<String, Object> remarksMap : remarks) {
                    remarksMap.put("serialNo", i++);
                }

                String combinedProposalSql = "select combined_proposal_id from combined_proposal where proposaltype is null and combinedstatus = ? and userid = ? and financialyear = ? and installmentno = ?";
                CombinedProposal = jdbcTemplate.queryForList(combinedProposalSql, "discard", userId, financialYear, installmentType);

                if (CombinedProposal != null && CombinedProposal.size() != 0) {
                    for (Map<String, Object> map : CombinedProposal) {
                        if (flag) {
                            str.append("?");
                            flag = false;
                        } else {
                            str.append(",?");
                        }
                    }

                    List<Object> params = CombinedProposal.stream()
                            .map(map -> map.get("combined_proposal_id"))
                            .collect(Collectors.toList());

                    String previousRemarks = "select status,combined_proposal_id as combinedproposalid,remarks,lowerdesignation as fromuser,upperdesignation as touser,forwardeddate from remarks where combined_proposal_id in (" + str + ") order by id";
                    previousRemarksList = jdbcTemplate.queryForList(previousRemarks, params.toArray());
                }

                if (previousRemarksList != null && previousRemarksList.size() != 0) {
                    for (Map<String, Object> map : previousRemarksList) {
                        map.put("serialNo", i++);
                        remarks.add(map);
                    }
                } else {
                    return remarks;
                }
                return remarks;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public int updateSanctioned(Map<String, Object> map) {
        try {
            // int user_id = this.getUserId2(map);
            @SuppressWarnings("unchecked")
            Map<String, Object> actualEligible = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("actualEligible");
            @SuppressWarnings("unchecked")
            Map<String, Object> proposedSanction = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("proposedSanction");

            @SuppressWarnings("unchecked")
            Map<String, Object> extraKey = (Map<String, Object>) map.get("expenditureDetail");
            // Assuming there's a proposal_id to identify which record to update
            int proposalIdint = (int) map.get("proposalid");
            BigDecimal unspentBalance = new BigDecimal(String.valueOf(extraKey.get("unspentBalance")));
            BigDecimal trench = new BigDecimal(String.valueOf(extraKey.get("trench")));
            BigDecimal netPaybleAmount = new BigDecimal(String.valueOf(extraKey.get("netPaybleAmount")));
            String updateQry = "UPDATE proposal_sanction SET "
                    + "actualtrainingeligible = ?, "
                    + "actualmealeligible = ?, "
                    + "actualtraveleligible = ?, "
                    + "actualpurchaseeligible = ?, "
                    + "actualsupplyeligible = ?, "
                    + "actualofficeeligible = ?, "
                    + "actualnonteachingeligible = ?, "
                    + "actualsalaryofcorefaculty = ?, "
                    + "actualeligibletotaleligible = ?, "
                    + "proposedtrainingsanction = ?, "
                    + "proposedmealsanction = ?, "
                    + "proposedtravelsanction = ?, "
                    + "proposedpurchasesanction = ?, "
                    + "proposedsupplysanction = ?, "
                    + "proposedofficesanction = ?, "
                    + "proposednonteachingsanction = ?, "
                    + "proposedsalaryofcorefacultysanction = ?, "
                    + "proposedtotalsanction = ?, "
                    + "unspentbalance = ?, "
                    + "trench = ?, "
                    + "netpaybleamount = ? "
                    + "WHERE proposalid = ?";

            return jdbcTemplate.update(updateQry,
                    actualEligible.get("actualTrainingEligible"),
                    actualEligible.get("actualMealEligible"),
                    actualEligible.get("actualTravelEligible"),
                    actualEligible.get("actualPurchaseEligible"),
                    actualEligible.get("actualSupplyEligible"),
                    actualEligible.get("actualOfficeEligible"),
                    actualEligible.get("actualNonTeachingEligible"),
                    actualEligible.get("actualSalaryOfCoreFaculty"),
                    actualEligible.get("actualEligibleTotalEligible"),
                    proposedSanction.get("proposedTrainingSanction"),
                    proposedSanction.get("proposedMealSanction"),
                    proposedSanction.get("proposedTravelSanction"),
                    proposedSanction.get("proposedPurchaseSanction"),
                    proposedSanction.get("proposedSupplySanction"),
                    proposedSanction.get("proposedOfficeSanction"),
                    proposedSanction.get("proposedNonTeachingSanction"),
                    proposedSanction.get("proposedSalaryOfCoreFacultySanction"),
                    proposedSanction.get("proposedTotalSanction"),
                    unspentBalance,
                    trench,
                    netPaybleAmount,
                    proposalIdint
            );


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public String updateProposal(Map<String, Object> map) {
        try {
            BigDecimal actualSalaryOfCoreFaculty = BigDecimal.ZERO;
            BigDecimal proposedSalaryOfCoreFaculty = BigDecimal.ZERO;
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadFile = (Map<String, Object>) map.get("uploadFile");
            @SuppressWarnings("unchecked")
            Map<String, Object> actualExpenditure = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("actualExpenditureIncurredInLastFy");
            @SuppressWarnings("unchecked")
            Map<String, Object> proposedExpenditure = (Map<String, Object>) ((Map<String, Object>) map
                    .get("expenditureDetail")).get("proposedExpenditureToBeIncurredForTheFY");

            @SuppressWarnings("unchecked")
            Map<String, Object> sirdInformation = (Map<String, Object>) map.get("sirdInformation");

            @SuppressWarnings("unchecked")
            Map<String, Object> demographicProfile = (Map<String, Object>) map.get("demographicProfile");

            @SuppressWarnings("unchecked")
            Map<String, Object> instituteInfo = (Map<String, Object>) map.get("instituteInfo");
            BigDecimal fundsReleasedbyMord = new BigDecimal(String.valueOf(instituteInfo.get("fundsReleasedbyMord")));
            BigDecimal otherResources = new BigDecimal(String.valueOf(instituteInfo.get("otherResources")));
            BigDecimal mordAndStateShare = new BigDecimal(String.valueOf(instituteInfo.get("mordAndStateShare")));
            BigDecimal fundsFromVariousSources = new BigDecimal(String.valueOf(instituteInfo.get("fundsFromVariousSources")));

            // Assuming there's a proposal_id to identify which record to update
            String proposalId = map.get("proposalId").toString();
            int proposalIdint = Integer.parseInt(map.get("proposalId").toString());

            BigDecimal actualTraining = new BigDecimal(String.valueOf(actualExpenditure.get("actualTraining")));
            BigDecimal actualMeal = new BigDecimal(String.valueOf(actualExpenditure.get("actualMeal")));
            BigDecimal actualTravel = new BigDecimal(String.valueOf(actualExpenditure.get("actualTravel")));
            BigDecimal actualPurchase = new BigDecimal(String.valueOf(actualExpenditure.get("actualPurchase")));
            BigDecimal actualSupply = new BigDecimal(String.valueOf(actualExpenditure.get("actualSupply")));
            BigDecimal actualOffice = new BigDecimal(String.valueOf(actualExpenditure.get("actualOffice")));
            BigDecimal actualNonTeaching = new BigDecimal(String.valueOf(actualExpenditure.get("actualNonTeaching")));

            if (actualExpenditure.get("actualSalaryOfCoreFaculty") != null) {
                actualSalaryOfCoreFaculty = new BigDecimal(String.valueOf(actualExpenditure.get("actualSalaryOfCoreFaculty")));
            }
            BigDecimal actualTotal = new BigDecimal(String.valueOf(actualExpenditure.get("actualTotal")));
            BigDecimal proposedTraining = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTraining")));
            BigDecimal proposedMeal = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedMeal")));
            BigDecimal proposedTravel = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTravel")));
            BigDecimal proposedPurchase = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedPurchase")));
            BigDecimal proposedSupply = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedSupply")));
            BigDecimal proposedOffice = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedOffice")));
            BigDecimal proposedNonTeaching = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedNonTeaching")));

            if (proposedExpenditure.get("proposedSalaryOfCoreFaculty") != null) {

                proposedSalaryOfCoreFaculty = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedSalaryOfCoreFaculty")));
            }
            BigDecimal proposedTotal = new BigDecimal(String.valueOf(proposedExpenditure.get("proposedTotal")));
            BigDecimal totalExpenditure = actualTotal.add(proposedTotal);

            String updateQry = "UPDATE final_proposal SET " + "actualTraining = ?, " + "actualMeal = ?, " + "actualTravel = ?, " + "actualPurchase = ?, " + "actualSupply = ?, " + "actualOffice = ?, " + "actualNonTeaching = ?, " + "actualSalaryOfCoreFaculty = ?, " + "actualTotal = ?, " + "proposedTraining = ?, " + "proposedMeal = ?, " + "proposedTravel = ?, " + "proposedPurchase = ?, " + "proposedSupply = ?, " + "proposedOffice = ?, " + "proposedNonTeaching = ?, " + "proposedSalaryOfCoreFaculty = ?, " + "proposedTotal = ?, " + "totaldemand = ?, " + "fundsreleasedbymord = ?, " + "otherresources = ?, " + "mordandstateshare = ?, " + "fundsfromvarioussources = ? " + "WHERE proposalid = ?";

            jdbcTemplate.update(updateQry, actualTraining, actualMeal, actualTravel, actualPurchase, actualSupply, actualOffice, actualNonTeaching, actualSalaryOfCoreFaculty, actualTotal, proposedTraining, proposedMeal, proposedTravel, proposedPurchase, proposedSupply, proposedOffice, proposedNonTeaching, proposedSalaryOfCoreFaculty, proposedTotal, totalExpenditure, fundsReleasedbyMord, otherResources, mordAndStateShare, fundsFromVariousSources, proposalIdint);


            String updateQry1 = "UPDATE upload_file SET " + "utilzationcertificate = ?, " + "participantstrained = ?, " + "attendancesheet = ?, " + "auditreportofthelastfY = ?, " + "certificatefromauditor = ?, " + "undertakingfromheadofsird = ?, " + "noembezzlement = ?, " + "continuationorderofcorefacultymembers = ?, " + "remittancefilename = ?, " + "expenditurebillfilename = ? " + "WHERE proposalid = ?";

            jdbcTemplate.update(updateQry1, uploadFile.get("utilzationCertificate"), uploadFile.get("participantsTrained"), uploadFile.get("attendanceSheet"), uploadFile.get("auditReportofthelastFY"), uploadFile.get("certificateFromAuditor"), uploadFile.get("underTakingFromHeadOfSIRD"), uploadFile.get("noEmbezzlement"), uploadFile.get("ContinuationOrderOfCoreFacultyMembers"), uploadFile.get("remittanceFileName"), uploadFile.get("expenditureBillFileName"), proposalId);


        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "updateProposal");
            return "Not Update";
        }
        return "Update Successfully";
    }


    public List<Map<String, Object>> proposalCountRec(String status, String userType) {
        try {
            String sqlCount = "SELECT status, sirdname, COUNT(*) AS count "
                    + "FROM final_proposal "
                    + "WHERE usertype = ? AND status = ? "
                    + "GROUP BY status, sirdname";

            List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sqlCount, userType, status);

            return queryForList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> proposalCountNonRec(String status, String userType) {
        try {
            String sqlCount = "SELECT status, sirdname, COUNT(*) AS count "
                    + "FROM nonrecurring_proposal "
                    + "WHERE usertype = ? AND status = ? "
                    + "GROUP BY status, sirdname";

            List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sqlCount, userType, status);
            return queryForList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    @Transactional
    public String addCombinedProposalNewNonRecuring(Map<String, Object> map) {
        try {
            StringBuilder proposalId = new StringBuilder();
            StringBuilder ids = new StringBuilder();
            StringBuilder sirdProposalids = new StringBuilder();
            StringBuilder proposalids = new StringBuilder();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            int userId = this.getUserId(map);
            String userIdString = String.valueOf(userId);

            // 1. Fetch remarks
            String remarks = "SELECT * FROM remarks_non_recurring WHERE status = ? AND sirdid = ? AND financialyear = ?";
            List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(remarks, "backward to sird", userIdString, map.get("financialYear"));

            if (remarksList != null && !remarksList.isEmpty()) {
                String update = "UPDATE remarks_non_recurring SET status = ? WHERE sirdid = ? AND status = ? AND financialyear = ?";
                jdbcTemplate.update(update, "create again combined proposal", userIdString, "backward to sird", map.get("financialYear"));
            }

            // 2. Check existing combined proposal
            String sql = "SELECT * FROM combined_proposal WHERE userid = ? AND proposaltype = ? AND (combinedstatus IS NULL OR combinedstatus != ?) AND financialyear = ?";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userId, "nonrecurring", "discard", map.get("financialYear"));

            if (list != null && !list.isEmpty()) {
                return "Combined Proposal Exist";
            }

            // 3. SIRD check for non-Himachal states
            if (!map.get("stateName").toString().equalsIgnoreCase("Himachal Pradesh")) {
                String proposalSird = "SELECT proposal_no, user_id, usertype, proposalid FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ? AND financialyear = ?";
                List<Map<String, Object>> sirdList = jdbcTemplate.queryForList(proposalSird, "sird", userId, map.get("financialYear"));

                if (sirdList == null || sirdList.isEmpty()) {
                    return "Create Sird Proposal First";
                }
            }

            // 4. Special state logic
            List<String> specialStates = Arrays.asList("Bihar", "Goa", "Gujarat", "Jharkhand", "Sikkim", "Tripura", "Arunachal Pradesh", "Haryana");

            if (specialStates.contains(map.get("stateName").toString())) {
                proposalId.append(0);
                ids.append(0);
                proposalids.append(0);

                String proposalNew = "SELECT user_id, usertype, proposalid FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ? AND financialyear = ?";
                List<Map<String, Object>> proposalListNew = jdbcTemplate.queryForList(proposalNew, "sird", userId, map.get("financialYear"));

                for (Map<String, Object> proposalNewMap : proposalListNew) {
                    if (flag3) {
                        sirdProposalids.append(proposalNewMap.get("proposalid").toString());
                        flag3 = false;
                    } else {
                        sirdProposalids.append(",").append(proposalNewMap.get("proposalid").toString());
                    }
                }
            } else {
                // 5. Get forwarded proposals
                String forward = "SELECT proposalid FROM forward_proposal WHERE proposaltype = ? AND sird_id = ? AND status = ? AND financialyear = ?";
                List<Map<String, Object>> forwardList = jdbcTemplate.queryForList(forward, "nonrecurring", userId, "Add to combined", map.get("financialYear"));

                if (forwardList == null || forwardList.isEmpty()) {
                    return "Create Etc Proposal First";
                }

                List<Integer> proposalIds = new ArrayList<>();
                for (Map<String, Object> forwardMap : forwardList) {
                    proposalIds.add(Integer.parseInt(forwardMap.get("proposalid").toString()));
                }

// Dynamically create placeholders (?, ?, ?) for IN clause
                String placeholders = proposalIds.stream()
                        .map(id -> "?")
                        .collect(Collectors.joining(","));

                // 6. Get ETC proposal details
                String proposal = "SELECT user_id, userType, proposalid FROM nonrecurring_proposal WHERE proposalid IN (" + placeholders + ")";
                List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposal, proposalIds.toArray());

                for (Map<String, Object> proposalMap : proposalList) {
                    if (flag1) {
                        ids.append(proposalMap.get("user_id").toString());
                        flag1 = false;
                    } else {
                        ids.append(",").append(proposalMap.get("user_id").toString());
                    }

                    if (flag2) {
                        proposalids.append(proposalMap.get("proposalid").toString());
                        flag2 = false;
                    } else {
                        proposalids.append(",").append(proposalMap.get("proposalid").toString());
                    }
                }

                if (map.get("stateName").toString().equalsIgnoreCase("Himachal Pradesh")) {
                    sirdProposalids.append(0);
                } else {
                    String proposalNew = "SELECT user_id, usertype, proposalid FROM nonrecurring_proposal WHERE usertype = ? AND user_id = ? AND financialyear = ?";
                    List<Map<String, Object>> proposalListNew = jdbcTemplate.queryForList(proposalNew, "sird", userId, map.get("financialYear"));

                    for (Map<String, Object> proposalNewMap : proposalListNew) {
                        if (flag3) {
                            sirdProposalids.append(proposalNewMap.get("proposalid").toString());
                            flag3 = false;
                        } else {
                            sirdProposalids.append(",").append(proposalNewMap.get("proposalid").toString());
                        }
                    }
                }
            }

            // 7. Insert combined proposal
            String insert = "INSERT INTO combined_proposal (userid, usertype, proposalid, etcids, status, etcsproposalid, proposaltype, financialyear) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(insert,
                    userId,
                    map.get("key"),
                    sirdProposalids.toString(),
                    ids.toString(),
                    "Generated",
                    proposalids.toString(),
                    "nonrecurring",
                    map.get("financialYear"));

            // 8. Update forward_proposal
            String updateEtc = "UPDATE forward_proposal SET combined = ? WHERE sird_id = ? AND financialyear = ?";
            jdbcTemplate.update(updateEtc, true, userId, map.get("financialYear"));

            // 9. Update nonrecurring_proposal
            String updateSird = "UPDATE nonrecurring_proposal SET combined = ? WHERE user_id = ? AND financialyear = ?";
            jdbcTemplate.update(updateSird, true, userId, map.get("financialYear"));
        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "addCombinedProposalNewNonRecuring");
            return "Not Generate Combined";
        }
        return "Generate Combined";
    }


    public Map<String, Object> getCombinedPropoalBooleanNonRecurring(String userType, String userName, String proposalType) {
        try {
            boolean backward = false;
            boolean forward = false;
            Map<String, Object> map = new HashMap<>();

            int userId = this.getUserId(userType, userName);

            // Check for "backward to sird" remarks
            String remarksQuery = "SELECT * FROM remarks_non_recurring WHERE status = ? AND sirdid = ?";
            List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(remarksQuery, "backward to sird", userId);

            if (remarksList != null && !remarksList.isEmpty()) {
                backward = true;
            }

            // Check for forwardable proposals
            String sql;
            List<Map<String, Object>> list;

            if (proposalType.equalsIgnoreCase("recurring")) {
                sql = "SELECT combined_proposal_id, proposaldate, status "
                        + "FROM combined_proposal "
                        + "WHERE userid = ? AND proposaltype IS NULL "
                        + "AND (combinedstatus IS NULL OR combinedstatus != ?)";
                list = jdbcTemplate.queryForList(sql, userId, "discard");
            } else {
                sql = "SELECT combined_proposal_id, proposaldate, status "
                        + "FROM combined_proposal "
                        + "WHERE userid = ? AND proposaltype = ? "
                        + "AND (combinedstatus IS NULL OR combinedstatus != ?)";
                list = jdbcTemplate.queryForList(sql, userId, "nonrecurring", "discard");
            }

            if (list != null && !list.isEmpty()) {
                forward = true;
            }

            map.put("backward", backward);
            map.put("forward", forward);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>();
    }


    public List<Map<String, Object>> getPropopsal1() {
        try {
//        Map<String, Object> resultMap = new HashMap<>();
            String sqlCount = "SELECT count(*) as count, sirdname,usertype FROM final_proposal group by sirdname,usertype";
            List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sqlCount);
            return queryForList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getNonPropopsal1() {
        try {
//        Map<String, Object> resultMap = new HashMap<>();
            String sqlCount = "SELECT count(*) as count,sirdname,usertype FROM nonrecurring_proposal group by sirdname,usertype";
            List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sqlCount);
            return queryForList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public int GetStateCode(String userType, String userName) {
        try {
            int userId = this.getUserId1(userName, userType);

            String tableName = userType.toLowerCase();
            String columnName = tableName + "_id";
            String sql = "SELECT state_code FROM " + tableName + " WHERE " + columnName + " = ?";

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userId);

            if (result.isEmpty()) {
                throw new RuntimeException("No record found for userId: " + userId);
            }

            int stateCode = Integer.parseInt(result.get(0).get("state_code").toString());
            return stateCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getUserId1(String userName, String userType) {
        try {
            String tableName;
            String columnName;

// Determine table and column name based on userType (whitelisting)
            if (userType.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
                columnName = "etc_id";
            } else if (userType.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
                columnName = "sird_id";
            } else if (userType.equalsIgnoreCase("ministry")) {
                tableName = "loginmaster_ministry";
                columnName = "ministry_id";
            } else {
                throw new IllegalArgumentException("Invalid userType: " + userType);
            }

// Safe parameterized query
            String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userName);

            if (result.isEmpty()) {
                throw new RuntimeException("User not found: " + userName);
            }

            return Integer.parseInt(result.get(0).get(columnName).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getCombinedDetailsBySirdNonRecurring(String userType, String userName) {
        try {
            int userId = this.getUserId(userType, userName);

// Fetch state name using parameterized query
            String sqlState = "SELECT state_name FROM sird WHERE sird_id = ?";
            String stateName = jdbcTemplate.queryForList(sqlState, userId)
                    .get(0)
                    .get("state_name")
                    .toString();

// Fetch combined proposal details safely
            String sql = "SELECT combined_proposal_id, userid, proposalid, etcsproposalid, status, " +
                    "remarks, forwarded, financialyear AS \"financialYear\" " +
                    "FROM combined_proposal " +
                    "WHERE proposaltype = ? AND userid = ? AND userType = ? " +
                    "AND (combinedstatus IS NULL OR combinedstatus != ?)";

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, "nonrecurring", userId, userType, "discard");

// Add state name to each result
            for (Map<String, Object> map : list) {
                map.put("stateName", stateName);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> GetAllCombinedListNonRecurring(String userType, String approved) {
        try {
            String status = "pending at " + userType;

            String sql = "SELECT combined_proposal_id, userid, proposalid, etcsproposalid, status, remarks " +
                    "FROM combined_proposal " +
                    "WHERE status = ? AND proposaltype = ?";

            return jdbcTemplate.queryForList(sql, status, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public int updateforwardProposalCombinedNonRecurring(String combinedProposalId, String remarks, String status, String approved) {
        try {
            String lowerDesignation = "";
            String upperDesignation = "";

            if (status.equalsIgnoreCase("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "MORD(Section Officer)";
            } else if (status.equalsIgnoreCase("MORD(Section Officer)")) {
                lowerDesignation = "MORD(Section Officer)";
                upperDesignation = "MORD(Under Secretary)";
            } else if (status.equalsIgnoreCase("MORD(Under Secretary)")) {
                lowerDesignation = "MORD(Under Secretary)";
                upperDesignation = "MORD(Deputy Secretary)";
            } else if (status.equalsIgnoreCase("MORD(Deputy Secretary)")) {
                lowerDesignation = "MORD(Deputy Secretary)";
                upperDesignation = "MORD(Additional Secretary)";
            }

            String insertQuery = "INSERT INTO remarks (combined_proposal_id, remarks, lowerdesignation, upperdesignation, proposaltype) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(insertQuery, combinedProposalId, remarks, lowerDesignation, upperDesignation, "nonrecurring");

            if (approved != null) {
                String updateSql = "UPDATE combined_proposal SET forwarded = ?, remarks = ?, status = ? WHERE combined_proposal_id = ?";
                return jdbcTemplate.update(updateSql, true, remarks, "Approved", combinedProposalId);
            } else {
                String updateSql = "UPDATE combined_proposal SET forwarded = ?, remarks = ?, status = ? WHERE combined_proposal_id = ?";
                String newStatus = "pending at " + status;
                return jdbcTemplate.update(updateSql, true, remarks, newStatus, combinedProposalId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String checkSanctionAmountExist(String proposalId) {
        try {
            String sql = "select * from proposal_sanction where proposalid=?";
            int proposalIdInt = Integer.parseInt(proposalId.toString());
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, proposalIdInt);
            if (list != null && list.size() != 0) {
                return "Sanction Record Already Exist";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<Map<String, Object>> GetAllEtcsByCombinedProposalIdNonRecurring(String combinedProposalId) {
        try {
            // Fetch etcsproposalid and etcids for the given combinedProposalId
            int combinedProposalIdInt = Integer.parseInt(combinedProposalId.toString());
            String sql = "SELECT etcsproposalid, etcids FROM combined_proposal WHERE combined_proposal_id = ? AND proposaltype = 'nonrecurring'";
            List<Map<String, Object>> etcidList = jdbcTemplate.queryForList(sql, combinedProposalIdInt);

            if (etcidList.isEmpty()) {
                return Collections.emptyList(); // No data found
            }

            StringBuilder idBuilder = new StringBuilder();
            String etcIds = null;

            boolean first = true;
            for (Map<String, Object> row : etcidList) {
                // Append etcsproposalid(s) comma separated with single quotes
                String etcsProposalId = String.valueOf(row.get("etcsproposalid"));
                if (first) {
                    idBuilder.append("'").append(etcsProposalId).append("'");
                    first = false;
                } else {
                    idBuilder.append(", '").append(etcsProposalId).append("'");
                }
                etcIds = String.valueOf(row.get("etcids")); // Assuming one row or same etcids for all rows
            }

// Query etc details
            String sql1 = String.format("SELECT etc_id, \"Name\" AS name FROM etc WHERE etc_id IN (%s)", etcIds);
            List<Map<String, Object>> etcList = jdbcTemplate.queryForList(sql1);

// Query proposals for these etcsproposalid(s)
            String proposalSql = String.format(
                    "SELECT proposaldate, user_id, proposalid, usertype, backwarded, financialYear AS \"financialYear\" " +
                            "FROM nonrecurring_proposal WHERE usertype='etc' AND proposalid IN (%s)",
                    idBuilder.toString()
            );
            List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposalSql);

// Map etc_id to name for quick lookup
            Map<String, String> etcIdNameMap = new HashMap<>();
            for (Map<String, Object> etc : etcList) {
                etcIdNameMap.put(String.valueOf(etc.get("etc_id")), String.valueOf(etc.get("name")));
            }

// Assign Name to each proposal based on matching etc_id (assuming etcsproposalid = etc_id)
            for (Map<String, Object> proposal : proposalList) {
                String proposalId = String.valueOf(proposal.get("proposalid"));
                // If etcsproposalid corresponds to proposalid, then lookup name by proposalid
                String name = etcIdNameMap.get(proposalId);
                proposal.put("Name", name != null ? name : "");
            }

            return proposalList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getSirdDetailsbyCombinedIdNonRecurring(String combinedProposalId) {
        try {
            // Fetch userid and proposalid from combined_proposal
            int proposalIdInt = Integer.parseInt(combinedProposalId.toString());

            String sql = "SELECT userid, proposalid FROM combined_proposal WHERE combined_proposal_id = ? AND proposaltype = 'nonrecurring'";
            List<Map<String, Object>> sirdMap = jdbcTemplate.queryForList(sql, proposalIdInt);

            if (sirdMap.isEmpty()) {
                return Collections.emptyList(); // no data found
            }

            String userId = sirdMap.get(0).get("userid").toString();
            int userIdInt = Integer.parseInt(sirdMap.get(0).get("userid").toString());
            String proposalId = sirdMap.get(0).get("proposalid").toString();
            int proposalIdInt1 = Integer.parseInt(sirdMap.get(0).get("proposalid").toString());

// Fetch the name from sird table
            String sql2 = "SELECT \"Name\" as name FROM sird WHERE sird_id = ?";
            List<Map<String, Object>> nameList = jdbcTemplate.queryForList(sql2, userIdInt);

            String name = nameList.isEmpty() ? "" : nameList.get(0).get("name").toString();

// Fetch proposal list from nonrecurring_proposal
            String proposalSql = "SELECT proposalid, proposaldate, user_id, usertype, backwarded, financialYear AS \"financialYear\" " +
                    "FROM nonrecurring_proposal WHERE usertype = 'sird' AND proposalid = ?";
            List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(proposalSql, proposalIdInt1);

// Add the name to each map in the proposal list
            for (Map<String, Object> map : proposalList) {
                map.put("name", name);
            }

            return proposalList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public Map<String, Object> proposalCount(String userName, String userType) {
        Map<String, Object> data = new HashMap<>();
        try {
            int userId = this.getUserId(userType, userName);
            String sql;
            if (userType.equalsIgnoreCase("etc")) {
                sql = "SELECT \"Name\" AS username FROM etc WHERE etc_id = ?";
            } else {
                sql = "SELECT \"Name\" AS username FROM sird WHERE sird_id = ?";
            }

            Map<String, Object> userMap = jdbcTemplate.queryForMap(sql, userId);
            String name = (String) userMap.get("username");

            String sqlRec = "SELECT COUNT(*) FROM final_proposal WHERE user_id = ? AND usertype = ?";
            String sqlNonRec = "SELECT COUNT(*) FROM nonrecurring_proposal WHERE user_id = ? AND usertype = ?";

            int recCount = jdbcTemplate.queryForObject(sqlRec, Integer.class, userId, userType);
            int nonRecCount = jdbcTemplate.queryForObject(sqlNonRec, Integer.class, userId, userType);

            data.put("name", name);
            data.put("recCount", recCount);
            data.put("nonRecCount", nonRecCount);

            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }


    public List<Map<String, Object>> GetRemarksNonRecurring(String proposalId, String userName, String userType, String backwarded,
                                                            String financialYear) {
        try {
            boolean flag = true;
            StringBuilder str = new StringBuilder();
            int i = 1;
            int proposalIdInt = Integer.parseInt(proposalId);
            if ("backwarded".equalsIgnoreCase(backwarded)) {
                String sql = "SELECT status, combined_proposal_id AS combinedproposalid, remarks, " + "lowerdesignation AS fromuser, upperdesignation AS touser, forwardeddate " + "FROM remarks_non_recurring WHERE combined_proposal_id = ? ORDER BY id";

                List<Map<String, Object>> remarks = jdbcTemplate.queryForList(sql, proposalIdInt);

                for (Map<String, Object> remarksMap : remarks) {
                    remarksMap.put("serialNo", i++);
                }
                return remarks;
            } else {
                int userId = 0;
                if ("sird".equalsIgnoreCase(userType)) {
                    userId = this.getUserId(userType, userName);
                } else {
                    String sqlUser = "SELECT sirdid FROM remarks_non_recurring WHERE combined_proposal_id = ?";
                    List<Map<String, Object>> listremarks = jdbcTemplate.queryForList(sqlUser, proposalIdInt);
                    if (listremarks != null && !listremarks.isEmpty()) {
                        userId = Integer.parseInt(listremarks.get(0).get("sirdid").toString());
                    }
                }

                String sqlRemarks = "SELECT status, combined_proposal_id AS combinedproposalid, remarks, " + "lowerdesignation AS fromuser, upperdesignation AS touser, forwardeddate " + "FROM remarks_non_recurring WHERE combined_proposal_id = ? ORDER BY id";
                List<Map<String, Object>> remarks = jdbcTemplate.queryForList(sqlRemarks, proposalIdInt);

                for (Map<String, Object> remarksMap : remarks) {
                    remarksMap.put("serialNo", i++);
                }

                StringBuilder sql = new StringBuilder("SELECT combined_proposal_id FROM combined_proposal WHERE proposaltype = 'nonrecurring' AND combinedstatus = 'discard'");
                List<Object> params = new ArrayList<>();
               if (userId!=0){
                   sql.append("and userid=?");
                   params.add(userId);
               }
                if (financialYear != null) {
                    sql.append(" AND financialyear = ?");
                    params.add(financialYear);
                }

                List<Map<String, Object>> combinedProposals = jdbcTemplate.queryForList(sql.toString(), params.toArray());


                if (combinedProposals != null && !combinedProposals.isEmpty()) {
                    for (Map<String, Object> map : combinedProposals) {
                        if (flag) {
                            str.append(map.get("combined_proposal_id"));
                            flag = false;
                        } else {
                            str.append(",").append(map.get("combined_proposal_id"));
                        }
                    }

                    String previousRemarksSql = "SELECT status, combined_proposal_id AS combinedproposalid, remarks, " + "lowerdesignation AS fromuser, upperdesignation AS touser, forwardeddate " + "FROM remarks_non_recurring WHERE combined_proposal_id IN (" + str + ") ORDER BY id";

                    List<Map<String, Object>> previousRemarksList = jdbcTemplate.queryForList(previousRemarksSql);

                    if (previousRemarksList != null && !previousRemarksList.isEmpty()) {
                        for (Map<String, Object> map : previousRemarksList) {
                            map.put("serialNo", i++);
                            remarks.add(map);
                        }
                    }
                }

                return remarks;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
}



