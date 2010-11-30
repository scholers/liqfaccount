package com.scholers.account.business.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.scholers.account.bean.Pay;
import com.scholers.account.bean.PayType;
import com.scholers.account.business.PayServicesIntf;
import com.scholers.account.dao.PMF;
import com.scholers.account.util.ComUtil;
import com.scholers.account.util.PayTypeSingle;

/**
 * 
 * @author weique.lqf
 *
 */
public class PayService implements PayServicesIntf {
	//private PayDao dao = new PayDao();

	public List<PayType> getPayTypes(String email) {
		if (email == null) {
			return null;
		}
		return PayTypeSingle.getInstance().getPayTypeList(email);
	}

	/**
	 * 查询指定支付类型
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 支付类型对象
	 */
	public PayType getPayType(Long payTypeId) {
		return  PayTypeSingle.getInstance().getPayType(payTypeId);
	}

	/**
	 * 增加支付类型
	 * 
	 * @param bookType
	 *            支付类型对象
	 * @return 增加成功返回true，否则返回false
	 */
	public Long addPayType(PayType payType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long iRtn = 0L;
		try {
			pm.makePersistent(payType);
			iRtn = payType.getId();
		} catch (Exception ex) {
			iRtn = -1L;
		} finally {
			pm.close();
		}
		if(iRtn > 0)
			PayTypeSingle.getInstance().reloadAllData();
		return iRtn;
	}

	/**
	 * 更新支付类型
	 * 
	 * @param bookType
	 *            支付类型对象
	 * @return 增加成功返回true，否则返回false
	 */
	public boolean updatePayType(PayType payType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isSuccess = true;
		try {
			PayType e = pm.getObjectById(PayType.class, payType.getId());
			e.setDetail(payType.getDetail());
			e.setTitle(payType.getTitle());
		} catch (RuntimeException e) {
			isSuccess = false;
		} catch (Exception ex) {
			isSuccess = false;
		} finally {
			pm.close();
		}
		if(isSuccess)
			PayTypeSingle.getInstance().reloadAllData();
		return isSuccess;
	}

	/**
	 * 查询某种类型支付的数量
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 支付数量
	 */
	public int getPayNum(Long payTypeId) {
		if (payTypeId <= 0) {
			return 0;
		}
		List<Pay> accountList2 = new ArrayList<Pay>();
		List<Pay> accountList = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";
			queryTemplate = "payTypeId == %s ";
			filter = String.format(queryTemplate, payTypeId);
			query = pm.newQuery(Pay.class, filter);
			// query.setOrdering("useDate desc");
			accountList = (List<Pay>) query.execute();
			accountList2.addAll(accountList);
			return accountList2.size();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		
		return accountList2.size();
	}

	/**
	 * 删除支付类型
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deletePayType(Long payTypeId) {
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			PayType payType = pm
					.getObjectById(PayType.class, payTypeId);
			pm.deletePersistent(payType);
		} catch (RuntimeException e) {  
			isSuccess = false;
		} finally {
			pm.close();
		}
		if(isSuccess)
			PayTypeSingle.getInstance().reloadAllData();
		return isSuccess;
	}


	/**
	 * 查询指定支付
	 * 
	 * @param bookId
	 *            支付id
	 * @return 支付对象
	 */
	public Pay getPay(Long payId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Pay pay = null;
		try {
			pay = pm.getObjectById(Pay.class, payId);
		} finally {
			pm.close();
		}
		return pay;
	}

	/**
	 * 增加支付
	 * 
	 * @param book
	 *            支付对象
	 * @return 增加成功返回true，否则返回false
	 */
	public Long addPay(Pay accBean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long payId = 0L;
		try {
			pm.makePersistent(accBean);
			payId = accBean.getId();
		} finally {
			pm.close();
		}
		return payId;
	}

	/**
	 * 更新支付信息
	 * 
	 * @param book
	 *            支付对象
	 * @return 更新支付成功返回true，否则返回false
	 */
	public boolean updatePay(Pay pay) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isSuccess = true;
		try {
			Pay e = pm.getObjectById(Pay.class, pay.getId());
			e.setNotes(pay.getNotes());
			e.setUseDate(pay.getUseDate());
			e.setCreateDate(pay.getCreateDate());
			e.setPayTypeId(pay.getPayTypeId());
			e.setPrice(pay.getPrice());
			e.setPayName(pay.getPayName());
			e.setAuthor(pay.getAuthor());
		
		} catch (RuntimeException e) {
			isSuccess = false;
		} catch (Exception ex) {
			isSuccess = false;
		} finally {
			pm.close();
		}

