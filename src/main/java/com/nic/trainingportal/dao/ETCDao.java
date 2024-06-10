package com.nic.trainingportal.dao;

import java.util.ArrayList;
import java.util.HashMap;
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
	public List<Map<String,Object>>getEtcDetails(int pageSize,int pageNumber)
    {
        try
        {
        	String sql = "select * from etc order by etc_id desc  offset ("+pageNumber+"*"+pageSize+")"+" "+"limit"+" "+pageSize;
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
	public int updateEtcDetails(Map<String,Object>map)
    {
        try
        {
        	String sql="update etc set \"Name\"=?,contact_number=?,email=? where etc_id=?";
        	
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
	public List<Map<String, Object>> getEtcById(String id) {
		try {
			String sql = "select * from etc where etc_id='"+id+"'";
			
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
	public int deleteEtcById(String id) {
		try {
			String sql = "delete from etc where etc_id='"+id+"'";
			
			return jdbctemplate.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getUserId(String userName,String userType)
	{
		try
		{
			String tableName="";
			/**
			 *  check login type
			 */
			if(userType.equalsIgnoreCase("etc"))
			{
				tableName="loginmaster_etc";
			}else if(userType.equalsIgnoreCase("sird"))
			{
				tableName="loginmaster_sird";
			}else
			{
				tableName="loginmaster_ministry";
			}
			
			String sql="select"+" "+userType+"_"+ "id  from"+" "+tableName+" "+"where username='"+userName+"'";
			
			return Integer.parseInt(jdbctemplate.queryForList(sql).get(0).get(userType+"_"+"id").toString());			
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
	public List<Map<String, Object>> getAllEtcs(String userName,String userType) {
		try {
			Map<String,Object> etcMaps = new HashMap<>();
			StringBuilder id=new StringBuilder();
			boolean flag=true;
            boolean flag1=true;
			List<Map<String,Object>>proposallist=new ArrayList<Map<String,Object>>();
			String stateCode="";
			String sird_id="";
			if(userType.equalsIgnoreCase("Sird"))
			{
				String sql="select sird_id from loginmaster_sird where username='"+userName+"'";
				sird_id=jdbctemplate.queryForList(sql).get(0).get("sird_id").toString();
				String sql1="select state_code from sird where sird_id='"+sird_id+"'";
				 stateCode=jdbctemplate.queryForList(sql1).get(0).get("state_code").toString();
			}
			String sql = "select etc_id,\"Name\" as name from etc where state_code='"+stateCode+"'";
			List<Map<String,Object>>etcList=jdbctemplate.queryForList(sql);
			
			for(Map<String,Object> etcPropasal:etcList ) {
				if(flag)
				{
					id.append("'"+etcPropasal.get("etc_id").toString()+"'");
					flag=false;
				}else
				{
					id.append(",'"+etcPropasal.get("etc_id").toString()+"'");
				}
			}
			
			String proposalSql="Select proposal_no,totaldemand,proposaldate,user_id from final_proposal where usertype='etc' and user_id in("+id.toString()+")";
			proposallist=jdbctemplate.queryForList(proposalSql);
			
			for(Map<String,Object>map:proposallist)
			{
				for(Map<String,Object>etcMap:etcList)
				{
					if(map.get("user_id").toString().equalsIgnoreCase(etcMap.get("etc_id").toString()))
					{
						map.put("Name",etcMap.get("name"));
						
					}
				}
				if(flag1)
				{
					etcMaps.put("demand", Integer.parseInt(map.get("totaldemand").toString()));
					flag1=false;
				}else
				{
					etcMaps.put("demand",Integer.parseInt(etcMaps.get("demand").toString())+Integer.parseInt(map.get("totaldemand").toString()));
				}
				
				
			}
          proposallist.add(etcMaps);
          return proposallist;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String, Object>>(0);
	}

}
