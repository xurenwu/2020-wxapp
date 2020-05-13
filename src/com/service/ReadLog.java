package com.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;

import com.dao.ReadLogDAO;
import com.utils.DBUtil;

public class ReadLog {
	
	/**
	 * 阅读记录业务层
	 */
//	public static void main(String args[]) {
//		System.out.print(getUserReadLog(20200001,20201000,(float)0.5));
//	}
	
	/**
	 * 1.先查看数据库中有没有该本书的阅读记录，阅读记录主要针对的是不在书架上的书籍
	 * 2.如果数据库中存在这个数据则将最新的数据信息返回给前台
	 * 3.如果没有这个数据的话返回0
	 * @param userId 用户id
	 * @param bookId 书籍id
	 */
	
	public static float getUserReadLog(int userId, int bookId, float progress) {
		Date date=new Date();
		Timestamp t = new Timestamp(date.getTime());
		insertReadLog(userId, bookId, progress, t);
		return selectProgress(userId, bookId);
	}
	
	/**
	 * 查询用户的阅读进度
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public static float selectProgress(int userId, int bookId) {
		Connection conn = DBUtil.getConnection();
		ReadLogDAO readDAO = new ReadLogDAO(conn);
		return readDAO.selectProgress(userId, bookId);
	}
	
	/**
	 * 用户每次离开阅读界面的时候会触发的函数向服务器发送请求将阅读的记录保存在数据库中
	 * @param userId
	 * @param bookId
	 * @param progress 阅读进度
	 * @return
	 */
	public static boolean insertReadLog(int userId, int bookId, float progress, Date date) {
		Connection conn = DBUtil.getConnection();
		ReadLogDAO readDAO = new ReadLogDAO(conn);
		readDAO.insertReadLog(userId, bookId, progress, date);
		return false;
	}
}
