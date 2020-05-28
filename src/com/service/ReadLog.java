package com.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dao.ReadLogDAO;
import com.pojo.readLog.ReadLogBook;
import com.utils.DBUtil;

public class ReadLog {
	
	/**
	 * 阅读记录业务层
	 */
	public static void main(String args[]) {
//		System.out.print(getReadlog(20200001).get(0).getBookId());
		System.out.println(setUserReadLog(20200003, 20201000, (float)0.6));
	}
	
	/**
	 * 1.先查看数据库中有没有该本书的阅读记录，阅读记录主要针对的是不在书架上的书籍
	 * 2.如果数据库中存在这个数据则将最新的数据信息返回给前台
	 * 3.如果没有这个数据的话返回0
	 */
	
	public static List<ReadLogBook> getReadlog(int userId) {
		try {
			Connection conn = DBUtil.getConnection();
			ReadLogDAO readDAO = new ReadLogDAO(conn);
			List<ReadLogBook> list = readDAO.getReadLogBookLsit(userId);
			if(list != null) return list;
			else return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 插入阅读记录到数据库中
	 * @param userId
	 * @param bookId
	 * @param progress
	 * @return
	 */
	public static boolean setUserReadLog(int userId, int bookId, float progress) {
		Date date=new Date();
		Timestamp t = new Timestamp(date.getTime());
		if(insertReadLog(userId, bookId, progress, t))return true;
		else return false;
	}
	
	/**
	 * 查询用户的阅读进度,用户每次开始阅读这本书的时候都应该访问一下这本书的阅读进度
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public static float selectProgress(int userId, int bookId) {
		Connection conn = DBUtil.getConnection();
		ReadLogDAO readDAO = new ReadLogDAO(conn);
		float progress = readDAO.selectProgress(userId, bookId);
		return progress;
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
		if(readDAO.insertReadLog(userId, bookId, progress, date)) return true;
		else return false;
	}
}
