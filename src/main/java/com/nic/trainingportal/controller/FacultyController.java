package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.FacultyDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.FacultyService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class FacultyController {
    @Autowired
    private JdbcTemplate jdbctemplate;
    @Autowired
    private HttpServletRequest httpservletrequest;

    @Autowired
    public FacultyDao facultydao;

    @Autowired
    private JWT jwt;

    @Autowired
    public Utility utility;
    /**
     * create logger class object
     */
    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);
    @Autowired
    public FacultyService facultyservice;

    /**
     * Add Faculty details
     *
     * @param map
     * @return
     */
    @PostMapping(value = Webhook.addFaculty)
    public Map<String, Object> addFaculty(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
//			if (Utility.checkNull(map.get("username"))) {
//				dataMap.put(Literal.status, Literal.badReuqest);
//				dataMap.put(Literal.message, "Kindly Provide Username");
//				return dataMap;
//			}
//			if (!map.get("username").toString().matches("^[a-zA-Z0-9]+$")) {
//				dataMap.put(Literal.status, Literal.badReuqest);
//				dataMap.put(Literal.message, "Username must contain only alphanumeric characters (no spaces or special characters)");
//				return dataMap;
//			}

            /**
             * check Null
             */
            // Validate 'name'
            if (Utility.checkNull(map.get("name"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Faculty Name");
                return dataMap;
            }
            String name = map.get("name").toString().trim();
            if (!name.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Faculty Name must contain only alphabets and spaces");
                return dataMap;
            }

// Validate 'postHeld'
            if (Utility.checkNull(map.get("postHeld"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Postheld Value");
                return dataMap;
            }
            String postHeld = map.get("postHeld").toString().trim();
            if (!postHeld.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Post Held must contain only alphabets and spaces");
                return dataMap;
            }

// Validate 'scalePay'
            if (Utility.checkNull(map.get("scalePay"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Scale Pay Value");
                return dataMap;
            }
            String scalePay = map.get("scalePay").toString().trim();
            if (!scalePay.matches("^\\d+(\\.\\d+)?$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Scale Pay must be a valid number");
                return dataMap;
            }

// Validate 'typeOfFaculty'
            if (Utility.checkNull(map.get("typeOfFaculty"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Value");
                return dataMap;
            }
            String typeOfFaculty = map.get("typeOfFaculty").toString().trim();
            if (!typeOfFaculty.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Type of Faculty must contain only alphabets and spaces");
                return dataMap;
            }

// Validate 'remarks'
            if (Utility.checkNull(map.get("remarks"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly Provide Remarks Value");
                return dataMap;
            }
            String remarks = map.get("remarks").toString().trim();
            if (!remarks.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Remarks must contain only alphabets and spaces");
                return dataMap;
            }

            /**
             * print log
             */
            logger.info("Add Faculty Request Data", map);
            dataMap.put(Literal.status, Literal.successCode);
            int result = facultyservice.addFaculty(map);
            dataMap.put(Literal.statusCode, result);

            if (result == 0) {
                dataMap.put(Literal.message, "Record not inserted");
            } else {
                dataMap.put(Literal.message, "Record inserted successfully");
            }
            dataMap.remove(Literal.statusCode);
            return dataMap;

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    /**
     * Get Faculty Details
     *
     * @return
     */
    @PostMapping(value = Webhook.getAllFaculty)
    public Map<String, Object> getAllFaculty(@RequestBody Map<String, Object> maps, @RequestHeader String token) {

        int pageSize = (int) maps.get("pageSize");
        int pageNumber = (int) maps.get("pageNumber");

        String userType = maps.get("userType").toString();
        String financialYear = maps.get("financialYear").toString();
        String installmentType = maps.get("installmentType").toString();
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        List<Map<String, Object>> totalRecord = new ArrayList<>();
        try {
            int i = (pageNumber * pageSize) + 1;
            int instituteType = 0;
            if (userType.equalsIgnoreCase("sird")) {
                instituteType = 1;
            } else {
                instituteType = 2;
            }

            String instituteTypeString = String.valueOf(instituteType);
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, facultyservice.getAllFaculty(pageSize, pageNumber, userName, userType, financialYear, installmentType));
            int userId = facultydao.getUserId(userName, userType);
            if ((financialYear != null && financialYear.length() != 0) && (installmentType != null && installmentType.length() != 0)) {
                String sql = "SELECT financialyear AS \"financialYear\", " +
                        "installmentno AS \"installmentType\", " +
                        "faculty_id, name, post AS \"postHeld\", " +
                        "pay_scale AS \"scalePay\", type_of_faculty AS \"typeOfFaculty\", remarks " +
                        "FROM faculty " +
                        "WHERE institutetype = ? AND user_id = ? AND financialyear = ? AND installmentno = ?";

                totalRecord = jdbctemplate.queryForList(
                        sql,
                        instituteTypeString,
                        userId,
                        financialYear,
                        installmentType
                );
                for (Map<String, Object> map : totalRecord) {
                    map.put("serialNo", i++);
                }
            } else {
                String sql = "SELECT financialyear AS \"financialYear\", " +
                        "installmentno AS \"installmentType\", faculty_id, name, " +
                        "post AS \"postHeld\", pay_scale AS \"scalePay\", " +
                        "type_of_faculty AS \"typeOfFaculty\", remarks " +
                        "FROM faculty " +
                        "WHERE institutetype = ? AND user_id = ?";

                totalRecord = jdbctemplate.queryForList(
                        sql,
                        instituteTypeString,
                        userId
                );
                for (Map<String, Object> map : totalRecord) {
                    map.put("serialNo", i++);
                }
            }
            dataMap.put("totalCount", facultydao.getFacultyCount(userId, userType, financialYear, installmentType));
            dataMap.put("totalRecord", totalRecord);
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;
        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @PostMapping(value = Webhook.updateFaculty)
    public Map<String, Object> updateDeleteFaculty(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            } else {
                /**
                 * check Null
                 */
                // Validate 'name'
                if (Utility.checkNull(map.get("name"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Faculty Name");
                    return dataMap;
                }
                String name = map.get("name").toString().trim();
                if (!name.matches("^[a-zA-Z\\s]+$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Faculty Name must contain only alphabets and spaces");
                    return dataMap;
                }

// Validate 'postHeld'
                if (Utility.checkNull(map.get("postHeld"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Postheld Value");
                    return dataMap;
                }
                String postHeld = map.get("postHeld").toString().trim();
                if (!postHeld.matches("^[a-zA-Z\\s]+$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Post Held must contain only alphabets and spaces");
                    return dataMap;
                }

// Validate 'scalePay'
                if (Utility.checkNull(map.get("scalePay"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Scale Pay Value");
                    return dataMap;
                }
                String scalePay = map.get("scalePay").toString().trim();
                if (!scalePay.matches("^\\d+(\\.\\d+)?$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Scale Pay must be a valid number");
                    return dataMap;
                }

// Validate 'typeOfFaculty'
                if (Utility.checkNull(map.get("typeOfFaculty"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Value");
                    return dataMap;
                }
                String typeOfFaculty = map.get("typeOfFaculty").toString().trim();
                if (!typeOfFaculty.matches("^[a-zA-Z\\s]+$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Type of Faculty must contain only alphabets and spaces");
                    return dataMap;
                }

// Validate 'remarks'
                if (Utility.checkNull(map.get("remarks"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Remarks Value");
                    return dataMap;
                }
                String remarks = map.get("remarks").toString().trim();
                if (!remarks.matches("^[a-zA-Z\\s]+$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Remarks must contain only alphabets and spaces");
                    return dataMap;
                }
                if (Utility.checkNull(map.get("financialYear"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Financial Year Value");
                    return dataMap;
                }
                String financialYear = map.get("financialYear").toString().trim();
                if (!financialYear.matches("^[0-9]{4}-[0-9]{4}$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Financial Year must be in YYYY-YYYY format (e.g. 2023-2024)");
                    return dataMap;
                }// Validate 'installmentType'
                if (Utility.checkNull(map.get("installmentType"))) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Kindly Provide Installment Type Value");
                    return dataMap;
                }
                String installmentType = map.get("installmentType").toString().trim();
                if (!installmentType.matches("^[a-zA-Z0-9\\s]+$")) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Installment Type must contain only letters, numbers, and spaces");
                    return dataMap;
                }

                /**
                 * print log
                 */
                logger.info("Update Faculty Details", map);

                dataMap.put(Literal.status, Literal.successCode);
                 int result=facultyservice.updateFacultyDetails(map);
                dataMap.put(Literal.statusCode, result);

                if (result == 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Record not Updated");
                } else {
                    dataMap.put(Literal.status, Literal.successCode);
                    dataMap.put(Literal.message, "Record Updated successfully");
                }
                dataMap.remove(Literal.statusCode);
                return dataMap;

            }

        } catch (Exception e) {
            /**
             * print error log
             */
            logger.error("An error occurred while doing something", e);
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @PostMapping(value = Webhook.getFacultyById)

    public Map<String, Object> getFacultyById(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, facultyservice.getFacultyById((map.get("id")).toString()));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @PostMapping(value = Webhook.deleteFacultyById)
    public Map<String, Object> deleteDemographicById(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, facultyservice.deleteFacultyById(map.get("id").toString()));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @PostMapping(value = Webhook.getFacultyName)

    public Map<String, Object> getFacultyName(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, facultyservice.getFacultyName(map));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }

    @PostMapping("deleteFaculty1")
    private Map<String, Object> deleteFaculty1(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("message", facultyservice.updateDeleteFaculty(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }

}
