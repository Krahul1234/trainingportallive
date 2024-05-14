package com.nic.trainingportal.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nic.trainingportal.jwt.JWT;
import com.nic.trainingportal.literal.Literal;
import com.nic.trainingportal.service.LoginService;

@RestController
public class LoginController {
	
	@Autowired
	public LoginService loginservice;
	
	@PostMapping(value="GetLoginDetails")
	
	public Map<String,Object>getLoginDetails(@RequestBody Map<String,Object>map)
	{
		Map<String,Object>dataMap=new HashMap<String,Object>(6);
		try
		{
			dataMap.put(Literal.status,Literal.success);
			dataMap.put(Literal.statusCode,1);
			dataMap.put("Token",loginservice.getLoginDetails(map));
			return dataMap;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new HashMap<String,Object>(0);
	}

}
