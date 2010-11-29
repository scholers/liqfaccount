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
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static Float add(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}

	/**
	 *  提供精确的减法运算。 　　 
	 * @param v1 被减数 　　
	 * @param v2 减数 　　
	 * @return 两个参数的差　　
	 */
	public static Float sub(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.subtract(b2).floatValue();
	}

	public static Date getStrToDate(String strDate) {
		Date date = null;
		// String strDate="2005年04月22日";
		// 注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 必须捕获异常
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
	 * 得到月初日期
	 * 
	 * @return
	 */
	public static Date getCurYearAndDate() {
		return getStrToDate(getYear() + "-" + getMonth() + "-01");
	}

	/**
	 * 得到年初日期
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
	 * 将GMAIL用户增加进用户表
	 */
	public static void addUser() {
		Users users = new Users();
		addUser(users);
	}

	/**
	 * 将GMAIL用户增加进用户表
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
		// 这里把昵称当做用户名
		users.setUsername(user.getNickname());
		// 统一设置密码
		users.setPassword("123456");
		userService.addUsers(users);
	}

}
