package com.scholers.account.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.scholers.account.bean.Users;

import com.scholers.account.business.impl.UsersService;

public final class ComUtil {

	/**
	 * 
	 * @param current
	 * @return
	 */
	public static String getForDate(java.util.Date current) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		return sdf.format(current);
	}

	/**
	 * 
	 * @param current
	 * @return
	 */
	public static String getForTime(java.util.Date current) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sdf.format(current);
	}

	/**
	 * 
	 * @param current
	 * @return
	 */
	public static String getBigDecimal(float current) {
		current *= 100;
		BigDecimal rtnBig = new BigDecimal(String.valueOf(current));
		BigDecimal one = new BigDecimal("100.00");
		BigDecimal testD = rtnBig.divide(one, 2, BigDecimal.ROUND_HALF_UP);
		return String.valueOf(testD);

	}

	/**
	 * �ṩ��ȷ�ļӷ����㡣
	 * 
	 * @param v1
	 *            ������
	 * @param v2
	 *            ����
	 * @return ���������ĺ�
	 */
	public static Float add(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}

	/**
	 *  �ṩ��ȷ�ļ������㡣 ���� 
	 * @param v1 ������ ����
	 * @param v2 ���� ����
	 * @return ���������Ĳ��
	 */
	public static Float sub(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.subtract(b2).floatValue();
	}

	public static Date getStrToDate(String strDate) {
		Date date = null;
		// String strDate="2005��04��22��";
		// ע�⣺SimpleDateFormat���캯������ʽ��strDate����ʽ�������
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// ���벶���쳣
		try {
			date = simpleDateFormat.parse(strDate);
		} catch (ParseException px) {
			px.printStackTrace();
		}
		return date;
	}

	public static String getMonth() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("M");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	public static String getYear() {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy");
		String ctime = formatter.format(new Date());
		return ctime;
	}

	/**
	 * �õ��³�����
	 * 
	 * @return
	 */
	public static Date getCurYearAndDate() {
		return getStrToDate(getYear() + "-" + getMonth() + "-01");
	}

	/**
	 * �õ��������
	 * 
	 * @return
	 */
	public static Date getCurYear() {
		return getStrToDate(getYear() + "-01" + "-01");
	}

	public static void main(String[] arges) {
		float current = 3.3F;
		System.out.println(getBigDecimal(current));
	}

	/**
	 * ��GMAIL�û����ӽ��û���
	 */
	public static void addUser() {
		Users users = new Users();
		addUser(users);
	}

	/**
	 * ��GMAIL�û����ӽ��û���
	 * 
	 * @param users
	 */
	public static void addUser(Users users) {
		UsersService userService = new UsersService();
		Date createDate = new Date();
		users.setCreateDate(createDate);
		users.setLoginNum(1L);
		UserService userServiceTemp = UserServiceFactory.getUserService();
		User user = userServiceTemp.getCurrentUser();
		users.setUser(user);
		users.setEmail(user.getEmail());
		users.setUserType(0L);
		users.setLastLoginTime(createDate);
		// ������ǳƵ����û���
		users.setUsername(user.getNickname());
		// ͳһ��������
		users.setPassword("123456");
		userService.addUsers(users);
	}

}
