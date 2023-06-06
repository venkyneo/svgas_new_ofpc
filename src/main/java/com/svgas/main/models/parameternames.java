package com.svgas.main.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="paramternames")
public class parameternames 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotBlank
//	@Size(max=30)
//	private String datetime;
	@Column(nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=60)
	private String paramcolname;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	private int paramcolnum;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	private int paramindex;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=60)
	private String paramname;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	private long paramtype;
	
	@Column(nullable=true)
	@Size(max=30)
	private String paramunit;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	private float paramvalue;
	
	@Column(nullable=false)
	private float raw_min;
	
	@Column(nullable=false)
	private float raw_max;
	
	@Column(nullable=false)
	private float scale_min;
	
	@Column(nullable=false)
	private float scale_max;
	
	@Column(nullable=false)
	private float thr_min;
	
	@Column(nullable=false)
	private float thr_max;
	
//	@NotNull
//	private long subsite;
	
	@Column(nullable=false)
	private int alarmflag;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="subsiteid")
	private subsitedetails subsitedetails;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="parametertypesid")
	private parametertypes parametertypes;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="pcodeid")
	private pcode pcodeid;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="alarmid")
	private alarmtype alarmid;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="categoryid")
	private CategoryType categoryid;

	public CategoryType getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(CategoryType categoryid) {
		this.categoryid = categoryid;
	}

	public subsitedetails getSubsitedetails() {
		return subsitedetails;
	}

	public void setSubsitedetails(subsitedetails subsitedetails) {
		this.subsitedetails = subsitedetails;
	}

	public parametertypes getParametertypes() {
		return parametertypes;
	}

	public void setParametertypes(parametertypes parametertypes) {
		this.parametertypes = parametertypes;
	}

	public pcode getPcodeid() {
		return pcodeid;
	}

	public void setPcodeid(pcode pcodeid) {
		this.pcodeid = pcodeid;
	}

	public alarmtype getAlarmid() {
		return alarmid;
	}

	public void setAlarmid(alarmtype alarmid) {
		this.alarmid = alarmid;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getDatetime() {
//		return datetime;
//	}
//
//	public void setDatetime(String datetime) {
//		this.datetime = datetime;
//	}
	
	

	public String getParamcolname() {
		return paramcolname;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public void setParamcolname(String paramcolname) {
		this.paramcolname = paramcolname;
	}

	public int getParamcolnum() {
		return paramcolnum;
	}

	public void setParamcolnum(int paramcolnum) {
		this.paramcolnum = paramcolnum;
	}

	public int getParamindex() {
		return paramindex;
	}

	public void setParamindex(int paramindex) {
		this.paramindex = paramindex;
	}

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public long getParamtype() {
		return paramtype;
	}

	public void setParamtype(long paramtype) {
		this.paramtype = paramtype;
	}

	public String getParamunit() {
		return paramunit;
	}

	public void setParamunit(String paramunit) {
		this.paramunit = paramunit;
	}

	public float getParamvalue() {
		return paramvalue;
	}

	public void setParamvalue(float paramvalue) {
		this.paramvalue = paramvalue;
	}

	public float getRaw_min() {
		return raw_min;
	}

	public void setRaw_min(float raw_min) {
		this.raw_min = raw_min;
	}

	public float getRaw_max() {
		return raw_max;
	}

	public void setRaw_max(float raw_max) {
		this.raw_max = raw_max;
	}

	public float getScale_min() {
		return scale_min;
	}

	public void setScale_min(float scale_min) {
		this.scale_min = scale_min;
	}

	public float getScale_max() {
		return scale_max;
	}

	public void setScale_max(float scale_max) {
		this.scale_max = scale_max;
	}

	public float getThr_min() {
		return thr_min;
	}

	public void setThr_min(float thr_min) {
		this.thr_min = thr_min;
	}

	public float getThr_max() {
		return thr_max;
	}

	public void setThr_max(float thr_max) {
		this.thr_max = thr_max;
	}

//	public long getSubsite() {
//		return subsite;
//	}
//
//	public void setSubsite(long subsite) {
//		this.subsite = subsite;
//	}

	public int getAlarmflag() {
		return alarmflag;
	}

	public void setAlarmflag(int alarmflag) {
		this.alarmflag = alarmflag;
	}
}