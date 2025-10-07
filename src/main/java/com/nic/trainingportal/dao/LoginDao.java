package com.nic.trainingportal.dao;

import com.nic.trainingportal.controller.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LoginDao {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AESUtil aes;


    public List<Map<String, Object>> getLoginDetails(Map<String, Object> map) {
        String tableName = "";
        String sql = "";
        String user_name = "";

        try {
            String key = map.get("key").toString();

            if (key.equalsIgnoreCase("etc")) {
                tableName = "loginmaster_etc";
                sql = "SELECT username,encrypted_password FROM " + tableName + " WHERE username = ?";
                return jdbctemplate.queryForList(sql, map.get("username"));

            } else if (key.equalsIgnoreCase("sird")) {
                tableName = "loginmaster_sird";
                sql = "SELECT username,encrypted_password FROM " + tableName + " WHERE username = ?";
                return jdbctemplate.queryForList(sql, map.get("username"));

            } else if (key.equalsIgnoreCase("nirdpr")) {
                tableName = "loginmaster_ministry";
                sql = "SELECT username,encrypted_password FROM " + tableName + " WHERE username = ?";
                return jdbctemplate.queryForList(sql, map.get("username"));

            } else {
                // ministry login with user_name condition
                String minstryTypeLogin = map.get("minstryTypeLogin").toString();

                switch (minstryTypeLogin) {
                    case "MORD(Section Officer)":
                        user_name = "MoRD(SO)";
                        break;
                    case "MORD(Under Secretary)":
                        user_name = "MoRD(US)";
                        break;
                    case "MORD(Deputy Secretary)":
                        user_name = "MoRD(DS)";
                        break;
                    case "MORD(Joint Secretary)":
                        user_name = "MoRD(JS)";
                        break;
                    default:
                        user_name = "MoRD(AS)";
                }

                tableName = "loginmaster_ministry";
                sql = "SELECT username,user_name,encrypted_password FROM " + tableName + " WHERE user_name = ?";
                return jdbctemplate.queryForList(sql, user_name);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    public void updateEncrypt() throws Exception {
        String sql = "select * from loginmaster_sird";
        List<Map<String, Object>> sirdmap = jdbctemplate.queryForList(sql);
        for (Map<String, Object> map : sirdmap) {
            String userPassword = passwordEncoder.encode((String) map.get("user_password"));
            String userPassword1 = AESUtil.encrypt((String) map.get("user_password"));
            String updatesql ="update loginmaster_sird set encrypted_password = '"+userPassword1+"' where username = ?";
            jdbctemplate.update(updatesql, map.get("username"));
        }

        String sql1 = "select * from loginmaster_etc";
        List<Map<String, Object>> etcmap = jdbctemplate.queryForList(sql1);
        for (Map<String, Object> map : etcmap) {
            String userPassword = passwordEncoder.encode((String) map.get("user_password"));
            String userPassword1 = AESUtil.encrypt((String) map.get("user_password"));
            String updatesql ="update loginmaster_etc set encrypted_password = '"+userPassword1+"' where username = ?";
            jdbctemplate.update(updatesql, map.get("username"));
        }

        String sql2 = "select * from loginmaster_ministry";
        List<Map<String, Object>> mordmap = jdbctemplate.queryForList(sql2);
        for (Map<String, Object> map : mordmap) {
            String userPassword = passwordEncoder.encode((String) map.get("user_password"));
            String userPassword1 = AESUtil.encrypt((String) map.get("user_password"));
            String updatesql ="update loginmaster_ministry set encrypted_password = '"+userPassword1+"' where username = ?";
            jdbctemplate.update(updatesql, map.get("username"));
        }
    }
}
