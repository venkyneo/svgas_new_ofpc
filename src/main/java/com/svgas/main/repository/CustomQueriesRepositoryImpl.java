package com.svgas.main.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.svgas.main.models.Single_Alarm_Resp;
import com.svgas.main.models.parameternames;
import com.svgas.main.models.reportdata_gs;
import com.svgas.main.models.subsitedetails;
import com.svgas.main.pojo.admin_page_data;
import com.svgas.main.pojo.barChartData;

@Repository
public class CustomQueriesRepositoryImpl implements CustomQueriesRepository
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	ParameterNamesRepository pnamesrepo;
	
	@Autowired
	SubSiteDetailsRepository ssdrepo;
	
	@Override
	public int CreateUniqueNameTable(String tablename) {
		// TODO Auto-generated method stub
		String Query = "create table "+tablename+"( id int not null identity(1,1) primary key, datetime datetime not null)";
		jdbcTemplate.execute(Query);
		Query = "create table "+tablename+"_alarms( id int not null identity(1,1) primary key, datetime datetime not null)";
		jdbcTemplate.execute(Query);
		return 0;
	}

	@Override
	public int AddColumnsToUniqueTable(String TableName,String ColumnName) {
		String Query = "alter table "+TableName+" add "+ColumnName+" varchar(max)";
		jdbcTemplate.execute(Query);
		
		return 0;
	}

	@Override
	public List<String> getDailyReport(String tablename, String pcolnames, String sdate, String edate) 
	{
		String Query = "select datetime as dt,"+pcolnames+" from "+tablename+" where datetime between '"+sdate+"' and '"+edate+"'";
		//System.out.println("Query:"+Query);
		List<String> ValuesList = jdbcTemplate.query(Query, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				List<String> vlist = new ArrayList<String>();
				StringBuilder strb = new StringBuilder();
				String[] pcolnames_arr = pcolnames.split(",");
				while(rs.next())
				{
					strb.setLength(0);
					strb.append(rs.getString("dt")+",");
					for(String pcolname : pcolnames_arr)
					{
						strb.append(rs.getString(pcolname)+",");
					}
					strb.setLength(strb.length()-1);
					vlist.add(strb.toString());
				}
				return vlist;
			}
		});
		return ValuesList;
	}
	
	public String CumulativeValue(String Query)
	{
		try
		{
			NumberFormat nf= NumberFormat.getInstance();
			nf.setGroupingUsed(false);
	        nf.setMaximumFractionDigits(2);
			String value_str = jdbcTemplate.query(Query, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					int counter=0;
					double fval=0;
					double lval=0;
					while(rs.next())
					{
						double val = Double.parseDouble(rs.getString("cumudata"));
						if(val > 0)
						{
							if(counter==0)
							{
								fval = val;							
							}
							else
							{
//								if(val > 0)
//								{
									lval = val;
//								}
							}
							counter++;
						}
					}
					String resp = nf.format(lval-fval).toString();
					return resp;
				}
				
			});
			return value_str;
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
			return null;
		}
	}

	@Override
	public List<String> getDailyReport_avg(String tablename, String pcolnames, String sdate, String edate, String timegap, List<String> tlist, String cumulativecolname,String mode_colname,String gasflow_colname) 
	{
		NumberFormat nf= NumberFormat.getInstance();
		nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
		StringBuilder strb = new StringBuilder();
		String[] tlist_arr = tlist.toArray(new String[0]);
		
		for(String pcolstr : pcolnames.split(","))
		{
			//if(checkList(tlist, pcolstr))
			if(pcolstr.equals(cumulativecolname))
			{
				strb.append("max(cast("+pcolstr+" as decimal(10,2))) as "+pcolstr+",");	
			}
			else
			{
				strb.append("avg(cast("+pcolstr+" as decimal(10,2))) as "+pcolstr+",");	
			}
		}
		//System.out.println("strb:"+strb.toString());
//		System.out.println("tbname:"+tablename);
		strb.setLength(strb.length()-1);
		String dts =  "dateadd(minute,(datediff(minute,0,datetime)/"+timegap+")*"+timegap+",0)";
		String Query = "select "+dts+" as dt,"+strb+" from "+tablename+" where datetime between '"+sdate+"' and '"+edate+"' group by "+dts+" order by "+dts+" asc";
		System.out.println("Query_v_day:"+Query);
		List<String> ValuesList = jdbcTemplate.query(Query, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				double prev_row=0;
				double curr_row=0;
				List<String> vlist = new ArrayList<String>();
				StringBuilder strb = new StringBuilder();
				StringBuilder strbr = new StringBuilder();
				String[] pcolnames_arr = pcolnames.split(",");
				while(rs.next())
				{
					strbr.setLength(0);
					strb.setLength(0);
					strb.append(rs.getString("dt")+",");
					for(String pcolname : pcolnames_arr)
					{
						if(pcolname.contains("value"))
						{
							strbr.append(pcolname+",");
							String stat = "status"+pcolname.substring(5);
//							String valueformat = SwitchCaseForStatus(rs.getString(stat));
//							if(valueformat.equals("0"))
//							{
								
								double data_rec=-1;
								String rs_string = rs.getString(pcolname);
								if(rs_string!=null)
								{
									 data_rec = Double.parseDouble(rs_string); 
								}	
								boolean mode_gas = false;
								String dval_rec = Double.toString(data_rec);
								if(pcolname.equals(mode_colname))
								{
									mode_gas = true;
									if(data_rec == 0)
									{
										dval_rec = "Manual";
									}
									if(data_rec == 1)
									{
										dval_rec = "Automatic";
									}
								}
								if(pcolname.equals(gasflow_colname))
								{
									mode_gas = true;
									if(data_rec == 0)
									{
										dval_rec = "Fixed Flow";
									}
									if(data_rec == 1)
									{
										dval_rec = "Real Flow";
									}
								}
								if(mode_gas)
								{
									strb.append(dval_rec+",");
								}
								else
								{
									strb.append(nf.format(Double.parseDouble(rs.getString(pcolname))).toString()+",");
								}
								if(pcolname.equals(cumulativecolname))
								{
									if(prev_row == 0)
									{
										strb.append(prev_row+",");
										curr_row = data_rec;
										prev_row = curr_row;
										//f_row_total = data_rec;
									}
									else
									{
										String diff = nf.format(data_rec - prev_row);
										if((data_rec - prev_row) < 0)
										{
											diff="0";
										}
										curr_row = data_rec;
										prev_row = curr_row;
										strb.append(diff+",");
										//l_row_total = data_rec;
									}
								}
//							}
//							else
//							{
//								strb.append(nf.format(Double.parseDouble(rs.getString(pcolname))).toString()+" ("+valueformat+"),");	
//							}
							
						}
						
					}
					strbr.setLength(strbr.length()-1);					
					strb.setLength(strb.length()-1);	
					//System.out.println("====================================================="+strbr.toString()+"==========================="+strb.toString());
					vlist.add(strb.toString());
				}
//				double cumulative_total = l_row_total - f_row_total;
//				vlist.add(Double.toString(cumulative_total));
				return vlist;
			}
		});
		return ValuesList;
	}
	
	@Override
	public boolean checkList(List<String> tlist,String pcolstr)
	{
		boolean resp=false;
		String[] tlist_arr = tlist.toArray(new String[0]);
		if(tlist_arr.length > 0)
		{
			for(String str : tlist)
			{
				if(str.equals(pcolstr))
				{
					resp = true;
					//strb.append("cast(max(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");	
				}
			}
			//strb.append("cast(max(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");
		}
		return resp;
	}
	
	//get alarm based on number of parameters selected or all selected for a particular subsite.
	//get alarm data compared to enumtypes specified, compare and create table for multiple options selected or single option selected.
	
	
	@Override
	public List<String> getMonthlyReport(String tablename,String pcolnames,String month,String year, String cumulativecolname,String mode_colname,String gasflow_colname)
	{
		StringBuilder strb = new StringBuilder();
		NumberFormat nf= NumberFormat.getInstance();
		nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
		
		for(String pcolstr : pcolnames.split(","))
		{
//			if(!pcolstr.contains("status"))
//			{
				if(pcolstr.equals(cumulativecolname))
				{
					strb.append("cast(max(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");
				}
				else
				{
					strb.append("cast(avg(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");	
				}	
//			}
		}
//		System.out.println("tbname:"+tablename);
		strb.setLength(strb.length()-1);
//		String Query = "select DATEPART(dd,datetime) as dt,"+strb+" from "+tablename+" where (DATEPART(mm,datetime) = " + month + " and DATEPART(yyyy,datetime) = " + year + ")"
//				+ " group by DATEPART(dd,datetime) order by DATEPART(dd,datetime) asc";
		String Query = "select DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) as dt,"+strb+" from "+tablename+" where (DATEPART(mm,datetime) = " + month + " and DATEPART(yyyy,datetime) = " + year + ")"
				+ " group by DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) order by DATEPART(dd,convert(date,dateadd(HH,-8,datetime))) asc";
		System.out.println("Query_V_month:"+Query);
		List<String> ValuesList = jdbcTemplate.query(Query, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				double prev_row=0;
				double curr_row=0;
				List<String> vlist = new ArrayList<String>();
				StringBuilder strb = new StringBuilder();
				String[] pcolnames_arr = pcolnames.split(",");
				while(rs.next())
				{
					strb.setLength(0);
					strb.append(rs.getString("dt")+"-"+month+"-"+year+",");
					for(String pcolname : pcolnames_arr)
					{
//						if(!pcolname.contains("status"))
//						{
						double data_rec=-1;
						String rs_string = rs.getString(pcolname);
						if(rs_string!=null)
						{
							 data_rec = Double.parseDouble(rs_string); 
						}	
						boolean mode_gas = false;
						String dval_rec = Double.toString(data_rec);
						if(pcolname.equals(mode_colname))
						{
							mode_gas = true;
							if(data_rec == 0)
							{
								dval_rec = "Manual";
							}
							if(data_rec == 1)
							{
								dval_rec = "Automatic";
							}
						}
						if(pcolname.equals(gasflow_colname))
						{
							mode_gas = true;
							if(data_rec == 0)
							{
								dval_rec = "Fixed Flow";
							}
							if(data_rec == 1)
							{
								dval_rec = "Real Flow";
							}
						}
						if(mode_gas)
						{
							strb.append(dval_rec+",");
						}
						else
						{
							strb.append(nf.format(Double.parseDouble(rs.getString(pcolname))).toString()+",");
						}
						if(pcolname.equals(cumulativecolname))
						{
							if(prev_row == 0)
							{
								strb.append(prev_row+",");
								curr_row = data_rec;
								prev_row = curr_row;
								//f_row_total = data_rec;
							}
							else
							{
								String diff = nf.format(data_rec - prev_row);
								if((data_rec - prev_row) < 0)
								{
									diff="0";
								}
								curr_row = data_rec;
								prev_row = curr_row;
								strb.append(diff+",");
								//l_row_total = data_rec;
							}
						}
						//strb.append(rs.getString(pcolname)+",");
//						}
					}
					strb.setLength(strb.length()-1);
					vlist.add(strb.toString());
				}
				return vlist;
			}
		});
