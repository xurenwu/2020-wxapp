package com.utils;

import java.io.InputStream;
import java.util.Properties;


public class Demo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		//InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("utils/config.properties");
//		 String s1 = Demo.class.getClassLoader().getResource("utils/config.properties").getPath();
//		 System.out.println(s1);
		 InputStream is=Demo.class.getClassLoader().getResourceAsStream("config.properties");
		 prop.load(is);
		 
		 System.out.print(prop.getProperty("url"));
	}
}
