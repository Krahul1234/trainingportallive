package com.nic.trainingportal.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nic.trainingportal.jwt.JWT;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Utility {
	
	@Autowired
	public JWT jwt;
	/**
	 * check null condition
	 * @param data
	 * @return
	 */
	public static final boolean checkNull(Object data)
	{
		if(data==null || data.toString().length()==0)
		{
			return true;
		}
		return false;
	}

	public static final boolean checkNotNull(Object data)
	{
		if(data==null || data.toString().length()==0)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * @param httpservletrequest
	 * @param username
	 * @return
	 */
	public  boolean getHeaderValue(HttpServletRequest httpservletrequest)
	{
		try
		{
		  String username=jwt.extractUsername(httpservletrequest.getHeader("token").toString());
          if(jwt.validateToken(httpservletrequest.getHeader("token").toString(),username))
        	  {
        	    return false;
        	  }else
        		  {
        		    return true;
        		  }
	  }  catch(Exception e)
		{
		  return true;
		}
	}
}
