package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.LoginService;

@RestController
@RequestMapping("/ao/trainingportal")
public class LoginController {
	
	@Autowired
	public LoginService loginservice;
	@PostMapping(value="GetLoginDetails")
	
	public Map<String,Object>getLoginDetails(@RequestBody Map<String,Object>map)
	{
		String check="";
		Map<String,Object>dataMap=new HashMap<String,Object>(6);
		try
		{
			check=loginservice.getLoginDetails(map);
			
			if(check.equalsIgnoreCase("Unauthorized User"))
			{
				dataMap.put(Literal.status,Literal.unauthorized);
				dataMap.put(Literal.message,"Unauthorized User");
				dataMap.put("token", "");
				return dataMap;
			}
			dataMap.put(Literal.status,Literal.successCode);
			dataMap.put(Literal.message,"User Auntheticate Successfully");
			dataMap.put("token",loginservice.getLoginDetails(map));
			return dataMap;
			
		}catch(Exception e)
		{
			dataMap.put(Literal.status,Literal.errorCode);
			dataMap.put(Literal.message,"Something Went Wrong");
			e.printStackTrace();
			return dataMap;
		}
	}

}
