package com.svgas.main.repository;

import java.util.List;

import com.svgas.main.models.EAlarmGroup;
import com.svgas.main.models.Single_Alarm_Resp;
import com.svgas.main.models.reportdata_gs;
import com.svgas.main.pojo.admin_page_data;
import com.svgas.main.pojo.barChartData;

public interface CustomQueriesRepository 
{
	int CreateUniqueNameTable(String tableName);
	int AddColumnsToUniqueTable(String tableName, String columnName);
	List<String> getDailyReport(String tablename,String pcolnames,String sdate,String edate);
	List<String> getDailyReport_avg(String tablename, String pcolnames, String sdate, String edate,String timegap,List<String> tlist, String cumulativecolname,String mode_colname,String gasflow_colname, String pump_running_col_name);
	boolean checkList(List<String> tlist,String pcolstr);
	List<String> getMonthlyReport(String tablename,String pcolnames,String month,String year, String cumulativecolname,String mode_colname,String gasflow_colname, String pump_running_col_name, List<String> tlist);
	List<Single_Alarm_Resp> Last10Records_Alarm(long subsiteid, String alarmgp);
	public List<String> getYearlyReport(String tablename,String pcolnames,String year, String cumulativecolname,String mode_colname,String gasflow_colname,  String pump_running_col_name,List<String> tlist);
	List<Single_Alarm_Resp> particularAlarmReport(long subsiteid, String alarmgp, String SDateTime, String EDateTime);
	List<admin_page_data> getAdminPageStatusData();
	barChartData last7DaysParamData(long subsiteid);
	List<admin_page_data> getAdminPageStatusData_new();
	reportdata_gs multipleAlarmReport(long subsiteid, String[] alarmgp, String SDateTime, String EDateTime);
	String CumulativeValue(String Query);
}