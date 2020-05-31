package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pojo.login.User;


public class UserDAO {
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	// 定义构造函数，实例化时完成连接的注入
	public UserDAO(Connection conn){
		super();
		this.conn = conn;
	}
	
	public User selectById(int userId) throws SQLException {
		String sql = "select * from user where userId=?";
		pst = conn.prepareStatement(sql);
		pst.setInt(1, userId);
		ResultSet rst = pst.executeQuery();
		if (rst.next()) {
			User user = new User();
			user.setUserId(rst.getInt("userId"));
			user.setName(rst.getString("name"));
			user.setGender(rst.getString("gender"));
			return user;
		} else {
			return null;
		}
	}
	
	public boolean update(User user) {
		try {
			String sql = "update user set name=?,gender=? where id=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getName());
			pst.setString(2, user.getGender());
			pst.setInt(3, user.getUserId());
			pst.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public int insert(User user){
		try {
			Date date=new Date();
			Timestamp t = new Timestamp(date.getTime());
			String sql = "insert into user(userName,registerTime) values(?,?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	// 返回数据表的主key
			pst.setString(1, user.getName());
			pst.setTimestamp(2, t);						//注册时间
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();		// 获取主key
			if(rs.next()){
				return rs.getInt(1);	// 返回主key
			}else{
				return -1;		// 插入失败返回-1
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public List<User> getUserList() {
		try {
			List<User> userList = new ArrayList<>();
			String sql = "select * from user";
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				User user = new User();
				user.setUserId(rst.getInt("userId"));
				user.setName(rst.getString("userName"));
				user.setGender(rst.getString("gender"));
				userList.add(user);
			}
			if(userList != null) {
				return userList;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteByUserId(int userId) {
		String sql = "delete from user where userId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<User> search(String keyword){
		String sql = "select * from user where userId='"+keyword+"'or userName='"+keyword+"'";
		try {
			List<User> userList = new ArrayList<>();
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				User user = new User();
				user.setUserId(rst.getInt("userId"));
				user.setName(rst.getString("userName"));
				user.setGender(rst.getString("gender"));
				userList.add(user);
			}
			return userList;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
