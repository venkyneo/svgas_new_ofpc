package com.svgas.main.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="sitedetails",
		uniqueConstraints= {
				@UniqueConstraint(columnNames="sitename")
		})
public class sitedetails 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=50)
	private String sitename;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=25)
	private String city;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=30)
	private String state;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=80)
	private String address;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=25)
	private String country;
	
//	@OneToMany(cascade=CascadeType.ALL)
//	private Set<subsitedetails> ssd;
	
	@NotBlank
	private int groupid;
	
	public sitedetails()
	{
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

//	public Set<subsitedetails> getSsd() {
//		return ssd;
//	}
//
//	public void setSsd(Set<subsitedetails> ssd) {
//		this.ssd = ssd;
//	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	
	
}