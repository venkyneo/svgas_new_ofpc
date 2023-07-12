package com.svgas.main.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.svgas.main.models.EAlarmGroup;
import com.svgas.main.models.ECategoryType;
import com.svgas.main.models.Reports_GS;
import com.svgas.main.models.Single_Alarm_Resp;
import com.svgas.main.models.cumulativetotal_gs;
import com.svgas.main.models.parameternames;
import com.svgas.main.models.reportdata_gs;
import com.svgas.main.models.subsitedetails;
import com.svgas.main.repository.CustomQueriesRepository;
import com.svgas.main.repository.ParameterNamesRepository;
import com.svgas.main.repository.SubSiteDetailsRepository;

@Service
public class ReportService 
{
	@Autowired
	SubSiteDetailsRepository ssdr;
	
	@Autowired
	ParameterNamesRepository pnrepo;
	
	@Autowired
	CustomQueriesRepository cqr;
	
	public cumulativetotal_gs getCumulativeData(Reports_GS rgs)
	{
		cumulativetotal_gs cgs = new cumulativetotal_gs();
		try
		{
			Optional<subsitedetails> ssd_o = ssdr.findById(Long.parseLong(rgs.getSubsiteid()));
			subsitedetails ssd = ssd_o.get();
			List<parameternames> plist = pnrepo.findBySubsitedetailsId(ssd.getId());
			List<String> paramids_str = Arrays.asList(rgs.getParameterids());
			String paramcolname="";
			String resp="";
			if(rgs.getReport_type().equals("normal"))
			{
				for(parameternames pn : plist)
				{
					if(pn.getCategoryid().getCategory().toString().equals("NORMAL_REPORT"))
					{
						if(paramids_str.contains(pn.getId().toString()))
						{
							if(pn.getPcodeid().getPcodename().toString().equals("TOTAL_INJECTION"))
							{
								paramcolname = pn.getParamcolname();
							}
						}
					}
				}
				if(!paramcolname.equals(""))
				{
					String Query = "";
					if(rgs.getSub_report_type().equals("daily") || rgs.getSub_report_type().equals("hourly"))
					{
						//String Query = "select "+paramcolname+" as cumudata from "+ssd.getUniquename()+" where datetime between '"+rgs.getStart_date()+"' and '"+rgs.getEnd_date()+"'";
						String pcol = "max(cast("+paramcolname+" as decimal(10,2))) as cumudata";
						String dts =  "dateadd(minute,(datediff(minute,0,datetime)/"+rgs.getTimegap()+")*"+rgs.getTimegap()+",0)";
						Query = "select "+dts+" as dt,"+pcol+" from "+ssd.getUniquename()+" where datetime between '"+rgs.getStart_date()+"' and '"+rgs.getEnd_date()+"' group by "+dts+" order by "+dts+" asc";
					}
					if(rgs.getSub_report_type().equals("monthly"))
					{
						String pcol = "cast(max(cast("+paramcolname+" as decimal(10,2))) as decimal(10,2)) as cumudata";
						Query = "select DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) as dt,"+pcol+" from "+ssd.getUniquename()+" where (DATEPART(mm,datetime) = " + rgs.getMonth() + " and DATEPART(yyyy,datetime) = " + rgs.getYear() + ")"
								+ " group by DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) order by DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) asc";
					}
					if(rgs.getSub_report_type().equals("yearly"))
					{
						String pcol = "cast(max(cast("+paramcolname+" as decimal(10,2))) as decimal(10,2)) as cumudata";
						Query = "select DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) as dt,"+pcol+" from "+ssd.getUniquename()+" where DATEPART(yyyy,datetime) = " + rgs.getYear() + ""
								+ " group by DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) order by DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) asc";
					}
					cgs.setHascumulative(true);
					cgs.setCumulativedata(cqr.CumulativeValue(Query));
				}
				else
				{
					cgs.setHascumulative(false);
					cgs.setCumulativedata("");
				}
			}
			return cgs;
		}
		catch(Exception et)
		{
			return null;
		}
	}
	
