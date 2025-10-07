package com.nic.trainingportal.dao;

import com.nic.trainingportal.service.ErrorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class TrainingDao {
    @Autowired
    private ErrorLogService service;
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
        try {
            int userId = this.getUserId(map);
            if (userId!=0) {

                String sql = "INSERT INTO training_final " + "(entry_date, name, training_venue, training_subject, number_of_trainees, target_group, enddate, usertype, user_id, nameofcordinator, totalduration, financialyear, installmentno) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                Object[] params = new Object[]{map.get("proposedDate"), map.get("facultyName"), map.get("venue"), map.get("trainingSubject"), map.get("trainessNumber"), map.get("targetGroup"), map.get("endDate"), map.get("key").toString().toLowerCase(), userId, map.get("nameOfCoordintor"), map.get("totalDuration"), map.get("financialYear"), map.get("installmentType")};

                logger.info("Add Training Details: {}", Arrays.toString(params));

                return jdbcTemplate.update(sql, params);
            }
            else {
                return 0;
            }

        } catch (Exception e) {
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
    public List<Map<String, Object>> getTrainingDetails(int pageSize, int pageNumber, String userName, String userType, String userId, String financialYear, String installmentType) {
        try {
            int i = (pageNumber * pageSize) + 1;
            List<Map<String, Object>> dataList = new ArrayList<>();

            String baseSql = "SELECT financialyear AS \"financialYear\", " + "installmentno AS \"installmentType\", " + "totalduration AS \"totalDuration\", enddate, training_id, " + "entry_date AS \"proposedDate\", name AS \"facultyName\", " + "training_venue AS \"venue\", training_subject AS \"trainingSubject\", " + "number_of_trainees AS \"trainessNumber\", target_group AS \"targetGroup\" " + "FROM training_final " + "WHERE usertype = ? AND user_id = ?";

            List<Object> params = new ArrayList<>();
            params.add(userType.toLowerCase());
            params.add(userId);

            if (financialYear != null && !financialYear.isEmpty() && installmentType != null && !installmentType.isEmpty()) {
                baseSql += " AND financialyear = ? AND installmentno = ?";
                params.add(financialYear);
                params.add(installmentType);
            }

            baseSql += " ORDER BY training_id DESC OFFSET ? LIMIT ?";
            params.add(pageNumber * pageSize);
            params.add(pageSize);

            dataList = jdbcTemplate.queryForList(baseSql, params.toArray());

            for (Map<String, Object> map : dataList) {
                map.put("serialNo", i++);
            }

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
            String sql = "DELETE FROM training_final WHERE id = ?";

            Object[] params = new Object[]{map.get("id")};

            logger.info("Delete Training Details with ID: {}", map.get("id"));

            return jdbcTemplate.update(sql, params);

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
            int userIdint= getUserId(map);
            String check="select user_id from training_final WHERE training_id=?";
            Map<String, Object> id = jdbcTemplate.queryForMap(check, map.get("id"));
            if (!(userIdint==Integer.parseInt( id.get("user_id").toString()))){
                return  0;
            }
            String sql = "UPDATE training_final SET " + "enddate = ?, entry_date = ?, name = ?, training_venue = ?, training_subject = ?, " + "number_of_trainees = ?, target_group = ?, nameofcordinator = ?, totalduration = ?, " + "installmentno = ?, financialyear = ? " + "WHERE training_id = ?";

            Object[] params = new Object[]{map.get("endDate"), map.get("proposedDate"), map.get("facultyName"), map.get("venue"), map.get("trainingSubject"), map.get("trainessNumber"), map.get("targetGroup"), map.get("nameOfCoordintor"), map.get("totalDuration"), map.get("installmentType"), map.get("financialYear"), map.get("id")};

            logger.info("Update Training Details for ID: {}", map.get("id"));

            return jdbcTemplate.update(sql, params);

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
    public List<Map<String, Object>> getCalendarInfoById(int id) {
        try {

            String sql = "SELECT financialyear AS \"financialYear\", " + "installmentno AS \"installmentType\", " + "totalduration AS \"totalDuration\", " + "nameofcordinator AS \"nameOfCoordintor\", " + "enddate AS \"endDate\", " + "entry_date AS \"proposedDate\", " + "name AS \"facultyName\", " + "training_venue AS \"venue\", " + "training_subject AS \"trainingSubject\", " + "number_of_trainees AS \"trainessNumber\", " + "target_group AS \"targetGroup\" " + "FROM training_final " + "WHERE training_id = ?";

            logger.info("Get All Faculty for ID: {}", id);
            return jdbcTemplate.queryForList(sql, new Object[]{id});

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
            String sql = "DELETE FROM training_final WHERE training_id = ?";
            int idInt = Integer.parseInt(id);

            logger.info("Delete Training Record for ID: {}", id);

            return jdbcTemplate.update(sql, idInt);

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return 0;
    }

    public int getfacultyCount() {
        try {
            String sql = "select count(*) as count from faculty";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserId(String userName, String userType) {
        try {
            // Validate userType and set tableName and columnName accordingly
            String tableName;
            String columnName;

            if (userType.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
                columnName = "etc_id";
            } else if (userType.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
                columnName = "sird_id";
            } else if (userType.equalsIgnoreCase("ministry")) {
                tableName = "loginmaster_ministry";
                columnName = "min_id";
            } else {
                throw new IllegalArgumentException("Invalid userType: " + userType);
            }

// Build SQL with validated table and column names
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

    public int getTrainingCalendarCount(String userId, String userType, String financialYear, String installmentType) {
        try {
            if (financialYear != null && !financialYear.isEmpty() && installmentType != null && !installmentType.isEmpty()) {

                String sql = "SELECT COUNT(*) AS count FROM training_final " + "WHERE usertype = ? AND user_id = ? AND financialyear = ? AND installmentno = ?";

                return jdbcTemplate.queryForObject(sql, Integer.class, userType.toLowerCase(), userId, financialYear, installmentType);

            } else {

                String sql = "SELECT COUNT(*) AS count FROM training_final " + "WHERE usertype = ? AND user_id = ?";

                return jdbcTemplate.queryForObject(sql, Integer.class, userType.toLowerCase(), userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getUserId(Map<String, Object> map) {
        try {
            String key = map.get("key").toString().toLowerCase();
            String userName = map.get("userName").toString();

            String tableName;
            String columnName;

            switch (key) {
                case "etc":
                    tableName = "loginmaster_etc";
                    columnName = "etc_id";
                    break;
                case "sird":
                    tableName = "loginmaster_sird";
                    columnName = "sird_id";
                    break;
                case "ministry":
                    tableName = "loginmaster_ministry";
                    columnName = "ministry_id";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid key value: " + key);
            }

            String sql = "SELECT " + columnName + " FROM " + tableName + " WHERE username = ?";

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, userName);

            if (result.isEmpty()) {
                throw new RuntimeException("User not found for username: " + userName);
            }

            return Integer.parseInt(result.get(0).get(columnName).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean exist(int userId) {
        try {
            String sql = "select * from training_final where user_id='" + userId + "'";
            List<Map<String, Object>> facultyList = new ArrayList<Map<String, Object>>();
            facultyList = jdbcTemplate.queryForList(sql);
            return facultyList != null && facultyList.size() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    public Map<String, Object> getTrainingCalenderCount(int userName, String userType) {
        try {
            Map<String, Object> map = new HashMap<>();

// Step 1: Get sird_id from loginmaster_sird
            String sql1 = "SELECT sird_id FROM loginmaster_sird WHERE username = ?";
            List<Map<String, Object>> sirdResult = jdbcTemplate.queryForList(sql1, userName);

            if (sirdResult.isEmpty()) {
                throw new RuntimeException("No sird_id found for username: " + userName);
            }

            Object sird_id = sirdResult.get(0).get("sird_id");

// Step 2: Get Name from sird table using sird_id
            String nameQuery = "SELECT name FROM sird WHERE sird_id = ?";
            String name = jdbcTemplate.queryForObject(nameQuery, String.class, sird_id);

// Step 3: Count from training_calendar table
            String countQuery = "SELECT COUNT(*) FROM training_calendar WHERE user_type = ? AND user_id = ?";
            Integer count = jdbcTemplate.queryForObject(countQuery, Integer.class, userType.toLowerCase(), sird_id);

// Step 4: Put name as key and count as value
            map.put(name, count);

            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }

    public List<Map<String, Object>> getCalendar(String userName, String userType) {
        try {
            String sql = "SELECT * FROM training_calendar WHERE user_type = ? AND user_id = ?";
            return jdbcTemplate.queryForList(sql, userType.toLowerCase(), userName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    private void insertRemarks(Integer proposalId, String status, String remarks, String lowerDesig, String upperDesig, String userId, String stateName, String fy, String installment) {
        String sql = "INSERT INTO remarks (combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear, installmentno) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, proposalId, status, remarks, lowerDesig, upperDesig, userId, stateName, fy, installment);
    }

    private void updateProposalForwarded(Integer proposalId) {
        String sql = "UPDATE combined_proposal SET forwarded = true WHERE combined_proposal_id = ?";
        jdbcTemplate.update(sql, proposalId);
    }

    private void discardProposal(Integer proposalIdInt, String userId, String fy, String installment, String exceptStatus) {
        int userIdInt= Integer.parseInt(userId.toString());
        jdbcTemplate.update("UPDATE combined_proposal SET combinedstatus = ? WHERE combined_proposal_id = ?", "discard", proposalIdInt);

        jdbcTemplate.update("UPDATE remarks SET status = ? WHERE status != ? AND combined_proposal_id = ?", "discard", exceptStatus, proposalIdInt);

        jdbcTemplate.update("UPDATE forward_proposal SET combined = ? WHERE sird_id = ? AND financialyear = ? AND installmentno = ?", false, userIdInt, fy, installment);

        jdbcTemplate.update("UPDATE final_proposal SET combined = ? WHERE user_id = ? AND financialyear = ? AND installmentno = ?", false, userIdInt, fy, installment);
    }

    private void updateRemarksStatus(Integer proposalIdInt, String newStatus, String... oldStatuses) {
        String sql = "UPDATE remarks SET forwarded = true, status = ? WHERE combined_proposal_id = ? AND status = ?";
        for (String oldStatus : oldStatuses) {
            jdbcTemplate.update(sql, newStatus, proposalIdInt, oldStatus);
        }
    }

    @Transactional
//    public String addForwardCombinedProposalNew(Map<String, Object> map) {
//        try {
//            String lowerDesignation = "";
//            String upperDesignation = "";
//
//            if (map.get("userType").toString().equalsIgnoreCase("sird")) {
//                lowerDesignation = "sird";
//                upperDesignation = "MORD(Section Officer)";
//
//                String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Section Officer)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                jdbcTemplate.update(sql);
//                String update = "update combined_proposal set forwarded=true  where combined_proposal_id='" + map.get("proposalId") + "'";
//                jdbcTemplate.update(update);
//            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Section Officer)")) {
//
//                if (map.get("send").toString().equalsIgnoreCase("backward")) {
//                    lowerDesignation = "MORD(Section Officer)";
//                    upperDesignation = "sird";
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to sird" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update2 = "update combined_proposal set combinedstatus='" + "discard" + "' where combined_proposal_id='" + map.get("proposalId") + "'";
//                    jdbcTemplate.update(update2);
//                    String update3 = "update remarks set status='" + "discard" + "' where status!='" + "backward to sird" + "' and combined_proposal_id='" + map.get("proposalId") + "'";
//                    jdbcTemplate.update(update3);
//
//                    String update4 = "update forward_proposal set combined='" + "false" + "' where sird_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
//                    jdbcTemplate.update(update4);
//
//                    String update5 = "update final_proposal set combined='" + "false" + "' where user_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
//                    jdbcTemplate.update(update5);
//
//                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
//                    if ("MORD(Deputy Secretary)".equals(map.get("status"))){
//                        lowerDesignation = "MORD(Section Officer)";
//                        upperDesignation = "MORD(Deputy Secretary)";
//                        String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Deputy Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                        jdbcTemplate.update(sql);
//
//                        String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Deputy Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "backward to MORD(Section Officer)" + "'";
//                        jdbcTemplate.update(update);
//                    }else {
//                        lowerDesignation = "MORD(Section Officer)";
//                        upperDesignation = "MORD(Under Secretary)";
//                        String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Under Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                        jdbcTemplate.update(sql);
//
//                        String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Under Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "backward to MORD(Section Officer)" + "'";
//                        jdbcTemplate.update(update);
//                    }
//                } else {
//                    if ("MORD(Deputy Secretary)".equals(map.get("status"))) {
//                        lowerDesignation = "MORD(Section Officer)";
//                        upperDesignation = "MORD(Deputy Secretary)";
//
//                        String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Deputy Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                        jdbcTemplate.update(sql);
//                        String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Deputy Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Section Officer)" + "'";
//                        jdbcTemplate.update(update);
//                    } else {
//                        lowerDesignation = "MORD(Section Officer)";
//                        upperDesignation = "MORD(Under Secretary)";
//                        String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Under Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                        jdbcTemplate.update(sql);
//
//                        String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Under Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Section Officer)" + "'";
//                        jdbcTemplate.update(update);
//                    }
//                }
//
//            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
//                if (map.get("send").toString().equalsIgnoreCase("backward")) {
//                    lowerDesignation = "MORD(Under Secretary)";
//                    upperDesignation = "MORD(Section Officer)";
//
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to MORD(Section Officer)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "backward" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Under Secretary)" + "'";
//                    jdbcTemplate.update(update);
//
//                    String updateStatus = "update remarks set status='Backward To MORD(Section Officer)' where combined_proposal_id='" + map.get("proposalId") + "' and status='forwarded to MORD(Under Secretary)'";
//                    jdbcTemplate.update(updateStatus);
//
//                } else if (map.get("send").toString().equalsIgnoreCase("backwardview")) {
//                    lowerDesignation = "MORD(Under Secretary)";
//                    upperDesignation = "MORD(Section Officer)";
//
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to MORD(Section Officer)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "backward" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "backward to MORD(Under Secretary)" + "'";
//                    jdbcTemplate.update(update);
//
//                    String updateStatus = "update remarks set status='Backward To MORD(Section Officer)' where combined_proposal_id='" + map.get("proposalId") + "' and status='forwarded to MORD(Under Secretary)'";
//                    jdbcTemplate.update(updateStatus);
//
//
//                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
//                    lowerDesignation = "MORD(Under Secretary)";
//                    upperDesignation = "MORD(Deputy Secretary)";
//
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Deputy Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Deputy Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "backward to MORD(Under Secretary)" + "'";
//                    jdbcTemplate.update(update);
//                } else {
//                    lowerDesignation = "MORD(Under Secretary)";
//                    upperDesignation = "MORD(Deputy Secretary)";
//
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "pending at MORD(Deputy Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "forwarded to MORD(Deputy Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Under Secretary)" + "'";
//                    jdbcTemplate.update(update);
//                    String update1 = "update remarks set forwarded=true,status='" + "pending at MORD(Deputy Secretary)" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "forwarded to MORD(Under Secretary)" + "'";
//                    jdbcTemplate.update(update1);
//                }
//            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Deputy Secretary)")) {
//                if (map.get("send").toString().equalsIgnoreCase("backward")) {
//                    lowerDesignation = "MORD(Deputy Secretary)";
//                    upperDesignation = "sird";
//
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to sird" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Deputy Secretary)" + "'";
//                    jdbcTemplate.update(update);
//                    String update2 = "update combined_proposal set combinedstatus='" + "discard" + "' where combined_proposal_id='" + map.get("proposalId") + "'";
//                    jdbcTemplate.update(update2);
//                    String update3 = "update remarks set status='" + "discard" + "' where status!='" + "backward to sird" + "' and combined_proposal_id='" + map.get("proposalId") + "'";
//                    jdbcTemplate.update(update3);
//
//                    String update4 = "update forward_proposal set combined='" + "false" + "' where sird_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
//                    jdbcTemplate.update(update4);
//
//                    String update5 = "update final_proposal set combined='" + "false" + "' where user_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
//                    jdbcTemplate.update(update5);
//
//                } else if (map.get("send").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
////    			lowerDesignation="MORD(Deputy Secretary)";
////    			upperDesignation="MORD(Additional Secretary)";
////    			String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename)values('"+map.get("proposalId")+"','"+"pending at MORD(Additional Secretary)"+"','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+map.get("userid")+"','"+map.get("stateName")+"')";
////    			jdbcTemplate.update(sql);
////    			String update="update remarks set forwarded=true,status='"+"forwarded to MORD(Additional Secretary)"+"'  where combined_proposal_id='"+map.get("proposalId")+"' and status='"+"pending at MORD(Deputy Secretary)"+"'";
////    			jdbcTemplate.update(update);
//                    lowerDesignation = "MORD(Deputy Secretary)";
//                    upperDesignation = "MORD(Under Secretary)";
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to MORD(Under Secretary)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "backward" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Deputy Secretary)" + "'";
//                    jdbcTemplate.update(update);
//                    String updateStatus = "update remarks set status='Backward To MORD(Under Secretary)' where combined_proposal_id='" + map.get("proposalId") + "' and status='forwarded to MORD(Deputy Secretary)'";
//                    jdbcTemplate.update(updateStatus);
//
//                } else if (map.get("send").toString().equalsIgnoreCase("MORD(Section Officer)")) {
////    			lowerDesignation="MORD(Deputy Secretary)";
////    			upperDesignation="MORD(Additional Secretary)";
////    			String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename)values('"+map.get("proposalId")+"','"+"pending at MORD(Additional Secretary)"+"','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+map.get("userid")+"','"+map.get("stateName")+"')";
////    			jdbcTemplate.update(sql);
////    			String update="update remarks set forwarded=true,status='"+"forwarded to MORD(Additional Secretary)"+"'  where combined_proposal_id='"+map.get("proposalId")+"' and status='"+"pending at MORD(Deputy Secretary)"+"'";
////    			jdbcTemplate.update(update);
//                    lowerDesignation = "MORD(Deputy Secretary)";
//                    upperDesignation = "MORD(Section Officer)";
//                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('" + map.get("proposalId") + "','" + "backward to MORD(Section Officer)" + "','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set forwarded=true,status='" + "backward" + "'  where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Deputy Secretary)" + "'";
//                    jdbcTemplate.update(update);
//                    String updateStatus = "update remarks set status='Backward To MORD(Section Officer)' where combined_proposal_id='" + map.get("proposalId") + "' and status='forwarded to MORD(Deputy Secretary)'";
//                    jdbcTemplate.update(updateStatus);
//
//                } else {
//                    String sql = "update remarks set forwarded=true,status='" + "approved" + "' where combined_proposal_id='" + map.get("proposalId") + "' and status='" + "pending at MORD(Deputy Secretary)" + "' ";
//                    jdbcTemplate.update(sql);
//                    String update = "update remarks set status='" + "approved" + "' where combined_proposal_id='" + map.get("proposalId") + "'";
//                    jdbcTemplate.update(update);
//                }
//            }
//
//
//        } catch (Exception e) {
//            String[] str = e.getMessage().split("\\[");
//            System.out.println(str[0]);
//            e.printStackTrace();
//            service.logError(str[0], "addForwardCombinedProposalNew");
//            return "Not Forwarded";
//        }
//        return "Forwarded Successfully";
//    }
    public String addForwardCombinedProposalNew(Map<String, Object> map) {

        try {
            String lowerDesignation = "";
            String upperDesignation = "";

            if (map.get("userType").toString().equalsIgnoreCase("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "MORD(Section Officer)";

                String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno)values('"
                        + map.get("proposalId") + "','" + "pending at MORD(Section Officer)" + "','" + map.get("remarks")
                        + "','" + lowerDesignation + "','" + upperDesignation + "','" + map.get("userid") + "','"
                        + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType")
                        + "')";
                jdbcTemplate.update(sql);
                String update = "update combined_proposal set forwarded=true  where combined_proposal_id='"
                        + map.get("proposalId") + "'";
                jdbcTemplate.update(update);

            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Section Officer)")) {
                if (map.get("send").toString().equalsIgnoreCase("backward")) {
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "sird";

                    // Insert backward remark
                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','backward to sird','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    // Apply discard at SIRD only
                    String update2 = "update combined_proposal set combinedstatus='discard' where combined_proposal_id='" + map.get("proposalId") + "'";
                    jdbcTemplate.update(update2);

                    String update3 = "update remarks set status='discard' where status!='backward to sird' and combined_proposal_id='" + map.get("proposalId") + "'";
                    jdbcTemplate.update(update3);

                    String update4 = "update forward_proposal set combined='false' where sird_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
                    jdbcTemplate.update(update4);

                    String update5 = "update final_proposal set combined='false' where user_id='" + map.get("userid") + "' and financialyear='" + map.get("financialYear") + "' and installmentno='" + map.get("installmentType") + "'";
                    jdbcTemplate.update(update5);
                } else if (map.get("send").toString().equalsIgnoreCase("forward")) {
                    // Normal forward to US
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Under Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Under Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    // Update previous remark
                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Under Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Section Officer)')";
                    jdbcTemplate.update(update);
                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
                    // Forward again to US after backward
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Under Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Under Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Under Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and status='backward to MORD(Section Officer)'";
                    jdbcTemplate.update(update);
                } else if (map.get("send").toString().equalsIgnoreCase("forwardDS")) {
                    // New: SO directly forward to DS
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Deputy Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Deputy Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Deputy Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Section Officer)')";
                    jdbcTemplate.update(update);
                } else if (map.get("send").toString().equalsIgnoreCase("forwardviewDS")) {
                    // New: Forward again to DS after backward
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Deputy Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Deputy Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Deputy Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and status='backward to MORD(Section Officer)'";
                    jdbcTemplate.update(update);
                }
            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
                if (map.get("send").toString().equalsIgnoreCase("backwardSO")) {
                    // US -> SO
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Section Officer)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','backward to MORD(Section Officer)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String updatePending = "update remarks set forwarded=true,status='backward' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Under Secretary)' OR status='backward to MORD(Under Secretary)' )";
                    jdbcTemplate.update(updatePending);

                    String update = "update remarks set status='Backward To MORD(Section Officer)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and status='forwarded to MORD(Under Secretary)'";
                    jdbcTemplate.update(update);
                } else if (map.get("send").toString().equalsIgnoreCase("backwardSIRD")) {
                    // US -> SIRD (discard case)
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "sird";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','backward to sird','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Under Secretary)')";
                    jdbcTemplate.update(update);

                    // Discard logic
                    String update2 = "update combined_proposal set combinedstatus='discard' where combined_proposal_id='" + map.get("proposalId") + "'";
                    jdbcTemplate.update(update2);

                    String update3 = "update remarks set status='discard' where status!='backward to sird' and combined_proposal_id='" + map.get("proposalId") + "'";
                    jdbcTemplate.update(update3);

                    String update4="update forward_proposal set combined='"+"false"+"' where sird_id='"+map.get("userid")+"' and financialyear='"+map.get("financialYear")+"' and installmentno='"+map.get("installmentType")+"'";
                    jdbcTemplate.update(update4);

                    String update5="update final_proposal set combined='"+"false"+"' where user_id='"+map.get("userid")+"' and financialyear='"+map.get("financialYear")+"' and installmentno='"+map.get("installmentType")+"'";
                    jdbcTemplate.update(update5);
                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
                    // US forward again to DS after backward
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Deputy Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Deputy Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Deputy Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and status='backward to MORD(Under Secretary)'";
                    jdbcTemplate.update(update);
                } else {
                    // Normal forward US -> DS
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Deputy Secretary)";

                    String sql = "insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('" + map.get("proposalId") + "','pending at MORD(Deputy Secretary)','" + map.get("remarks") + "','" + lowerDesignation + "','" + upperDesignation + "','" +
                            map.get("userid") + "','" + map.get("stateName") + "','" + map.get("financialYear") + "','" + map.get("installmentType") + "')";
                    jdbcTemplate.update(sql);

                    String update = "update remarks set forwarded=true,status='forwarded to MORD(Deputy Secretary)' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Under Secretary)' or status='forwarded to MORD(Under Secretary)' )";
                    jdbcTemplate.update(update);
                }
            }
            else if(map.get("userType").toString().equalsIgnoreCase("MORD(Deputy Secretary)")) {
                if(map.get("send").toString().equalsIgnoreCase("SIRD")) {
                    // DS -> SIRD (discard)
                    lowerDesignation="MORD(Deputy Secretary)";
                    upperDesignation="sird";

                    String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('"+map.get("proposalId")+"','backward to sird','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+
                            map.get("userid")+"','"+map.get("stateName")+"','"+map.get("financialYear")+"','"+map.get("installmentType")+"')";
                    jdbcTemplate.update(sql);

                    String update="update remarks set forwarded=true where combined_proposal_id='"+map.get("proposalId")+"' and (status='pending at MORD(Deputy Secretary)')";
                    jdbcTemplate.update(update);

                    String update2="update combined_proposal set combinedstatus='discard' where combined_proposal_id='"+map.get("proposalId")+"'";
                    jdbcTemplate.update(update2);

                    String update3="update remarks set status='discard' where status!='backward to sird' and combined_proposal_id='"+map.get("proposalId")+"'";
                    jdbcTemplate.update(update3);

                    String update4="update forward_proposal set combined='false' where sird_id='"+map.get("userid")+"' and financialyear='"+map.get("financialYear")+"' and installmentno='"+map.get("installmentType")+"'";
                    jdbcTemplate.update(update4);

                    String update5="update final_proposal set combined='false' where user_id='"+map.get("userid")+"' and financialyear='"+map.get("financialYear")+"' and installmentno='"+map.get("installmentType")+"'";
                    jdbcTemplate.update(update5);

                } else if(map.get("send").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
                    // DS -> US
                    lowerDesignation="MORD(Deputy Secretary)";
                    upperDesignation="MORD(Under Secretary)";

                    String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('"+map.get("proposalId")+"','backward to MORD(Under Secretary)','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+
                            map.get("userid")+"','"+map.get("stateName")+"','"+map.get("financialYear")+"','"+map.get("installmentType")+"')";
                    jdbcTemplate.update(sql);
                    String updatePending = "update remarks set forwarded=true,status='backward' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Deputy Secretary)' )";
                    jdbcTemplate.update(updatePending);
                    String update="update remarks set status='Backward To MORD(Under Secretary)' where combined_proposal_id='"+map.get("proposalId")+"' and status='forwarded to MORD(Deputy Secretary)'";
                    jdbcTemplate.update(update);

                } else if(map.get("send").toString().equalsIgnoreCase("MORD(Section Officer)")) {
                    // DS -> SO
                    lowerDesignation="MORD(Deputy Secretary)";
                    upperDesignation="MORD(Section Officer)";

                    String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename,financialyear,installmentno) " +
                            "values('"+map.get("proposalId")+"','backward to MORD(Section Officer)','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+
                            map.get("userid")+"','"+map.get("stateName")+"','"+map.get("financialYear")+"','"+map.get("installmentType")+"')";
                    jdbcTemplate.update(sql);
                    String updatePending = "update remarks set forwarded=true,status='backward' " +
                            "where combined_proposal_id='" + map.get("proposalId") + "' and (status='pending at MORD(Deputy Secretary)')";
                    jdbcTemplate.update(updatePending);
                    String update="update remarks set status='Backward To MORD(Section Officer)' where combined_proposal_id='"+map.get("proposalId")+"' and status='forwarded to MORD(Deputy Secretary)'";
                    jdbcTemplate.update(update);

                } else {
                    // DS final approval
                    String sql="update remarks set forwarded=true,status='approved' where combined_proposal_id='"+map.get("proposalId")+"' and status='pending at MORD(Deputy Secretary)' ";
                    jdbcTemplate.update(sql);

                    String update="update remarks set status='approved' where combined_proposal_id='"+map.get("proposalId")+"'";
                    jdbcTemplate.update(update);
                }
            }

        } catch (Exception e) {
            String str[] = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            service.logError(str[0], "addForwardCombinedProposalNew");
            return "Not Forwarded";
        }
        return "Forwarded Successfully";
    }
    private void updateStatusOnly(Integer proposalIdInt, String newStatus, String matchStatus) {
        String sql = "UPDATE remarks SET status = ? WHERE combined_proposal_id = ? AND status = ?";
        jdbcTemplate.update(sql, newStatus, proposalIdInt, matchStatus);
    }

    private void updateRemarksOnlyForwarded(Integer proposalIdInt, String matchStatus) {
        String sql = "UPDATE remarks SET forwarded = true WHERE combined_proposal_id = ? AND status = ?";
        jdbcTemplate.update(sql, proposalIdInt, matchStatus);
    }

    private void updateAllRemarksToApproved(Integer proposalIdInt) {
        String sql = "UPDATE remarks SET status = ? WHERE combined_proposal_id = ?";
        jdbcTemplate.update(sql, "approved", proposalIdInt);
    }


    public List<Map<String, Object>> getAllCombinedDetails(String userType) {
        try {
            String sql = "SELECT DISTINCT ON (combined_proposal_id) financialyear AS \"financialYear\", " + "installmentno AS \"installmentType\", " + "combined_proposal_id, " + "sirdid AS userid, " + "status, remarks, forwarded, " + "statename AS \"stateName\" " + "FROM remarks " + "WHERE upperdesignation = ? " + "AND status NOT LIKE '%backward%' " + "AND status NOT IN ('discard', 'approved')";

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, userType);

            int serialNo = 1;
            for (Map<String, Object> map : list) {
                map.put("serialNo", serialNo++);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getAggregatedProposalCounts() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Map<String, Object>> aggregatedMap = new LinkedHashMap<>();
        int serialNo = 1;

        try {
            String sqlRecurring = "SELECT count(*) AS count, financialyear, installmentno, sirdname, UPPER(usertype) AS usertype, user_id " + "FROM final_proposal GROUP BY financialyear, installmentno, sirdname, usertype, user_id ORDER BY usertype DESC";
            List<Map<String, Object>> recurringList = jdbcTemplate.queryForList(sqlRecurring);

            String sqlNonRecurring = "SELECT count(*) AS count, financialyear, sirdname, UPPER(usertype) AS usertype, user_id " + "FROM nonrecurring_proposal GROUP BY financialyear, sirdname, usertype, user_id ORDER BY usertype DESC";
            List<Map<String, Object>> nonRecurringList = jdbcTemplate.queryForList(sqlNonRecurring);

            // Handle Recurring Proposals
            for (Map<String, Object> map : recurringList) {
                String userType = map.get("usertype").toString();
                String userId = map.get("user_id").toString();

                if (userType.equalsIgnoreCase("sird") || userType.equalsIgnoreCase("etc")) {
                    String table = userType.equalsIgnoreCase("sird") ? "sird" : "etc";
                    String userSql = "SELECT \"Name\" AS userName, state_name FROM " + table + " WHERE " + table + "_id = '" + userId + "'";
                    List<Map<String, Object>> userInfo = jdbcTemplate.queryForList(userSql);
                    if (!userInfo.isEmpty()) {
                        map.put("userName", userInfo.get(0).get("userName"));
                        map.put("stateName", userInfo.get(0).get("state_name"));
                    }

                    String key = map.get("sirdname") + "-" + userType + "-" + map.get("financialyear") + "-" + map.get("userName") + "-" + map.get("stateName");

                    Map<String, Object> aggregated = aggregatedMap.getOrDefault(key, new HashMap<>());
                    aggregated.put("sirdname", map.get("sirdname"));
                    aggregated.put("usertype", userType);
                    aggregated.put("financialyear", map.get("financialyear"));
                    aggregated.put("stateName", map.get("stateName"));
                    aggregated.put("recurringCount", map.get("count"));
                    aggregated.put("installmentnoRecurring", map.get("installmentno"));
                    aggregated.putIfAbsent("nonrecurringCount", 0);
                    aggregatedMap.put(key, aggregated);
                }
            }

            // Handle Non-Recurring Proposals
            for (Map<String, Object> map : nonRecurringList) {
                String userType = map.get("usertype").toString();
                String userId = map.get("user_id").toString();

                if (userType.equalsIgnoreCase("sird") || userType.equalsIgnoreCase("etc")) {
                    String table = userType.equalsIgnoreCase("sird") ? "sird" : "etc";
                    String userSql = "SELECT \"Name\" AS userName, state_name FROM " + table + " WHERE " + table + "_id = '" + userId + "'";
                    List<Map<String, Object>> userInfo = jdbcTemplate.queryForList(userSql);
                    if (!userInfo.isEmpty()) {
                        map.put("userName", userInfo.get(0).get("userName"));
                        map.put("stateName", userInfo.get(0).get("state_name"));
                    }

                    String key = map.get("sirdname") + "-" + userType + "-" + map.get("financialyear") + "-" + map.get("userName") + "-" + map.get("stateName");

                    Map<String, Object> aggregated = aggregatedMap.getOrDefault(key, new HashMap<>());
                    aggregated.put("sirdname", map.get("sirdname"));
                    aggregated.put("usertype", userType);
                    aggregated.put("financialyear", map.get("financialyear"));
                    aggregated.put("stateName", map.get("stateName"));
                    aggregated.put("nonrecurringCount", map.get("count"));
                    aggregated.put("installmentnoNonRecurring", map.get("installmentno"));
                    aggregated.putIfAbsent("recurringCount", 0);
                    aggregatedMap.put(key, aggregated);
                }
            }

            // Add serial numbers and finalize result
            for (Map<String, Object> map : aggregatedMap.values()) {
                map.put("serial_number", serialNo++);
                resultList.add(map);
            }

            resultList.sort(Comparator.comparingInt(m -> (int) m.get("serial_number")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }


    /**
     * Get All Faculty Details
     *
     * @return
     */
    public List<Map<String, Object>> getBackwardProposalForSirdNew(String userType, String userName) {
        try {
            int userId = this.getUserId(userName, userType);
           String useridSttring = String.valueOf(userId);
            String sql = "SELECT * FROM remarks WHERE status = ? AND sirdid = ?";
            return jdbcTemplate.queryForList(sql, "backward to sird", useridSttring);

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
    public List<Map<String, Object>> getApprovedProposalForAsNew() {
        try {
//			int i=1;
//			String sql="select distinct status,combined_proposal_id as combinedproposalid,remarks,lowerdesignation as fromuser,upperdesignation as touser,forwardeddate,statename as \"stateName\"  from remarks where status='"+"approved"+"' and upperdesignation='"+"MORD(Deputy Secretary)"+"'";
//			List<Map<String,Object>>list=jdbcTemplate.queryForList(sql);
//			for(Map<String,Object>map:list)
//			{
//				map.put("serialNo", i++);
//			}
//			return list;
            int i = 1;
            String sql = "SELECT DISTINCT  financialyear As \"financialYear\",installmentno As \"installmentType\", status, combined_proposal_id AS combinedproposalid, remarks, lowerdesignation AS fromuser, upperdesignation AS touser, forwardeddate, statename AS \"stateName\" FROM remarks WHERE status = 'approved' AND upperdesignation = 'MORD(Deputy Secretary)'  order by combined_proposal_id desc ";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

            // Use a Map to store unique results based on stateName
            Map<String, Map<String, Object>> uniqueResultsMap = new LinkedHashMap<>();

            for (Map<String, Object> map : list) {
                // Get the value of stateName for uniqueness
                String stateName = map.get("combinedproposalid").toString();

                // Add the entry if it doesn't already exist (based on stateName)
                if (!uniqueResultsMap.containsKey(stateName)) {
                    uniqueResultsMap.put(stateName, map);
                    map.put("serialNo", i++); // Add serial number
                }
            }

            // Convert the Map back to a List
            List<Map<String, Object>> uniqueList = new ArrayList<>(uniqueResultsMap.values());

            return uniqueList;

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> GetTrainingCalendarWithCount() {
        try {
            String sql = "select count(*) as title,entry_date as date from training_final group by entry_date";
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> GetTrainingCalendarWithCountWithFinnancialYear(String date, int pageSize, int pageNumber) {
        try {
            String sql = "SELECT * FROM training_final " + "WHERE entry_date = ? " + "ORDER BY training_id DESC " + "OFFSET ? LIMIT ?";

            int offset = pageNumber * pageSize;

            return jdbcTemplate.queryForList(sql, date, offset, pageSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    @Transactional
    public String addForwardCombinedProposalNewNonRecurring(Map<String, Object> map) {
        try {
            String lowerDesignation = "";
            String upperDesignation = "";
            int proposalIdInt = Integer.parseInt(map.get("proposalId").toString());
             int userIdInt= Integer.parseInt(map.get("userid").toString());


            if (map.get("userType").toString().equalsIgnoreCase("sird")) {
                lowerDesignation = "sird";
                upperDesignation = "MORD(Section Officer)";

                String sql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                jdbcTemplate.update(sql, proposalIdInt, "pending at MORD(Section Officer)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

                String update = "UPDATE combined_proposal SET forwarded = true WHERE combined_proposal_id = ?";
                jdbcTemplate.update(update, proposalIdInt);

            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Section Officer)")) {

                if (map.get("send").toString().equalsIgnoreCase("backward")) {
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "sird";

// Insert into remarks_non_recurring
                    String sql = "INSERT INTO remarks_non_recurring (combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(sql, proposalIdInt, "backward to sird", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update combined_proposal
                    String update2 = "UPDATE combined_proposal SET combinedstatus = ? WHERE combined_proposal_id = ?";
                    jdbcTemplate.update(update2, "discard", proposalIdInt);

// Update remarks_non_recurring
                    String update3 = "UPDATE remarks_non_recurring SET status = ? WHERE status != ? AND combined_proposal_id = ?";
                    jdbcTemplate.update(update3, "discard", "backward to sird", proposalIdInt);

// Update forward_proposal
                    String update4 = "UPDATE forward_proposal SET combined = ? WHERE sird_id = ? AND financialyear = ?";
                    jdbcTemplate.update(update4, "false",userIdInt, map.get("financialYear"));

// Update nonrecurring_proposal
                    String update5 = "UPDATE nonrecurring_proposal SET combined = ? WHERE user_id = ? AND financialyear = ?";
                    jdbcTemplate.update(update5, "false", userIdInt, map.get("financialYear"));


                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Under Secretary)";

// Insert into remarks_non_recurring
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(insertSql, proposalIdInt, "pending at MORD(Under Secretary)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update existing record's status and forwarded flag
                    String updateSql = "UPDATE remarks_non_recurring " + "SET forwarded = true, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";
                    jdbcTemplate.update(updateSql, "forwarded to MORD(Under Secretary)", proposalIdInt, "backward to MORD(Section Officer)");

                } else {
                    lowerDesignation = "MORD(Section Officer)";
                    upperDesignation = "MORD(Under Secretary)";

// INSERT statement
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertSql, proposalIdInt, "pending at MORD(Under Secretary)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// UPDATE statement
                    String updateSql = "UPDATE remarks_non_recurring " + "SET forwarded = true, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                    jdbcTemplate.update(updateSql, "forwarded to MORD(Under Secretary)", proposalIdInt, "pending at MORD(Section Officer)");

                }

            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
                if (map.get("send").toString().equalsIgnoreCase("backward")) {
                    if (map.get("status").toString().equalsIgnoreCase("sird")) {
                        lowerDesignation = "MORD(Under Secretary)";
                        upperDesignation = "sird";

// INSERT into remarks_non_recurring
                        String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        jdbcTemplate.update(insertSql, proposalIdInt, "backward to sird", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// UPDATE combined_proposal
                        String updateCombinedProposal = "UPDATE combined_proposal SET combinedstatus = ? WHERE combined_proposal_id = ?";
                        jdbcTemplate.update(updateCombinedProposal, "discard", proposalIdInt);

// UPDATE remarks_non_recurring (set status = 'discard' for all except 'backward to sird')
                        String updateRemarksNonRecurring = "UPDATE remarks_non_recurring " + "SET status = ? WHERE status != ? AND combined_proposal_id = ?";
                        jdbcTemplate.update(updateRemarksNonRecurring, "discard", "backward to sird", proposalIdInt);

// UPDATE forward_proposal
                        String updateForwardProposal = "UPDATE forward_proposal " + "SET combined = ? WHERE sird_id = ? AND financialyear = ?";
                        jdbcTemplate.update(updateForwardProposal, "false", userIdInt, map.get("financialYear"));

// UPDATE nonrecurring_proposal
                        String updateNonRecurringProposal = "UPDATE nonrecurring_proposal " + "SET combined = ? WHERE user_id = ? AND financialyear = ?";
                        jdbcTemplate.update(updateNonRecurringProposal, "false",userIdInt, map.get("financialYear"));

                    } else {
                        lowerDesignation = "MORD(Under Secretary)";
                        upperDesignation = "MORD(Section Officer)";

// INSERT into remarks_non_recurring
                        String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                        jdbcTemplate.update(insertSql, proposalIdInt, "backward to MORD(Section Officer)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// UPDATE forwarded=true, status='backward' where pending at MORD(Under Secretary)
                        String updateForwardedSql = "UPDATE remarks_non_recurring SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                        jdbcTemplate.update(updateForwardedSql, true, "backward", proposalIdInt, "pending at MORD(Under Secretary)");

// UPDATE status from 'forwarded to MORD(Under Secretary)' to 'Backward To MORD(Section Officer)'
                        String updateStatusSql = "UPDATE remarks_non_recurring SET status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                        jdbcTemplate.update(updateStatusSql, "Backward To MORD(Section Officer)", proposalIdInt, "forwarded to MORD(Under Secretary)");

                    }
                } else if (map.get("send").toString().equalsIgnoreCase("backwardview")) {
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Section Officer)";

// Insert into remarks_non_recurring
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertSql, proposalIdInt, "backward to MORD(Section Officer)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update status to 'backward' where previous status was 'backward to MORD(Under Secretary)'
                    String updateBackwardSql = "UPDATE remarks_non_recurring SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                    jdbcTemplate.update(updateBackwardSql, true, "backward", proposalIdInt, "backward to MORD(Under Secretary)");

// Update status to 'Backward To MORD(Section Officer)' where status was 'forwarded to MORD(Under Secretary)'
                    String updateStatusSql = "UPDATE remarks_non_recurring SET status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                    jdbcTemplate.update(updateStatusSql, "Backward To MORD(Section Officer)", proposalIdInt, "forwarded to MORD(Under Secretary)");


                } else if (map.get("send").toString().equalsIgnoreCase("forwardview")) {
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Deputy Secretary)";

// Insert new remark
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertSql, proposalIdInt, "pending at MORD(Deputy Secretary)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update previous remark
                    String updateSql = "UPDATE remarks_non_recurring " + "SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                    jdbcTemplate.update(updateSql, true, "forwarded to MORD(Deputy Secretary)", proposalIdInt, "backward to MORD(Under Secretary)");

                } else {
                    lowerDesignation = "MORD(Under Secretary)";
                    upperDesignation = "MORD(Deputy Secretary)";

// Insert new remark
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertSql, proposalIdInt, "pending at MORD(Deputy Secretary)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update forwarded status of previous record
                    String updateSql = "UPDATE remarks_non_recurring " + "SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";

                    jdbcTemplate.update(updateSql, true, "forwarded to MORD(Deputy Secretary)", proposalIdInt, "pending at MORD(Under Secretary)");

                }
            } else if (map.get("userType").toString().equalsIgnoreCase("MORD(Deputy Secretary)")) {
                if (map.get("send").toString().equalsIgnoreCase("backward")) {

                    lowerDesignation = "MORD(Deputy Secretary)";
                    upperDesignation = "sird";

// Insert new backward remark to SIRD
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    int proposalIdint = Integer.parseInt(map.get("proposalId").toString());
                    jdbcTemplate.update(insertSql, proposalIdint, "backward to sird", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update current remark as forwarded
                    String update1 = "UPDATE remarks_non_recurring SET forwarded = ? " + "WHERE combined_proposal_id = ? AND status = ?";
                    jdbcTemplate.update(update1, true, proposalIdint, "pending at MORD(Deputy Secretary)");

// Update combined_proposal table to discard
                    String update2 = "UPDATE combined_proposal SET combinedstatus = ? WHERE combined_proposal_id = ?";
                    jdbcTemplate.update(update2, "discard", proposalIdint);

// Discard all other remarks (not 'backward to sird') for same proposal
                    String update3 = "UPDATE remarks_non_recurring SET status = ? " + "WHERE status != ? AND combined_proposal_id = ?";
                    jdbcTemplate.update(update3, "discard", "backward to sird", proposalIdint);

// Update forward_proposal table's combined flag
                    String update4 = "UPDATE forward_proposal SET combined = ? " + "WHERE sird_id = ? AND financialyear = ?";
                    jdbcTemplate.update(update4, false, userIdInt, map.get("financialYear"));

// Update nonrecurring_proposal table's combined flag
                    String update5 = "UPDATE nonrecurring_proposal SET combined = ? " + "WHERE user_id = ? AND financialyear = ?";
                    jdbcTemplate.update(update5, false, userIdInt, map.get("financialYear"));


                } else if (map.get("send").toString().equalsIgnoreCase("MORD(Under Secretary)")) {
//    			lowerDesignation="MORD(Deputy Secretary)";
//    			upperDesignation="MORD(Additional Secretary)";
//    			String sql="insert into remarks(combined_proposal_id,status,remarks,lowerdesignation,upperdesignation,sirdid,statename)values('"+map.get("proposalId")+"','"+"pending at MORD(Additional Secretary)"+"','"+map.get("remarks")+"','"+lowerDesignation+"','"+upperDesignation+"','"+map.get("userid")+"','"+map.get("stateName")+"')";
//    			jdbcTemplate.update(sql);
//    			String update="update remarks set forwarded=true,status='"+"forwarded to MORD(Additional Secretary)"+"'  where combined_proposal_id='"+map.get("proposalId")+"' and status='"+"pending at MORD(Deputy Secretary)"+"'";
//    			jdbcTemplate.update(update);
                    lowerDesignation = "MORD(Deputy Secretary)";
                    upperDesignation = "MORD(Under Secretary)";
                    int proposalIdint = Integer.parseInt(map.get("proposalId").toString());
// Insert a new backward remark to Under Secretary
                    String insertSql = "INSERT INTO remarks_non_recurring " + "(combined_proposal_id, status, remarks, lowerdesignation, upperdesignation, sirdid, statename, financialyear) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    jdbcTemplate.update(insertSql, proposalIdint, "backward to MORD(Under Secretary)", map.get("remarks"), lowerDesignation, upperDesignation, map.get("userid"), map.get("stateName"), map.get("financialYear"));

// Update previous remark to forwarded + backward
                    String update1 = "UPDATE remarks_non_recurring SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";
                    jdbcTemplate.update(update1, true, "backward", proposalIdint, "pending at MORD(Deputy Secretary)");

// Update status from forwarded to Deputy Secretary → Backward To Under Secretary
                    String update2 = "UPDATE remarks_non_recurring SET status = ? " + "WHERE combined_proposal_id = ? AND status = ?";
                    jdbcTemplate.update(update2, "Backward To MORD(Under Secretary)", proposalIdint, "forwarded to MORD(Deputy Secretary)");


                } else {
                    int proposalIdint = Integer.parseInt(map.get("proposalId").toString());
                    // Update the specific remark to 'approved' if it's currently pending at MORD(Deputy Secretary)
                    String updatePendingSql = "UPDATE remarks_non_recurring SET forwarded = ?, status = ? " + "WHERE combined_proposal_id = ? AND status = ?";
                    jdbcTemplate.update(updatePendingSql, true, "approved", proposalIdint, "pending at MORD(Deputy Secretary)");

// Update all remarks for the proposal to 'approved'
                    String updateAllSql = "UPDATE remarks_non_recurring SET status = ? " + "WHERE combined_proposal_id = ?";
                    jdbcTemplate.update(updateAllSql, "approved", proposalIdint);

                }
            }


        } catch (Exception e) {
            String[] str = e.getMessage().split("\\[");
            System.out.println(str[0]);
            e.printStackTrace();
            service.logError(str[0], "addForwardCombinedProposalNewNonRecurring");
            return "Not Forwarded";
        }
        return "Forwarded Successfully";
    }

    //remarks listing
    public List<Map<String, Object>> GetAllCombinedListNewNonRecuring(String userType) {
        try {
            String sql = "SELECT combined_proposal_id, sirdid AS userid, status, remarks, forwarded, " +
                    "financialyear AS \"financialYear\", statename AS \"stateName\" " +
                    "FROM remarks_non_recurring " +
                    "WHERE upperdesignation = ? AND status NOT IN (?, ?)";

            return jdbcTemplate.queryForList(sql, userType, "discard", "approved");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getBackwardProposalForSirdNewNonRecurring(String userType, String userName) {
        try {
            int userId = this.getUserId(userName, userType);
            String userIdString = String.valueOf(userId);
            String sql = "SELECT * FROM remarks_non_recurring WHERE status = ? AND sirdid = ?";
            return jdbcTemplate.queryForList(sql, "backward to sird", userIdString);

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }


    public List<Map<String, Object>> getApprovedProposalForAsNewNonRecurring() {
        try {
            int i = 1;
            String sql = "SELECT DISTINCT financialyear AS \"financialYear\", statename AS \"stateName\", status, " +
                    "combined_proposal_id AS combinedproposalid, remarks, lowerdesignation AS fromuser, " +
                    "upperdesignation AS touser, forwardeddate " +
                    "FROM remarks_non_recurring " +
                    "WHERE status = ? AND upperdesignation = ? " +
                    "ORDER BY combined_proposal_id DESC";

            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, "approved", "MORD(Deputy Secretary)");


            // Use a Map to store unique results based on stateName
            Map<String, Map<String, Object>> uniqueResultsMap = new LinkedHashMap<>();

            for (Map<String, Object> map : list) {
                // Get the value  for uniqueness
                String stateName = map.get("combinedproposalid").toString();

                // Add the entry if it doesn't already exist
                if (!uniqueResultsMap.containsKey(stateName)) {
                    uniqueResultsMap.put(stateName, map);
                    map.put("serialNo", i++); // Add serial number
                }
            }

            // Convert the Map back to a List
            List<Map<String, Object>> uniqueList = new ArrayList<>(uniqueResultsMap.values());

            return uniqueList;
        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    @Transactional
    public String deleteUpdateTrainingCalendar(Map<String, Object> map) {
        try {
            int userId = this.getUserId(map);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> trainingCalender = (List<Map<String, Object>>) map.get("trainingCalender");
            int proposalIdint = Integer.parseInt(map.get("proposalId").toString());
// Safely delete using parameterized query
            String deleteSql = "DELETE FROM training_calender_final WHERE proposalno = ?";
            jdbcTemplate.update(deleteSql, proposalIdint);

// Insert each training calendar entry securely
            String insertCalenderQry = "INSERT INTO training_calender_final " +
                    "(traningStartDate, nameOfFaculty, placeOfTraining, subjectOfTraining, noOfTrainees, targetGroup, " +
                    "proposalno, userId, totalduration, trainingenddate, financialyear, installmentno) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            for (Map<String, Object> trainingCalenderMap : trainingCalender) {

                String proposedDateStr = trainingCalenderMap.get("proposedDate").toString();
                LocalDate localDate = LocalDate.parse(proposedDateStr, dateFormatter);
                LocalDateTime localStartDate = localDate.atStartOfDay(); // sets time to 00:00:00


                String proposedDateEnd = trainingCalenderMap.get("enddate").toString();
                LocalDate localDate1 = LocalDate.parse(proposedDateEnd, dateFormatter);
                LocalDateTime localEndDate = localDate1.atStartOfDay(); // sets time to 00:00:00

                jdbcTemplate.update(insertCalenderQry,
                        localStartDate,
                        trainingCalenderMap.get("facultyName"),
                        trainingCalenderMap.get("venue"),
                        trainingCalenderMap.get("trainingSubject"),
                        trainingCalenderMap.get("trainessNumber"),
                        trainingCalenderMap.get("targetGroup"),
                        proposalIdint,
                        userId,
                        trainingCalenderMap.get("totalDuration"),
                        localEndDate,
                        trainingCalenderMap.get("financialYear"),
                        trainingCalenderMap.get("installmentType")
                );
            }
            return "Updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Not Updated";
        }
    }


    public List<Map<String, Object>> getBackwardProposalForAllRecurring(String userType) {
        try {
            int i = 1;
            String sql = "SELECT financialyear AS \"financialYear\", installmentno AS \"installmentType\", combined_proposal_id, sirdid AS userid, status, remarks, forwarded, statename AS \"stateName\" " +
                    "FROM remarks WHERE status = ?";
            String statusValue = "backward to " + userType;
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, statusValue);

            for (Map<String, Object> map : list) {
                map.put("serialNo", i++);
            }
            return list;
        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public List<Map<String, Object>> getSanctionedAmountStatusRecurring(String combinedProposalId) {
        try {
            int combinedProposalIdInt = Integer.parseInt(combinedProposalId);
            String sql = "SELECT proposalid, etcsproposalid FROM combined_proposal WHERE combined_proposal_id = ?";
            List<Map<String, Object>> combinedProposal = jdbcTemplate.queryForList(sql, combinedProposalIdInt);

            List<Map<String, Object>> santionDetails = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : combinedProposal) {
                Map<String, Object> sanction = new HashMap<String, Object>();

                String sirdProposalId = map.get("proposalid").toString();
                int sirdProposalIdInt = Integer.parseInt(sirdProposalId.toString());
                String sql1 = "SELECT sirdname FROM final_proposal WHERE proposalid = ?";
                List<Map<String, Object>> result = jdbcTemplate.queryForList(sql1, sirdProposalIdInt);

                String sirdName = result.isEmpty() ? null : result.get(0).get("sirdname").toString();
                if (!sirdProposalId.equalsIgnoreCase("0")) {
                    String sql2 = "SELECT COUNT(*) FROM proposal_sanction WHERE proposalid = ?";
                    int count = jdbcTemplate.queryForObject(sql2, Integer.class, sirdProposalIdInt);

                    if (count == 0) {
                        sanction.put("sirdProposalId", sirdProposalId);
                        sanction.put("sirdName", sirdName);
                        sanction.put("sanctionStatus", false);
                    } else {
                        sanction.put("sirdProposalId", sirdProposalId);
                        sanction.put("sirdName", sirdName);
                        sanction.put("sanctionStatus", true);
                    }
                    santionDetails.add(sanction);
                }

                String etcProposalId = map.get("etcsproposalid").toString();

                if (!etcProposalId.equalsIgnoreCase("0")) {
                    if (etcProposalId.contains(",")) {
                        String[] str = etcProposalId.split(",");
                        for (int i = 0; i < str.length; i++) {
                            sanction = new HashMap<String, Object>();
                            int etcidInt = Integer.parseInt(str[i].toString());
                            String etcName = jdbcTemplate.queryForObject(
                                    "SELECT sirdname FROM final_proposal WHERE proposalid = ?",
                                    String.class,
                                    etcidInt
                            );

                            int count = jdbcTemplate.queryForObject(
                                    "SELECT COUNT(*) FROM proposal_sanction WHERE proposalid = ?",
                                    Integer.class,
                                    etcidInt
                            );

                            if (count == 0) {
                                sanction.put("etcProposalId", str[i]);
                                sanction.put("etcName", etcName);
                                sanction.put("sanctionStatus", false);
                                santionDetails.add(sanction);
                            } else {
                                sanction.put("etcProposalId", str[i]);
                                sanction.put("etcName", etcName);
                                sanction.put("sanctionStatus", true);
                                santionDetails.add(sanction);
                            }
                            count = 0;
                        }

                    } else {
                        int etcProposalIddInt = Integer.parseInt(etcProposalId.toString());
                        sanction = new HashMap<String, Object>();
                        String etcName = jdbcTemplate.queryForObject(
                                "select sirdname from final_proposal where proposalid = ?",
                                String.class,
                                etcProposalIddInt
                        );

                        int count = jdbcTemplate.queryForObject(
                                "select count(*) from proposal_sanction where proposalid = ?",
                                Integer.class,
                                etcProposalIddInt
                        );

                        if (count == 0) {
                            sanction.put("etcProposalId", etcProposalId);
                            sanction.put("etcName", etcName);
                            sanction.put("sanctionStatus", false);
                            santionDetails.add(sanction);
                        } else {
                            sanction.put("etcProposalId", etcProposalId);
                            sanction.put("etcName", etcName);
                            sanction.put("sanctionStatus", true);
                            santionDetails.add(sanction);
                        }
                        count = 0;
                    }
                }

            }

            return santionDetails;

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
}
