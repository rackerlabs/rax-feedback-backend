package com.rackspace.feedback.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="page")
public class Page implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6761582390823406281L;
	
	private Long           id;
	private String         url;
	private Long           count;
	private Long           negcount;
	private List<Feedback> feedbacks;

	public Page() {
	}

	public Page(Long id, Long count, Long negcount) {
		this.id = id;
		this.count = count;
		this.negcount = negcount;
	}

	public Page(Long id, String url, Long count, Long negcount, List<Feedback> feedbacks) {
		this.id = id;
		this.url = url;
		this.count = count;
		this.negcount = negcount;
		this.feedbacks = feedbacks;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "url", length = 400)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "count", nullable = false)
	public Long getCount() {
		return this.count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Column(name = "negcount", nullable = false)
	public Long getNegcount() {
		return this.negcount;
	}

	public void setNegcount(Long negcount) {
		this.negcount = negcount;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "page")
	@JsonIgnore
	public List<Feedback> getFeedbacks() {
		return this.feedbacks;
	}

	public void setFeedbacks( List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	public void addPositiveReviewCount(){
		++this.count;
	}
	
	public void addNegativeReviewCount(){
		++this.negcount;
	}

}
