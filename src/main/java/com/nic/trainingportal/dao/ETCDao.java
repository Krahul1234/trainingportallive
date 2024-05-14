package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ETCDao {
	
	
	

	@Autowired
	private JdbcTemplate jdbctemplate;
	
	/** Add SIRD Details
     * @param map
     * @return
     */
	public List<Map<String,Object>>getEtcDetails()
    {
        try
        {
            String sql="select * from etc";
            return jdbctemplate.queryForList(sql);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }
    

}
