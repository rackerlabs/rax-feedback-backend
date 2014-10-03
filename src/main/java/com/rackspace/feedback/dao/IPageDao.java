package com.rackspace.feedback.dao;

import java.math.BigInteger;
import java.util.List;

import com.rackspace.feedback.entity.Page;

public interface IPageDao extends IAbstractDao<Page>{
	public List<Page>findRangeOfPages(int startFrom, int retrieveTotal);
    public List<Page>findAllPages();
    public Page findPageById(Long id);
    public BigInteger countPages();
}
