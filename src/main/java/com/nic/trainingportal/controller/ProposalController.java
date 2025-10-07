package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.ProposalDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.ProposalService;
import com.nic.trainingportal.utility.Utility;
import com.nic.trainingportal.webhook.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class ProposalController {
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    public Utility utility;

    @Autowired
    private JWT jwt;

    @Autowired
    private HttpServletRequest httpservletrequest;

    @Autowired
    private ProposalService proposalservice;

    @Autowired
    private ProposalDao dao;

    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = Webhook.getProposal)
    public Map<String, Object> getDemographicDetails(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
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
            dataMap.put("Data", proposalservice.getProposal(map));
            return dataMap;

        } catch (Exception e) {
            map.put(Literal.status, Literal.errorCode);
            map.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return map;
        }

    }

    /**
     * get DemographicDetails
     *
     * @return
     */
    @PostMapping(value = Webhook.forwardProposal)
    public Map<String, Object> forwardPorposal(@RequestBody Map<String, Object> map) {
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
            dataMap.put(Literal.data, proposalservice.addForwardProposal(map));

            return dataMap;

        } catch (Exception e) {
            map.put(Literal.status, Literal.errorCode);
            map.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return map;
        }


    }


    @GetMapping(value = Webhook.getForwardProposal)
    public Map<String, Object> getForwardProposal(@RequestParam String userType, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
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

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("Data", proposalservice.getForwardProposal(userType, userName));
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }

    }

    @GetMapping(value = Webhook.updateForwardProposal)
    public Map<String, Object> updateForwardProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(4);
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
            dataMap.put("Data", proposalservice.updateForwardProposal(map));
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }

    }

    @PostMapping("addProposalNew")
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
            dataMap.put("message", proposalservice.addProposal(map));
            return dataMap;
        } catch (Exception e) {
            dataMap.put("message", "Record Not Saved");
            dataMap.put(Literal.status, Literal.errorCode);
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }

    @PostMapping("getAllProposal")
    public Map<String, Object> getProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String sql = "SELECT usertype, certified FROM final_proposal WHERE proposalid = ?";
            int proposalIdInt = Integer.parseInt(map.get("proposalId").toString());
            Map<String, Object> result = jdbc.queryForMap(sql, proposalIdInt);

            String userType = result.get("usertype").toString();
            String certified = result.get("certified").toString();

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getProposalNew(map.get("proposalId").toString()));
            dataMap.put("userType", userType);
            dataMap.put("certified", certified);
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping(value = Webhook.addCombinedProposal)
    private Map<String, Object> addCombinedProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.addCombinedProposal(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @GetMapping("getCombinedProposal")
    public Map<String, Object> getCombinedProposal(@RequestParam String userType, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getCombinedProposal(userType, userName));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }

    @PostMapping("checkProposal")
    public Map<String, Object> checkProposal(@RequestBody Map<String,Object> map, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("count", proposalservice.checkProposal(map.get("userType").toString(), map.get("userName").toString()));
            dataMap.put(Literal.message, "Your Two Installment have been done");
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping("addSentioned")
    private Map<String, Object> addSanctioned(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.addSantioned(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @PostMapping ("getUpperLevel")
    public Map<String, Object> getUpperLevel(@RequestHeader String token,@RequestBody  Map<String,Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getUpperLevel(map.get("userType").toString()));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping("updateCombinedProposalStatus")
    public Map<String, Object> updateCombinedProposalStatus(@RequestParam String proposalNo, @RequestParam String userType) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.updateCombinedProposalStatus(proposalNo, userType));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }

    @GetMapping("GetAllCombinedList")
    public Map<String, Object> GetAllCombinedList(@RequestParam String userType, @RequestParam(required = false) String approved) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getAllCombinedList(userType, approved));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping("updateforwardProposalCombined")
    public Map<String, Object> updateforwardProposalCombined(@RequestParam String combinedProposalId, @RequestParam String remarks, @RequestParam(required = false) String status, @RequestParam(required = false) String approved) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.updateforwardProposalCombined(combinedProposalId, remarks, status, approved));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }

    @PostMapping("GetCombinedListBySird")
    public Map<String, Object> GetCombinedListBySird(@RequestBody Map<String,Object> map, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.GetCombinedListBySird(map.get("userType").toString(), userName));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping ("GetAllEtcsByCombinedProposalId")
    public Map<String, Object> GetAllEtcsByCombinedProposalId(@RequestHeader String token,@RequestBody Map<String,Object> map1) {
        List<Map<String, Object>> proposal = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            proposal = proposalservice.GetAllEtcsByCombinedProposalId(map1.get("combinedProposalId").toString());

            Iterator<Map<String, Object>> iterator = proposal.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> map = iterator.next();
                if (map.get("demand") != null) {
                    String totalDemandStr = map.get("demand").toString();
                    BigDecimal demand = new BigDecimal(totalDemandStr);
//		            demand = Integer.parseInt(map.get("demand").toString());
                    dataMap.put("demand", demand);
//		            demand = Integer.parseInt(map.get("demand").toString());
                    iterator.remove();  // Safe removal
                }
            }
            dataMap.put(Literal.data, proposal);
