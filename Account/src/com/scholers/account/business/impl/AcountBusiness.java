package com.scholers.account.business.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.scholers.account.bean.Book;
import com.scholers.account.bean.CountBean;
import com.scholers.account.bean.Pay;
import com.scholers.account.dao.PMF;
import com.scholers.account.util.ComUtil;

public class AcountBusiness {


	/**
	 * 保存
	 * 
	 * @param accBean
	 */
	public void saveAccount(Pay accBean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(accBean);
		} finally {
			pm.close();
		}
	}

/**
 * 	 * 查询
	 * 支持分页
 * @param startNum 起始数目
 * @param endNum 结束数目
 * @param user 当前用户信息
 * @return
 */
	@SuppressWarnings("unchecked")
	public List<Pay> qryAccount(int startNum, int endNum, User user) {
		if (startNum > endNum) {
			return null;
		}
		List<Pay> accountList2 = new ArrayList<Pay>();
		List<Pay> accountList3 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			
            String queryTemplate = "",filter="";  
            String username = user.getEmail();
            queryTemplate = "email == \"%s\" ";  
            filter = String.format(queryTemplate, username);  
			query = pm.newQuery(Pay.class,filter);  
			query.setOrdering("useDate desc");
			accountList = (List<Pay>) query.execute();
			
			// 管理员
			if (!user.getEmail().equals("scholers@gmail.com")) {
				for (Pay tempBean : accountList) {
					if (tempBean.getUser().getEmail().equals(user.getEmail())) {
						accountList2.add(tempBean);
					}
				}
			} else { // 普通用户
				accountList2.addAll(accountList);
			}
			//
			if (accountList2 == null) {
				return new ArrayList<Pay>();
			}
			if (endNum > accountList2.size()) {
				endNum = accountList2.size();
			}
			accountList3.addAll(accountList2.subList(startNum, endNum));
			if (accountList3 != null) {
				return accountList3;
			} else {
				return new ArrayList<Pay>();
			}
		 } catch (RuntimeException e) {  
	            e.printStackTrace();  
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList3;
	}

	/**
	 * 得到记录总数
	 * 
	 * @return
	 */
	public int qryAccCount(User user) {
		List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {

			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			query = pm.newQuery(Pay.class);
			query.setOrdering("useDate desc");
			// query.setResult();
			// query.setResult("user.author = 'scholers@gmail.com'");
			accountList = (List<Pay>) pm.newQuery(query).execute();
			// 管理员
			if (!user.getEmail().equals("scholers@gmail.com")) {
				for (Pay tempBean : accountList) {
					if (tempBean.getUser().getEmail().equals(user.getEmail())) {
						accountList2.add(tempBean);
					}
				}
			} else {
				accountList2.addAll(accountList);
			}
			if (accountList2 != null)
				return accountList2.size();
			else
				return 0;
		} finally {
			pm.close();
		}
	}

	/*
	 * public void closePersistenceManager() { if (this.pm != null)
	 * this.pm.close(); }
	 */
	/**
	 * 根据消费编号删除
	 * 
	 * @param accBean
	 */
	public void delAccount(long accountId) {
		if (accountId <= 0) {
			return;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Pay accBean = pm
					.getObjectById(Pay.class, accountId);
			pm.deletePersistent(accBean);
		} finally {
			pm.close();
		}
	}

	/**
	 * 更新
	 * 
	 * @param accBean
	 */
	public void updateAccount(Pay accBean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Pay e = pm.getObjectById(Pay.class, accBean
					.getId());
			// if (titleChangeIsAuthorized(e, newTitle) {
			//e.setContent(accBean.getNotes());
			//e.setSueAmt(accBean.getSueAmt());
			//e.setNotes(accBean.getContent());
			// } else {
			// throw new UnauthorizedTitleChangeException(e, newTitle);
			// }
		} finally {
			pm.close();
		}
	}
	
	
	/**
	 * 按时间查询支出
	 * @param startNum 起始记录数
	 * @param endNum 截止记录数
	 * @param email 用户email
	 * @param times 时间
	 * @return
	 */
	public CountBean getPayByTimeAll(String email, String times, String times2) {
		if (email == null || email.equals("")) {
			return null;
		}
		CountBean tolPay = new CountBean();
		List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//开始日期
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			} 
			//结束日期
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Pay.class, filter);
			if(times != null && !times.equals("")
					&& times2 != null && !times2.equals("")) {
				query.declareImports("import java.util.Date"); 
				//开始日期
				query.declareParameters("Date today, Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				//结束日期
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Pay>) query.executeWithArray(today, today2);
				
			} else if(times != null && !times.equals("")
					&& (times2 == null || times2.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				accountList = (List<Pay>) query.execute(today);
			}
			//结束日期
			else if(times2 != null && !times2.equals("")
					 && (times == null || times.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				}   
				accountList = (List<Pay>) query.execute(today2);
			} else {
				accountList = (List<Pay>) query.execute();
			}
			accountList2.addAll(accountList);
			if (accountList2 == null || accountList2.size() == 0) {
				tolPay = new CountBean();
				tolPay.setTypeName("总计");
				tolPay.setPrice(Float.MIN_VALUE);
				tolPay.setNotes("总支出！");
				tolPay.setAuthor(email);
				tolPay.setEmail(email);
				return tolPay;
			}
			
			//计算总支出
			Float tempFloat = Float.MIN_VALUE;
			for(Pay pay : accountList2) {
				tempFloat = ComUtil.add(tempFloat, pay.getPrice());
			}

			tolPay = new CountBean();
			tolPay.setAuthor(email);
			tolPay.setEmail(email);
			tolPay.setTypeName("总计");
			tolPay.setNotes("总支出");
			tolPay.setPrice(tempFloat);

			return tolPay;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return tolPay;
	}
	
	
	/**
	 * 按时间查询支出
	 * @param startNum 起始记录数
	 * @param endNum 截止记录数
	 * @param email 用户email
	 * @param times 时间
	 * @return
	 */
	public CountBean getInByTimeAll(String email, String times, String times2) {
		if (email == null || email.equals("")) {
			return null;
		}
		CountBean tolPay = new CountBean();
		List<CountBean> payList2 = new ArrayList<CountBean>();
		List<Book> accountList2 = new ArrayList<Book>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//开始日期
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			} 
			//结束日期
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			if(times != null && !times.equals("")
					&& times2 != null && !times2.equals("")) {
				query.declareImports("import java.util.Date"); 
				//开始日期
				query.declareParameters("Date today, Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				//结束日期
				//query.declareParameters("Date today2"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.executeWithArray(today, today2);
				
			} else if(times != null && !times.equals("")
					&& (times2 == null || times2.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today = null; 
				try {   
					today = format1.parse(times);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				} 
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today);
			}
			//结束日期
			else if(times2 != null && !times2.equals("")
					 && (times == null || times.equals(""))) {
				query.declareImports("import java.util.Date"); 
				query.declareParameters("Date today2"); 
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); 
				Date today2 = null; 
				try {   
					today2 = format1.parse(times2);  
				} catch (ParseException e) {   
				    e.printStackTrace();   
				}   
			
				
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query.execute(today2);
			} else {
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute();
			}
			accountList2.addAll(accountList);
			if (accountList2 == null || accountList2.size() == 0) {
				tolPay = new CountBean();
				//Float price = Float.parseFloat(tempFloat);
				tolPay.setTypeName("总计");
				tolPay.setNotes("总收入！");
				tolPay.setPrice(Float.MIN_VALUE);
				tolPay.setEmail(email);
				tolPay.setAuthor(email);
				payList2.add(tolPay);
				return tolPay;
			}
			
			//计算总收入
			Float tempFloat = Float.MIN_VALUE;
			for(Book book : accountList2) {
				tempFloat = ComUtil.add(tempFloat, book.getPrice());
			}
			tolPay = new CountBean();
			tolPay.setAuthor(email);
			tolPay.setEmail(email);
			tolPay.setTypeName("总计");
			tolPay.setNotes("总收入");
			tolPay.setPrice(tempFloat);
			payList2.add(tolPay);
			return tolPay;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return tolPay;
	}
}
