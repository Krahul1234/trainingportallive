package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SIRDDao {
	
	@Autowired
	private JdbcTemplate jdbctemplate;
	
	/** Add SIRD Details
     * @param map
     * @return
     */
	public List<Map<String,Object>>getSirdDetails(Map<String,Object>map)
    {
        try
        {
        	String tableName="";
			/**
			 *  check login type
			 */
			if(map.get("key").toString().equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(map.get("key").toString().equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}
			String sql="select id from"+" "+tableName+" "+"where username='"+map.get("username")+"'";
            int id=Integer.parseInt(jdbctemplate.queryForList(sql).get(0).get("sird_id").toString());
            
            List<Map<String,Object>>list=jdbctemplate.queryForList("select \"Name\",contact,email from"+tableName+ "where id='"+id+"'");
            
            Map<String,Object>map1=jdbctemplate.queryForList("select total_population_state,total_rural_population,percent_rural_population from demographic where sird_id='"+id+"'").get(0);
            list.add(map1);
            return list;
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }
	
	
	public List<Map<String,Object>>sirdDetails(int pageSize,int pageNumber)
	{
		try
		{
			String sql = "select * from demographic order by id desc  offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
			return jdbctemplate.queryForList(sql);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
    
	/** Add SIRD Details
     * @param map
     * @return
     */
	public int updateSirdDetails(Map<String,Object>map)
    {
        try
        {
        	String sql="update sird set \"Name\"=?,contact=?,email=? where sird_id=?";
        	
        	return jdbctemplate.update(sql,map.get("name"),map.get("mobileNo"),map.get("emailId"),map.get("id"));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
       return 0;
    }
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSirdById(String id) {
		try {
			String sql = "select * from sird where sird_id='"+id+"'";
			
			return jdbctemplate.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public int deleteSirdById(String id) {
		try {
			String sql = "delete from sird where sird_id='"+id+"'";
			
			return jdbctemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Get All Faculty Details
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSirdDetails(String userName) {
		try {
			String sql="select sird_id from loginmaster_sird where username='"+userName+"'";
			String sirdId=jdbctemplate.queryForList(sql).get(0).get("sird_id").toString();
			String sql2="select \"Name\" as name from sird where sird_id='"+sirdId+"' ";
			String name=jdbctemplate.queryForList(sql2).get(0).get("name").toString();
			
			String proposalSql="Select proposalid,proposaldate, proposal_no,totaldemand,proposaldate,user_id from final_proposal where usertype='sird' and user_id='"+sirdId+"'";
			List<Map<String,Object>>proposallist=jdbctemplate.queryForList(proposalSql);
			for(Map<String,Object>map:proposallist)
			{
				map.put("name",name);
				
			}
			return proposallist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

}
