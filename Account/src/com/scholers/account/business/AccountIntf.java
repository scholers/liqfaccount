package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.CountBean;


/**
 * 
 * @author weique.lqf
 *
 */
public interface AccountIntf {

	/**
	 * ��ʱ���ѯ֧��
	 * @param startNum ��ʼ��¼��
	 * @param endNum ��ֹ��¼��
	 * @param email �û�email
	 * @param times ʱ��
	 * @return
	 */
	 List<CountBean> getPayByTimeAll(String email, String times, String endTime, boolean isPayType) ;
	
	
	/**
	 * ��ʱ���ѯ֧��--����֧������ͳ��
	 * @param startNum ��ʼ��¼��
	 * @param endNum ��ֹ��¼��
	 * @param email �û�email
	 * @param times ʱ��
	 * @return
	 */
	 List<CountBean> getInByTimeAll(String email, String times, String endTime, boolean isInType) ;
}
