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
	 * 按时间查询支出
	 * @param startNum 起始记录数
	 * @param endNum 截止记录数
	 * @param email 用户email
	 * @param times 时间
	 * @return
	 */
	 List<CountBean> getPayByTimeAll(String email, String times, String endTime, boolean isPayType) ;
	
	
	/**
	 * 按时间查询支出--根据支出类型统计
	 * @param startNum 起始记录数
	 * @param endNum 截止记录数
	 * @param email 用户email
	 * @param times 时间
	 * @return
	 */
	 List<CountBean> getInByTimeAll(String email, String times, String endTime, boolean isInType) ;
}
