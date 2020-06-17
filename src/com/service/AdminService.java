package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.AdminDAO;
import com.pojo.admin.AdminInfo;
import com.utils.DBUtil;

public class AdminService {
	
	public static void main(String []args) {
//		System.out.println(select("123456","admin").getAdmin_desc());
//		System.out.println(getAdminInfo("13657039057")==null);
//		AdminInfo adminInfo = new AdminInfo();
//		adminInfo.setAdmin_name("admin");
//		adminInfo.setAdmin_email("ad@qq.com");
//		adminInfo.setAdmin_desc("4513");
//		adminInfo.setAdmin_nickname("hellokitty");
//		adminInfo.setAdmin_avatar("dnakng.jpg");
//		System.out.println(modify(adminInfo));
//		System.out.print(updatePsd("789456","admin"));
//		System.out.println(register("shaoxuewen","123456").getAdmin_password());
		System.out.println(getUserAndBookNum());
	}
	
	
	//登录查找用户信息
	public static AdminInfo select (String password,String admin_name) {
		AdminInfo adminInfo = new AdminInfo();
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		try {
			adminInfo = adminDao.select(password, admin_name);
			if(adminInfo != null) {
				return adminInfo;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//修改管理员用户信息
	public static boolean modify(AdminInfo adminInfo) {
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(adminDao.modify(adminInfo)) {
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn!=null)DBUtil.closeConnection(conn);
		}
		return false;
	}
	
	
	//更具用户名获取用户信息
	public static AdminInfo getAdminInfo(String admin_name) {
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		try{
			AdminInfo adminInfo = new AdminInfo();
			adminInfo = adminDao.selectAdmin(admin_name);
			if(adminInfo != null) {
				return adminInfo;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//修改密码
	public static boolean updatePsd(String psd,String name) {
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(adminDao.updataPsd(psd, name)) {
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn!=null)DBUtil.closeConnection(conn);
		}
		return false;
	}
	
	//管理员注册
	public static AdminInfo register(String admin_name,String password) {
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(adminDao.addAdminUser(admin_name,password)) {
				conn.commit();
				AdminInfo adminInfo = getAdminInfo(admin_name);
				if(adminInfo != null) {
					return adminInfo;
				}else{
					return null;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn!=null)DBUtil.closeConnection(conn);
		}
		return null;
	}
	
	//获取最近一周用户数量
	public static Map<String,List<Integer>> getUserAndBookNum(){
		Date date = new Date();
		Date dStart= new Date();
		Date dEnd =new Date();
		Connection conn = DBUtil.getConnection();
		AdminDAO adminDao = new AdminDAO(conn);
		List<Integer> userList = new ArrayList<>();
		List<Integer> bookList = new ArrayList<>();
		Map<String,List<Integer>> map = new HashMap<>();
		try {
			Calendar calendar = Calendar.getInstance(); //得到日历
			calendar.setTime(date);
			for(int i=1;i<=7;i++) {
				calendar.add(Calendar.DAY_OF_MONTH, -i);  //设置为前一天
				dStart = calendar.getTime();   //得到前一天的时间
				calendar.add(Calendar.DAY_OF_MONTH, -i+1);  //设置为前一天
				dEnd = calendar.getTime();   //得到前一天的时间
				userList.add(adminDao.getUserNum(dStart,dEnd));//每天的新增用户数加入到list集合中
				bookList.add(adminDao.getBookNum(dStart,dEnd));//每天的新增用户数加入到list集合中
			}
			//获取所有的用户数量
			userList.add(adminDao.getUserNum(date));
			bookList.add(adminDao.getBookNum(date));
			map.put("userList", userList);
			map.put("bookList", bookList);
			return map;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	

}
