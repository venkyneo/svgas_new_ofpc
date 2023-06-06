package com.svgas.main.pojo;

public class admin_page_data 
{
	long id;
	String subsitename;
	String city;
	String state;
	long ssid;
	int status;
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSubsitename() {
		return subsitename;
	}
	public void setSubsitename(String subsitename) {
		this.subsitename = subsitename;
	}
	public long getSsid() {
		return ssid;
	}
	public void setSsid(long ssid) {
		this.ssid = ssid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
