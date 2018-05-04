package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Users;

public interface UsersServicesIntf {
	//UsersDao dao = new UsersDao();
	 List<Users> getUsers(int startNum, int endNum);
		
	/**
	 * 根据编号查询用户信息
	 * @param bookTypeId 编号ID
	 * @return 用户信息
	 */
	 Users getUsers(Long usersId);
	/**
	 * 增加用户信息
	 * @param bookType 用户信息对象
	 * @return  增加成功返回true，否则返回false
	 */
	 void addUsers(Users users);
	/**
	 * 更新用户信息
	 * @param bookType 用户信息对象
	 * @return 增加成功返回true，否则返回false
	 */
	 boolean updateUsers(Users users);
	/**
	 *  
	 * @param bookTypeId id
	 * @return 
	 */
	 int getUsersNum(String email);
	/**
	 * 删除用户信息
	 * @param bookTypeId 用户信息id
	 * @return 删除成功返回true，否则返回false
	 */
	 boolean deleteUsers(List<Long> userIds) ;
	

}
