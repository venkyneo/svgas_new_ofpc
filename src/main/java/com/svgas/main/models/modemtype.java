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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="modemtype")
public class modemtype 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=40)
	private String modemname;
	
//	@OneToMany(cascade = CascadeType.ALL)
//	private Set<subsitedetails> ssd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModemname() {
		return modemname;
	}

	public void setModemname(String modemname) {
		this.modemname = modemname;
	}

//	public Set<subsitedetails> getSsd() {
//		return ssd;
//	}
//
//	public void setSsd(Set<subsitedetails> ssd) {
//		this.ssd = ssd;
//	}
}