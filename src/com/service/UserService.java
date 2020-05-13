package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dao.UserDAO;
import com.pojo.baseData.BaseDataPojo;
import com.pojo.login.User;
import com.utils.DBUtil;



public class UserService {

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
	
	//后期更新用户信息，从前端返回用户的昵称和性别
	public boolean updataById(int userId) {
		return false;
	}
}
