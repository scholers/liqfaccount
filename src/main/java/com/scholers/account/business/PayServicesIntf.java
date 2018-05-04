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
	 * ��ѯָ��֧������
	 * 
	 * @param bookTypeId
	 *            ֧������id
	 * @return ֧�����Ͷ���
	 */
	 PayType getPayType(Long payTypeId)  ;
	/**
	 * ����֧������
	 * 
	 * @param bookType
	 *            ֧�����Ͷ���
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 Long addPayType(PayType payType) ;
	/**
	 * ����֧������
	 * 
	 * @param bookType
	 *            ֧�����Ͷ���
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 boolean updatePayType(PayType payType)  ;

	/**
	 * ��ѯĳ������֧��������
	 * 
	 * @param bookTypeId
	 *            ֧������id
	 * @return ֧������
	 */
	 int getPayNum(Long payTypeId)  ;

	/**
	 * ɾ��֧������
	 * 
	 * @param bookTypeId
	 *            ֧������id
	 * @return ɾ���ɹ�����true�����򷵻�false
	 */
	 boolean deletePayType(Long payTypeId)  ;


	/**
	 * ��ѯָ��֧��
	 * 
	 * @param bookId
	 *            ֧��id
	 * @return ֧������
	 */
	 Pay getPay(Long payId)  ;

	/**
	 * ����֧��
	 * 
	 * @param book
	 *            ֧������
	 * @return ���ӳɹ�����true�����򷵻�false
	 */
	 Long addPay(Pay accBean)  ;

	/**
	 * ����֧����Ϣ
	 * 
	 * @param book
	 *            ֧������
	 * @return ����֧���ɹ�����true�����򷵻�false
	 */
	 boolean updatePay(Pay pay) ;

	/**
	 * ɾ��֧��
	 * 
	 * @param bookId
	 *            ֧��id
	 * @return ɾ��֧���ɹ�����true�����򷵻�false
	 */
	 boolean deletePay(Long payId)  ;

	/**
	 * ����ɾ��֧����Ϣ
	 * 
	 * @param bookIds
	 *            ֧��id�б�
	 * @return ����ɾ��֧���ɹ�����true�����򷵻�false
	 */
	 boolean deletePays(List<Long> payIds) ;
	
	/**
	 * �õ���¼����
	 * 
	 * @return
	 */
	 int qryAccCount(String email, String times, String times2)  ;

	/**
	 * ��ʱ���ѯ֧��
	 * @param startNum ��ʼ��¼��
	 * @param endNum ��ֹ��¼��
	 * @param email �û�email
	 * @param times ʱ��
	 * @return
	 */
	 List<Pay> getPayByTime(int startNum, int endNum, String email, String times, String times2)  ;
	
	/**
	 * �õ��¶���֧��
	 * @param email
	 * @return
	 */
	 Float getSumPay(String email) ;
	
	/**
	 * �������֧��
	 * @param email
	 * @return
	 */
	 Float getSumPayY(String email) ;
}

