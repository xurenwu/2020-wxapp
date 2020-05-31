package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.UserDAO;
import com.pojo.login.User;
import com.utils.DBUtil;



public class UserService {
	
	public static void main(String args[]) {
		UserService userService = new UserService();
		System.out.print(userService.search("20200001").get(0).getUserId());
	}

	public User selectById(int userId) {
		Connection conn = DBUtil.getConnection();
		UserDAO userDAO = new UserDAO(conn);
		User user = null;
		try {
			user = userDAO.selectById(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return user;
	}
	
	public boolean deleteByUserId(int userId) {
		Connection conn = DBUtil.getConnection();
		UserDAO userDAO = new UserDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(userDAO.deleteByUserId(userId)) {
				conn.commit();
				return true;
			}
			else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 
		}finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}
	
	public List<User> getUserList(){
		Connection conn = DBUtil.getConnection();
		UserDAO userDAO = new UserDAO(conn);
		List<User> userList = new ArrayList<>();
		try {
			userList = userDAO.getUserList();
			if(userList != null)return userList;
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return null;
	}
	
	public List<User> search(String keyword){
		Connection conn = DBUtil.getConnection();
		UserDAO userDAO = new UserDAO(conn);
		List<User> userList = new ArrayList<>();
		try {
			userList = userDAO.search(keyword);
			if(userList != null) {
				return userList;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return null;
	}
}
