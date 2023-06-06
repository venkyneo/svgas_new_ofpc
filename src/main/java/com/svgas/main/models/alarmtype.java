package com.svgas.main.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="alarmtype")
public class alarmtype 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotBlank
	@NotEmpty
	@Enumerated(EnumType.STRING)
	private EAlarmType atypename;
	
	public alarmtype()
	{
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EAlarmType getAtypename() {
		return atypename;
	}

	public void setAtypename(EAlarmType atypename) {
		this.atypename = atypename;
	}
}