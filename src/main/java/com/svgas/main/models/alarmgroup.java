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
@Table(name="alarmgroup")
public class alarmgroup 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@Enumerated(EnumType.STRING)
	private EAlarmGroup alarmgroup;
	
	public alarmgroup()
	{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EAlarmGroup getAlarmgroup() {
		return alarmgroup;
	}

	public void setAlarmgroup(EAlarmGroup alarmgroup) {
		this.alarmgroup = alarmgroup;
	}
	
}