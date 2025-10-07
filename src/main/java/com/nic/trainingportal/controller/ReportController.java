package com.nic.trainingportal.controller;

import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.ReportService;
import com.nic.trainingportal.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class ReportController {
	@Autowired
	private JdbcTemplate jdbctemplate;
	@Autowired
	private ReportService report;
    @Autowired private JWT jwt;
    @Autowired
    public Utility utility;
    @Autowired
    private HttpServletRequest httpservletrequest;

    @PostMapping(value ="GetEtcReport")
    public Map<String,Object>GetAllEtcAddToCombinedNonRecuring(@RequestHeader String token,@RequestBody(required = false) Map<String,Object>map)

    {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try
        {

            if(utility.getHeaderValue(httpservletrequest))
            {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Your Token Is Expired");
                dataMap.put(Literal.statusCode, Literal.zero);

                return dataMap;
            }
            String userType = (map != null && map.get("userType") != null) ? map.get("userType").toString() : null;

            String userName = jwt.extractUsername(token);
            dataMap.put("Data",report.getEtcReport(userName,userType));
            dataMap.put(Literal.status, Literal.successCode);

            String sql = "SELECT COUNT(*) FROM final_proposal WHERE usertype = ?";
            int count = jdbctemplate.queryForObject(sql, Integer.class, "etc");
            dataMap.put("count", count);
            /**
             * print log
             */
            return dataMap;

        }catch(Exception e)
        {
            e.printStackTrace();
            dataMap.put(Literal.message,"Something Went Wrong");
            dataMap.put(Literal.status,Literal.errorCode);
            return dataMap;
        }
    }
	@PostMapping("reportForSird")
    public Map<String, Object> getreportForSird(@RequestHeader String token,@RequestBody(required=false) Map<String,Object> map) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
           try
           {
               /**
                *  check token is valid or not
                */
                if(utility.getHeaderValue(httpservletrequest))
                {
                       dataMap.put(Literal.status, Literal.unauthorized);
                       dataMap.put(Literal.message, "Your Token Is Expired");
                       return dataMap;
                }
//               String userName = jwt.extractUsername(token);
               String userName = map != null ? (String) map.get("userName") : null;
               String userType = map != null ? (String) map.get("userType") : null;
                   dataMap.put(Literal.status, Literal.successCode);
                   dataMap.put(Literal.data, report.getreportForSird(userName,userType));


               dataMap.put("count",jdbctemplate.queryForObject("select count(*) from sird", Integer.class));
                   /**
                    * print log
                    */
                   return dataMap;

           }catch(Exception e)
           {
               e.printStackTrace();
               dataMap.put(Literal.message,"Something Went Wrong");
               dataMap.put(Literal.status,Literal.errorCode);
               return dataMap;
           }
    }
	@GetMapping("reportForMinistry")
    public Map<String, Object> getreportForMinistry(@RequestHeader String token,@RequestParam(required=false) String userType) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
           try
           {
               /**
                *  check token is valid or not
                */
                if(utility.getHeaderValue(httpservletrequest))
                {
                       dataMap.put(Literal.status, Literal.unauthorized);
                       dataMap.put(Literal.message, "Your Token Is Expired");
                       return dataMap;
                }
               String userName = jwt.extractUsername(token);
                   dataMap.put(Literal.status, Literal.successCode);
                   dataMap.put(Literal.data, report.getreportForMinistry(userName,userType));
                   dataMap.put("count",jdbctemplate.queryForObject("select count(*) from sird", Integer.class));
                   /**
                    * print log
                    */
                   return dataMap;
                
           }catch(Exception e)
           {
               e.printStackTrace();
               dataMap.put(Literal.message,"Something Went Wrong");
               dataMap.put(Literal.status,Literal.errorCode);
               return dataMap;
           }
    }
	
	@GetMapping("reportForMinistryNonRecurring")
    public Map<String, Object> reportForMinistryNonRecurring(@RequestHeader String token, @RequestParam(required=false) String userType) {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
           try
           {
               /**
                *  check token is valid or not
                */
                if(utility.getHeaderValue(httpservletrequest))
                {
                       dataMap.put(Literal.status, Literal.unauthorized);
                       dataMap.put(Literal.message, "Your Token Is Expired");
                       return dataMap;
                }
               String userName = jwt.extractUsername(token);
                   dataMap.put(Literal.status, Literal.successCode);
                   dataMap.put(Literal.data, report.reportForMinistryNonRecurring(userName,userType));
                   dataMap.put("count",jdbctemplate.queryForObject("select count(*) from sird", Integer.class));
                   /**
                    * print log
                    */
                   return dataMap;
                
           }catch(Exception e)
           {
               e.printStackTrace();
               dataMap.put(Literal.message,"Something Went Wrong");
               dataMap.put(Literal.status,Literal.errorCode);
               return dataMap;
           }
    }


}
