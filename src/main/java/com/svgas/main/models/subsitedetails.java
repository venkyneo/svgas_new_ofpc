package com.svgas.main.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="subsitedetails",
uniqueConstraints = {
			@UniqueConstraint(columnNames="subsitename"),
			@UniqueConstraint(columnNames="uniquename")
})
public class subsitedetails 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=30)
	private String subsitename;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=30)
	private String subsitelocation;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=30)
	private  String uniquename;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="siteid")
	private sitedetails sitedetails;
	
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="modemid")
	private modemtype modemtype;
//	@ManyToMany(fetch=FetchType.LAZY)
//	@JoinTable(name="site_subsite",
//			joinColumns=@JoinColumn(name="subsite_id"),
//			inverseJoinColumns=@JoinColumn(name="site_id"))
//	private Set<sitedetails> site_details = new HashSet<>();
	//private long siteid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubsitename() {
		return subsitename;
	}

	public void setSubsitename(String subsitename) {
		this.subsitename = subsitename;
	}
	

	public String getSubsitelocation() {
		return subsitelocation;
	}

	public void setSubsitelocation(String subsitelocation) {
		this.subsitelocation = subsitelocation;
	}

	public sitedetails getSitedetails() {
		return sitedetails;
	}

	public void setSitedetails(sitedetails sitedetails) {
		this.sitedetails = sitedetails;
	}

	public modemtype getModemtype() {
		return modemtype;
	}

	public void setModemtype(modemtype modemtype) {
		this.modemtype = modemtype;
	}

	public String getUniquename() {
		return uniquename;
	}

	public void setUniquename(String uniquename) {
		this.uniquename = uniquename;
	}

	
//	public Set<sitedetails> getSite_details() {
//		return site_details;
//	}
//
//	public void setSite_details(Set<sitedetails> site_details) {
//		this.site_details = site_details;
//	}
}