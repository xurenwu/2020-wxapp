package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.pojo.admin.AdminInfo;

public class AdminDAO {

	private Connection conn = null;
	private PreparedStatement pst = null;

	public AdminDAO(Connection conn) {
		super();
		this.conn = conn;
	}
	
	public boolean modify(AdminInfo adminInfo) {
		
		String sql = "update admin set admin_nickname=?,admin_desc=?,admin_email=?,admin_avatar=? where admin_name=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, adminInfo.getAdmin_nickname());
			pst.setString(2, adminInfo.getAdmin_desc());
			pst.setString(3, adminInfo.getAdmin_email());
			pst.setString(4, adminInfo.getAdmin_avatar());
			pst.setString(5, adminInfo.getAdmin_name());
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public AdminInfo select(String password,String admin_name) {
		String sql = "select * from admin where admin_name=? and password=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, admin_name);
			pst.setString(2, password);
			
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				AdminInfo adminInfo = new AdminInfo();
				adminInfo.setAdmin_name(rst.getString("admin_name"));
				adminInfo.setAdmin_password(rst.getString("password"));
				adminInfo.setAdmin_email(rst.getString("admin_email"));
				adminInfo.setAdmin_desc(rst.getString("admin_desc"));
				adminInfo.setAdmin_avatar(rst.getString("admin_avatar"));
				adminInfo.setAdmin_nickname(rst.getString("admin_nickname"));
				return adminInfo;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public AdminInfo selectAdmin(String admin_name) {
		String sql = "select * from admin where admin_name=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, admin_name);
			
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				AdminInfo adminInfo = new AdminInfo();
				adminInfo.setAdmin_name(rst.getString("admin_name"));
				adminInfo.setAdmin_password(rst.getString("password"));
				adminInfo.setAdmin_email(rst.getString("admin_email"));
				adminInfo.setAdmin_desc(rst.getString("admin_desc"));
				adminInfo.setAdmin_avatar(rst.getString("admin_avatar"));
				adminInfo.setAdmin_nickname(rst.getString("admin_nickname"));
				return adminInfo;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//修改用户密码
	public boolean updataPsd(String psd,String name) {
		String sql = "update admin set password=? where admin_name=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, psd);
			pst.setString(2,name);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addAdminUser(String admin_name, String password) {
		String sql = "insert into admin (admin_name,password) values(?,?)";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, admin_name);
			pst.setString(2,password);
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getUserNum(Date dStart, Date dEnd) {
		Timestamp tStart = new Timestamp(dStart.getTime());
		Timestamp tEnd = new Timestamp(dEnd.getTime());
		String sql = "select * from user where registerTime>? and registerTime<?";
		try{
			pst = conn.prepareStatement(sql);
			pst.setTimestamp(1, tEnd);
			pst.setTimestamp(2, tStart);
			ResultSet rst = pst.executeQuery();
			rst.last();
			return rst.getRow();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getUserNum(Date dStart) {
		Timestamp tStart = new Timestamp(dStart.getTime());
		String sql = "select * from user where registerTime<?";
		try{
			pst = conn.prepareStatement(sql);
			pst.setTimestamp(1, tStart);
			ResultSet rst = pst.executeQuery();
			rst.last();
			return rst.getRow();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getBookNum(Date dStart, Date dEnd) {
		Timestamp tStart = new Timestamp(dStart.getTime());
		Timestamp tEnd = new Timestamp(dEnd.getTime());
		String sql = "select * from bookinfo where enterTime>? and enterTime<?";
		try{
			pst = conn.prepareStatement(sql);
			pst.setTimestamp(1, tEnd);
			pst.setTimestamp(2, tStart);
			ResultSet rst = pst.executeQuery();
			rst.last();
			return rst.getRow();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getBookNum(Date dStart) {
		Timestamp tStart = new Timestamp(dStart.getTime());
		String sql = "select * from bookinfo where enterTime<?";
		try{
			pst = conn.prepareStatement(sql);
			pst.setTimestamp(1, tStart);
			ResultSet rst = pst.executeQuery();
			rst.last();
			return rst.getRow();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
