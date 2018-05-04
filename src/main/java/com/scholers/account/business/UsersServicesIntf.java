package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Users;

public interface UsersServicesIntf {
	//UsersDao dao = new UsersDao();
	 List<Users> getUsers(int startNum, int endNum);
		
	/**
	 * ���ݱ�Ų�ѯ�û���Ϣ
	 * @param bookTypeId ���ID
	 * @return �û���Ϣ
	 */
	 Users getUsers(Long usersId);
	/**
	 * �����û���Ϣ
	 * @param bookType �û���Ϣ����
	 * @return  ���ӳɹ�����true�����򷵻�false
	 */
	 void addUsers(Users users);
	/**
	 * �����û���Ϣ
	 * @param bookType �û���Ϣ����
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 boolean updateUsers(Users users);
	/**
	 *  
	 * @param bookTypeId id
	 * @return 
	 */
	 int getUsersNum(String email);
	/**
	 * ɾ���û���Ϣ
	 * @param bookTypeId �û���Ϣid
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	 boolean deleteUsers(List<Long> userIds) ;
	

}
