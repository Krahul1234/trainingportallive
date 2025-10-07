package com.nic.trainingportal.controller;

import com.nic.trainingportal.dao.LoginDao;
import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.LoginService;
import com.nic.trainingportal.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins="https://mordtrainingportal.nic.in")
@RestController
//@RequestMapping("/ao/trainingportal")
@RequestMapping("/tp/trainingportal")
public class LoginController {

    @Autowired
    public AESUtil aes;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public LoginService loginservice;
    @Autowired
    public LoginDao logindao;
    @Autowired
    public Utility utility;
    @Autowired
    private HttpServletRequest httpservletrequest;
    @Autowired
    JWT jwt;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("static-access")
    @PostMapping(value = "GetLoginDetails")

    public Map<String, Object> getLoginDetails(@RequestBody Map<String, Object> map) {
        String check = "";
        Map<String, Object> dataMap = new HashMap<String, Object>(6);
        try {

            String userCaptcha = map.get("userCaptcha") != null ? map.get("userCaptcha").toString() : null;
            String captchaToken = map.get("captchaToken") != null ? map.get("captchaToken").toString() : null;

            Map<String, Object> captchaValidationResult = validateCaptcha(userCaptcha, captchaToken);
            if (captchaValidationResult != null) {
                return captchaValidationResult;
            }


            String username = aes.decrypt(map.get("username").toString());
            String password = aes.decrypt(map.get("password").toString());
            map.put("username", username);
            map.put("password", password);

            check = loginservice.getLoginDetails(map);

            if (check.equalsIgnoreCase("Unauthorized User")) {
                dataMap.put(Literal.status, Literal.unauthorized);
                dataMap.put(Literal.message, "Unauthorized User");
                dataMap.put("token", "");
                return dataMap;
            }
//			List<Map<String,Object>>data=logindao.getLoginDetails(map);
            dataMap.put(Literal.status, Literal.successCode);
            dataMap.put(Literal.message, "User Auntheticate Successfully");
            dataMap.put("token", loginservice.getLoginDetails(map));

            if (captchaToken != null) {
                Utility.markCaptchaTokenAsUsed(captchaToken);
            }
//			for(Map<String,Object>data_map:data) {
//			dataMap.put("name", data_map.get("user_name"));}
            return dataMap;

        } catch (Exception e) {
            dataMap.put(Literal.status, Literal.errorCode);
            dataMap.put(Literal.message, "Something Went Wrong");
            e.printStackTrace();
            return dataMap;
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader String token) {
        if (token != null && !token.isEmpty()) {
           return loginservice.logout(token);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("status", "400");
            map.put("message", "Something Went Wrong");
            return map;

        }
    }

    @PostMapping(value = "changePassword")
    public Map<String, Object> changePassword(@RequestBody Map<String, Object> map) throws Exception {
        Map<String, Object> dataMap = new HashMap<String, Object>(6);

        if(utility.getHeaderValue(httpservletrequest))
        {
            dataMap.put(Literal.status, Literal.unauthorized);
            dataMap.put(Literal.message, "Your Token Is Expired");
            return dataMap;
        }

        String username = aes.decrypt(map.get("username").toString());
        String newPassword = aes.decrypt(map.get("newPassword").toString());
        String oldPassword = aes.decrypt(map.get("oldPassword").toString());
        map.put("username", username);
        map.put("newPassword", newPassword);
        map.put("oldPassword", oldPassword);

        return loginservice.changePassword(map);
    }


    @GetMapping(value = "encodedPassword")

    public void encodedPassword() {

        String sql = "select * from loginmaster_ministry";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : queryForList) {
            String encript = passwordEncoder.encode(map.get("user_password").toString());
            String update = "UPDATE loginmaster_ministry SET encrypted_password = ? WHERE min_id = ?";
            jdbcTemplate.update(update, encript, map.get("min_id"));


        }
    }

    @GetMapping(value = "chechPassword")
    public void chechPassword() {
        String sql = "select * from loginmaster_ministry";
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);


        for (Map<String, Object> map1 : queryForList) {
            String pass = map1.get("user_password").toString();
            String encr = map1.get("encrypted_password").toString();
            boolean matches = passwordEncoder.matches(pass, encr);
            if (matches) {
                System.out.println("t");
            } else {
                System.out.println("False  for  " + map1.get("min_id"));
            }
        }

    }

    @GetMapping(value = "GetPublic")
    public Map<String, String> getPublicKey() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", AESUtil.getPublicKey());
        return map;
    }

    @PostMapping("/encrypt")
    public String encryptMessage(@RequestBody String message) {
        try {
            return AESUtil.encrypt(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during encryption: " + e.getMessage();
        }
    }
    @PostMapping(value = "GetEncrypt")
    public String getPublicKey1(@RequestBody Map<String, Object> map) throws Exception {
        return AESUtil.decrypt(map.get("text").toString());
    }

    private Map<String, Object> validateCaptcha(String userCaptcha, String captchaToken) {
        Map<String, Object> response = new HashMap<>();
        if (userCaptcha == null || captchaToken == null || userCaptcha.isEmpty()) {
            response.put(Literal.status, Literal.unauthorized);
            response.put(Literal.message, "CAPTCHA Required");
            response.put("token", "");
            return response;
        }
//		if (Utility.isCaptchaTokenUsed(captchaToken)) {
//			response.put(Literal.status, Literal.unauthorized);
//			response.put(Literal.message, "Captcha token already used");
//			response.put("token", "");
//			return response;
//		}

        String actualCaptcha = Utility.getCaptchaFromToken(captchaToken);
        if (actualCaptcha == null) {
            response.put(Literal.status, Literal.unauthorized);
            response.put(Literal.message, "CAPTCHA Expired or Invalid");
            response.put("token", "");
            return response;
        }

        if (!actualCaptcha.equalsIgnoreCase(userCaptcha)) {
            response.put(Literal.status, Literal.unauthorized);
            response.put(Literal.message, "Invalid CAPTCHA");
            response.put("token", "");
            return response;
        }

        return null; // means captcha validated successfully
    }

    @PostMapping(value = "updateEncrypt")
    public void updateEncrypt(){
//        String password =map.get("password").toString();
        loginservice.updateEncrypt();

    }


    @GetMapping(value = "generateToken")
    public String generateToken(@RequestBody Map<String, Object> map){
       return   jwt.generateToken(map.get("inputUsername").toString(), map.get("key").toString(), map.get("stateName").toString());
    }

}
