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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(name="publishtopics")
public class publishtopics 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	@NotBlank
	@NotEmpty
	@NotNull
	@Size(max=100)
	private String topicname;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false,cascade = CascadeType.MERGE)
	@JoinColumn(name="subsiteid", nullable=false)
	private subsitedetails subsitedetails;
	

	public subsitedetails getSubsitedetails() {
		return subsitedetails;
	}

	public void setSubsitedetails(subsitedetails subsitedetails) {
		this.subsitedetails = subsitedetails;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTopicname() {
		return topicname;
	}

	public void setTopicname(String topicname) {
		this.topicname = topicname;
	}
	
	public publishtopics()
	{
		
	}
}