//			dataMap.put("demand", demand);
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }

    @PostMapping(value = "getSirdDetailsByCombinedProposalId")
    public Map<String, Object> getSirdDetailsByCombinedProposalId(@RequestHeader String token ,@RequestBody Map<String,Object> map) {
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
            dataMap.put(Literal.data, proposalservice.getSirdDetailsByCombinedId(map.get("combinedProposalId").toString()));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @GetMapping(value = "checkCombinedProposal")

    public Map<String, Object> checkCombinedProposal(@RequestHeader String token, @RequestParam String userType) {
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
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.checkCombinedProposal(userName, userType));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @GetMapping(value = "BackwardCombinedProposalByDs")

    public Map<String, Object> BackwardCombinedProposalByDs(@RequestHeader String token, @RequestParam String userType) {
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
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.BackwardCombinedProposalByDs(userName, userType));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @GetMapping("getCombinedPropoalBoolean")
    public Map<String, Object> getCombinedPropoalBoolean(@RequestParam String userType, @RequestHeader String token, @RequestParam String proposalType) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getCombinedPropoalBoolean(userType, userName, proposalType));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping("updateforwardProposalCombinedByDs")
    public Map<String, Object> updateforwardProposalCombinedByDs(@RequestParam String combinedProposalId, @RequestParam String remarks, @RequestParam String status) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.updateforwardProposalCombinedByDs(combinedProposalId, remarks, status));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping("updateProposal")
    private Map<String, Object> updateProposal(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put("message", proposalservice.updateProposal(map));
            return dataMap;
        } catch (Exception e) {
            dataMap.put("message", "Record Not Saved");
            dataMap.put(Literal.status, Literal.errorCode);
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @PostMapping(value = "addCombinedProposalNew")
    private Map<String, Object> addCombinedProposalNew(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.addCombinedProposalNew(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @PostMapping("GetRemarks")
    public Map<String, Object> GetRemarks(
            @RequestHeader String token,
            @RequestBody(required = false) Map<String, Object> maps) {

        Map<String, Object> dataMap = new HashMap<>();

        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);
                return dataMap;
            }


            if (maps == null) {
                maps = new HashMap<>(); // empty map to avoid NPE
            }


            String proposalId = maps.containsKey("proposalId") ? maps.get("proposalId").toString() : null;
            String userName = maps.containsKey("userName") ? maps.get("userName").toString() : null;
            String userType = maps.containsKey("userType") ? maps.get("userType").toString() : null;
            String backwarded = maps.containsKey("backwarded") ? maps.get("backwarded").toString() : null;
            String financialYear = maps.containsKey("financialYear") ? maps.get("financialYear").toString() : null;
            String installmentType = maps.containsKey("installmentType") ? maps.get("installmentType").toString() : null;


            Object remarks = proposalservice.GetRemarks(
                    proposalId,
                    userName,
                    userType,
                    backwarded,
                    financialYear,
                    installmentType
            );

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, remarks);
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something went wrong");
            dataMap.put(Literal.statusCode, Literal.zero);
            return dataMap;
        }
    }



    @PostMapping("updateSentioned")
    private Map<String, Object> updateSentioned(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.updateSantioned(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @PostMapping(value = "proposalCount")
    public Map<String, Object> proposalCount(@RequestHeader String token, @RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.data, proposalservice.proposalCount(userName, map.get("userType").toString()));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);

    }

    @GetMapping(value = "nonRecurringCount")
    public Map<String, Object> proposalCountNonRec(@RequestParam String status, @RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            dataMap.put(Literal.data, proposalservice.proposalCountNonRec(status, map.get("userType").toString()));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);


    }


    @PostMapping(value = "addCombinedProposalNewNonRecuring")
    private Map<String, Object> addCombinedProposalNewNonRecuring(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {
            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.addCombinedProposalNewNonRecuring(map));
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }


    @GetMapping("getCombinedPropoalBooleanNonRecurring")
    public Map<String, Object> getCombinedPropoalBooleanNonRecurring(@RequestParam String userType, @RequestHeader String token,
                                                                     @RequestParam String proposalType) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.getCombinedPropoalBooleanNonRecurring(userType, userName, proposalType));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping(value = "getRecProposal1")
    public Map<String, Object> getProposal1() {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            dataMap.put(Literal.data, proposalservice.getProposal1());
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);

    }

    @GetMapping(value = "getNonRecProposal1")
    public Map<String, Object> getNonProposal1() {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {
            dataMap.put(Literal.data, proposalservice.getNonProposal1());
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping("GetCombinedListBySirdNonRecurring")
    public Map<String, Object> GetCombinedListBySirdNonRecurring(@RequestBody Map<String,Object> map, @RequestHeader String token) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.GetCombinedListBySirdNonRecurring(map.get("userType").toString(), userName));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping("GetAllCombinedListNonRecurring")
    public Map<String, Object> GetAllCombinedListNonRecurring(@RequestParam String userType, @RequestParam(required = false) String approved) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.GetAllCombinedListNonRecurring(userType, approved));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @GetMapping("updateforwardProposalCombinedNonRecurring")
    public Map<String, Object> updateforwardProposalCombinedNonRecurring(@RequestParam String combinedProposalId, @RequestParam String remarks, @RequestParam String status, @RequestParam(required = false) String approved) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.data, proposalservice.updateforwardProposalCombinedNonRecurring(combinedProposalId, remarks, status, approved));
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping("checkSanctionAmountExist")
    public Map<String, Object> checkSanctionAmountExist(@RequestBody Map<String, Object> map) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put("message", proposalservice.checkSanctionAmountExist(map.get("proposalId").toString()));
            return dataMap;

        } catch (Exception e) {
           e.printStackTrace();
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping("GetAllEtcsByCombinedProposalIdNOnRecurring")
    public Map<String, Object> GetAllEtcsByCombinedProposalIdNOnRecurring( @RequestBody Map<String,Object> map) {
        List<Map<String, Object>> proposal = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }

            dataMap.put(Literal.status, Literal.successCode);
            proposal = proposalservice.GetAllEtcsByCombinedProposalIdNonRecurring(map.get("combinedProposalId").toString());
            dataMap.put(Literal.data, proposal);
            return dataMap;

        } catch (Exception e) {
            // TODO: handle exception
        }
        return new HashMap<String, Object>(0);

    }


    @PostMapping(value = "getSirdDetailsByCombinedProposalIdNonRecurring")

    public Map<String, Object> getSirdDetailsByCombinedProposalIdNonRecurring(@RequestBody Map<String,Object> map ) {
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
            dataMap.put(Literal.data, proposalservice.getSirdDetailsByCombinedIdNonRecurring(map.get("combinedProposalId").toString()));
            return dataMap;

        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put(Literal.message, "Something Went Wrong");
            dataMap.put(Literal.status, Literal.errorCode);
            return dataMap;
        }
    }


    @PostMapping ("GetRemarksNonRecurring")
    public Map<String, Object> GetRemarksNonRecurring(@RequestHeader String token,@RequestBody Map<String,Object> map ) {
        Map<String, Object> dataMap = new HashMap<>();
        try {

            if (utility.getHeaderValue(httpservletrequest)) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userName = jwt.extractUsername(token);
            dataMap.put(Literal.status, Literal.successCode);
            String financialYear = map.get("financialYear") != null ? map.get("financialYear").toString() : null;
            String backwarded = map.get("backwarded") != null ? map.get("backwarded").toString() : null;

             dataMap.put(Literal.data, proposalservice.GetRemarksNonRecurring(
                    map.get("proposalId").toString(),
                    map.get("userName").toString(),
                    map.get("userType").toString(),
                    backwarded,
                    financialYear
            ));
          return  dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);

    }

}

