package com.nic.trainingportal.controller;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.NonRecuringService;
import com.nic.trainingportal.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class NonRecuringController {
    @Autowired
    private NonRecuringService nonrecuring;
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    public Utility utility;
    @Autowired
    private HttpServletRequest httpservletrequest;


    @PostMapping(value = "addRecurringProposal")
    private Map<String, Object> addProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, nonrecuring.addRecurringProposal(map));
            dataMap.put("message", "Success");
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @PostMapping (value = "getNonRecurringProposal")
    private Map<String, Object> getRecurringProposal(@RequestBody Map<String,Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String sql = "SELECT usertype FROM nonrecurring_proposal WHERE proposalid = ?";
            int propsalInt = Integer.parseInt(map.get("proposalId").toString());
            String userType = jdbc.queryForList(sql, propsalInt).get(0).get("usertype").toString();

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, nonrecuring.getNonRecurringProposal(map.get("proposalId").toString()));
            dataMap.put("userType", userType);

            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }

    @PostMapping(value = "updateNonRecurringProposal")
    private Map<String, Object> updateNonRecurringProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, nonrecuring.updateNonRecurringProposal(map));
            dataMap.put("message", "Success");
            return dataMap;
        } catch (Exception e) {
            dataMap.put("message", "Record Not Saved");
            dataMap.put(Literal.status, Literal.errorCode);
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }
}
