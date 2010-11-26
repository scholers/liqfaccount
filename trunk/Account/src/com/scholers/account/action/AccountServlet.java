package com.scholers.account.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.scholers.account.bean.Pay;
import com.scholers.account.business.AcountBusiness;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.ExtHelper;

/**
 * 
 * @author jill
 *
 */
public class AccountServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 880988976432412551L;
	private static final Logger log = Logger.getLogger(AccountServlet.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String xmlList = null;
		//操作
		Object obj = userOper(req, resp);
		if(obj != null) {
			xmlList = String.valueOf(obj);
			resp.setContentType("application/xml;charset=UTF-8");
			resp.getWriter().write(xmlList);   
			resp.sendRedirect("bookList.jsp");
		} else {
		/*
		 * //查询 List<Pay> accountList = accBus.qryAccount(); if
		 * (accountList.isEmpty() == false) { log.info("accountList size is " +
		 * accountList.size()); } req.setAttribute("AccountList", accountList);
		 */
		
			resp.sendRedirect("qryAccountList.jsp");
		}
	}
	
	
	private Object userOper(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		AcountBusiness accBus = new AcountBusiness();
		

		// 判断操作类型
		String strOperType = req.getParameter("operType");
		if (strOperType == null || "".equals(strOperType)) {
			strOperType = "qry";
		}
		
		
		// 保存
		if ("add".equals(strOperType)) {
			String content = req.getParameter("content");
			// 消费金额
			Float temAmt = Float.valueOf(req.getParameter("useAmt"));
			// 消费日期
			Date date = ComUtil.getStrToDate(req.getParameter("useDate"));
			Date createDate = new Date();
			Pay accBean = new Pay(user, content, date,
					createDate);
			accBean.setPrice(temAmt);
			if (user != null) {
				log.info("Greeting posted by user " + user.getNickname() + ": "
						+ content);
			} else {
				log.info("Greeting posted anonymously: " + content);
			}

			accBus.saveAccount(accBean);
			
		} else if ("del".equals(strOperType)) {
			accBus.delAccount(Long.valueOf(req.getParameter("accountId")));
		} else if("qry".equals(strOperType)) {
			int count = 20;
		    AcountBusiness actBus = new AcountBusiness();
		    //总数
		    int totalSize = actBus.qryAccCount(user);
		    int startNum = 0;
		    if(req.getParameter("start") != null){
		    	startNum = Integer.parseInt(req.getParameter("start"));
		    }
		    //当前页
		    int curPage = startNum;
		    startNum *= count;
		    int endNum = startNum + count;
		    
		    if(endNum > totalSize){
		    	endNum = totalSize;
		    }
		    List<Pay> accountList = actBus.qryAccount(startNum, endNum, user);
		    String xml = ExtHelper.getXmlFromList(accountList.size(), accountList);
		    return xml;
		}
		
		return null;
	}

}
