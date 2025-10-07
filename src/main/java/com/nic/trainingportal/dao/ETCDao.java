package com.nic.trainingportal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ETCDao {
    @Autowired
    private JdbcTemplate jdbctemplate;

    /**
     * Add SIRD Details
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> getEtcDetails(int pageSize, int pageNumber) {
        try {
            int page = pageNumber;
            int size = pageSize;
            int offset = page * size;

            String sql = "SELECT * FROM etc ORDER BY etc_id DESC OFFSET " + offset + " LIMIT " + size;
            return jdbctemplate.queryForList(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    /**
     * Add SIRD Details
     *
     * @param map
     * @return
     */
    public int updateEtcDetails(Map<String, Object> map) {
        try {
            String sql = "update etc set \"Name\"=?,contact_number=?,email=? where etc_id=?";

            return jdbctemplate.update(sql, map.get("name"), map.get("mobileNo"), map.get("emailId"), map.get("id"));
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
    public List<Map<String, Object>> getEtcById(String id) {
        try {
            String sql = "SELECT * FROM etc WHERE etc_id = ?";
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
    public int deleteEtcById(String id) {
        try {
            String sql = "DELETE FROM etc WHERE etc_id = ?";
            return jdbctemplate.update(sql, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserId(String userName, String userType) {
        try {
            String tableName;
            String columnName;

            if (userType.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
                columnName = "etc_id";
            } else if (userType.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
                columnName = "sird_id";
            } else {
                tableName = "loginmaster_ministry";
                columnName = "min_id";
            }

            String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";
            return Integer.parseInt(jdbctemplate.queryForList(sql, userName).get(0).get(columnName).toString());
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
    public List<Map<String, Object>> getAllEtcs(String userName, String userType) {
        try {
            Map<String, Object> etcMaps = new HashMap<>();
            StringBuilder id = new StringBuilder();
            boolean flag = true;
            boolean flag1 = true;
            List<Map<String, Object>> proposallist = new ArrayList<Map<String, Object>>();
            String stateCode = "";
            Object sird_id = "";
            if (userType.equalsIgnoreCase("Sird")) {
                // First query (safe)
                String sql = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
                sird_id = jdbctemplate.queryForList(sql, userName).get(0).get("sird_id");

// Second query (safe)
                String sql1 = "SELECT state_code FROM sird WHERE sird_id = ?";
                stateCode = jdbctemplate.queryForList(sql1, sird_id).get(0).get("state_code").toString();

            }
            String sql = "SELECT etc_id, \"Name\" AS name FROM etc WHERE state_code = ?";
            List<Map<String, Object>> etcList = jdbctemplate.queryForList(sql, stateCode);


            // 1. Extract etc_ids into a List
            List<Object> userIds = new ArrayList<>();
            for (Map<String, Object> etcProposal : etcList) {
                userIds.add(etcProposal.get("etc_id").toString());
            }

// 2. If list is empty, avoid SQL call
            if (userIds.isEmpty()) {
                proposallist = new ArrayList<>();
            } else {
                // 3. Create placeholders: ?,?,?... (one ? for each ID)
                String placeholders = String.join(",", Collections.nCopies(userIds.size(), "?"));

                // 4. Prepare SQL query
                String proposalSql = "SELECT proposal_no, totaldemand, proposaldate, user_id, proposalid, usertype, backwarded " +
                        "FROM final_proposal WHERE usertype = ? AND user_id IN (" + placeholders + ")";

                // 5. Add 'etc' and all user IDs as parameters
                List<Object> params = new ArrayList<>();
                params.add("etc");
                params.addAll(userIds);

                // 6. Execute securely
                proposallist = jdbctemplate.queryForList(proposalSql, params.toArray());
            }


            for (Map<String, Object> map : proposallist) {
                for (Map<String, Object> etcMap : etcList) {
                    if (map.get("user_id").toString().equalsIgnoreCase(etcMap.get("etc_id").toString())) {
                        map.put("Name", etcMap.get("name"));

                    }
                }
                if (flag1) {
                    etcMaps.put("demand", Integer.parseInt(map.get("totaldemand").toString()));
                    flag1 = false;
                } else {
                    etcMaps.put("demand", Integer.parseInt(etcMaps.get("demand").toString()) + Integer.parseInt(map.get("totaldemand").toString()));
                }


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

    /**
     * Get All Faculty Details
     *
     * @return
     */
    public List<Map<String, Object>> GetAllEtcAddToCombined(String userName, String userType, String installmentType, String financialYear) {
        try {
            int i = 1;
            int userId = this.getUserId(userName, userType);
            Map<String, Object> etcMaps = new HashMap<>();
            List<Map<String, Object>> proposalList = new ArrayList<>();

// Build dynamic query parameters
            List<Object> queryParams = new ArrayList<>();
            StringBuilder sqlBuilder = new StringBuilder("SELECT status,proposalid FROM forward_proposal WHERE status = 'Add to combined' AND sird_id = ? AND proposaltype IS NULL AND combined = 'false'");
            queryParams.add(userId);

            if (!"ALL".equals(financialYear)) {
                sqlBuilder.append(" AND financialyear = ?");
                queryParams.add(financialYear);
            }
            if (!"ALL".equals(installmentType)) {
                sqlBuilder.append(" AND installmentno = ?");
                queryParams.add(installmentType);
            }

            List<Map<String, Object>> proposals = jdbctemplate.queryForList(sqlBuilder.toString(), queryParams.toArray());

// Extract proposal IDs
            List<Integer> proposalIds = proposals.stream()
                    .map(p -> Integer.parseInt(p.get("proposalid").toString()))
                    .collect(Collectors.toList());

            if (!proposalIds.isEmpty()) {
                // Fetch proposals
                String placeholders = String.join(",", Collections.nCopies(proposalIds.size(), "?"));

                String proposalSql = "SELECT financialyear AS \"financialYear\", installmentno AS \"installmentType\", " +
                        "user_id, proposal_no, proposedtotal, proposaldate, user_id, proposalid, usertype, backwarded " +
                        "FROM final_proposal WHERE proposalid IN (" + placeholders + ")";
                proposalList = jdbctemplate.queryForList(proposalSql, proposalIds.toArray());

                // Collect user_ids and proposal_ids for batch lookup
                Set<Object> userIds = proposalList.stream().map(p -> p.get("user_id")).collect(Collectors.toSet());
                Set<Object> finalProposalIds = proposalList.stream().map(p -> p.get("proposalid").toString()).collect(Collectors.toSet());

                // Fetch all names in one go
                Map<Object, String> userIdNameMap = new HashMap<>();
                if (!userIds.isEmpty()) {
                    String namePlaceholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
                    String etcSql = "SELECT etc_id, \"Name\" AS name FROM etc WHERE etc_id IN (" + namePlaceholders + ")";
                    List<Map<String, Object>> etcData = jdbctemplate.queryForList(etcSql, userIds.toArray());
                    for (Map<String, Object> row : etcData) {
                        userIdNameMap.put(row.get("etc_id"), row.get("name").toString());
                    }
                }

                // Fetch all statuses in one go
                Map<Object, String> proposalStatusMap = new HashMap<>();
                if (!finalProposalIds.isEmpty()) {
                    String statusPlaceholders = String.join(",", Collections.nCopies(finalProposalIds.size(), "?"));
                    String statusSql = "SELECT proposalid, status FROM forward_proposal WHERE proposalid IN (" + statusPlaceholders + ")";
                    List<Map<String, Object>> statusData = jdbctemplate.queryForList(statusSql, finalProposalIds.toArray());
                    for (Map<String, Object> row : statusData) {
                        proposalStatusMap.put(row.get("proposalid"), row.get("status").toString());
                    }
                }

                // Total demand
                BigDecimal totalDemand = BigDecimal.ZERO;
                for (Map<String, Object> proposalMap : proposalList) {
                    BigDecimal demand = new BigDecimal(proposalMap.get("proposedtotal").toString());
                    totalDemand = totalDemand.add(demand);

                    // Add name and status
                    proposalMap.put("Name", userIdNameMap.getOrDefault(proposalMap.get("user_id"), ""));
                    proposalMap.put("status", proposalStatusMap.getOrDefault(proposalMap.get("proposalid").toString(), ""));
                    proposalMap.put("serialNo", i++);
                }

                // Add demand map at the end
                if (!proposalList.isEmpty()) {
                    etcMaps.put("demand", totalDemand);
                    proposalList.add(etcMaps);
                }
            }

            return proposalList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> GetAllEtcAddToCombinedNonRecuring(String userName, String userType, String financialYear) {
        try {
            int userId = this.getUserId(userName, userType);
            List<Object> queryParams = new ArrayList<>();
            StringBuilder queryBuilder = new StringBuilder(
                    "SELECT status, proposalid FROM forward_proposal WHERE status = ? AND sird_id = ? AND proposaltype = 'nonrecurring' AND combined = ?"
            );

            queryParams.add("Add to combined");
            queryParams.add(userId);
            queryParams.add("false");

            if (!"ALL".equals(financialYear)) {
                queryBuilder.append(" AND financialyear = ?");
                queryParams.add(financialYear);
            }

            List<Map<String, Object>> proposal = jdbctemplate.queryForList(queryBuilder.toString(), queryParams.toArray());

// Collect proposal IDs
            List<Integer> proposalIds = proposal.stream()
                    .map(p -> Integer.parseInt(p.get("proposalid").toString()))
                    .collect(Collectors.toList());

            List<Map<String, Object>> proposalList = new ArrayList<>();

            if (!proposalIds.isEmpty()) {
                // Build placeholders
                String placeholders = String.join(",", Collections.nCopies(proposalIds.size(), "?"));
                String proposalSql = "SELECT user_id, status, proposaldate, proposalid, usertype, backwarded, financialyear AS \"financialYear\" " +
                        "FROM nonrecurring_proposal WHERE proposalid IN (" + placeholders + ")";

                proposalList = jdbctemplate.queryForList(proposalSql, proposalIds.toArray());

                // Collect user_ids
                Set<Object> userIds = proposalList.stream()
                        .map(p -> p.get("user_id"))
                        .collect(Collectors.toSet());

                // Fetch names in batch
                Map<Object, String> nameMap = new HashMap<>();
                if (!userIds.isEmpty()) {
                    String userPlaceholders = String.join(",", Collections.nCopies(userIds.size(), "?"));
                    String nameSql = "SELECT etc_id, \"Name\" AS name FROM etc WHERE etc_id IN (" + userPlaceholders + ")";
                    List<Map<String, Object>> etcList = jdbctemplate.queryForList(nameSql, userIds.toArray());

                    for (Map<String, Object> row : etcList) {
                        nameMap.put(row.get("etc_id"), row.get("name").toString());
                    }
                }

                // Map names to proposals
                for (Map<String, Object> proposalMap : proposalList) {
                    proposalMap.put("Name", nameMap.getOrDefault(proposalMap.get("user_id"), ""));
                }
            }

            return proposalList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

}
