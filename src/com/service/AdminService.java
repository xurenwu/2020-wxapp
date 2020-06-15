package com.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.dao.AdminDAO;
import com.pojo.admin.AdminInfo;
import com.utils.DBUtil;

public class AdminService {
	
	public static void main(String []args) {
//		System.out.println(select("123456","admin").getAdmin_desc());
		System.out.println(getAdminInfo("13657039057")==null);
//		AdminInfo adminInfo = new AdminInfo();
//		adminInfo.setAdmin_name("admin");
//		adminInfo.setAdmin_email("ad@qq.com");
//		adminInfo.setAdmin_desc("4513");
//		adminInfo.setAdmin_nickname("hellokitty");
//		adminInfo.setAdmin_avatar("dnakng.jpg");
//		System.out.println(modify(adminInfo));
//		System.out.print(updatePsd("789456","admin"));
		System.out.println(register("shaoxuewen","123456").getAdmin_password());
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

}
