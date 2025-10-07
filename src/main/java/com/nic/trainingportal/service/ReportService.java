package com.nic.trainingportal.service;

import com.nic.trainingportal.dao.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
	
	@Autowired
	private ReportDao reportdao;
	
	public List<Map<String,Object>>getEtcReport(String userName,String userType)
	
	{
		try
		{
			 return reportdao.getEtcReport(userName, userType);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Map<String,Object>>(0);
	}
	
	public  List<Map<String, Object>> getreportForSird(String userName,String userType) {
        // TODO Auto-generated method stub
        try {
            /**
             * get faculty
             */
            return reportdao.getreportForSird(userName,userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }
	public  List<Map<String, Object>> getreportForMinistry(String userName,String userType) {
        // TODO Auto-generated method stub
        try {
            /**
             * get faculty
             */
            return reportdao.getreportForMinistry(userName,userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

	public Object reportForMinistryNonRecurring(String userName, String userType) {
        // TODO Auto-generated method stub
        try {
            /**
             * get faculty
             */
            return reportdao.reportForMinistryNonRecurring(userName,userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public Object nonRecurringReportForSird(String userName, String userType) {
        // TODO Auto-generated method stub
        try {
            /**
             * get faculty
             */
            return reportdao.nonRecurringReportForSird(userName,userType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Map<String, Object>>(0);
    }

    public Object nonRecurringEtcReport(String userName, String userType) {
        try
        {
            return reportdao.nonRecurringEtcReport(userName, userType);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Map<String,Object>>(0);
    }
}
