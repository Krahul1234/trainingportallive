package com.nic.trainingportal.controller;

import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.EtcSirdForwardProposalService;
import com.nic.trainingportal.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class EtcSirdForwardProposalController {
    @Autowired
    public Utility utility;
    @Autowired
    private HttpServletRequest httpservletrequest;
    @Autowired
    private JWT jwt;

    @Autowired
    private EtcSirdForwardProposalService etcsirdforward;

    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getforwardEtcProposal")
    public Map<String, Object> getforwardEtcProposal(@RequestHeader String token, @RequestBody Map<String, Object> maps) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String userName = jwt.extractUsername(token);


            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getforwardEtcProposal(userName, (String) maps.get("userType"), (String) maps.get("installmentType"), (String) maps.get("financialYear")));
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "addForwardProposal")
    public Map<String, Object> addForwardProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("message", etcsirdforward.addForwardProposalNew(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "updateForwardProposalNew")
    public Map<String, Object> updateForwardProposalNew(@RequestHeader String token , @RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
            String username = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.updateForwardProposalNew(map));
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getForwardProposalForSird")
    public Map<String, Object> getForwardProposalForSird(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getForwardProposalForSird(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }

    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getForwardProposalForEtc")
    public Map<String, Object> getForwardProposalForEtc(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getForwardProposalForEtc(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getBackwardProposalForEtc")
    public Map<String, Object> getBackwardProposalForEtc(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getBackwardProposalForEtc(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    @PostMapping(value = "getforwardEtcProposalNonRecuring")
    public Map<String, Object> getforwardEtcProposalNonRecuring(@RequestHeader String token, @RequestBody Map<String,Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }
//            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getforwardEtcProposalNonRecuring(map.get("userName").toString(),map.get("userType").toString()));
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "addForwardProposalNonRecurring")
    public Map<String, Object> addForwardProposalNonRecurring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("message", etcsirdforward.addForwardProposalNonRecurring(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "updateForwardProposalNewNonRecurring")
    public Map<String, Object> updateForwardProposalNewNonRecurring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.updateForwardProposalNewNonRecurring(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getForwardProposalForSirdNonRecurring")
    public Map<String, Object> getForwardProposalForSirdNonRecurring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getForwardProposalForSirdNonRecurring(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getForwardProposalForEtcNonRecurring")
    public Map<String, Object> getForwardProposalForEtcNonRecurring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getForwardProposalForEtcNonRecurring(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = "getBackwardProposalForEtcNonRecuring")
    public Map<String, Object> getBackwardProposalForEtcNonRecuring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
        try {
//			/**
//			 *  check token is valid or not
//			 */
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, etcsirdforward.getBackwardProposalForEtcNonRecuring(map));

            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }


    }


}
