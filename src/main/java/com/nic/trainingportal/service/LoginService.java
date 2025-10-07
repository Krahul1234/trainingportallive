package com.nic.trainingportal.service;

import com.nic.trainingportal.controller.AESUtil;
import com.nic.trainingportal.dao.LoginDao;
import com.nic.trainingportal.jwt.JWT;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class LoginService {
    @Autowired

    private JdbcTemplate jdbctemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired

    public LoginDao logindao;

    @Autowired
    public JWT jwt;

    public String getLoginDetails(Map<String, Object> map) {
        try {String stateName = "";
            String tableName = "";
            String key = (map.get("key") != null) ? map.get("key").toString().toLowerCase() : "";

            if (key.equals("etc") || key.equals("sird")) {
                int userId = this.getUserId2(map);

                // Set tableName safely based on key
                if (key.equals("etc")) {
                    tableName = "etc";
                } else if (key.equals("sird")) {
                    tableName = "sird";
                }

                // Prepare SQL with parameter placeholder
                String sql = "SELECT state_name FROM " + tableName + " WHERE " + key + "_id = ?";

                List<Map<String, Object>> results = jdbctemplate.queryForList(sql, userId);
                if (!results.isEmpty()) {
                    stateName = results.get(0).get("state_name").toString();
                    map.put("stateName", stateName);
                } else {
                    // Optionally handle no result found
                    map.put("stateName", "");
                }
            }

/**
 * Get Login Details
 */
            List<Map<String, Object>> data = logindao.getLoginDetails(map);
            if (data != null && !data.isEmpty()) {
                for (Map<String, Object> data_map : data) {
                    String inputUsername = map.get("username").toString();
                    String storedUsername = data_map.get("username").toString();


                    // Check username and password match
                    if (inputUsername.equalsIgnoreCase(storedUsername) &&
                            passwordEncoder.matches(map.get("password").toString(), data_map.get("encrypted_password").toString())) {

                        if (key.equals("ministry")) {
                            return jwt.generateToken(inputUsername, map.get("minstryTypeLogin").toString());
                        } else {
                            return jwt.generateToken(inputUsername, key, map.get("stateName").toString());
                        }
                    }
                }
                return "Unauthorized User";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unauthorized User";
    }


    private int getUserId2(Map<String, Object> map) {
        try {String tableName = "";
            String key = (map.get("key") != null) ? map.get("key").toString().toLowerCase() : "";

            if (key.equals("etc")) {
                tableName = "loginmaster_etc";
            } else if (key.equals("sird")) {
                tableName = "loginmaster_sird";
            } else {
                key = "min";
                map.put("key", key);
                tableName = "loginmaster_ministry";
            }

// Build SQL - tableName and column names cannot be parameterized, so validate carefully before
// For added safety, you could check these values against a whitelist if you want.

            String idColumn = key + "_id";

            String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE username = ?";

            List<Map<String, Object>> results = jdbctemplate.queryForList(sql, map.get("username"));

            if (results.isEmpty()) {
                // handle no user found - maybe throw exception or return null/zero
                return 0;  // or throw new UsernameNotFoundException("User not found");
            }

            return Integer.parseInt(results.get(0).get(idColumn).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public Map<String, Object> changePassword(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        String tableName;
        int userId = getUserId2(map);
        String key = map.get("key").toString().toLowerCase();

        try {switch (key) {
            case "etc":
                tableName = "loginmaster_etc";
                break;
            case "sird":
                tableName = "loginmaster_sird";
                break;
            default:
                key = "min";
                tableName = "loginmaster_ministry";
                break;
        }

// Query user by ID
            String sql = "SELECT * FROM " + tableName + " WHERE " + key + "_id = ?";
            List<Map<String, Object>> users = jdbctemplate.queryForList(sql, userId);

            if (users.isEmpty()) {
                result.put("status", HttpStatus.SC_NOT_FOUND);
                result.put("message", "User not found.");
                return result;
            }

            Map<String, Object> user = users.get(0);
            String storedHashedPassword = user.get("encrypted_password").toString();
            String oldPassword = map.get("oldPassword").toString();
            String newPassword = map.get("newPassword").toString();

// Verify old password
            if (!passwordEncoder.matches(oldPassword, storedHashedPassword)) {
                result.put("status", HttpStatus.SC_UNAUTHORIZED);
                result.put("message", "Old password does not match.");
                return result;
            }

            if (oldPassword.equals(newPassword)) {
                result.put("status", HttpStatus.SC_CONFLICT);
                result.put("message", "New password cannot be the same as old password.");
                return result;
            }

// Validate new password format
            if (!isValidPassword(newPassword)) {
                result.put("status", HttpStatus.SC_NOT_ACCEPTABLE);
                result.put("message", "Invalid password format.");
                return result;
            }

// Hash new password and update
            String newHashedPassword = passwordEncoder.encode(newPassword);
            String newEnryptPassword = AESUtil.encrypt(newPassword);
            String updateSql = "UPDATE " + tableName + " SET encrypted_password = ?, password_encrypted = ? WHERE " + key + "_id = ?";
            int rowsAffected = jdbctemplate.update(updateSql, newHashedPassword, newEnryptPassword, userId);

            if (rowsAffected > 0) {
                result.put("status", HttpStatus.SC_OK);
                result.put("message", "Password successfully updated.");
            } else {
                result.put("status", HttpStatus.SC_INTERNAL_SERVER_ERROR);
                result.put("message", "Password update failed.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.put("message", "Something went wrong.");
        }

        return result;
    }


    public static boolean isValidPassword(String password) {
        return password.length() >= 8 && Pattern.compile("[A-Z]").matcher(password).find() && Pattern.compile("[a-z]").matcher(password).find() && Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find() && !hasConsecutiveNumbers(password);
    }


    private static boolean hasConsecutiveNumbers(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char first = password.charAt(i);
            char second = password.charAt(i + 1);
            char third = password.charAt(i + 2);
            if (Character.isDigit(first) && Character.isDigit(second) && Character.isDigit(third) && (second - first == 1) && (third - second == 1)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> logout(String token) {
        return jwt.invalidateToken(token);

    }

    public void updateEncrypt() {
        try {
            logindao.updateEncrypt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
