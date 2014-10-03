package com.rackspace.feedback.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rackspace.feedback.dao.IPageDao;
import com.rackspace.feedback.entity.Page;

@Repository
public class PageDaoImpl extends AbstractDaoImpl<Page> implements IPageDao{

	private static Logger log = Logger.getLogger(PageDaoImpl.class);
			
	protected PageDaoImpl() {
		super(Page.class);
	}
	
	@Transactional(readOnly=true)
	public BigInteger countPages(){
		String METHOD_NAME="countPages()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START:");
		}
		Session session=super.getCurrentSession();	
		Query query=session.createSQLQuery("SELECT count(p.id) from page p");
		BigInteger retVal=(BigInteger)query.uniqueResult();
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": END: retVal="+retVal);
		}
		return retVal;
	
	}

	@Transactional(readOnly=true)
	public List<Page>findRangeOfPages(int startFrom, int retrieveTotal){
		String METHOD_NAME="findRangeOfPages()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START: startFrom="+startFrom+" retrieveTotal="+retrieveTotal);
		}		
		Session session=super.getCurrentSession();	
		Query query=session.createQuery("from Page p order by p.id");
		query.setFirstResult(startFrom);
		query.setMaxResults(retrieveTotal);
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": query.getQueryString()="+query.getQueryString());
		}		
		List<Page>retVal=query.list();
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": END: retVal.size()="+retVal.size());
		}		
		return retVal;		
	}
	
	
	@Transactional(readOnly=true)
	public List<Page>findAllPages(){
		String METHOD_NAME="findAllPages()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START:");
		}
		Session session=super.getCurrentSession();	
		Query query=session.createQuery("from Page p order by p.id");
		List<Page>retVal=query.list();
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": END: retVal.size()="+retVal.size());
		}		
		return retVal;		
	}

	@Transactional(readOnly=true)
	public Page findPageById(Long id) {
		Page retVal=null;
		String METHOD_NAME="findPageById()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START:");
		}
		Session session=super.getCurrentSession();
		Query query=session.createQuery("from Page p where p.id = :id order by p.id");
		query.setParameter("id", id);

		List<Page>pages=query.list();
		if(log.isDebugEnabled()){
			log.debug("(null!=pages&&pages.size()>0)="+
		    (null!=pages&&pages.size()>0));
		}
		if(null!=pages&&pages.size()>0){
			retVal=pages.get(0);
		}
		if(log.isDebugEnabled()){			
			log.debug(METHOD_NAME+": END:");
		}
		return retVal;
	}
	
	

}
