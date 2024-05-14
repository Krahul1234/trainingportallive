package com.nic.trainingportal.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nic.trainingportal.dao.LoginDao;
import com.nic.trainingportal.jwt.JWT;

@Service
public class LoginService {
	
	@Autowired
	
	public LoginDao logindao;
	
	@Autowired
	public JWT jwt;
	
	public String getLoginDetails(Map<String,Object>map)
	{
		try
		{
			List<Map<String,Object>>data=logindao.getLoginDetails(map);
			
			if(data!=null &&data.size()!=0)
			{
				for(Map<String,Object>data_map:data)
				{
					if(map.get("username").toString().equalsIgnoreCase(data_map.get("username").toString())&& map.get("password").toString().equalsIgnoreCase(data_map.get("user_password").toString()))
					{
						return jwt.generateToken(map.get("username").toString());
					}else
					{
						 return "Not Valid User";
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
