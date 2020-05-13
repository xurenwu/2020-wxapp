package com.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	private static String driverClass;
	private static String url;
	private static String user;
	private static String psd;
	
	//静态块初始化，定义变量...
	static {
		Properties prop = new Properties();
		//InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("utils/config.properties");
		InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(is);
			
			driverClass = prop.getProperty("driverClass");
			url=prop.getProperty("url");
			
			user=prop.getProperty("user");
			psd=prop.getProperty("psd");
			Class.forName(driverClass);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//连接数据库
	public static Connection getConnection() {
		Connection conn=null;
		try {
			conn = DriverManager.getConnection(url,user,psd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	//断开数据库
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
