package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pojo.login.LoginSession;


public class LoginDAO {
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	// 定义构造函数，实例化时完成连接的注入
	public LoginDAO(Connection conn){
		super();
		this.conn = conn;
	}
	
	public LoginSession selectByOpenId(String openId){
		try {
			String sql = "select * from loginsession where openid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, openId);  // oepnId替换占位符“？”
			ResultSet rs = pst.executeQuery();	// 执行sql语句
			if(rs.next()){			// 查询成功
				LoginSession loginSession = new LoginSession();
				loginSession.setUserId(rs.getInt("userId"));
				loginSession.setOpenId(rs.getString("openId"));
				loginSession.setToken(rs.getString("token"));
				return loginSession;
			}else{			// 查询失败
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean update(LoginSession loginSession){		 
		try {
			String sql = "update loginsession set token=? where openid=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, loginSession.getToken());  
			pst.setString(2, loginSession.getOpenId()); 
			pst.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public LoginSession selectByToken(String token) throws SQLException {
		try {
			String sql = "select * from loginsession where token=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, token);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				LoginSession session = new LoginSession();
				session.setUserId(rs.getInt("userId"));
				session.setOpenId(rs.getString("openId"));
				session.setToken(rs.getString("token"));
				return session;
			}else{
				return null;
			}			
		} catch (Exception e) {
			return null;
		}
	}

	public boolean insert(LoginSession loginSession) throws SQLException {
		try {
			String sql = "insert into loginsession(userId,openId,token) values(?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, loginSession.getUserId());
			pst.setString(2, loginSession.getOpenId());
			pst.setString(3, loginSession.getToken());
			pst.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
