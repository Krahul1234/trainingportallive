package com.nic.trainingportal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReportDao {

    @Autowired
    private JdbcTemplate jdbctemplate;

    public List<Map<String, Object>> getEtcReport(String userName, String userType) {
        try {

            List<Map<String, Object>> reportList = new ArrayList<>();
            List<Map<String, Object>> finalList = new ArrayList<>();

            if (userName != null || userType != null) {
                Map<String, Object> etcReport = new HashMap<>();
                int userId = this.getUserId(userName, userType);

                // Fetch etc details
                String etcDetails = "SELECT name_of_etcs, state_name FROM etc WHERE etc_id = ?";
                List<Map<String, Object>> etcList = jdbctemplate.queryForList(etcDetails, userId);

                for (Map<String, Object> etcMap : etcList) {
                    String name = etcMap.get("name_of_etcs") != null ? etcMap.get("name_of_etcs").toString() : null;
                    String stateName = etcMap.get("state_name").toString();
                    etcReport.put("etcName", name);
                    etcReport.put("stateName", stateName);
                }

                // Fetch proposals for this user
                String proposalSql = "SELECT proposalid, status, proposaldate AS createdAt, financialyear, installmentno FROM final_proposal WHERE user_id = ? AND usertype = ?";
                List<Map<String, Object>> proposalList = jdbctemplate.queryForList(proposalSql, userId, userType);

                if (proposalList != null && !proposalList.isEmpty()) {
                    for (Map<String, Object> proposalMap : proposalList) {
                        etcReport.put("financialyear", proposalMap.get("financialyear"));
                        etcReport.put("installmentno", proposalMap.get("installmentno"));
                        etcReport.put("proposalNo", proposalMap.get("proposalid"));
                        etcReport.put("generated", "generated");
                        etcReport.put("serialNo", 1);
                        etcReport.put("createdAt", proposalMap.get("createdAt"));
                    }

                    String forwardProposalSql = "SELECT sird_id, status FROM forward_proposal WHERE etc_id = ?";
                    List<Map<String, Object>> forwardList = jdbctemplate.queryForList(forwardProposalSql, userId);

                    if (forwardList != null && !forwardList.isEmpty()) {
                        Map<String, Object> forwardData = forwardList.get(0);
                        String status = forwardData.get("status").toString();

                        if (status.equalsIgnoreCase("Add to combined")) {
                            Object sirdId = forwardData.get("sird_id");
                            String combinedSql = "SELECT combined_proposal_id FROM combined_proposal WHERE userid = ? AND (combinedstatus IS NULL OR combinedstatus != ?)";
                            List<Map<String, Object>> combinedList = jdbctemplate.queryForList(combinedSql, sirdId, "discard");

                            if (combinedList != null && !combinedList.isEmpty()) {
                                Object combinedProposalId = combinedList.get(0).get("combined_proposal_id");
                                if (combinedProposalId != null) {
                                    String remarksSql = "SELECT * FROM remarks WHERE combined_proposal_id = ? ORDER BY id DESC LIMIT 1";
                                    List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarksSql, combinedProposalId);

                                    if (remarksList != null && !remarksList.isEmpty()) {
                                        Map<String, Object> remarksMap = remarksList.get(0);
                                        String remarksStatus = remarksMap.get("status").toString();
                                        if (remarksStatus.contains("pending")) {
                                            etcReport.put("Status", remarksStatus.split("pending")[1]);
                                        } else {
                                            etcReport.put("Status", "approved");
                                        }
                                    } else {
                                        etcReport.put("Status", "at SIRD");
                                    }
                                } else {
                                    etcReport.put("Status", "at SIRD");
                                }
                            } else {
                                etcReport.put("Status", "at SIRD");
                            }
                        } else if (status.equalsIgnoreCase("Forwarded to sird")) {
                            etcReport.put("Status", "at SIRD");
                        } else {
                            etcReport.put("Status", "at ETC");
                        }
                    } else {
                        etcReport.put("Status", "Not Forwarded");
                    }
                } else {
                    // No proposals
                    etcReport.put("proposalNo", null);
                    etcReport.put("generated", "Not Generated");
                    etcReport.put("Status", null);
                    etcReport.put("createdAt", null);
                }
                reportList.add(etcReport);
                return reportList;

            } else {
                // No user specified, get all final proposals for usertype 'etc'
                int i = 1;
                String finalProposalSql = "SELECT proposalid, status, financialyear, installmentno, user_id, sirdname, proposaldate AS createdAt FROM final_proposal WHERE usertype = ?";
                List<Map<String, Object>> finalProposalList = jdbctemplate.queryForList(finalProposalSql, "etc");

                for (Map<String, Object> finalProposalMap : finalProposalList) {
                    if (!finalProposalMap.get("sirdname").toString().equalsIgnoreCase("Test")) {
                        String stateSql = "SELECT state_name FROM etc WHERE etc_id = ?";
                        Object userId = finalProposalMap.get("user_id");
                        String stateName = jdbctemplate.queryForList(stateSql, userId).get(0).get("state_name").toString();

                        finalProposalMap.put("financialyear", finalProposalMap.get("financialyear"));
                        finalProposalMap.put("installmentno", finalProposalMap.get("installmentno"));
                        finalProposalMap.put("etcName", finalProposalMap.get("sirdname"));
                        finalProposalMap.put("proposalNo", finalProposalMap.get("proposalid"));
                        finalProposalMap.put("stateName", stateName);

                        String status = finalProposalMap.get("status").toString();
                        if (status.equalsIgnoreCase("Add to combined")) {
                            String forwardProposalSql = "SELECT sird_id FROM forward_proposal WHERE etc_id = ?";
                            Object userId1 = finalProposalMap.get("user_id");
                            Object sirdId = jdbctemplate.queryForList(forwardProposalSql, userId1).get(0).get("sird_id");

                            String combinedProposalSql = "SELECT combined_proposal_id FROM combined_proposal WHERE userid = ? AND (combinedstatus IS NULL OR combinedstatus != ?)";
                            List<Map<String, Object>> combinedList = jdbctemplate.queryForList(combinedProposalSql, sirdId, "discard");

                            if (combinedList != null && !combinedList.isEmpty()) {
                                Object combinedProposalId = combinedList.get(0).get("combined_proposal_id");

                                String remarksSql = "SELECT * FROM remarks WHERE combined_proposal_id = ? ORDER BY id DESC LIMIT 1";
                                List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarksSql, combinedProposalId);

                                if (remarksList != null && !remarksList.isEmpty()) {
                                    Map<String, Object> remarksMap = remarksList.get(0);
                                    String remarksStatus = remarksMap.get("status").toString();
                                    if (remarksStatus.contains("pending")) {
                                        finalProposalMap.put("Status", remarksStatus.split("pending")[1]);
                                    } else {
                                        finalProposalMap.put("Status", "approved");
                                    }
                                } else {
                                    finalProposalMap.put("Status", "at SIRD");
                                }
                            } else {
                                finalProposalMap.put("Status", "at SIRD");
                            }
                        } else if (status.equalsIgnoreCase("Forwarded to sird")) {
                            finalProposalMap.put("Status", "At SIRD");
                        } else if (status.equalsIgnoreCase("Backward to etc")) {
                            finalProposalMap.put("Status", "at ETC");
                        } else {
                            finalProposalMap.put("Status", "Not Forwarded");
                        }

                        finalProposalMap.put("serialNo", i++);
                        finalProposalMap.remove("sirdname");
                        finalProposalMap.put("generated", "generated");
                        finalProposalMap.remove("proposalid");
                        finalList.add(finalProposalMap);
                    }
                }
                return finalList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public int getUserId(String userName, String userType) {
        try {

            String tableName;
            String idColumn;

            // Determine table and column names based on userType
            if ("etc".equalsIgnoreCase(userType)) {
                tableName = "loginmaster_etc";
                idColumn = "etc_id";
            } else if ("sird".equalsIgnoreCase(userType)) {
                tableName = "loginmaster_sird";
                idColumn = "sird_id";
            } else {
                tableName = "loginmaster_ministry";
                idColumn = "min_id";
            }

            // Prepare SQL query using parameters
            String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE username = ?";

            List<Map<String, Object>> result = jdbctemplate.queryForList(sql, userName);
            if (result.isEmpty()) {
                throw new RuntimeException("User not found with username: " + userName);
            }

            Object idObj = result.get(0).get(idColumn);
            if (idObj == null) {
                throw new RuntimeException("User ID not found for username: " + userName);
            }

            return Integer.parseInt(idObj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getreportForSird(String userName, String userType) {
        List<Map<String, Object>> details = new ArrayList<>();
        int serialNo = 1;

        try {
            List<Map<String, Object>> sirdList;
            if (userName == null || userType == null) {
                String sirdSql = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird";
                sirdList = jdbctemplate.queryForList(sirdSql);
            } else {
                int userId = this.getUserId(userName, userType);
                String sirdSql = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird WHERE sird_id = ?";
                sirdList = jdbctemplate.queryForList(sirdSql, userId);
            }

            for (Map<String, Object> sirdMap : sirdList) {
                String sirdName = sirdMap.get("sirdName").toString();
                if (!"Test".equalsIgnoreCase(sirdName)) {
                    Map<String, Object> sirdDetail = new HashMap<>();

                    String proposalSql = "SELECT proposalid, status, financialyear, installmentno, proposaldate AS \"createdAt\" "
                            + "FROM final_proposal WHERE user_id = ? AND usertype = 'sird'";
                    Object sirdId = sirdMap.get("sird_id");
                    List<Map<String, Object>> proposals = jdbctemplate.queryForList(proposalSql, sirdId);

                    if (!proposals.isEmpty()) {
                        // Assuming you want the last or first proposal, adjust if multiple needed
                        for (Map<String, Object> proposal : proposals) {
                            sirdDetail.put("financialyear", proposal.get("financialyear"));
                            sirdDetail.put("installmentno", proposal.get("installmentno"));

                            String combinedSql = "SELECT combined_proposal_id FROM combined_proposal WHERE proposalid = ? "
                                    + "AND (combinedstatus IS NULL OR combinedstatus != 'discard')";
                            String proposalid = proposal.get("proposalid").toString();
                            List<Map<String, Object>> combinedList = jdbctemplate.queryForList(combinedSql, proposalid);

                            if (!combinedList.isEmpty()) {
                                for (Map<String, Object> combined : combinedList) {
                                    Object combinedProposalId = combined.get("combined_proposal_id");

                                    String remarksSql = "SELECT * FROM remarks WHERE combined_proposal_id = ? ORDER BY id DESC LIMIT 1";
                                    List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarksSql, combinedProposalId);

                                    if (!remarksList.isEmpty()) {
                                        Map<String, Object> remarks = remarksList.get(0);
                                        String status = remarks.get("status").toString();

                                        if (status.contains("pending")) {
                                            sirdDetail.put("Status", status.split("pending")[1]);
                                        } else if (status.contains("backward")) {
                                            sirdDetail.put("Status", status);
                                        } else if ("MORD(Deputy Secretary)".equals(remarks.get("upperdesignation"))
                                                && "approved".equals(status)) {
                                            sirdDetail.put("Status", "approved");
                                        }
                                    } else {
                                        sirdDetail.put("Status", "Combined but not forwarded to MORD SECTION OFFICER");
                                    }
                                    sirdDetail.put("combined_id", combinedProposalId);
                                }
                            } else {
                                sirdDetail.put("Status", "At Sird");
                            }
                            sirdDetail.put("proposalno", proposal.get("proposalid"));
                            sirdDetail.put("generated", proposal.get("status"));
                            sirdDetail.put("createdAt", proposal.get("createdAt"));
                        }
                    } else {
                        sirdDetail.put("proposalno", null);
                        sirdDetail.put("generated", null);
                        sirdDetail.put("createdAt", null);
                    }

                    sirdDetail.put("serialNo", serialNo++);
                    sirdDetail.put("sirdName", sirdName);
                    sirdDetail.put("stateName", sirdMap.get("state_name"));

                    details.add(sirdDetail);
                }
            }
            return details;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public List<Map<String, Object>> getreportForMinistry(String userName, String userType) {// TODO Auto-generated method stub
        List<Map<String, Object>> detail = new ArrayList<>();
        int serialNo = 1;

        try {
            String sirdQuery = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird";
            List<Map<String, Object>> sirdDetails = jdbctemplate.queryForList(sirdQuery);

            for (Map<String, Object> sirdMap : sirdDetails) {
                if (!"Test".equalsIgnoreCase(String.valueOf(sirdMap.get("sirdName")))) {
                    Map<String, Object> sirdRecord = new HashMap<>();

                    String proposalQuery = "SELECT proposalid, status, proposaldate AS \"createdAt\", financialyear, installmentno "
                            + "FROM final_proposal WHERE user_id = ? AND usertype = 'sird'";
                    Object sirdId = sirdMap.get("sird_id");
                    List<Map<String, Object>> sirdProposals = jdbctemplate.queryForList(proposalQuery, sirdId);

                    if (!sirdProposals.isEmpty()) {
                        for (Map<String, Object> proposalMap : sirdProposals) {
                            String combinedQuery = "SELECT combined_proposal_id FROM combined_proposal WHERE proposalid = ? ORDER BY combined_proposal_id DESC LIMIT 1";
                            String proposalId = proposalMap.get("proposalid").toString();
                            List<Map<String, Object>> combinedProposals = jdbctemplate.queryForList(combinedQuery, proposalId);

                            if (!combinedProposals.isEmpty()) {
                                for (Map<String, Object> combinedMap : combinedProposals) {
                                    String remarksQuery = "SELECT id, status, remarks, lowerdesignation AS \"from\", upperdesignation AS \"to\", creation_time "
                                            + "FROM remarks WHERE combined_proposal_id = ? ORDER BY id DESC";
                                    Object combinedProposalId = combinedMap.get("combined_proposal_id");
                                    List<Map<String, Object>> remarks = jdbctemplate.queryForList(remarksQuery, combinedProposalId);

                                    if (!remarks.isEmpty()) {
                                        sirdRecord.put("remarks", remarks);
                                    } else {
                                        sirdRecord.put("Status", "Combined but not forwarded to MORD SECTION OFFICER");
                                    }
                                    sirdRecord.put("combined_id", combinedMap.get("combined_proposal_id"));
                                }
                            } else {
                                sirdRecord.put("Status", "At Sird");
                            }

                            sirdRecord.put("proposalno", proposalMap.get("proposalid"));
                            sirdRecord.put("generated", proposalMap.get("status"));
                            sirdRecord.put("createdAt", proposalMap.get("createdAt"));
                            sirdRecord.put("financialYear", proposalMap.get("financialyear"));
                            sirdRecord.put("installmentNo", proposalMap.get("installmentno"));
                        }
                    } else {
                        sirdRecord.put("proposalno", null);
                        sirdRecord.put("generated", null);
                        sirdRecord.put("createdAt", null);
                    }

                    sirdRecord.put("serialNo", serialNo++);
                    sirdRecord.put("sirdName", sirdMap.get("sirdName"));
                    sirdRecord.put("stateName", sirdMap.get("state_name"));

                    detail.add(sirdRecord);
                }
            }

            return detail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public Object reportForMinistryNonRecurring(String userName, String userType) {// TODO Auto-generated method stub
        List<Map<String, Object>> detail = new ArrayList<>();
        int serialNo = 1;

        try {
            String sirdQuery = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird";
            List<Map<String, Object>> sirdDetails = jdbctemplate.queryForList(sirdQuery);

            for (Map<String, Object> sirdMap : sirdDetails) {
                if (!"Test".equalsIgnoreCase(String.valueOf(sirdMap.get("sirdName")))) {
                    Map<String, Object> sirdRecord = new HashMap<>();

                    String proposalQuery = "SELECT proposalid, status, created_at AS createdAt, financialyear, installmentno "
                            + "FROM nonrecurring_proposal WHERE user_id = ? AND usertype = 'sird'";
                    Object sirdId = sirdMap.get("sird_id");
                    List<Map<String, Object>> sirdProposals = jdbctemplate.queryForList(proposalQuery, sirdId);

                    if (!sirdProposals.isEmpty()) {
                        for (Map<String, Object> proposalMap : sirdProposals) {
                            String combinedQuery = "SELECT combined_proposal_id FROM combined_proposal WHERE proposalid = ? ORDER BY combined_proposal_id DESC LIMIT 1";
                            String proposalId =proposalMap.get("proposalid").toString();
                            List<Map<String, Object>> combinedProposals = jdbctemplate.queryForList(combinedQuery, proposalId);

                            if (!combinedProposals.isEmpty()) {
                                for (Map<String, Object> combinedMap : combinedProposals) {
                                    String remarksQuery = "SELECT id, status, remarks, lowerdesignation AS \"from\", upperdesignation AS \"to\", creation_time "
                                            + "FROM remarks_non_recurring WHERE combined_proposal_id = ? ORDER BY id DESC";
                                    Object combinedProposalId = combinedMap.get("combined_proposal_id");
                                    List<Map<String, Object>> remarks = jdbctemplate.queryForList(remarksQuery, combinedProposalId);

                                    if (!remarks.isEmpty()) {
                                        sirdRecord.put("remarks", remarks);
                                    } else {
                                        sirdRecord.put("Status", "Combined but not forwarded to MORD SECTION OFFICER");
                                    }
                                    sirdRecord.put("combined_id", combinedMap.get("combined_proposal_id"));
                                }
                            } else {
                                sirdRecord.put("Status", "At Sird");
                            }

                            sirdRecord.put("proposalno", proposalMap.get("proposalid"));
                            sirdRecord.put("generated", proposalMap.get("status"));
                            sirdRecord.put("createdAt", proposalMap.get("createdAt"));
                            sirdRecord.put("financialYear", proposalMap.get("financialyear"));
                            sirdRecord.put("installmentNo", proposalMap.get("installmentno"));
                        }
                    } else {
                        sirdRecord.put("proposalno", null);
                        sirdRecord.put("generated", null);
                        sirdRecord.put("createdAt", null);
                    }

                    sirdRecord.put("serialNo", serialNo++);
                    sirdRecord.put("sirdName", sirdMap.get("sirdName"));
                    sirdRecord.put("stateName", sirdMap.get("state_name"));

                    detail.add(sirdRecord);
                }
            }
            return detail;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Object nonRecurringReportForSird(String userName, String userType) {
        List<Map<String, Object>> detail = new ArrayList<>();
        int i = 1;

        try {
            List<Map<String, Object>> sirdDetails;

            if (userName == null && userType == null) {
                String sirdSql = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird";
                sirdDetails = jdbctemplate.queryForList(sirdSql);
            } else {
                int userId = this.getUserId(userName, userType);
                String sirdSql = "SELECT state_name, \"Name\" AS sirdName, sird_id FROM sird WHERE sird_id = ?";
                sirdDetails = jdbctemplate.queryForList(sirdSql, userId);
            }

            for (Map<String, Object> sird : sirdDetails) {
                String sirdName = String.valueOf(sird.get("sirdName"));
                if (!"Test".equalsIgnoreCase(sirdName)) {
                    Map<String, Object> sirdD = new HashMap<>();

                    String proposalSql = "SELECT proposalid, status, financialyear, proposaldate AS createdAt "
                            + "FROM nonrecurring_proposal WHERE user_id = ? AND usertype = 'sird'";
                    Object sirdId = sird.get("sird_id");
                    List<Map<String, Object>> proposals = jdbctemplate.queryForList(proposalSql, sirdId);

                    if (!proposals.isEmpty()) {
                        for (Map<String, Object> proposal : proposals) {
                            sirdD.put("financialyear", proposal.get("financialyear"));

                            String combinedSql = "SELECT combined_proposal_id FROM combined_proposal "
                                    + "WHERE proposalid = ? AND (combinedstatus IS NULL OR combinedstatus != 'discard') AND proposaltype = 'nonrecurring'";
                            String proposalId = proposal.get("proposalid").toString();
                            List<Map<String, Object>> combinedProposals = jdbctemplate.queryForList(combinedSql, proposalId);

                            if (!combinedProposals.isEmpty()) {
                                for (Map<String, Object> combined : combinedProposals) {
                                    Object combinedId = combined.get("combined_proposal_id");
                                    String remarksSql = "SELECT * FROM remarks_non_recurring WHERE combined_proposal_id = ? "
                                            + "ORDER BY id DESC LIMIT 1";
                                    List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarksSql, combinedId);

                                    if (!remarksList.isEmpty()) {
                                        Map<String, Object> remark = remarksList.get(0);
                                        String status = String.valueOf(remark.get("status"));

                                        if (status.contains("pending")) {
                                            sirdD.put("Status", status.split("pending")[1]);
                                        } else if (status.contains("backward")) {
                                            sirdD.put("Status", status);
                                        } else if ("MORD(Deputy Secretary)".equals(remark.get("upperdesignation")) && "approved".equals(status)) {
                                            sirdD.put("Status", "approved");
                                        }
                                    } else {
                                        sirdD.put("Status", "Combined but not forwarded to MORD SECTION OFFICER");
                                    }
                                    sirdD.put("combined_id", combinedId);
                                }
                            } else {
                                sirdD.put("Status", "At Sird");
                            }
                            sirdD.put("proposalno", proposal.get("proposalid"));
                            sirdD.put("generated", proposal.get("status"));
                            sirdD.put("createdAt", proposal.get("createdAt"));
                        }
                    } else {
                        sirdD.put("proposalno", null);
                        sirdD.put("generated", null);
                        sirdD.put("createdAt", null);
                    }

                    sirdD.put("serialNo", i++);
                    sirdD.put("sirdName", sirdName);
                    sirdD.put("stateName", sird.get("state_name"));

                    detail.add(sirdD);
                }
            }
            return detail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public Object nonRecurringEtcReport(String userName, String userType) {
        List<Map<String, Object>> reportList = new ArrayList<>();
        List<Map<String, Object>> finalList = new ArrayList<>();
        try {
            if (userName != null && userType != null) {
                Map<String, Object> etcReport = new HashMap<>();
                String name = null;
                String stateName = null;
                int userId = this.getUserId(userName, userType);

                // Query ETC details
                String etcDetails = "SELECT name_of_etcs, state_name FROM etc WHERE etc_id = ?";
                List<Map<String, Object>> etcList = jdbctemplate.queryForList(etcDetails, userId);
                if (!etcList.isEmpty()) {
                    Map<String, Object> etcMap = etcList.get(0);
                    if (etcMap.get("name_of_etcs") != null) {
                        name = etcMap.get("name_of_etcs").toString();
                    }
                    stateName = etcMap.get("state_name") != null ? etcMap.get("state_name").toString() : null;
                }
                etcReport.put("etcName", name);
                etcReport.put("stateName", stateName);

                // Query proposals
                String proposal = "SELECT proposalid, status, proposaldate AS \"createdAt\", financialyear FROM nonrecurring_proposal WHERE user_id = ? AND usertype = ?";
                List<Map<String, Object>> proposalList = jdbctemplate.queryForList(proposal, userId, userType);

                if (proposalList != null && !proposalList.isEmpty()) {
                    for (Map<String, Object> proposalMap : proposalList) {
                        etcReport.put("financialyear", proposalMap.get("financialyear"));
                        etcReport.put("proposalNo", proposalMap.get("proposalid"));
                        etcReport.put("generated", "generated");
                        etcReport.put("serialNo", 1);
                        etcReport.put("createdAt", proposalMap.get("createdAt"));
                    }

                    // Query forward proposal status
                    String forwardProposal = "SELECT sird_id, status FROM forward_proposal WHERE etc_id = ? AND proposaltype = ?";
                    List<Map<String, Object>> forwardList = jdbctemplate.queryForList(forwardProposal, userId, "nonrecurring");
                    if (forwardList != null && !forwardList.isEmpty()) {
                        Map<String, Object> forwardMap = forwardList.get(0);
                        String status = forwardMap.get("status") != null ? forwardMap.get("status").toString() : null;

                        if ("Add to combined".equalsIgnoreCase(status)) {
                            Object sirdId = forwardMap.get("sird_id") != null ? forwardMap.get("sird_id") : null;
                            if (sirdId != null) {
                                String combined = "SELECT combined_proposal_id FROM combined_proposal WHERE userid = ? AND (combinedstatus IS NULL OR combinedstatus != ?) AND proposaltype = ?";
                                List<Map<String, Object>> combinedListProposal = jdbctemplate.queryForList(combined, sirdId, "discard", "nonrecurring");

                                if (combinedListProposal != null && !combinedListProposal.isEmpty()) {
                                    Object combinedProposalId = combinedListProposal.get(0).get("combined_proposal_id");
                                    if (combinedProposalId != null) {
                                        String remarks = "SELECT * FROM remarks_non_recurring WHERE combined_proposal_id = ? ORDER BY id DESC LIMIT 1";
                                        List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarks, combinedProposalId);
                                        if (remarksList != null && !remarksList.isEmpty()) {
                                            Map<String, Object> remarksMap = remarksList.get(0);
                                            String remarksStatus = remarksMap.get("status") != null ? remarksMap.get("status").toString() : "";
                                            if (remarksStatus.contains("pending")) {
                                                String[] parts = remarksStatus.split("pending", 2);
                                                etcReport.put("Status", parts.length > 1 ? parts[1] : "pending");
                                            } else {
                                                etcReport.put("Status", "approved");
                                            }
                                        } else {
                                            etcReport.put("Status", "at SIRD");
                                        }
                                    } else {
                                        etcReport.put("Status", "at SIRD");
                                    }
                                } else {
                                    etcReport.put("Status", "at SIRD");
                                }
                            } else {
                                etcReport.put("Status", "at SIRD");
                            }
                        } else if ("Forwarded to sird".equalsIgnoreCase(status)) {
                            etcReport.put("Status", "at SIRD");
                        } else {
                            etcReport.put("Status", "at ETC");
                        }
                    } else {
                        etcReport.put("Status", "Not Forwarded");
                    }

                } else {
                    etcReport.put("proposalNo", null);
                    etcReport.put("generated", "Not Generated");
                    etcReport.put("Status", null);
                    etcReport.put("createdAt", null);
                }
                reportList.add(etcReport);
                return reportList;

            } else {
                int i = 1;
                String finalProposal = "SELECT proposalid, status, financialyear, user_id, sirdname, proposaldate AS \"createdAt\" FROM nonrecurring_proposal WHERE usertype = ?";
                List<Map<String, Object>> finalProposalList = jdbctemplate.queryForList(finalProposal, "etc");

                for (Map<String, Object> finalProposalMap : finalProposalList) {
                    if (!"Test".equalsIgnoreCase(finalProposalMap.get("sirdname").toString())) {
                        // Get state name for user_id
                        String stateSql = "SELECT state_name FROM etc WHERE etc_id = ?";
                        Integer id = Integer.parseInt((String) finalProposalMap.get("user_id"));
                        List<Map<String, Object>> stateList = jdbctemplate.queryForList(stateSql, id);
                        String stateName = null;
                        if (!stateList.isEmpty()) {
                            stateName = stateList.get(0).get("state_name") != null ? stateList.get(0).get("state_name").toString() : null;
                        }
                        finalProposalMap.put("stateName", stateName);
                        finalProposalMap.put("etcName", finalProposalMap.get("sirdname"));
                        finalProposalMap.put("proposalNo", finalProposalMap.get("proposalid"));

                        String status = finalProposalMap.get("status") != null ? finalProposalMap.get("status").toString() : "";

                        if ("Add to combined".equalsIgnoreCase(status)) {
                            String forwardProposal = "SELECT sird_id FROM forward_proposal WHERE etc_id = ?";
                            Integer ids = Integer.parseInt((String) finalProposalMap.get("user_id"));
                            List<Map<String, Object>> forwardList = jdbctemplate.queryForList(forwardProposal, ids);
                            Object sirdId = null;
                            if (forwardList != null && !forwardList.isEmpty()) {
                                sirdId = forwardList.get(0).get("sird_id") != null ? forwardList.get(0).get("sird_id").toString() : null;
                            }

                            if (sirdId != null) {
                                String combinedProposal = "SELECT combined_proposal_id FROM combined_proposal WHERE userid = ? AND (combinedstatus IS NULL OR combinedstatus != ?) AND proposaltype = ?";
                                List<Map<String, Object>> combinedList = jdbctemplate.queryForList(combinedProposal, sirdId, "discard", "nonrecurring");

                                if (combinedList != null && !combinedList.isEmpty()) {
                                    Object combinedProposalId = combinedList.get(0).get("combined_proposal_id");

                                    String remarks = "SELECT * FROM remarks_non_recurring WHERE combined_proposal_id = ? ORDER BY id DESC LIMIT 1";
                                    List<Map<String, Object>> remarksList = jdbctemplate.queryForList(remarks, combinedProposalId);

                                    if (remarksList != null && !remarksList.isEmpty()) {
                                        Map<String, Object> remarksMap = remarksList.get(0);
                                        String remarksStatus = remarksMap.get("status") != null ? remarksMap.get("status").toString() : "";
                                        if (remarksStatus.contains("pending")) {
                                            String[] parts = remarksStatus.split("pending", 2);
                                            finalProposalMap.put("Status", parts.length > 1 ? parts[1] : "pending");
                                        } else {
                                            finalProposalMap.put("Status", "approved");
                                        }
                                    } else {
                                        finalProposalMap.put("Status", "at SIRD");
                                    }
                                } else {
                                    finalProposalMap.put("Status", "at SIRD");
                                }
                            } else {
                                finalProposalMap.put("Status", "at SIRD");
                            }
                        } else if ("Forwarded to sird".equalsIgnoreCase(status)) {
                            finalProposalMap.put("Status", "At SIRD");
                        } else if ("Backward to etc".equalsIgnoreCase(status)) {
                            finalProposalMap.put("Status", "at ETC");
                        } else {
                            finalProposalMap.put("Status", "Not Forwarded");
                        }

                        finalProposalMap.put("serialNo", i++);
                        finalProposalMap.remove("sirdname");
                        finalProposalMap.put("generated", "generated");
                        finalProposalMap.remove("proposalid");
                        finalList.add(finalProposalMap);
                    }
                }
                return finalList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
