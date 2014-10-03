package com.rackspace.feedback.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;


@Entity
@Table(name="feedback")
public class Feedback implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6573410801048114768L;
	
	private Long id;		
	private String pageurl;			
	private Boolean error;
	private Boolean moreinfo;	
	private String comment;
	private String email;	
	private Page page;
	private String date;

	public Feedback() {
	}
	
	public Feedback(Page page, String pageurl, boolean error, boolean moreinfo,
			String comment, String email,String date) {
		this.page = page;
		this.pageurl = pageurl;
		this.error = error;
		this.moreinfo = moreinfo;
		this.comment = comment;
		this.email = email;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pageid", nullable = false)
	@JsonIgnore
	public Page getPage() {
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Column(name = "pageurl", nullable = false, length = 400)
	public String getPageurl() {
		return this.pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}
	
	@Column(name = "date", nullable = false)
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date=date;
	}

	@Column(name = "error", nullable = false)
	public boolean isError() {
		return this.error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	@Column(name = "moreinfo", nullable = false)
	public boolean isMoreinfo() {
		return this.moreinfo;
	}

	public void setMoreinfo(boolean moreinfo) {
		this.moreinfo = moreinfo;
	}

	@Column(name = "comment", length = 1200)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "email", length = 200)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