//		int index = ValuesList.size()-1;
//		ValuesList.remove(index);
		return ValuesList;
	}
	
	@Override
	public List<String> getYearlyReport(String tablename,String pcolnames,String year, String cumulativecolname,String mode_colname,String gasflow_colname)
	{
		StringBuilder strb = new StringBuilder();
		NumberFormat nf= NumberFormat.getInstance();
		nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
		for(String pcolstr : pcolnames.split(","))
		{
//			if(!pcolstr.contains("status"))
//			{
				if(pcolstr.equals(cumulativecolname))
				{
					strb.append("cast(max(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");
				}
				else
				{
					strb.append("cast(avg(cast("+pcolstr+" as decimal(10,2))) as decimal(10,2)) as "+pcolstr+",");	
				}	
//			}
		}
//		System.out.println("tbname:"+tablename);
		strb.setLength(strb.length()-1);
//		String Query = "select DATEPART(dd,datetime) as dt,"+strb+" from "+tablename+" where (DATEPART(mm,datetime) = " + month + " and DATEPART(yyyy,datetime) = " + year + ")"
//				+ " group by DATEPART(dd,datetime) order by DATEPART(dd,datetime) asc";
		String Query = "select DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) as dt,"+strb+" from "+tablename+" where DATEPART(yyyy,datetime) = " + year + ""
				+ " group by DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) order by DATEPART(MM,convert(date,dateadd(HH,-8,datetime))) asc";
		System.out.println("Query_V_year:"+Query);
		List<String> ValuesList = jdbcTemplate.query(Query, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				double prev_row=0;
				double curr_row=0;
				List<String> vlist = new ArrayList<String>();
				StringBuilder strb = new StringBuilder();
				String[] pcolnames_arr = pcolnames.split(",");
				while(rs.next())
				{
					strb.setLength(0);
					//strb.append(rs.getString("dt")+"-"+month+"-"+year+",");
					String dts = "01-"+rs.getString("dt")+"-"+year;
					strb.append(dts+",");
					for(String pcolname : pcolnames_arr)
					{
						double data_rec=-1;
						String rs_string = rs.getString(pcolname);
						if(rs_string!=null)
						{
							 data_rec = Double.parseDouble(rs_string); 
						}						 
						boolean mode_gas = false;
						String dval_rec = Double.toString(data_rec);
						if(pcolname.equals(mode_colname))
						{
							mode_gas = true;
							if(data_rec == 0)
							{
								dval_rec = "Manual";
							}
							if(data_rec == 1)
							{
								dval_rec = "Automatic";
							}
						}
						if(pcolname.equals(gasflow_colname))
						{
							mode_gas = true;
							if(data_rec == 0)
							{
								dval_rec = "Fixed Flow";
							}
							if(data_rec == 1)
							{
								dval_rec = "Real Flow";
							}
						}
						if(mode_gas)
						{
							strb.append(dval_rec+",");
						}
						else
						{
							strb.append(nf.format(Double.parseDouble(rs.getString(pcolname))).toString()+",");
						}
						if(pcolname.equals(cumulativecolname))
						{
							if(prev_row == 0)
							{
								strb.append(prev_row+",");
								curr_row = data_rec;
								prev_row = curr_row;
								//f_row_total = data_rec;
							}
							else
							{
								String diff = nf.format(data_rec - prev_row);
								if((data_rec - prev_row) < 0)
								{
									diff="0";
								}
								curr_row = data_rec;
								prev_row = curr_row;
								strb.append(diff+",");
								//l_row_total = data_rec;
							}
						}
//						if(!pcolname.contains("status"))
//						{
							//strb.append(rs.getString(pcolname)+",");
//						}
					}
					strb.setLength(strb.length()-1);
					vlist.add(strb.toString());
				}
				return vlist;
			}
		});
