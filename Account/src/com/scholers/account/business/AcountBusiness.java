package com.scholers.account.business;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import com.google.appengine.api.users.User;
import com.scholers.account.bean.Pay;
import com.scholers.account.dao.PMF;

public class AcountBusiness {


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
 * 	 * ��ѯ
	 * ֧�ַ�ҳ
 * @param startNum ��ʼ��Ŀ
 * @param endNum ������Ŀ
 * @param user ��ǰ�û���Ϣ
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
			Pay accBean = pm
					.getObjectById(Pay.class, accountId);
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
}
