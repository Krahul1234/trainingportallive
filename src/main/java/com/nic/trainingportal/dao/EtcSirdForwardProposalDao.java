package com.nic.trainingportal.dao;

import com.nic.trainingportal.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EtcSirdForwardProposalDao {

    @Autowired
    private ErrorLogService error;

    @Autowired

    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> getforwardEtcProposal(String userName, String userType, String installmentType, String financialYear) {
        try {
            String sql = "";
            List<Object> params = new ArrayList<>();
            List<Map<String, Object>> proposalList = new ArrayList<Map<String, Object>>();
            int userId = this.getUserId(userType, userName);
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT financialyear AS \"financialYear\", proposalid, proposaldate, forwarded, remarks, status, installmentno AS \"installmentType\" " +
                            "FROM final_proposal WHERE user_id = ? AND usertype = ?"
            );

// Add parameters for user_id and usertype
            params.add(userId);
            params.add(userType);

            if (installmentType != null && !installmentType.isEmpty() &&
                    financialYear != null && !financialYear.isEmpty()) {

                sqlBuilder.append(" AND installmentno = ? AND financialyear = ?");
                params.add(installmentType);
                params.add(financialYear);
            }

            sql = sqlBuilder.toString();
            proposalList = jdbcTemplate.queryForList(sql, params.toArray());
            return proposalList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    @Transactional
    public String addForwardProposalNew(Map<String, Object> map) {
        try {
            String sql1 = "SELECT * FROM forward_proposal WHERE proposalid = ? and proposaltype is null ";
            String propsalId = map.get("proposalid").toString();
            int  propsalIdInt = Integer.parseInt(map.get("proposalid").toString());
            List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(sql1, propsalId);

            if (proposalList != null && proposalList.size() != 0) {
                return "Already Exist";
            } else {
                int userId = this.getUserId(map);
                String etc = "select state_code from etc where etc_id=?";
                String state_code = jdbcTemplate.queryForList(etc, userId).get(0).get("state_code").toString();
                String sird = "select sird_id from sird where state_code=?";
                int sird_id = Integer.parseInt(jdbcTemplate.queryForList(sird, state_code).get(0).get("sird_id").toString());

                String insertSql = "INSERT INTO forward_proposal " +
                        "(proposalid, etc_id, sird_id, status, remarks, installmentno, financialyear) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(
                        insertSql,
                        propsalId,
                        userId,
                        sird_id,
                        "Forwarded to " + map.get("status"),
                        map.get("remarks"),
                        map.get("installmentType"),
                        map.get("financialYear")
                );

                int proposalIdInt = Integer.parseInt(propsalId.toString());
                String updateProposal = "UPDATE final_proposal SET forwarded = ?, remarks = ?, status = ? WHERE proposalid = ?";

                jdbcTemplate.update(
                        updateProposal,
                        true,
                        map.get("remarks"),
                        "Forwarded to " + map.get("status"),
                        proposalIdInt
                );


            }
        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "Add Forward Proposal");
            return "Not Forwarded";
        }
        return "Forwarded Successfully";
    }

    @Transactional
    public String updateForwardProposalNew(Map<String, Object> map) {
        try {
            String proposalIdString = map.get("proposalid").toString();
            int proposalIdint = Integer.parseInt(map.get("proposalid").toString());
            if (map.get("keySird") != null && map.get("keySird").toString().equals("sird")) {
                if (map.get("send").toString().equalsIgnoreCase("SirdCombined")) {
                    String updateProposal = "UPDATE final_proposal SET status = ?, forwarded = ? WHERE proposalid = ?";

                    jdbcTemplate.update(
                            updateProposal,
                            "Forward to combined",
                            true,
                            proposalIdint
                    );

                } else if (map.get("send").toString().equals("Backward")) {
                    String sql = "UPDATE forward_proposal SET status = ? WHERE proposalid = ? and proposaltype is null";
                    jdbcTemplate.update(
                            sql,
                            "Forwarded to sird",
                            proposalIdString
                    );

                } else {
                    String updateProposal = "UPDATE final_proposal SET status = ?, forwarded = ? WHERE proposalid = ?";
                    jdbcTemplate.update(
                            updateProposal,
                            "Generated",
                            false,
                            proposalIdint
                    );

                }
            } else {
                int userId = this.getUserId(map);
                String proposalSql = "SELECT * FROM forward_proposal WHERE proposalid = ? and proposaltype is null";
//                String propsalId = (String) map.get("proposalid");

                List<Map<String, Object>> list = jdbcTemplate.queryForList(proposalSql, proposalIdString);


                if (list == null || list.size() == 0) {
                    String sql = "INSERT INTO forward_proposal (proposalid, etc_id, status, remarks, sird_id) VALUES (?, ?, ?, ?, ?)";
                    jdbcTemplate.update(
                            sql,
                            proposalIdString,
                            map.get("userid"),
                            "Backward to " + map.get("key"),
                            map.get("remarks"),
                            userId
                    );

                }

                if (map.get("send").toString().equals("Backward")) {

                    String status = "Backward to " + map.get("key");
                    String remarks = (String) map.get("remarks");


                    String updateForwardProposalSql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ?  and proposaltype is null";
                    String updateFinalProposalSql = "UPDATE final_proposal SET remarks = ?, status = ? WHERE proposalid = ?";

                    jdbcTemplate.update(updateFinalProposalSql, remarks, status, proposalIdint);
                    jdbcTemplate.update(updateForwardProposalSql, remarks, status, proposalIdString);

                } else if (map.get("send").toString().equalsIgnoreCase("Forward")) {
                    String status = "Forwarded to " + map.get("key");
                    String remarks = map.get("remarks").toString();


                    String sql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ?  and proposaltype is null";

                    int proposalId1= Integer.parseInt(map.get("proposalid").toString());
                    String updateProposal = "UPDATE final_proposal SET remarks = ?, status = ? WHERE proposalid = ?";

                    jdbcTemplate.update(updateProposal, remarks, status, proposalIdint);
                    jdbcTemplate.update(sql, remarks, status, proposalIdString);

                } else {
                    String remarks = map.get("remarks").toString();

                    String status = "Add to combined";

                    String sql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ?  and proposaltype is null";
                    String updateProposal = "UPDATE final_proposal SET remarks = ?, status = ? WHERE proposalid = ?";

                    jdbcTemplate.update(updateProposal, remarks, status, proposalIdint);
                    jdbcTemplate.update(sql, remarks, status, proposalIdString);

                }
            }
        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            error.logError(str[0], "updateForwardProposalNew");
            return "Not Forwarded";
        }
        return "Forwarded Successfully";
    }


    public int getUserId(String userType, String userName) {
        try {
            String tableName = "";
            if (userType.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
            } else if (userType.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
            } else {
                tableName = "loginmaster_ministry";
            }

            String column = userType.toLowerCase() + "_id";
            String sql = "SELECT " + column + " FROM " + tableName + " WHERE username = ?";

            return Integer.parseInt(
                    jdbcTemplate.queryForList(sql, new Object[]{userName}).get(0).get(column).toString()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getUserId(Map<String, Object> map) {
        try {
            String tableName = "";
            String key = map.get("key").toString().toLowerCase();
            String username = map.get("username").toString();
            if (key.equals("etc")) {
                tableName = "loginmaster_etc";
            } else if (key.equals("sird")) {
                tableName = "loginmaster_sird";
            } else {
                tableName = "loginmaster_ministry";
            }

            String column = key + "_id";
            String sql = "SELECT " + column + " FROM " + tableName + " WHERE username = ?";

            return Integer.parseInt(
                    jdbcTemplate.queryForList(sql, username).get(0).get(column).toString()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Map<String, Object>> getForwardProposalForSird(Map<String, Object> map) {
        try {
            int i = 1;
            String sql = "";
            List<Map<String, Object>> list = new ArrayList<>();
            int userId = this.getUserId(map);

            String key = map.get("key").toString();
            String installmentType = map.get("installmentType") != null ? map.get("installmentType").toString() : null;
            String financialYear = map.get("financialYear") != null ? map.get("financialYear").toString() : null;
            List<Object> params = new ArrayList<>();

            String baseStatus = "Forwarded to " + key;

            if (installmentType != null && !installmentType.isEmpty() && financialYear != null && !financialYear.isEmpty()) {
                sql = "SELECT financialyear AS \"financialYear\", id, remarks, proposalid, status, installmentno AS \"installmentType\" " +
                        "FROM forward_proposal " +
                        "WHERE status = ? " +
                        "AND sird_id = ? " +
                        "AND (proposaltype IS NULL OR proposaltype != ?) " +
                        "AND installmentno = ? " +
                        "AND financialyear = ?";
                params.add(baseStatus);
                params.add(userId);
                params.add("nonrecurring");
                params.add(installmentType);
                params.add(financialYear);
            } else {
                sql = "SELECT financialyear AS \"financialYear\", id, remarks, proposalid, status, installmentno AS \"installmentType\" " +
                        "FROM forward_proposal " +
                        "WHERE status = ? " +
                        "AND sird_id = ? " +
                        "AND (proposaltype IS NULL OR proposaltype != ?)";
                params.add(baseStatus);
                params.add(userId);
                params.add("nonrecurring");
            }

            list = jdbcTemplate.queryForList(sql, params.toArray());


            for (Map<String, Object> map1 : list) {
                map1.put("serialNo", i++);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getForwardProposalForEtc(Map<String, Object> map) {
        try {

            int userId = this.getUserId(map);
            String sql = "select id, remarks, proposalid, status, financialyear as \"financialYear\" " +
                    "from forward_proposal " +
                    "where status = ? and etc_id = ? and (proposaltype is null or proposaltype != ?)";

            return jdbcTemplate.queryForList(sql, "Forwarded to sird", userId, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getBackwardProposalForEtc(Map<String, Object> map) {
        try {

            int userId = this.getUserId(map);
            String sql = "select financialyear As \"financialYear\", installmentno As \"installmentType\", id, remarks, proposalid, status " +
                    "from forward_proposal " +
                    "where status = ? and etc_id = ? and (proposaltype is null or proposaltype != ?)";

            return jdbcTemplate.queryForList(sql, "Backward to etc", userId, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getforwardEtcProposalNonRecuring(String userName, String userType) {
        try {
            List<Map<String, Object>> proposalList = new ArrayList<Map<String, Object>>();
            int userId = this.getUserId(userType, userName);
            String sql = "select proposalid, proposaldate, forwarded, remarks, status, financialyear as \"financialYear\" " +
                    "from nonrecurring_proposal where user_id = ? and usertype = ?";
            proposalList = jdbcTemplate.queryForList(sql, userId, userType);

            return proposalList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    @Transactional
    public String addForwardProposalNonRecurring(Map<String, Object> map) {
        try {
            String sql1 = "select * from forward_proposal where proposalid = ? and proposaltype = 'nonrecurring'";
            String propsalId = map.get("proposalid").toString();
            List<Map<String, Object>> proposalList = jdbcTemplate.queryForList(sql1, propsalId);

            if (proposalList != null && proposalList.size() != 0) {
                return "Already Exist";
            } else {
                int userId = this.getUserId(map);
                String etc = "select state_code from etc where etc_id = ?";
                String state_code = jdbcTemplate.queryForObject(etc, new Object[]{userId}, String.class);
                String sird = "select sird_id from sird where state_code = ?";
                String sird_id = jdbcTemplate.queryForObject(sird, new Object[]{state_code}, String.class);
                int sirdInt = Integer.parseInt(sird_id.toString());
                String sql = "insert into forward_proposal (proposalid, etc_id, sird_id, status, remarks, proposaltype, financialyear) " +
                        "values (?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(sql,
                        propsalId,
                        userId,
                        sirdInt,
                        "Forwarded to " + map.get("status"),
                        map.get("remarks"),
                        "nonrecurring",
                        map.get("financialYear")
                );
                int proposalIdint = (int) map.get("proposalid");
                String updateProposal = "update nonrecurring_proposal set forwarded = ?, remarks = ?, status = ? where proposalid = ?";
                jdbcTemplate.update(updateProposal, true, map.get("remarks"), "Forwarded to " + map.get("status"), proposalIdint);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }
    @Transactional
    public int updateForwardProposalNewNonRecurring(Map<String, Object> map) {
        try {
            if (map.get("keySird") != null && map.get("keySird").toString().equals("sird")) {
                if (map.get("send").toString().equalsIgnoreCase("SirdCombined")) {
                    String updateProposal = "UPDATE nonrecurring_proposal SET status = ?, forwarded = ? WHERE proposalid = ?";
                    int propsalId = (int)map.get("proposalid");
                    jdbcTemplate.update(updateProposal, "Forward to combined", true, propsalId);
                } else if (map.get("send").toString().equals("Backward")) {
                    String sql = "UPDATE forward_proposal SET status = ? WHERE proposalid = ? and proposaltype='nonrecurring'";
                    String propsalId = map.get("proposalid").toString();
                    jdbcTemplate.update(sql, "Forwarded to sird", propsalId);

                } else {
                    String updateProposal = "UPDATE nonrecurring_proposal SET status = ?, forwarded = ? WHERE proposalid = ? ";
                    int propsalId = (int)map.get("proposalid");
                    jdbcTemplate.update(updateProposal, "Generated", false, propsalId);

                }
            } else {
                int userId = this.getUserId(map);
                String proposalSql = "SELECT * FROM forward_proposal WHERE proposalid = ? and proposaltype='nonrecurring'";
                String propsalId = map.get("proposalid").toString();
                int proposalIdint = Integer.parseInt(map.get("proposalid").toString());
                List<Map<String, Object>> list = jdbcTemplate.queryForList(proposalSql, propsalId);


                if (list == null || list.size() == 0) {
                    String sql = "INSERT INTO forward_proposal (proposalid, etc_id, status, remarks, sird_id) VALUES (?, ?, ?, ?, ?)";
                    return jdbcTemplate.update(
                            sql,
                            propsalId,
                            Integer.parseInt( map.get("userid").toString()),
                            "Backward to " + map.get("key").toString(),
                            map.get("remarks").toString(),
                            userId
                    );
                }

                if (map.get("send").toString().equals("Backward")) {

                    String forwardSql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ? and proposaltype='nonrecurring'";
                    proposalSql = "UPDATE nonrecurring_proposal SET remarks = ?, status = ? WHERE proposalid = ?";
                    String status = "Backward to " + map.get("key");

                    jdbcTemplate.update(proposalSql, map.get("remarks"), status, proposalIdint);
                    return jdbcTemplate.update(forwardSql, map.get("remarks"), status, propsalId);

                } else if (map.get("send").toString().equalsIgnoreCase("Forward")) {

                    String forwardSql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ? and proposaltype='nonrecurring'";
                    proposalSql = "UPDATE nonrecurring_proposal SET remarks = ?, status = ? WHERE proposalid = ?";
                    String status = "Forwarded to " + map.get("key");

                    jdbcTemplate.update(proposalSql, map.get("remarks"), status, proposalIdint);
                    return jdbcTemplate.update(forwardSql, map.get("remarks"), status, propsalId);

                } else {

                    String forwardSql = "UPDATE forward_proposal SET remarks = ?, status = ? WHERE proposalid = ? and proposaltype='nonrecurring'";
                    proposalSql = "UPDATE nonrecurring_proposal SET remarks = ?, status = ? WHERE proposalid = ?";
                    String status = "Add to combined";

                    jdbcTemplate.update(proposalSql, map.get("remarks"), status, proposalIdint);
                    return jdbcTemplate.update(forwardSql, map.get("remarks"), status, propsalId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
    public List<Map<String, Object>> getForwardProposalForSirdNonRecurring(Map<String, Object> map) {
        try {

            int userId = this.getUserId(map);

            String sql = "SELECT id, remarks, proposalid, status, financialyear AS \"financialYear\" " +
                    "FROM forward_proposal " +
                    "WHERE status = ? AND sird_id = ? AND proposaltype = ?";

            return jdbcTemplate.queryForList(sql, "Forwarded to " + map.get("key"), userId, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getForwardProposalForEtcNonRecurring(Map<String, Object> map) {
        try {
            int userId = this.getUserId(map);

            String sql = "SELECT id, remarks, proposalid, status, financialyear AS \"financialYear\" " +
                    "FROM forward_proposal " +
                    "WHERE status = ? AND etc_id = ? AND proposaltype = ?";

            return jdbcTemplate.queryForList(sql, "Forwarded to sird", userId, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getBackwardProposalForEtcNonRecuring(Map<String, Object> map) {
        try {

            int userId = this.getUserId(map);

            String sql = "SELECT id, remarks, proposalid, status, financialyear AS \"financialYear\" " +
                    "FROM forward_proposal " +
                    "WHERE status = ? AND etc_id = ? AND proposaltype = ?";

            return jdbcTemplate.queryForList(sql, "Backward to etc", userId, "nonrecurring");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

}
