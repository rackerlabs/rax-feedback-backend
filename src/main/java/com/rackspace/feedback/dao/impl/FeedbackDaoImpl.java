package com.rackspace.feedback.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rackspace.feedback.dao.IFeedbackDao;
import com.rackspace.feedback.entity.Feedback;
import com.rackspace.feedback.entity.Page;

@Repository
public class FeedbackDaoImpl extends AbstractDaoImpl<Feedback> implements IFeedbackDao{

	private static Logger log = Logger.getLogger(FeedbackDaoImpl.class);
			
	public FeedbackDaoImpl(){
		super(Feedback.class);
	}
	

	@Transactional(readOnly=true)
	public Page getPage() {
		// TODO Auto-generated method stub
		return this.getPage();
	}

	@Transactional(readOnly=true)
	public List<Feedback> findAllFeedbacksByPageId(Long pageId) {
		Session session=super.getCurrentSession();		
		Query query=session.createQuery("from Feedback f where f.page.id = :pageid order by f.id");
		query.setParameter("pageid", pageId);
		return query.list();
	}
	
	@Transactional(readOnly=true)
	public List<Feedback>findAllFeedbacks(){
		Session session=super.getCurrentSession();	
		Query query=session.createQuery("from Feedback");		
		return  query.list();
	}
	
	@Transactional(readOnly=true)
	public List<Feedback>findFeedbacksByPageId(Long pageid){
		Session session=super.getCurrentSession();		
		Query query=session.createQuery("from Feedback f order by f.id where f.pageid = :pageid");	
		query.setParameter("pageid", pageid);
		return query.list();
	}

}