		return isSuccess;
	}

	/**
	 * 删除支付
	 * 
	 * @param bookId
	 *            支付id
	 * @return 删除支付成功返回true，否则返回false
	 */
	public boolean deletePay(Long payId) {
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Pay pay = pm
					.getObjectById(Pay.class, payId);
			pm.deletePersistent(pay);
		} catch (RuntimeException e) {  
			isSuccess = false;
		} finally {
			pm.close();
		}
		return isSuccess;
	}

	/**
	 * 成批删除支付信息
	 * 
	 * @param bookIds
	 *            支付id列表
	 * @return 成批删除支付成功返回true，否则返回false
	 */
	public boolean deletePays(List<Long> payIds) {
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			for (int i = 0; i < payIds.size(); i++) {
				Long payId = (Long) payIds.get(i);
				Pay pay = pm
				.getObjectById(Pay.class, payId);
				pm.deletePersistent(pay);
			}
			
		} catch (Exception e) {
			
			isSuccess = false;
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return isSuccess;
	}

	
	/**
	 * 得到记录总数
	 * 
	 * @return
	 */
	public int qryAccCount(String email, String times, String times2) {
		//List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {

			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//开始日期
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate > today";
			} 
			//结束日期
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate < today2";
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
				//query.declareParameters("Date today2"); 
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
			
				
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Pay>) query.execute(today2);
			} else {
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Pay>) query.execute();
			}
			
			if (accountList != null)
				return accountList.size();
			else
				return 0;
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
	public List<Pay> getPayByTime(int startNum, int endNum, String email, String times, String times2) {
		if (email == null || email.equals("")) {
			return null;
		}
		List<Pay> accountList2 = new ArrayList<Pay>();
		List<Pay> accountList3 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			//开始日期
			if(times != null && !times.equals("")) {
				queryTemplate += "&& useDate > today";
			} 
			//结束日期
			if(times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate < today2";
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
				//query.declareParameters("Date today2"); 
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
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
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
			
				
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Pay>) query.execute(today2);
			} else {
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Pay>) query.execute();
			}
			accountList2.addAll(accountList);
			if (endNum > accountList2.size()) {
				endNum = accountList2.size();
			}
			//计算总支出
			Float tempFloat = Float.MIN_VALUE;
			for(Pay pay : accountList2) {
				tempFloat = ComUtil.add(tempFloat, pay.getPrice());
			}
			accountList3.addAll(accountList2.subList(startNum, endNum));
			//显示编号
			List<Pay> payList2 = new ArrayList<Pay>();
			for(Pay pay : accountList3) {
				String typeName = "";
				PayType payType =  getPayType(pay.getPayTypeId());
				if(payType != null) {
					typeName = payType.getTitle();
				}
				pay.setTypeName(typeName);
				payList2.add(pay);
			}
			Pay tolPay = new Pay();
			//Float price = Float.parseFloat(tempFloat);
			tolPay.setTypeName("总计");
			tolPay.setNotes("当前查询出来结果的总计！");
			tolPay.setPrice(tempFloat);
			payList2.add(tolPay);
			return payList2;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
	}
	
	/**
	 * 得到月度总支出
	 * @param email
	 * @return
	 */
	public Float getSumPay(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		Float totalSumPay = 0.0F;
		List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			Date date = ComUtil.getCurYearAndDate();
			if(date != null ) {
				queryTemplate = "email == \"%s\" && useDate >= start && useDate <= today";
			}
			filter = String.format(queryTemplate, email);
			
			query = pm.newQuery(Pay.class, filter);
			if(date != null ) {
				//query.declareImports("import java.util.Date,java.util.Date"); 
				query.declareImports("import java.util.Date;" +"import java.util.Date" ); 
				query.declareParameters("Date start, Date today"); 
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Pay>) query.execute(date, new Date());
			} else {
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Pay>) query.execute();
			}
			accountList2.addAll(accountList);

		
			//显示编号
			
			for(Pay pay : accountList2) {
				totalSumPay = ComUtil.add(pay.getPrice(), totalSumPay);
			}
			

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return totalSumPay;
	} 
	
	/**
	 * 本年度总支出
	 * @param email
	 * @return
	 */
	public Float getSumPayY(String email){
		if (email == null || email.equals("")) {
			return null;
		}
		Float totalSumPay = 0.0F;
		List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			String queryTemplate = "", filter = "";
			Date date = ComUtil.getCurYear();
			if(date != null ) {
				queryTemplate = "email == \"%s\" && useDate >= start && useDate <= today";
			}
			filter = String.format(queryTemplate, email);
			
			query = pm.newQuery(Pay.class, filter);
			if(date != null ) {
				//query.declareImports("import java.util.Date,java.util.Date"); 
				query.declareImports("import java.util.Date;" +"import java.util.Date" ); 
				query.declareParameters("Date start, Date today"); 
				query.setOrdering("useDate desc,id desc");
				//query.setOrdering("useDate desc");
				accountList = (List<Pay>) query.execute(date, new Date());
			} 
			accountList2.addAll(accountList);
			//显示编号
			for(Pay pay : accountList2) {
				totalSumPay = ComUtil.add(pay.getPrice(), totalSumPay);
			}
			

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return totalSumPay;
	} 
}