//		int index = ValuesList.size()-1;
//		ValuesList.remove(index);
		return ValuesList;
	}
	
	@Override
	public barChartData last7DaysParamData(long subsiteid)
	{
		try
		{
			subsitedetails ssd = null;
			parameternames pn_main = null;
			List<parameternames> pnamesList = pnamesrepo.findBySubsitedetailsId(subsiteid);
			for(parameternames pn : pnamesList)
			{
				if(pn.getPcodeid().getPcodename().toString().equals("TOTAL_INJECTION"))
				{
					pn_main = pn;
					ssd = pn.getSubsitedetails();
					break;
				}
			}
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -8);
			Date lastDate = cal.getTime();
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);
			Date mainlastdate = cal.getTime();
			Date currentDate = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String lastDateStr = sdf.format(lastDate);
			String MainLastDateStr = sdf.format(mainlastdate);
			String currentDateStr  = sdf.format(currentDate);
			String param_col_name = pn_main.getParamcolname();
			
			String Query = "select datepart(dd,datetime) as dtd, datepart(MM,datetime) as dtm,datepart(YYYY,datetime) as dty,max(cast("+param_col_name+" as decimal(13,2))) as pcol from "+ssd.getUniquename()+" where datetime between '"+lastDateStr+" 00:00:00' and '"+currentDateStr+" 23:59:59' group by datepart(dd,datetime),datepart(MM,datetime),datepart(YYYY,datetime) order by datepart(dd,datetime)";
			System.out.println("Q:---------:"+Query);
			barChartData respStr = jdbcTemplate.query(Query, new ResultSetExtractor<barChartData>() {
				public barChartData extractData(ResultSet rs) throws SQLException, DataAccessException {
//					List<barChartData> vlist = new ArrayList<barChartData>();
					barChartData bcd = new barChartData();
					List<String> col_names_str = new ArrayList<String>();
					List<String> data_val_str = new ArrayList<String>();
					double prev_val = 0;
					double curr_val = 0;
					int counter = 0;
					while(rs.next())
					{
						//barChartData bcd = new barChartData();
						String date = rs.getString("dty")+"-"+rs.getString("dtm")+"-"+rs.getString("dtd");
						Date dt;
						try {
							dt = sdf.parse(date);
							date = sdf.format(dt);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						LocalDate lds = LocalDate.parse(date);
						String dval = rs.getString("pcol");
						double v=0;
						if(counter > 0)
						{
							double d = Double.parseDouble(dval);
							
							if(d > 0)
							{
								v = d - prev_val;								
								if(v < 0)
								{
									v=0;
								}
								curr_val=d;
								prev_val = curr_val;
							}
							
						}
						else
						{							
							prev_val = Double.parseDouble(dval);
							curr_val=prev_val;
						}
						if(counter>0)
						{
							col_names_str.add(lds.toString());
							data_val_str.add(Double.toString(v));	
						}						
						//String respstr = date+","+dval;
						//.add(respstr);
						
						counter++;
					}
					bcd.setCol_names(col_names_str);
					bcd.setData_values(data_val_str);
					return bcd;
				}
			});
			barChartData bcd_1 = getChart7days_adjust(MainLastDateStr,currentDateStr,respStr);
			return bcd_1;
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
		}
		return null;
	}
	
	public barChartData getChart7days_adjust(String startDate, String endDate, barChartData bcd_prev)
	{
		barChartData bcd = new barChartData();
		List<String> cols = new ArrayList<String>();
		List<String> datas = new ArrayList<String>();
		
//		String s = "2014-05-01";
//		String e = "2014-05-10";
		System.out.println("sd--------------"+startDate);
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		System.out.println("sd1--------------"+start.toString()+"    "+end.toString());
		
		List<LocalDate> totalDates = new ArrayList<>();
		while (!start.isAfter(end)) {
		    totalDates.add(start);
		    start = start.plusDays(1);
		}
		
		for(LocalDate ld : totalDates)
		{
			System.out.println(ld.toString());
			cols.add(ld.toString());
			if(!bcd_prev.getCol_names().isEmpty())
			{
				if(bcd_prev.getCol_names().indexOf(ld.toString()) != -1)
				{
					int ind = bcd_prev.getCol_names().indexOf(ld.toString());
					List<String> bd = bcd_prev.getData_values();
					datas.add(bd.get(ind));
				}
				else
				{
					datas.add("0");
				}
			}
			else
			{
				datas.add("0");
			}
		}
		bcd.setCol_names(cols);
		bcd.setData_values(datas);
		return bcd;
	}
	
	@Override
	public List<Single_Alarm_Resp> Last10Records_Alarm(long subsiteid, String alarmgp)
	{
		try
		{
			List<parameternames> pnamesList = pnamesrepo.findBySubsitedetailsId(subsiteid);
			List<parameternames> pnames_grouped = new ArrayList<parameternames>();
			subsitedetails ssd = null;
			String pn_high_colname =  "";
			String pn_low_colname = "";
			for(parameternames pn : pnamesList)
			{
				if(pn.getPcodeid().getAlarmgroup().getAlarmgroup().toString().equals(alarmgp))
				{
					pnames_grouped.add(pn);
					if(pn.getAlarmid().getAtypename().toString().equals("HIGH"))
					{
						pn_high_colname = pn.getParamcolname();
					}
					if(pn.getAlarmid().getAtypename().toString().equals("LOW"))
					{
						pn_low_colname = pn.getParamcolname();
					}
					ssd = pn.getSubsitedetails();					
				}
			}
			String uniqueTable = ssd.getUniquename()+"_alarms";
			final String hn = pn_high_colname;
			final String ln = pn_low_colname;
			
			//String Query = "select top 10 datetime,"+pn_high_colname+","+pn_low_colname+" from "+uniqueTable+"  order by datetime desc";
			String Query = "select top 10 datetime,"+pn_high_colname+","+pn_low_colname+" from "+uniqueTable+" where ("+pn_high_colname+" = 1 or "+pn_low_colname+" = 1) order by datetime desc";
			List<Single_Alarm_Resp> AlarmResp = jdbcTemplate.query(Query, new ResultSetExtractor<List<Single_Alarm_Resp>>(){
				@Override
				public List<Single_Alarm_Resp> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Single_Alarm_Resp> sar_list = new ArrayList<Single_Alarm_Resp>();
					
					while(rs.next())
					{
						Single_Alarm_Resp sar = new Single_Alarm_Resp();
						double high_col_val = 0;
						double low_col_val = 0;
//						double high_col_val = Double.parseDouble(rs.getString(hn));
//						double low_col_val = Double.parseDouble(rs.getString(ln));
						String hstr = rs.getString(hn);
						String lstr = rs.getString(ln);
						if(hstr != null)
						{
							high_col_val = Double.parseDouble(rs.getString(hn));
						}
						if(lstr != null)
						{
							low_col_val = Double.parseDouble(rs.getString(ln));
						}
						String dt = rs.getString("datetime");
						
						if(high_col_val > 0)
						{
							sar.setDatetime(dt);
							sar.setHigh_low("High");
							//sar.setValue(high_col_val);
							sar_list.add(sar);
						}
						if(low_col_val > 0)
						{
							sar.setDatetime(dt);
							sar.setHigh_low("Low");
//							sar.setValue(low_col_val);
							sar_list.add(sar);
						}
					}
					
					return sar_list;
				}
			});
			return AlarmResp;
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
			return null;
		}
	}
	
	@Override
	public List<admin_page_data> getAdminPageStatusData_new()
	{
		List<admin_page_data> apd_list = new ArrayList<admin_page_data>();
		try
		{
			int count = 1;
			List<subsitedetails>ssdlist = ssdrepo.findAll();
			
			for(subsitedetails ssd : ssdlist)
			{
				admin_page_data apd = new admin_page_data();
				String db_dt = pnamesrepo.findMaxDateParameternamesBySubsiteNative(ssd.getId());
				System.out.println("sp"+db_dt);
				Date ndt = new Date();
				long seconds = compareDates(db_dt, ndt);
				//System.out.println(db_dt+"------------"+ndt+"-----------"+seconds);
				if(seconds < 60)
				{
					apd.setStatus(1);//online
				}
				else
				{
					apd.setStatus(0);	//offline
				}
				apd.setSsid(ssd.getId());
				apd.setCity(ssd.getSitedetails().getCity());
				apd.setSubsitename(ssd.getSubsitename());
				apd.setState(ssd.getSitedetails().getState());
				apd.setId(count);
				apd_list.add(apd);
				count++;
			}
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
		}
		return apd_list;
	}
	
	public long compareDates(String dbdt, Date dtnow)
	{
		long seconds = -1;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dbdt_dt = sdf.parse(dbdt);
			//Date dtnow_dt = sdf.parse(dtnow);
			
			seconds = (dtnow.getTime()-dbdt_dt.getTime())/1000;
			//System.out.println(dbdt_dt+"------------"+dtnow+"-----------"+seconds);
		}
		catch(Exception et)
		{
			System.out.println("e:"+et.toString());
			seconds=-1;
		}
		return seconds;
	}
	
	@Override
	public List<admin_page_data> getAdminPageStatusData()
	{
		try
		{
			String Query = "select id,subsitename,city,state,ssid,status from svgas_main order by id asc";
			List<admin_page_data> apd_list = jdbcTemplate.query(Query, new ResultSetExtractor<List<admin_page_data>>() {

				@Override
				public List<admin_page_data> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<admin_page_data> apd_list_inner = new ArrayList<admin_page_data>();
					while(rs.next())
					{
						admin_page_data apd = new admin_page_data();
						apd.setId(Long.parseLong(rs.getString("id")));
						apd.setSsid(Long.parseLong(rs.getString("ssid")));
						apd.setSubsitename(rs.getString("subsitename"));
						apd.setCity(rs.getString("city"));
						apd.setState(rs.getString("state"));
						apd.setStatus(Integer.parseInt(rs.getString("status")));
						
						apd_list_inner.add(apd);
					}
					return apd_list_inner;
				}
				
			});
			
			return apd_list;
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
			return null;
		}
	}
	
	@Override
	public reportdata_gs multipleAlarmReport(long subsiteid, String[] alarmgp, String SDateTime, String EDateTime)
	{
		try
		{
			List<parameternames> pnamesList = pnamesrepo.findBySubsitedetailsId(subsiteid);
			List<parameternames> pnames_grouped = new ArrayList<parameternames>();
			HashMap<String, String[]> alarmCols = new HashMap<String, String[]>();
			String Query_paramnames = "";
			subsitedetails ssd = null;
			String[] hl_arr = new String[] {"",""};
			for(parameternames pn : pnamesList)
			{
				boolean contains = Arrays.stream(alarmgp).anyMatch(pn.getPcodeid().getAlarmgroup().getAlarmgroup().toString()::equals);
				if(contains)
				{
					if(pn.getAlarmid().getAtypename().toString().equals("HIGH"))
					{
						hl_arr[0] = pn.getParamcolname();
					}
					if(pn.getAlarmid().getAtypename().toString().equals("LOW"))
					{
						hl_arr[1] = pn.getParamcolname();
					}
				}
				if(!hl_arr[0].isBlank() && !hl_arr[0].isEmpty() && !hl_arr[1].isBlank() && !hl_arr[1].isEmpty())
				{
					alarmCols.put(pn.getPcodeid().getAlarmgroup().getAlarmgroup().toString(), hl_arr);
					Query_paramnames += hl_arr[0]+","+hl_arr[1]+",";
					hl_arr = new String[] {"",""};
				}
				ssd = pn.getSubsitedetails();
			}
			String uniqueTable = ssd.getUniquename()+"_alarms";
			String main_query = "select "+Query_paramnames+" datetime from "+uniqueTable+" where datetime between '"+SDateTime+" 00:00:00' and '"+EDateTime+" 23:00:00' order by datetime asc";
			reportdata_gs rdgs = jdbcTemplate.query(main_query, new ResultSetExtractor<reportdata_gs>() {
				@Override
				public reportdata_gs extractData(ResultSet rs) throws SQLException, DataAccessException{
					reportdata_gs rdgss = new reportdata_gs();
					List<String> colnames=new ArrayList<String>();
					List<String> respdata = new ArrayList<String>();
					colnames.add("Date Time");
					StringBuilder dataarr = new StringBuilder();
					for(Entry<String, String[]> mapelement : alarmCols.entrySet())
					{
						colnames.add(mapelement.getKey());
					}
					while(rs.next())
					{
						String dt = rs.getString("datetime");
						dataarr.append(dt);
						for(Entry<String, String[]> mapelement : alarmCols.entrySet())
						{
							String respv = "--";
							String[] stb = mapelement.getValue();
							String high_s = rs.getString(stb[0]);
							String low_s = rs.getString(stb[1]);
							if(high_s != null && high_s.equals("1"))
							{
								respv="High";
							}
							if(low_s != null && low_s.equals("1"))
							{
								respv = "Low";
							}
							dataarr.append(","+respv);
						}
						String[] darr = dataarr.toString().split(",");
						Set<String> s = new HashSet<>(Arrays.asList(darr));
						//System.out.println("darr:"+dataarr+"   size:"+s.size());
						int c=1,dcount=0;
						for(String sdarr : darr)
						{
							if(c > 1)
							{
								if(sdarr.trim().equals("--"))
								{
									dcount++;
								}
							}
							c++;
						}
						if(dcount != darr.length-1)
						{
							respdata.add(dataarr.toString());
						}
//						if(darr.length == 2 && !darr[1].trim().equals("--"))
//						{
//							System.out.println("t1:"+dataarr.toString());
//							respdata.add(dataarr.toString());
//						}
//						else
//						{
//							if(s.size() > darr.length-1 && darr.length > 2)
//							{
//								System.out.println("t2:"+dataarr.toString());
//								respdata.add(dataarr.toString());
//							}
//						}
						
						dataarr.setLength(0);
					}
					rdgss.setColumn_names(colnames);
					rdgss.setObj_list(respdata);
					
					return rdgss;
				}
			});
			return rdgs;
		}
		catch(Exception et)
		{
			System.out.println("T:"+et.toString());
			return null;
		}
		
	}
	
	@Override
	public List<Single_Alarm_Resp> particularAlarmReport(long subsiteid, String alarmgp, String SDateTime, String EDateTime)
	{
		try
		{
			List<parameternames> pnamesList = pnamesrepo.findBySubsitedetailsId(subsiteid);
			List<parameternames> pnames_grouped = new ArrayList<parameternames>();
			
			subsitedetails ssd = null;
			String pn_high_colname =  "";
			String pn_low_colname = "";
			for(parameternames pn : pnamesList)
			{
				if(pn.getPcodeid().getAlarmgroup().getAlarmgroup().toString().equals(alarmgp))
				{
					System.out.println("almgp:"+pn.getPcodeid().getAlarmgroup().getAlarmgroup().toString()+"   "+alarmgp);
					pnames_grouped.add(pn);
					if(pn.getAlarmid().getAtypename().toString().equals("HIGH"))
					{
						pn_high_colname = pn.getParamcolname();
					}
					if(pn.getAlarmid().getAtypename().toString().equals("LOW"))
					{
						pn_low_colname = pn.getParamcolname();
					}
					ssd = pn.getSubsitedetails();					
				}
			}
			String uniqueTable = ssd.getUniquename()+"_alarms";
			final String hn = pn_high_colname;
			final String ln = pn_low_colname;
			
			String Query = "select datetime,"+pn_high_colname+","+pn_low_colname+" from "+uniqueTable+" where datetime between '"+SDateTime+" 00:00:00' and '"+EDateTime+" 23:00:00' and ("+pn_high_colname+" = 1 or "+pn_low_colname+" = 1) order by datetime asc";
			System.out.println("Q:"+Query);
			List<Single_Alarm_Resp> AlarmResp = jdbcTemplate.query(Query, new ResultSetExtractor<List<Single_Alarm_Resp>>(){
				@Override
				public List<Single_Alarm_Resp> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Single_Alarm_Resp> sar_list = new ArrayList<Single_Alarm_Resp>();
					
					while(rs.next())
					{
						double high_col_val = 0;
						double low_col_val = 0;
						Single_Alarm_Resp sar = new Single_Alarm_Resp();
						String hstr = rs.getString(hn);
						String lstr = rs.getString(ln);
						if(hstr != null)
						{
							high_col_val = Double.parseDouble(rs.getString(hn));
						}
						if(lstr != null)
						{
							low_col_val = Double.parseDouble(rs.getString(ln));
						}
						
						
						String dt = rs.getString("datetime");
						
						if(high_col_val > 0)
						{
							sar.setDatetime(dt);
							sar.setHigh_low("High");
//							sar.setValue(high_col_val);
							sar_list.add(sar);
						}
						if(low_col_val > 0)
						{
							sar.setDatetime(dt);
							sar.setHigh_low("Low");
//							sar.setValue(low_col_val);
							sar_list.add(sar);
						}
					}
					
					return sar_list;
				}
			});
			return AlarmResp;
		}
		catch(Exception et)
		{
			System.out.println(et.toString());
			return null;
		}
	}
}