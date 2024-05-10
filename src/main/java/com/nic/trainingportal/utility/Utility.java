package com.nic.trainingportal.utility;

public class Utility {
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
}
