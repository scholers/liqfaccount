package com.scholers.account.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Component;



import com.scholers.account.bean.Users;
import com.scholers.account.business.UsersServicesIntf;
import com.scholers.account.dao.PMF;

/**
 * 用户信息查询
 * @author jill
 *
 */

@Component("usersService")
public class UsersService implements UsersServicesIntf{
	
	/**
	 * 
	 */
	public List<Users> getUsers(int startNum, int endNum){
		
		List<Users> accountList2 = new ArrayList<Users>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			List<Users> accountList = null;
			pm.setDetachAllOnCommit(true);
			
			
			//filter = String.format(queryTemplate, );
			String filter = "email == \"%s\" ";
			query = pm.newQuery(Users.class);
			
			query.setOrdering("createDate desc,id desc");
			accountList = (List<Users>) query.execute();
			
			accountList2.addAll(accountList);
			if (accountList2 == null || accountList2.size() == 0) {
				return new ArrayList<Users>();
			} else {
				//手工去掉无效的用户
				for(Users user : accountList2) {
					if(user.getEmail() == null || user.getEmail().equals("")
							|| user.getLastLoginTime() == null || user.getLastLoginTime().equals("")) {
						accountList2.remove(user);
					}
				}
			}
			
			
			return accountList2;

		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			query.closeAll();
			pm.close();
		}
		return accountList2;
		
	}
	/**
	 * 根据编号查询用户信息
	 * @param bookTypeId 编号ID
	 * @return 用户信息
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
	 * 增加用户信息
	 * @param bookType 用户信息对象
	 * @return  增加成功返回true，否则返回false
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
			//判断用户是否存在，如果已经存在，那么登录次数加1
			if(userLists.size() >  0) {
				users = userLists.get(0);
				Long tempLong = users.getLoginNum();
				if(tempLong == null) tempLong = 0L;
				tempLong += 1L;
				users.setLoginNum(tempLong);
				//更新最后登录时间
				users.setLastLoginTime(new Date());
			} 
			pm.makePersistent(users);
			
		} finally {
			pm.close();
		}
	}  
	/**
	 * 更新用户信息
	 * @param bookType 用户信息对象
	 * @return 增加成功返回true，否则返回false
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
	 * 删除用户信息
	 * @param  usersId用户信息id
	 * @return 删除成功返回true，否则返回false
	 */
	public boolean deleteUsers(List<Long> userIds) {
		boolean isSuccess = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			for (int i = 0; i < userIds.size(); i++) {
				Long userId = (Long) userIds.get(i);
				Users user = pm
				.getObjectById(Users.class, userId);
				pm.deletePersistent(user);
			}
			
		} catch (Exception e) {
			
			isSuccess = false;
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return isSuccess;
	}
	

}
