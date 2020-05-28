package com.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.dao.LoginDAO;
import com.dao.UserDAO;
import com.pojo.baseData.BaseDataPojo;
import com.pojo.login.LoginSession;
import com.pojo.login.User;
import com.utils.DBUtil;


public class LoginService {
	
	public static void main(String[] args) {
		// 登录测试
//		LoginService loginService = new LoginService();
//		BaseDataPojo<LoginSession> baseDataPojo = loginService.login("oKfeH5MGcgXL1jjtO71seR4jc_EA", "token_1586333625324");
//		System.out.println(new Gson().toJson(baseDataPojo));
		
		// 校验token是否有效测试
		LoginService loginService = new LoginService();
		int id = loginService.checkToken("token_1586333625324");
		System.out.println(id);		
	}
//	public static void main(String[] args) {
//		LoginService loginService = new LoginService();
//		User user = new User();
//		user.setName("新用户");
//		loginService.addUser(user);
//	}

	// 处理登录业务
	public BaseDataPojo<LoginSession> login(String openId, String token) {

		// 将openId和token插入数据库表，先查询数据库表中是否已存在此openId
		LoginSession loginSession = selectByOpenId(openId);
		if (loginSession != null) { // 数据库中已存在此openId，这时只需要更新token
			loginSession.setToken(token); // 更新token
			if (update(loginSession)) { // 更新token成功，将新的token返回到前端缓存
				return new BaseDataPojo<LoginSession>("登录成功", true, loginSession);
			} else { // 更新失败，返回错误提示信息
				return new BaseDataPojo<LoginSession>("登录失败，更新token时错误", false, null);
			}
		} else { // 不存在此openId，则先添加一个新用户，然后将新用户的id与用户登录表关联
			User user = new User();
			user.setName("新用户"); // 初始设置一个随机姓名，后期可改名
			int userId = addUser(user); // 添加新用户
			if (userId != -1) { // 将userId和openId、token一起插入到数据库表中
				LoginSession loginSession1 = new LoginSession();
				loginSession1.setOpenId(openId);
				loginSession1.setToken(token);
				loginSession1.setUserId(userId);
				if (insert(loginSession1)) { // 新用户添加成功后，插入一条登录信息，关联openId、token及userId
					return new BaseDataPojo<LoginSession>("登录成功", true, loginSession1);
				} else {
					return new BaseDataPojo<LoginSession>("登录失败，添加openId及token时错误", false, null);
				}
			} else {
				return new BaseDataPojo<LoginSession>("登录失败，添加新用户时错误", false, null);
			}
		}
	}

	// 查验token是否有效
	public int checkToken(String token) {
		LoginSession loginSession = selectByToken(token);
		if (loginSession != null) { // 数据库查到此token，表示token有效并返回userId
			return loginSession.getUserId();
		} else { // 数据库中没查到此token，表示token过期或者无此记录
			return -1;
		}
	}

	public LoginSession selectByToken(String token) {
		Connection conn = DBUtil.getConnection();
		LoginDAO loginDAO = new LoginDAO(conn);
		LoginSession session = null;
		try {
			session = loginDAO.selectByToken(token);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return session;
	}

	public boolean insert(LoginSession loginSession) {
		Connection conn = DBUtil.getConnection();
		LoginDAO loginDAO = new LoginDAO(conn);
		try {
			conn.setAutoCommit(false);
			loginDAO.insert(loginSession);
			conn.commit();			//只有在之前的业务没有出错才让commit提交才修改数据库的数据
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} 		//数据回滚，在数据库没有进行commit之前rollback可以恢复修改之前之前的数据
			return false;
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
	}

	public int addUser(User user) {
		Connection conn = DBUtil.getConnection();
		UserDAO userDAO = new UserDAO(conn);
		try {
			int id = userDAO.insert(user);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
	}

	// 根据openId查询
	public LoginSession selectByOpenId(String openId) {
		Connection conn = DBUtil.getConnection();
		LoginDAO loginDAO = new LoginDAO(conn);
		LoginSession loginSession = null;
		try {
			loginSession = loginDAO.selectByOpenId(openId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return loginSession;
	}

	// 更新数据库表loginsession
	public boolean update(LoginSession loginSession) {
		Connection conn = DBUtil.getConnection();
		LoginDAO loginDAO = new LoginDAO(conn);
		try {
			loginDAO.update(loginSession);
			conn.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}

	}
}
