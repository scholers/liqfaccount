package com.scholers.account.business.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.appengine.api.users.User;
import com.scholers.account.bean.Book;
import com.scholers.account.bean.CountBean;
import com.scholers.account.bean.Pay;
import com.scholers.account.business.AccountIntf;
import com.scholers.account.dao.PMF;
import com.scholers.account.util.ComUtil;

@Component("acountBusiness")
public class AcountBusiness implements AccountIntf {

	/**
	 * ����
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
	 * * ��ѯ ֧�ַ�ҳ
	 * 
	 * @param startNum
	 *            ��ʼ��Ŀ
	 * @param endNum
	 *            ������Ŀ
	 * @param user
	 *            ��ǰ�û���Ϣ
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

			String queryTemplate = "", filter = "";
			String username = user.getEmail();
			queryTemplate = "email == \"%s\" ";
			filter = String.format(queryTemplate, username);
			query = pm.newQuery(Pay.class, filter);
			query.setOrdering("useDate desc");
			accountList = (List<Pay>) query.execute();

			// ����Ա
			if (!user.getEmail().equals("scholers@gmail.com")) {
				for (Pay tempBean : accountList) {
					if (tempBean.getUser().getEmail().equals(user.getEmail())) {
						accountList2.add(tempBean);
					}
				}
			} else { // ��ͨ�û�
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
	 * �õ���¼����
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
			// ����Ա
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
	 * �������ѱ��ɾ��
	 * 
	 * @param accBean
	 */
	public void delAccount(long accountId) {
		if (accountId <= 0) {
			return;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Pay accBean = pm.getObjectById(Pay.class, accountId);
			pm.deletePersistent(accBean);
		} finally {
			pm.close();
		}
	}

