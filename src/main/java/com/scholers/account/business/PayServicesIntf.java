package com.scholers.account.business;

import java.util.List;

import com.scholers.account.bean.Pay;
import com.scholers.account.bean.PayType;

/**
 * 
 * @author weique.lqf
 *
 */
public interface PayServicesIntf {
	 List<PayType> getPayTypes(String email) ;

	/**
	 * 查询指定支付类型
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 支付类型对象
	 */
	 PayType getPayType(Long payTypeId)  ;
	/**
	 * 增加支付类型
	 * 
	 * @param bookType
	 *            支付类型对象
	 * @return 增加成功返回true，否则返回false
	 */
	 Long addPayType(PayType payType) ;
	/**
	 * 更新支付类型
	 * 
	 * @param bookType
	 *            支付类型对象
	 * @return 增加成功返回true，否则返回false
	 */
	 boolean updatePayType(PayType payType)  ;

	/**
	 * 查询某种类型支付的数量
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 支付数量
	 */
	 int getPayNum(Long payTypeId)  ;

	/**
	 * 删除支付类型
	 * 
	 * @param bookTypeId
	 *            支付类型id
	 * @return 删除成功返回true，否则返回false
	 */
	 boolean deletePayType(Long payTypeId)  ;


	/**
	 * 查询指定支付
	 * 
	 * @param bookId
	 *            支付id
	 * @return 支付对象
	 */
	 Pay getPay(Long payId)  ;

	/**
	 * 增加支付
	 * 
	 * @param book
	 *            支付对象
	 * @return 增加成功返回true，否则返回false
	 */
	 Long addPay(Pay accBean)  ;

	/**
	 * 更新支付信息
	 * 
	 * @param book
	 *            支付对象
	 * @return 更新支付成功返回true，否则返回false
	 */
	 boolean updatePay(Pay pay) ;

	/**
	 * 删除支付
	 * 
	 * @param bookId
	 *            支付id
	 * @return 删除支付成功返回true，否则返回false
	 */
	 boolean deletePay(Long payId)  ;

	/**
	 * 成批删除支付信息
	 * 
	 * @param bookIds
	 *            支付id列表
	 * @return 成批删除支付成功返回true，否则返回false
	 */
	 boolean deletePays(List<Long> payIds) ;
	
	/**
	 * 得到记录总数
	 * 
	 * @return
	 */
	 int qryAccCount(String email, String times, String times2)  ;

	/**
	 * 按时间查询支出
	 * @param startNum 起始记录数
	 * @param endNum 截止记录数
	 * @param email 用户email
	 * @param times 时间
	 * @return
	 */
	 List<Pay> getPayByTime(int startNum, int endNum, String email, String times, String times2)  ;
	
	/**
	 * 得到月度总支出
	 * @param email
	 * @return
	 */
	 Float getSumPay(String email) ;
	
	/**
	 * 本年度总支出
	 * @param email
	 * @return
	 */
	 Float getSumPayY(String email) ;
}