	public reportdata_gs getHistoryData(Reports_GS rgs)
	{
		//get the subsite id
		//get the uniquename table of the subsite
		//get parameternames list related to the subsite
		//get the sub_report_type. daily/monthly/yearly
		//get the reporttype:
			//if reporttype is normal then
				//get all the parameternames from the list who have category type as NORMAL_REPORT
				//get all the paramcolnames from the list and call the daily or monthly or yearly methods from customqueries repository
			//else reporttype is alarm then
				//get all the parameternames from the list who have ALARM_REPORT category type
				//get all the paramcolnames from the list and call alarm report method from the customqueries repository.
		
		HashSet<EAlarmGroup> alarm_gp_set = new HashSet<EAlarmGroup>();
		Optional<subsitedetails> ssd_o = ssdr.findById(Long.parseLong(rgs.getSubsiteid()));
		subsitedetails ssd = ssd_o.get();
		List<String> lstr = new ArrayList<String>();
		List<String> ColNamesList = new ArrayList<String>();
		ColNamesList.add("Date Time");
		String rdetails = "";
		
		List<parameternames> plist = pnrepo.findBySubsitedetailsId(ssd.getId());
		StringBuilder pcolnames_csv = new StringBuilder();
		String totalinjection_cumulative = "";
		String mode_colname="";
		String gasflow_colname = "";
		String pump_running_colname="";
		List max_value_list = new ArrayList<String>();
		//StringBuilder pcolnames_csv_alarm = new StringBuilder();
		List<String> paramids_str = Arrays.asList(rgs.getParameterids());
		if(rgs.getReport_type().equals("normal"))
		{
			for(parameternames pn : plist)
			{
//				System.out.println("pid-1:"+pn.getId());
				if(pn.getCategoryid().getCategory().toString().equals("NORMAL_REPORT"))
				{
//					System.out.println("pid-2:"+pn.getId());
					if(paramids_str.contains(pn.getId().toString()))
					{
//						System.out.println("pid-3:"+pn.getId());
						pcolnames_csv.append(pn.getParamcolname()+",");
						if(pn.getParamunit() != null && !pn.getParamunit().isBlank() && !pn.getParamunit().isEmpty())
						{
							ColNamesList.add(pn.getParamname() + " ("+pn.getParamunit()+")");
							if(pn.getPcodeid().getPcodename().toString().equals("TOTAL_INJECTION"))
							{
								totalinjection_cumulative = pn.getParamcolname();
								ColNamesList.add("Total Injection(each record)");
							}
							if(pn.getPcodeid().getPcodename().toString().equals("MODE"))
							{
								mode_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("GAS_FLOW"))
							{
								gasflow_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("PUMP_RUNNING_TYPE"))
							{
								pump_running_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("PUMP_RUNNING_TYPE") || pn.getPcodeid().getPcodename().toString().equals("PUMP_1_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_2_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_3_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_4_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_5_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_6_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_7_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_8_COUNTER"))
							{
								max_value_list.add(pn.getParamcolname());
							}
							
						}
						else
						{
							ColNamesList.add(pn.getParamname());
							if(pn.getPcodeid().getPcodename().toString().equals("TOTAL_INJECTION"))
							{
								totalinjection_cumulative = pn.getParamcolname();
								ColNamesList.add("Total Injection(each record)");
							}
							if(pn.getPcodeid().getPcodename().toString().equals("GAS_FLOW"))
							{
								gasflow_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("MODE"))
							{
								mode_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("PUMP_RUNNING_TYPE"))
							{
								pump_running_colname = pn.getParamcolname();
							}
							if(pn.getPcodeid().getPcodename().toString().equals("PUMP_RUNNING_TYPE") || pn.getPcodeid().getPcodename().toString().equals("PUMP_1_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_2_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_3_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_4_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_5_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_6_COUNTER") || pn.getPcodeid().getPcodename().toString().equals("PUMP_7_COUNTER")
									 || pn.getPcodeid().getPcodename().toString().equals("PUMP_8_COUNTER"))
							{
								max_value_list.add(pn.getParamcolname());
							}
						}
						
					}					
				}
//				if(pn.getCategoryid().getCategory() == ECategoryType.ALARM_REPORT)
//				{
//					//pcolnames_csv_alarm.append(pn.getParamcolname());
//					alarm_gp_set.add(pn.getPcodeid().getAlarmgroup().getAlarmgroup());
//					ColNamesList.add(pn.getPcodeid().getAlarmgroup().getAlarmgroup().name());
//				}
			}
			System.out.println(pcolnames_csv.length());
			pcolnames_csv.setLength(pcolnames_csv.length()-1);
			
			if(rgs.getSub_report_type().equals("daily") || rgs.getSub_report_type().equals("hourly"))
			{
				System.out.println("daily");
				lstr = cqr.getDailyReport_avg(ssd.getUniquename(), pcolnames_csv.toString(), rgs.getStart_date(), rgs.getEnd_date(), rgs.getTimegap(),max_value_list,totalinjection_cumulative,mode_colname,gasflow_colname,pump_running_colname);
				for(String s : lstr)
				{
					//System.out.println("ls:"+s);
				}
				rdetails = "from: " + rgs.getStart_date() + "  to: " + rgs.getEnd_date();
			}
			if(rgs.getSub_report_type().equals("monthly"))
			{
				lstr = cqr.getMonthlyReport(ssd.getUniquename(), pcolnames_csv.toString(), rgs.getMonth(), rgs.getYear(),totalinjection_cumulative,mode_colname,gasflow_colname,pump_running_colname,max_value_list);
				rdetails = "Month: " + GetMonthName(rgs.getMonth()) + "  Year: " + rgs.getYear();
			}
			if(rgs.getSub_report_type().equals("yearly"))
			{
				lstr = cqr.getYearlyReport(ssd.getUniquename(), pcolnames_csv.toString(), rgs.getYear(),totalinjection_cumulative,mode_colname,gasflow_colname,pump_running_colname,max_value_list);
				rdetails = "Year: " + rgs.getYear();
			}
		}
		if(rgs.getReport_type().equals("alarm"))
		{
			String[] alarmGpName = rgs.getParameterids();
			reportdata_gs rdgss = cqr.multipleAlarmReport(ssd.getId(),alarmGpName,rgs.getStart_date(),rgs.getEnd_date());
			ColNamesList = rdgss.getColumn_names();
			lstr = rdgss.getObj_list();
			/********** Original logic-start ****************/
//			List<Single_Alarm_Resp> sa_list = cqr.particularAlarmReport(ssd.getId(),alarmGpName[0],rgs.getStart_date(),rgs.getEnd_date());
//			for(Single_Alarm_Resp sa : sa_list)
//			{
//				lstr.add(sa.getDatetime()+","+sa.getHigh_low()+"");//+sa.getValue());
//			}
//			rdetails = "from: " + rgs.getStart_date() + "  to: " + rgs.getEnd_date();
//			ColNamesList.add("Alarm Type");
			/********** Original logic-end *************/
			//ColNamesList.add(alarmGpName[0]);
//			HashMap<String,String> alm_str_list = new HashMap<String,String>();
//			
//			for(parameternames pn : plist)
//			{
//				if(alarmGpName[0].equals(pn.getPcodeid().getAlarmgroup().getAlarmgroup().name()))
//				{
//					alm_str_list.put(pn.getParamcolname(),pn.getAlarmid().getAtypename().name());
//				}
//			}
			
		}
		reportdata_gs rdgs = new reportdata_gs();
		rdgs.setSitename(ssd.getSubsitename());
		rdgs.setReport_type(rgs.getSub_report_type());
		rdgs.setReport_details(rdetails);
		rdgs.setColumn_names(ColNamesList);
		rdgs.setObj_list(lstr);
		
		return rdgs;
	}
	