	/**
	 * ����
	 * 
	 * @param accBean
	 */
	public void updateAccount(Pay accBean) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Pay e = pm.getObjectById(Pay.class, accBean.getId());
			// if (titleChangeIsAuthorized(e, newTitle) {
			// e.setContent(accBean.getNotes());
			// e.setSueAmt(accBean.getSueAmt());
			// e.setNotes(accBean.getContent());
			// } else {
			// throw new UnauthorizedTitleChangeException(e, newTitle);
			// }
		} finally {
			pm.close();
		}
	}

	/**
	 * ��ʱ���ѯ֧��
	 * 
	 * @param startNum
	 *            ��ʼ��¼��
	 * @param endNum
	 *            ��ֹ��¼��
	 * @param email
	 *            �û�email
	 * @param times
	 *            ʱ��
	 * @return
	 */
	public List<CountBean> getPayByTimeAll(String email, String times,
			String times2, boolean isPayType) {
		if (email == null || email.equals("")) {
			return null;
		}
		CountBean tolPay = new CountBean();
		List<Pay> accountList2 = new ArrayList<Pay>();
		List<CountBean> rtnList = new ArrayList<CountBean>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Pay> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			// ��ʼ����
			if (times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			}
			// ��������
			if (times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Pay.class, filter);
			
			if (times != null && !times.equals("") && times2 != null
					&& !times2.equals("")) {
				query.declareImports("import java.util.Date");
				// ��ʼ����
				query.declareParameters("Date today, Date today2");
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date today = null;
				try {
					today = format1.parse(times);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// ��������
				Date today2 = null;
				try {
					today2 = format1.parse(times2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.setOrdering("useDate desc,id desc");

				accountList = (List<Pay>) query.executeWithArray(today, today2);

			} else if (times != null && !times.equals("")
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
			// ��������
			else if (times2 != null && !times2.equals("")
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
				tolPay.setTypeName("�ܼ�");
				tolPay.setPrice(Float.MIN_VALUE);
				tolPay.setNotes("��֧��");
				tolPay.setAuthor(email);
				tolPay.setEmail(email);
				rtnList.add(tolPay);
				return rtnList;
			}
			// ����ͳ��
			if (!isPayType) {
				// ������֧��
				Float tempFloat = Float.MIN_VALUE;
				for (Pay pay : accountList2) {
					tempFloat = ComUtil.add(tempFloat, pay.getPrice());
				}

				tolPay = new CountBean();
				tolPay.setAuthor(email);
				tolPay.setEmail(email);
				tolPay.setTypeName("�ܼ�");
				tolPay.setNotes("��֧��");
				tolPay.setPrice(tempFloat);
				rtnList.add(tolPay);
			} else {
				Map<Long, CountBean> tempMap = new HashMap<Long, CountBean>();
				for (Pay pay : accountList2) {
					tolPay = new CountBean();
					BeanUtils.copyProperties(pay, tolPay);
					tolPay.setAccTypeId(pay.getPayTypeId());
					//�����ֶ�����
					if(tempMap.containsKey(tolPay.getAccTypeId())) {
						Float tempFloat = tolPay.getPrice();
						CountBean tempBean = tempMap.get(tolPay.getAccTypeId());
						tempBean.setPrice(ComUtil.add(tempFloat, tempBean.getPrice()));
						tempMap.put(tolPay.getAccTypeId(), tempBean);
					} else {
						tempMap.put(tolPay.getAccTypeId(), tolPay);
					}
					
					
				}
				Iterator<CountBean> it = tempMap.values().iterator();
		        while (it.hasNext()) {
		        	rtnList.add(it.next());
		        }
			}

			return rtnList;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return rtnList;
	}

	/**
	 * ��ʱ���ѯ����
	 * 
	 * @param startNum
	 *            ��ʼ��¼��
	 * @param endNum
	 *            ��ֹ��¼��
	 * @param email
	 *            �û�email
	 * @param times
	 *            ʱ��
	 * @param endtime
	 *            ����ʱ��
	 * @param isInType
	 *            �Ƿ�������ͳ��
	 * @return
	 */
	public List<CountBean> getInByTimeAll(String email, String times,
			String times2, boolean isInType) {
		if (email == null || email.equals("")) {
			return null;
		}
		CountBean tolPay = new CountBean();
		List<Book> accountList2 = new ArrayList<Book>();
		List<CountBean> rtnList = new ArrayList<CountBean>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Book> accountList = null;
			pm.setDetachAllOnCommit(true);

			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";
			// ��ʼ����
			if (times != null && !times.equals("")) {
				queryTemplate += "&& useDate >= today";
			}
			// ��������
			if (times2 != null && !times2.equals("")) {
				queryTemplate += " && useDate <= today2";
			}
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Book.class, filter);
			if (times != null && !times.equals("") && times2 != null
					&& !times2.equals("")) {
				query.declareImports("import java.util.Date");
				// ��ʼ����
				query.declareParameters("Date today, Date today2");
				DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				Date today = null;
				try {
					today = format1.parse(times);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// ��������
				// query.declareParameters("Date today2");
				Date today2 = null;
				try {
					today2 = format1.parse(times2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				query.setOrdering("useDate desc,id desc");
				accountList = (List<Book>) query
						.executeWithArray(today, today2);

			} else if (times != null && !times.equals("")
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
			// ��������
			else if (times2 != null && !times2.equals("")
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
				// query.setOrdering("useDate desc");
				accountList = (List<Book>) query.execute();
			}
			accountList2.addAll(accountList);
			if (accountList2 == null || accountList2.size() == 0) {
				tolPay = new CountBean();
				// Float price = Float.parseFloat(tempFloat);
				tolPay.setTypeName("�ܼ�");
				tolPay.setNotes("������");
				tolPay.setPrice(Float.MIN_VALUE);
				tolPay.setEmail(email);
				tolPay.setAuthor(email);
				rtnList.add(tolPay);
				return rtnList;
			}
			if (!isInType) {
				// ����������
				Float tempFloat = Float.MIN_VALUE;
				for (Book book : accountList2) {
					tempFloat = ComUtil.add(tempFloat, book.getPrice());
				}
				tolPay = new CountBean();
				tolPay.setAuthor(email);
				tolPay.setEmail(email);
				tolPay.setTypeName("�ܼ�");
				tolPay.setNotes("������");
				tolPay.setPrice(tempFloat);
				rtnList.add(tolPay);
			} else {
				Map<Long, CountBean> tempMap = new HashMap<Long, CountBean>();
				for (Book book : accountList2) {
					tolPay = new CountBean();
					BeanUtils.copyProperties(book, tolPay);
					tolPay.setAccTypeId(book.getBookTypeId());
					//�����ֶ�����
					if(tempMap.containsKey(tolPay.getAccTypeId())) {
						Float tempFloat = tolPay.getPrice();
						CountBean tempBean = tempMap.get(tolPay.getAccTypeId());
						tempBean.setPrice(ComUtil.add(tempFloat, tempBean.getPrice()));
						tempMap.put(tolPay.getAccTypeId(), tempBean);
					} else {
						tempMap.put(tolPay.getAccTypeId(), tolPay);
					}
					
					
				}
				Iterator<CountBean> it = tempMap.values().iterator();
		        while (it.hasNext()) {
		        	rtnList.add(it.next());
		        }
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return rtnList;
	}
}
