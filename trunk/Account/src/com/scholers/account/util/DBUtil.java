package com.scholers.account.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	static String jdbcURL = "jdbc:oracle:thin:@192.168.1.109:1521:orcl";
	static String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
	static String userName = "test";
	static String password = "test";
	
	private static final String DB_URL = "jdbc:mysql://localhost/liqf?useUnicode=true&characterEncoding=GBK&useServerPrepStmts=false";

	/**
	 * 获取数据库连接对象
	 * @return 数据库连接对象  
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	/*public static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(jdbcDriver);
		return DriverManager.getConnection(jdbcURL, userName, password);
	}*/
	
	
	public static Connection getConnection() throws  ClassNotFoundException,
	SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = java.sql.DriverManager.getConnection(DB_URL, "liqf",
				"790521");
		return con;
	}
}
