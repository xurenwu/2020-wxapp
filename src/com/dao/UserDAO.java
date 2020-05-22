package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			String sql = "insert into user(userName) values(?)";
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	// 返回数据表的主key
			pst.setString(1, user.getName());
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
}
