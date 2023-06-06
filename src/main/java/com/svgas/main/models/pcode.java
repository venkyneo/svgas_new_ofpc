package com.svgas.main.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="pcode")
public class pcode 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@NotEmpty
	@Enumerated(EnumType.STRING)
	@Column(length=70, nullable = false)
	private EPcode pcodename;
	
	@ManyToOne
	@JoinColumn(name="main_category")
	private CategoryType main_category;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "view_category")
	private viewcategory view_category;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "alarm_group")
	private alarmgroup alarm_group;
	
	public pcode() {
		
	}

	public CategoryType getMain_category() {
		return main_category;
	}


	public void setMain_category(CategoryType main_category) {
		this.main_category = main_category;
	}


	public alarmgroup getAlarmgroup() {
		return alarm_group;
	}


	public void setAlarmgroup(alarmgroup alarmgroup) {
		this.alarm_group = alarmgroup;
	}

	public viewcategory getView_category() {
		return view_category;
	}

	public void setView_category(viewcategory view_category) {
		this.view_category = view_category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EPcode getPcodename() {
		return pcodename;
	}

	public void setPcodename(EPcode pcodename) {
		this.pcodename = pcodename;
	}
}