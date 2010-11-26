package com.scholers.account.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.scholers.account.bean.Users;
import com.scholers.account.dao.PMF;

/**
 * �û���Ϣ��ѯ
 * @author jill
 *
 */
public class UsersService {
	//UsersDao dao = new UsersDao();
	public List<Users> getUsers(int startNum, int endNum){
		
		List<Users> accountList2 = new ArrayList<Users>();
		List<Users> accountList3 = new ArrayList<Users>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Users> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			
			//filter = String.format(queryTemplate, );
			query = pm.newQuery(Users.class);
			
			query.setOrdering("createDate desc,id desc");
			accountList = (List<Users>) query.execute();
			
			accountList2.addAll(accountList);
			if (accountList2 == null) {
				return new ArrayList<Users>();
			}
			
			if (endNum > accountList2.size()) {
				endNum = accountList2.size();
			}
			accountList3.addAll(accountList2);
			if (accountList3 == null) {
				accountList3 = new ArrayList<Users>();
			}
			return accountList3;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	/**
	 * ���ݱ�Ų�ѯ�û���Ϣ
	 * @param bookTypeId ���ID
	 * @return �û���Ϣ
	 */
	public Users getUsers(Long usersId){
		if (usersId <= 0) {
			return null;
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Users userBean = null;
		Users userBean2 = null;
		try {
			userBean = pm
					.getObjectById(Users.class, usersId);
			userBean2 = userBean;
		} finally {
			pm.close();
		}
		return userBean2;
	}
	/**
	 * �����û���Ϣ
	 * @param bookType �û���Ϣ����
	 * @return  ���ӳɹ�����true�����򷵻�false
	 */
	public void addUsers(Users users){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Users> userLists = null;
			//pm.setDetachAllOnCommit(true);
			String queryTemplate = "", filter = "";
			queryTemplate = "email == \"%s\" ";

			filter = String.format(queryTemplate, users.getEmail());
			query = pm.newQuery(Users.class, filter);
			userLists = (List<Users>) query.execute();
			//�ж��û��Ƿ���ڣ�����Ѿ����ڣ���ô��¼������1
			if(userLists.size() >  0) {
				users = userLists.get(0);
				Long tempLong = users.getLoginNum();
				if(tempLong == null) tempLong = 0L;
				tempLong += 1L;
				users.setLoginNum(tempLong);
				//��������¼ʱ��
				users.setLastLoginTime(new Date());
			} 
			pm.makePersistent(users);
			
		} finally {
			pm.close();
		}
	}  
	/**
	 * �����û���Ϣ
	 * @param bookType �û���Ϣ����
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	public boolean updateUsers(Users users){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isSuccess = true;
		try {
			Users e = pm.getObjectById(Users.class, users.getId());
			e.setEmail(users.getEmail());
			e.setCreateDate(users.getCreateDate());
			e.setCreateDate(users.getCreateDate());
			e.setPassword(users.getPassword());
			//e.setUser(users.getUser());
			e.setUsername(users.getUsername());
			e.setUserType(users.getUserType());
		
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
	 *  
	 * @param bookTypeId id
	 * @return 
	 */
	public int getUsersNum(String email){
		//List<Pay> accountList2 = new ArrayList<Pay>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {

			List<Users> accountList = null;
			pm.setDetachAllOnCommit(true);
			String queryTemplate = "", filter = "";
			
			queryTemplate = "email == \"%s\" ";
			
			filter = String.format(queryTemplate, email);
			query = pm.newQuery(Users.class, filter);
			query.setOrdering("createDate desc");
			
			accountList = (List<Users>) pm.newQuery(query).execute();
			
			if (accountList != null)
				return accountList.size();
			else
				return 0;
		} finally {
			pm.close();
		}
	}
	/**
	 * ɾ���û���Ϣ
	 * @param bookTypeId �û���Ϣid
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	public boolean deleteUsers(Long usersId){
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Users users = pm
					.getObjectById(Users.class, usersId);
			pm.deletePersistent(users);
		} catch (RuntimeException e) {  
			isSuccess = false;
		} finally {
			pm.close();
		}
		return isSuccess;
	}
	

}
