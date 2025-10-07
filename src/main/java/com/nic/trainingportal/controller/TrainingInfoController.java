package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.TrainingInfoDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.TrainingInfoService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class TrainingInfoController {
    /**
     * create logger class object
     */
    @Autowired
    private JdbcTemplate jdbctemplate;
    private static final Logger logger = LoggerFactory.getLogger(TrainingInfoController.class);
    @Autowired
    private TrainingInfoService trainingInfoService;
    @Autowired
    private HttpServletRequest httpservletrequest;

    @Autowired
    private JWT jwt;

    @Autowired
    public Utility utility;
    @Autowired
    private TrainingInfoDao traininginfodao;

    @PostMapping(value = "addTrainingInfo")
    public Map<String, Object> addTrainingInfo(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            int userId = traininginfodao.getUserId(map);

            if (traininginfodao.exist(userId, map)) {
                dataMap.put(Literal.message, "Training Info Details Already Exist");
                return dataMap;

            }
            /**
             *  check token is valid or not
             */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            if (Utility.checkNull(map.get("functional"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide functional status");
                return dataMap;
            }

            if (Utility.checkNull(map.get("building"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide building availability");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of permanent faculty");
                return dataMap;
            }
            try {
                int permanentFaculty = Integer.parseInt(map.get("number_of_permanent_faculty").toString().trim());
                if (permanentFaculty < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of permanent faculty must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of permanent faculty must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of contractual faculty");
                return dataMap;
            }
            try {
                int contractualFaculty = Integer.parseInt(map.get("number_of_contractual_faculty").toString().trim());
                if (contractualFaculty < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of contractual faculty must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of contractual faculty must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_other_staff"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of other staff");
                return dataMap;
            }
            try {
                int otherStaff = Integer.parseInt(map.get("number_of_other_staff").toString().trim());
                if (otherStaff < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of other staff must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of other staff must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hostel_facility"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly specify hostel facility availability");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_seat"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of seats");
                return dataMap;
            }
            try {
                int seatCount = Integer.parseInt(map.get("number_of_seat").toString().trim());
                if (seatCount < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of seats must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of seats must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("lab_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide lab capacity");
                return dataMap;
            }
            try {
                int labCapacity = Integer.parseInt(map.get("lab_capacity").toString().trim());
                if (labCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Lab capacity must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Lab capacity must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("lab_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of computers");
                return dataMap;
            }
            try {
                int labNumber = Integer.parseInt(map.get("lab_number").toString().trim());
                if (labNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of computers must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of computers must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hall_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of training halls");
                return dataMap;
            }
            try {
                int hallNumber = Integer.parseInt(map.get("hall_number").toString().trim());
                if (hallNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of training halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of training halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hall_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of training halls");
                return dataMap;
            }
            try {
                int hallCapacity = Integer.parseInt(map.get("hall_capacity").toString().trim());
                if (hallCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of training halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of training halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("dining_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of dining halls");
                return dataMap;
            }
            try {
                int diningNumber = Integer.parseInt(map.get("dining_number").toString().trim());
                if (diningNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of dining halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of dining halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("dining_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of dining halls");
                return dataMap;
            }
            try {
                int diningCapacity = Integer.parseInt(map.get("dining_capacity").toString().trim());
                if (diningCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of dining halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of dining halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_kitchens"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of kitchens");
                return dataMap;
            }
            try {
                int kitchensNumber = Integer.parseInt(map.get("number_of_kitchens").toString().trim());
                if (kitchensNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of kitchens must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of kitchens must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("auditorium_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of auditoriums");
                return dataMap;
            }
            try {
                int auditoriumNumber = Integer.parseInt(map.get("auditorium_number").toString().trim());
                if (auditoriumNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of auditoriums must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of auditoriums must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("auditorium_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of auditoriums");
                return dataMap;
            }
            try {
                int auditoriumCapacity = Integer.parseInt(map.get("auditorium_capacity").toString().trim());
                if (auditoriumCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of auditoriums must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of auditoriums must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("remarks"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide remarks");
                return dataMap;
            }
            String remarks = map.get("remarks").toString().trim();
            if (!remarks.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Remarks must contain only alphabets and spaces");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
              int result =trainingInfoService.addTrainingInfo(map);
                dataMap.put(Literal.statusCode, result);

            if (result == 0) {
                dataMap.put(Literal.message, "Record not inserted");
            } else {
                dataMap.put(Literal.message, "Record inserted successfully");
            }
            dataMap.remove(Literal.statusCode);
            return dataMap;

        }catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }

    @PostMapping(value = "gettraininginfo")
    public Map<String, Object> getTrainingInfo(@RequestBody Map<String,Object>maps, @RequestHeader String token) {
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

            String userName = jwt.extractUsername(token);
            int userId = traininginfodao.getUserId(userName, (String) maps.get("userType"));
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, trainingInfoService.getAllFaculty((int)maps.get("pageSize"), (int)maps.get("pageNumber"), userId, (String) maps.get("userType")));
            dataMap.put("totalCount", traininginfodao.getTrainingInfoCount(userId, (String) maps.get("userType")));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }

    @PostMapping(value = "updatetraininginfo")
    public Map<String, Object> updateTrainingInfo(@RequestBody Map<String, Object> map) {
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
            if (Utility.checkNull(map.get("functional"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide functional status");
                return dataMap;
            }

            if (Utility.checkNull(map.get("building"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide building availability");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_permanent_faculty"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of permanent faculty");
                return dataMap;
            }
            try {
                int permanentFaculty = Integer.parseInt(map.get("number_of_permanent_faculty").toString().trim());
                if (permanentFaculty < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of permanent faculty must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of permanent faculty must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_contractual_faculty"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of contractual faculty");
                return dataMap;
            }
            try {
                int contractualFaculty = Integer.parseInt(map.get("number_of_contractual_faculty").toString().trim());
                if (contractualFaculty < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of contractual faculty must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of contractual faculty must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_other_staff"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of other staff");
                return dataMap;
            }
            try {
                int otherStaff = Integer.parseInt(map.get("number_of_other_staff").toString().trim());
                if (otherStaff < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of other staff must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of other staff must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hostel_facility"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly specify hostel facility availability");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_seat"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of seats");
                return dataMap;
            }
            try {
                int seatCount = Integer.parseInt(map.get("number_of_seat").toString().trim());
                if (seatCount < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of seats must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of seats must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("lab_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide lab capacity");
                return dataMap;
            }
            try {
                int labCapacity = Integer.parseInt(map.get("lab_capacity").toString().trim());
                if (labCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Lab capacity must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Lab capacity must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("lab_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of computers");
                return dataMap;
            }
            try {
                int labNumber = Integer.parseInt(map.get("lab_number").toString().trim());
                if (labNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of computers must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of computers must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hall_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of training halls");
                return dataMap;
            }
            try {
                int hallNumber = Integer.parseInt(map.get("hall_number").toString().trim());
                if (hallNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of training halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of training halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("hall_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of training halls");
                return dataMap;
            }
            try {
                int hallCapacity = Integer.parseInt(map.get("hall_capacity").toString().trim());
                if (hallCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of training halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of training halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("dining_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of dining halls");
                return dataMap;
            }
            try {
                int diningNumber = Integer.parseInt(map.get("dining_number").toString().trim());
                if (diningNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of dining halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of dining halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("dining_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of dining halls");
                return dataMap;
            }
            try {
                int diningCapacity = Integer.parseInt(map.get("dining_capacity").toString().trim());
                if (diningCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of dining halls must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of dining halls must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("number_of_kitchens"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of kitchens");
                return dataMap;
            }
            try {
                int kitchensNumber = Integer.parseInt(map.get("number_of_kitchens").toString().trim());
                if (kitchensNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of kitchens must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of kitchens must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("auditorium_number"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide number of auditoriums");
                return dataMap;
            }
            try {
                int auditoriumNumber = Integer.parseInt(map.get("auditorium_number").toString().trim());
                if (auditoriumNumber < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Number of auditoriums must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Number of auditoriums must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("auditorium_capacity"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide capacity of auditoriums");
                return dataMap;
            }
            try {
                int auditoriumCapacity = Integer.parseInt(map.get("auditorium_capacity").toString().trim());
                if (auditoriumCapacity < 0) {
                    dataMap.put(Literal.status, Literal.badReuqest);
                    dataMap.put(Literal.message, "Capacity of auditoriums must be 0 or greater");
                    return dataMap;
                }
            } catch (NumberFormatException e) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Capacity of auditoriums must be a valid number");
                return dataMap;
            }

            if (Utility.checkNull(map.get("remarks"))) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Kindly provide remarks");
                return dataMap;
            }
            String remarks = map.get("remarks").toString().trim();
            if (!remarks.matches("^[a-zA-Z\\s]+$")) {
                dataMap.put(Literal.status, Literal.badReuqest);
                dataMap.put(Literal.message, "Remarks must contain only alphabets and spaces");
                return dataMap;
            }
             int result=trainingInfoService.updateInformation(map);
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


        }catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }


    @PostMapping(value = Webhook.getTrainingInfoById)

    public Map<String, Object> getDemographicById(@RequestBody Map<String, Object> map) {
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
            dataMap.put(Literal.data, trainingInfoService.getTrainingInfoById((Integer) map.get("id")));
            /**
             * print log
             */
            logger.info("Get All Faculty", dataMap);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status,HttpStatusCode.valueOf(404));
            return dataMap;
        }
    }

    @PostMapping(value = Webhook.deleteTrainingInfoById)

    public Map<String, Object> deleteDemographicById(@RequestBody Map<String,Object> map) {
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
            dataMap.put(Literal.data, trainingInfoService.deleteTrainingInfoById(map.get("id").toString()));
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

    @GetMapping(value = "getAllEtc")
    public Map<String, Object> getAllEtcs() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if (utility.getHeaderValue(httpservletrequest)) {
            dataMap.put(Literal.status, Literal.unauthorized);
            dataMap.put(Literal.message, "Your Token Is Expired");
            return dataMap;
        }


        dataMap.put(Literal.status, Literal.successCode);
        dataMap.put(Literal.data, trainingInfoService.getAllEtc());
        return dataMap;
    }

    @GetMapping(value = "getAllSird")
    public Map<String, Object> getAllSird() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        if (utility.getHeaderValue(httpservletrequest)) {
            dataMap.put(Literal.status, Literal.unauthorized);
            dataMap.put(Literal.message, "Your Token Is Expired");
            return dataMap;
        }


        dataMap.put(Literal.status, Literal.successCode);
        dataMap.put(Literal.data, trainingInfoService.getAllSird());
        return dataMap;
    }

    @GetMapping(value = "getCount")
    public Map<String, Object> getAllcount() {
        Map<String, Object> dataMap = new HashMap<String, Object>();

        if (utility.getHeaderValue(httpservletrequest)) {
            dataMap.put(Literal.status, Literal.unauthorized);
            dataMap.put(Literal.message, "Your Token Is Expired");
            return dataMap;
        }

        dataMap.put(Literal.status, Literal.successCode);
        dataMap.put(Literal.data, trainingInfoService.getAllCount());
        return dataMap;
    }


    @GetMapping(value = "getinfo")
    public Map<String, Object> getTrainingInfo(@RequestHeader String token, @RequestParam String userType) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
//        /**
//         *  check token is valid or not
//         */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, trainingInfoService.getTrainingInfo(userName, userType));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }

    @GetMapping(value = "/getCalendar")
    public Map<String, Object> getCalendar(@RequestHeader String token, @RequestParam String userType) {
        Map<String, Object> dataMap = new HashMap<>();

        try {
//         /**
//          * Check if token is valid or not
//          */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

//     
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
//        
            dataMap.put(Literal.data, trainingInfoService.getCalendar(userName, userType));
        } catch (Exception e) {
            // Handle exceptions appropriately
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, e.getMessage()); // Optionally, include error message
        }
        return dataMap;


    }


    @PostMapping(value = "getAllCalendar")
    public Map<String, Object> getAllCal() {
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
            dataMap.put(Literal.data, trainingInfoService.getAllCal());
            dataMap.put("count", traininginfodao.getTrainingCalendarCount());
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }

    @PostMapping(value = "getAllInfo")
    public Map<String, Object> getAllInfo(@RequestBody Map<String,Object> map) {
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
            dataMap.put(Literal.data, trainingInfoService.getAllInfo(map));
//         dataMap.put("count",traininginfodao.getTrainingInfoCount());
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }
    }

    @PostMapping(value = "calendarCount")
    public Map<String, Object> calendarCount() {
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
            dataMap.put(Literal.data, trainingInfoService.calendarCount());

            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            return dataMap;
        }

    }


    @PostMapping(value = "updateTranningProposal")
    public Map<String, Object> updateTranningProposal(@RequestBody Map<String, Object> map) {
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
            dataMap.put(Literal.message, trainingInfoService.updateTranningProposal(map));
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

    @PostMapping(value = "updateFundsProposal")
    public Map<String, Object> updateFundsProposal(@RequestBody Map<String, Object> map) {
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
            dataMap.put(Literal.message, trainingInfoService.updateFundsProposal(map));
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
}
