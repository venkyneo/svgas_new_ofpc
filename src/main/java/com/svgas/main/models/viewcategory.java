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
@Table(name="viewcategory")
public class viewcategory 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	@NotEmpty
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length=50, nullable = false)
	private EViewCategory viewcategoryname;
	
	public viewcategory()
	{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EViewCategory getViewcategoryname() {
		return viewcategoryname;
	}

	public void setViewcategoryname(EViewCategory viewcategoryname) {
		this.viewcategoryname = viewcategoryname;
	}
}