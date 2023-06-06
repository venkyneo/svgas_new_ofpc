package com.svgas.main.models;

import java.util.List;

public class reportdata_gs 
{
	String sitename;
	String report_type;
	String report_details;
	List<String> column_names;
	List<String> obj_list;
	
	
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	public String getReport_details() {
		return report_details;
	}
	public void setReport_details(String report_details) {
		this.report_details = report_details;
	}
	public List<String> getColumn_names() {
		return column_names;
	}
	public void setColumn_names(List<String> column_names) {
		this.column_names = column_names;
	}
	public List<String> getObj_list() {
		return obj_list;
	}
	public void setObj_list(List<String> obj_list) {
		this.obj_list = obj_list;
	}
}	