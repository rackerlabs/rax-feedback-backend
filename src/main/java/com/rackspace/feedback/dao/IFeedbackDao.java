package com.rackspace.feedback.dao;

import java.util.List;

import com.rackspace.feedback.entity.Feedback;
import com.rackspace.feedback.entity.Page;

public interface IFeedbackDao extends IAbstractDao<Feedback>{

	public List<Feedback>findAllFeedbacksByPageId(Long pageId);
	public Page getPage();
	public List<Feedback>findAllFeedbacks();
	public List<Feedback>findFeedbacksByPageId(Long id);
}
