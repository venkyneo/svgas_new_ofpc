package com.svgas.main.models;

public class Reports_GS 
{
	String subsiteid;
	String report_type;//normal or alarm report
	String sub_report_type;//daily,monthly,yearly
	String start_date;
	String end_date;
	String month;
	String year;
	String timegap;
	String[] parameterids;
	public String getSubsiteid() {
		return subsiteid;
	}
	public void setSubsiteid(String subsiteid) {
		this.subsiteid = subsiteid;
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	public String getSub_report_type() {
		return sub_report_type;
	}
	public void setSub_report_type(String sub_report_type) {
		this.sub_report_type = sub_report_type;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTimegap() {
		return timegap;
	}
	public void setTimegap(String timegap) {
		this.timegap = timegap;
	}
	public String[] getParameterids() {
		return parameterids;
	}
	public void setParameterids(String[] parameterids) {
		this.parameterids = parameterids;
	}
}