	public reportdata_gs getLast10Alarms(String sid,String almgp)
	{
		List<String> lstr = new ArrayList<String>();
		List<String> ColNamesList = new ArrayList<String>();
		ColNamesList.add("Date Time");
		String rdetails = "";
		List<Single_Alarm_Resp> sa_list = cqr.Last10Records_Alarm(Long.parseLong(sid), almgp);
		for(Single_Alarm_Resp sa : sa_list)
		{
			lstr.add(sa.getDatetime()+","+sa.getHigh_low()+"");//+sa.getValue());
		}
		rdetails = "";
		ColNamesList.add("Alarm Type");
		//ColNamesList.add(almgp);
		
		
		reportdata_gs rdgs = new reportdata_gs();
		rdgs.setSitename("");
		rdgs.setReport_type("");
		rdgs.setReport_details(rdetails);
		rdgs.setColumn_names(ColNamesList);
		rdgs.setObj_list(lstr);
		
		return rdgs;
	}
	
	public String GetMonthName(String MonthNumber) {
		String name = "";
		switch (MonthNumber) {
		case "1":
			name = "January";
			break;
		case "2":
			name = "February";
			break;
		case "3":
			name = "March";
			break;
		case "4":
			name = "April";
			break;
		case "5":
			name = "May";
			break;
		case "6":
			name = "June";
			break;
		case "7":
			name = "July";
			break;
		case "8":
			name = "August";
			break;
		case "9":
			name = "September";
			break;
		case "10":
			name = "October";
			break;
		case "11":
			name = "November";
			break;
		case "12":
			name = "December";
			break;
		}
		return name;
	}
}