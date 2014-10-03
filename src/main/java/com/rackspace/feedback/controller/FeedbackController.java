package com.rackspace.feedback.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rackspace.feedback.dao.IFeedbackDao;
import com.rackspace.feedback.dao.IPageDao;
import com.rackspace.feedback.entity.Feedback;
import com.rackspace.feedback.entity.Page;

@Controller
@RequestMapping(value="/")
@Configuration
public class FeedbackController {

	private static Logger log = Logger.getLogger(FeedbackController.class);

	@Autowired
	private IPageDao pageDao;

	@Autowired
	private IFeedbackDao feedbackDao;
	
	@RequestMapping(value="/getFeedbackPages", method=RequestMethod.GET)
	@ResponseBody
	public FeedbackPages getFeedbackPages(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("startfrom")Integer startFrom, 
			@RequestParam("retrievetotal")Integer retrieveTotal){
		String METHOD_NAME="getFeedbackPages()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START: startFrom="+startFrom+" retrieveTotal="+retrieveTotal);			
		}
		this.allowOrigin(request, response);
		FeedbackPages retVal=new FeedbackPages();
	    retVal.setCount(this.pageDao.countPages().longValue());
	    retVal.setPages(this.pageDao.findRangeOfPages(startFrom, retrieveTotal));
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": END");			
		}
		return retVal;
	}
	
	@RequestMapping(value="/countPages", method=RequestMethod.GET)
	@ResponseBody
	public Long getPageTotal(HttpServletRequest request, HttpServletResponse response){
		allowOrigin(request, response);
		return this.pageDao.countPages().longValue();
	}
	
	@RequestMapping(value="/getRangeOfFeedbacks", method=RequestMethod.GET)
	@ResponseBody	
	public List<Page>getRangeOfFeedbacks(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("startfrom") Integer startFrom, 
			@RequestParam("retrievetotal") Integer retrieveTotal){
		allowOrigin(request, response);
		return this.pageDao.findRangeOfPages(startFrom, retrieveTotal);

	}

	@RequestMapping(value="/getAllFeedbacks", method=RequestMethod.GET)
	@ResponseBody	
	public List<Feedback>getAllFeedbacks(HttpServletRequest request, HttpServletResponse response){
		allowOrigin(request, response);
		return this.feedbackDao.findAllFeedbacks();
	}

	@RequestMapping(value="/getAllFeedbacksByPageId", method=RequestMethod.GET)
	@ResponseBody	
	public List<Feedback>getAllFeedbacksByPageId(HttpServletRequest request, HttpServletResponse response,@RequestParam("pageid") Long pageid){
		allowOrigin(request, response);
		return this.feedbackDao.findAllFeedbacksByPageId(pageid);
	}

	@RequestMapping(value="/getAllPages", method=RequestMethod.GET)
	@ResponseBody	
	public List<Page>getAllPages(HttpServletRequest request, HttpServletResponse response){
		allowOrigin(request, response);
		return this.pageDao.findAllPages();
	}
	
	
	@RequestMapping(value="/saveOrUpdatePage")
	@ResponseBody
	public String upsertPage(HttpServletRequest request, HttpServletResponse response){		
		String METHOD_NAME="upsertPage()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START: ");
			log.debug(METHOD_NAME+": calling allowCrossDomain(request,response)!!!");
		}
		String pageUrl=request.getParameter("pageurl");
		String isYes=request.getParameter("isyes");		
		if(null!=pageUrl&&pageUrl.length()>400){
			pageUrl=pageUrl.substring(0,400);
		}
		if(null!=isYes&&isYes.length()>4){
			isYes=isYes.substring(0, 4);
		}
		allowOrigin(request,response);
		String reqMethod=request.getMethod();
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": reqMethod="+reqMethod);
			log.debug(METHOD_NAME+": pageurl="+pageUrl);
			log.debug(METHOD_NAME+": isyes="+isYes);
		}
		if(null!=reqMethod && reqMethod.equalsIgnoreCase("options")){
			allowOrigin(request, response);
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": This is an options request, just allow the domain");				
			}	
			return "Options method request, just allow the domain";
		}
		else if(null!=reqMethod && reqMethod.equalsIgnoreCase("post")){
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": (null==pageUrl||null==isYes)="+(null==pageUrl||null==isYes));				
			}
			if(null==pageUrl||pageUrl.isEmpty()||null==isYes||isYes.isEmpty()){
				String page;
				StringBuffer pageStrBuff=new StringBuffer("");
				int readInt;
				char readChar;
				try {
					InputStream inny=request.getInputStream();
					if(null!=inny){
						while(-1!=(readInt=inny.read())){
							readChar=(char)readInt;
							pageStrBuff.append(readChar);
						}
						page=pageStrBuff.toString();
						if(log.isDebugEnabled()){
							log.debug(METHOD_NAME+": page="+page);
						}
						if(log.isDebugEnabled()){
							log.debug(METHOD_NAME+": request.getHeader(\"Origin\")="+request.getHeader("Origin"));
						}
						try {
							if(log.isDebugEnabled()){
								log.debug(METHOD_NAME+": creating pageJSON page="+page);
							}
							JSONObject pageJSON=new JSONObject(page);
							if(pageJSON.has("pageurl")){
								pageUrl=pageJSON.getString("pageurl");
							}
							if(null!=pageUrl&&pageUrl.length()>400){
								pageUrl=pageUrl.substring(0,400);
							}
							//true is only 4 letters long
							if(null!=isYes&&isYes.length()>4){
								isYes=isYes.substring(0, 4);
							}
							if(pageJSON.has("isyes")){
								isYes=pageJSON.getString("isyes");
							}
							if(log.isDebugEnabled()){
								log.debug(METHOD_NAME+": pageUrl="+pageUrl);
								log.debug(METHOD_NAME+": isYes="+isYes);
							}
						} 
						catch (JSONException e) {					
							e.printStackTrace();
							return "JSONException error: "+e.getMessage();
						}
						catch(Throwable e){					
							e.printStackTrace();
							return "Thorwable exception error: "+e.getMessage();
						}
					}
				} 
				catch (IOException e1) {
					e1.printStackTrace();
					return "IOException error: "+e1.getMessage();
				}
				if(log.isDebugEnabled()){
					log.debug(METHOD_NAME+": END: response.getResponse");
				}
			}
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": (null!=pageUrl&&null!=isYes)="+(null!=pageUrl&&null!=isYes));				
			}
			if(null!=pageUrl && null!=isYes){
				long pageId=pageUrl.hashCode();
				Page thePage=pageDao.findPageById(pageId);
				try{
					Boolean isYesBool=new Boolean(isYes);
					if(log.isDebugEnabled()){
						log.debug(METHOD_NAME+": pageId="+pageId);
						log.debug(METHOD_NAME+": isYesBool="+isYesBool);
						log.debug(METHOD_NAME+": (null==thePage)="+(null==thePage));
					}
					//This is the first time there has been any form of feedback on the page
					//create the page
					if(null==thePage){
						thePage = new Page();
						thePage.setId(pageId);
						thePage.setUrl(pageUrl);	
						thePage.setCount(0l);
						thePage.setNegcount(0l);
					}
					if(isYesBool){
						thePage.addPositiveReviewCount();
					}
					else{
						thePage.addNegativeReviewCount();
					}
					this.pageDao.save(thePage);	
				}
				catch(Throwable e){
					return "Throwable error: "+e.getMessage();
				}
			}
			return "Everything passed";
		}
		else{
		   return "Method not supported: "+reqMethod;
		}
	}


	@RequestMapping(value="/recordFeedback", method=RequestMethod.POST)
	@ResponseBody
	public String recordFeedback(HttpServletRequest request, HttpServletResponse response){
		String METHOD_NAME="recordFeedback()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START: ");
		}
		String feedbackStr;
		StringBuffer feedbackStrBuff=new StringBuffer("");
		int readInt;
		char readChar;
		allowOrigin(request,response);
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": request.getHeader(\"Origin\")="+request.getHeader("Origin"));
		}
		try{

			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": request.getHeader(\"Origin\")="+request.getHeader("Origin"));
			}
			try{
				String pageUrl=request.getParameter("pageurl");	
				long pageId=-1;
				if(null!=pageUrl&&!pageUrl.isEmpty()){
					pageId=pageUrl.hashCode();
				}
				String errorStr=request.getParameter("error");
				boolean error=Boolean.parseBoolean(errorStr);
				String moreinfoStr=request.getParameter("moreinfo");
				boolean moreinfo=Boolean.parseBoolean(moreinfoStr);
				String comment=request.getParameter("comment");
				String email=request.getParameter("email");
				boolean goOn=true;
				if(log.isDebugEnabled()){
					log.debug(METHOD_NAME+": goOn="+goOn);
					log.debug(METHOD_NAME+": email="+email);
				}
				if(null!=email){
					try{
						InternetAddress emailAddr=new InternetAddress(email);
						if(log.isDebugEnabled()){
							log.debug(METHOD_NAME+": emailAddr="+emailAddr);
						}
						emailAddr.validate();
					}
					catch(AddressException e){
						if(log.isDebugEnabled()){
							log.debug(METHOD_NAME+": AddressException caught, email: "+email);
						}
						if(email.isEmpty()){
							if(log.isDebugEnabled()){
								log.debug(METHOD_NAME+": Setting goOn to true");
							}
							goOn=true;
						}
						else{
							if(log.isDebugEnabled()){
								log.debug(METHOD_NAME+": Setting goOn to false");
							}
							goOn=false;
						}
					}
				}
				if(goOn){
					String dateStr=request.getParameter("date");
					if(null!=pageUrl&&pageUrl.length()>400){
						pageUrl=pageUrl.substring(0,400);
					}
					if(null!=comment&&comment.length()>1200){
						//Max size of comment is 1200
						comment=comment.substring(0,1200);
					}
					if(null!=email&&email.length()>200){
						email=email.substring(0,200);
					}
					if(log.isDebugEnabled()){
						log.debug(METHOD_NAME+": pageUrl="+pageUrl);
						log.debug(METHOD_NAME+": pageId="+pageId);
						log.debug(METHOD_NAME+": error="+error);
						log.debug(METHOD_NAME+": moreinfo="+moreinfo);
						log.debug(METHOD_NAME+": comment="+comment);
						log.debug(METHOD_NAME+": email="+email);
						log.debug(METHOD_NAME+": dateStr="+dateStr);
					}	


					//There were no parameters sent, check the body for data
					if(null==pageUrl||pageUrl.isEmpty()){
						try {
							InputStream inny=request.getInputStream();
							if(null!=inny){
								while(-1!=(readInt=inny.read())){
									readChar=(char)readInt;
									feedbackStrBuff.append(readChar);
								}
								feedbackStr=feedbackStrBuff.toString();
								if(log.isDebugEnabled()){
									log.debug(METHOD_NAME+": creating pageJSON page="+feedbackStr);
								}
								JSONObject feedbackJSON=new JSONObject(feedbackStr);
								if(feedbackJSON.has("pageurl")){
									pageUrl=feedbackJSON.getString("pageurl");
								}
								if(null!=pageUrl&&!pageUrl.isEmpty()){
									pageId=pageUrl.hashCode();
								}
								if(feedbackJSON.has("error")){
									error=feedbackJSON.getBoolean("error");
								}
								if(feedbackJSON.has("moreinfo")){
									moreinfo=feedbackJSON.getBoolean("moreinfo");
								}
								if(feedbackJSON.has("comment")){
									comment=feedbackJSON.getString("comment");
								}
								if(feedbackJSON.has("email")){
									email=feedbackJSON.getString("email");
								}
								if(feedbackJSON.has("date")){
									dateStr=feedbackJSON.getString("date");
								}
								if(null!=pageUrl&&pageUrl.length()>400){
									pageUrl=pageUrl.substring(0,400);
								}
								if(null!=comment&&comment.length()>1200){
									//Max size of comment is 1200
									comment=comment.substring(0,1200);
								}
								if(null!=email&&email.length()>200){
									email=email.substring(0,200);
								}
								if(log.isDebugEnabled()){
									log.debug(METHOD_NAME+": pageUrl="+pageUrl);
									log.debug(METHOD_NAME+": pageId="+pageId);
									log.debug(METHOD_NAME+": error="+error);
									log.debug(METHOD_NAME+": moreinfo="+moreinfo);
									log.debug(METHOD_NAME+": comment="+comment);
									log.debug(METHOD_NAME+": email="+email);
									log.debug(METHOD_NAME+": dateStr="+dateStr);
								}
							}
						}
						catch (JSONException e) {					
							e.printStackTrace();
							if(log.isDebugEnabled()){
								log.debug(METHOD_NAME+": ~~~END: ");
							}
							return "JSONException error: "+e.getMessage();
						}
					}
					String sanitizedComments=sanitizeComments(comment);
					//We need to double check to make sure we have this page				
					Page thePage=pageDao.findPageById(pageId);

					//The page does not exist, the browser may not have gone through the UI,
					//therefore we should do nothing, this could be spam
					if(null!=thePage){
						Feedback theFeedback=new Feedback();
						theFeedback.setComment(sanitizedComments);
						theFeedback.setEmail(email);
						theFeedback.setError(error);
						theFeedback.setMoreinfo(moreinfo);
						theFeedback.setPageurl(pageUrl);
						theFeedback.setPage(thePage);
						theFeedback.setDate(dateStr);
						this.feedbackDao.save(theFeedback);
					}

				}
				else{
					return "everything passed";
				}
			}
			catch(NumberFormatException e){
				e.printStackTrace();
				if(log.isDebugEnabled()){
					log.debug(METHOD_NAME+": ~~~END: ");
				}
				return "NumberFormatException error: "+e.getMessage();
			}
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": ~~~END: ");
			}
			return "Everything Passed!!!";
		
		}
		catch(Throwable e){					
			e.printStackTrace();
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": ~~~END: ");
			}
			return "Throwable error: "+e.getMessage();
		}
	}
	
	private String sanitizeComments(String comment){
		String retVal="";
		if(null!=comment && !comment.isEmpty()){
			comment=comment.replaceAll("\"", "&quot;");
			comment=comment.replaceAll("'", "&#39;");
			comment=comment.replaceAll("<", "&lt;");
			comment=comment.replaceAll(">", "&gt;");
		    comment=comment.replaceAll("Ç","&laquo;");
		    comment=comment.replaceAll("È","&raquo;");
		    if(comment.length()>1200){
		    	comment=comment.substring(0,1200);
		    }
		    retVal=comment;
		}
		return retVal;
	}
	

	private void allowOrigin(HttpServletRequest request, HttpServletResponse response){
		String METHOD_NAME="allowOrigin()";
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": START:");	
		}		
		String headerOrigin=request.getHeader("Origin");
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": headerOrigin="+headerOrigin);
		}
		if(null!=headerOrigin ){//&& (headerOrigin.toLowerCase()).endsWith("rackspace.com")){
			response.setHeader("Access-Control-Allow-Origin", headerOrigin);
		}
		else{
			String serverName=request.getServerName();
			if(log.isDebugEnabled()){
				log.debug(METHOD_NAME+": serverName="+serverName);				
			}
			response.setHeader("Access-Control-Allow-Origin", serverName);
		}
		if(log.isDebugEnabled()){
			log.debug(METHOD_NAME+": END:");	
		}
	}
	
	private class FeedbackPages{
		private List<Page>pages;
		private Long count;
		public List<Page> getPages() {
			return pages;
		}
		public void setPages(List<Page> pages) {
			this.pages = pages;
		}
		public Long getCount() {
			return count;
		}
		public void setCount(Long count) {
			this.count = count;
		}
	}

}
