package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class ReadLogDAO {
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	public ReadLogDAO(Connection conn) {
		super();
		this.conn = conn;
	}
	
	public float getProgressByDate(int userId,int bookId, String date) {
		String sql = "select * from readlog where userId=? and bookId=? and readTime=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			pst.setString(3, date);
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				return rst.getFloat("progress");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 查询数据库中是否存在阅读记录
	 * 如果存在 就从数据库中返回最新的阅读记录
	 * @param userId
	 * @param bookId
	 * @return progress -1(没有这条记录)
	 */
	public float selectProgress(int userId, int bookId) {
		String sql = "select * from readlog where userId=? and bookId=?";
		try {
			float progress;
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			ResultSet rst = pst.executeQuery();
			Date datetime,currentDate;
			if(rst.next()) {
				datetime = rst.getTimestamp("readTime");
				currentDate = rst.getTimestamp("readTime");
				while(rst.next()) {
					currentDate = rst.getTimestamp("readTime");
					if(currentDate.compareTo(datetime) > 0) {
						datetime = currentDate;
					}
				}
				progress = getProgressByDate(userId,bookId,datetime.toString());
				return progress;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @param userId
	 * @param bookId
	 * @param progress
	 * @param date
	 * @return
	 */
	public boolean insertReadLog(int userId, int bookId, float progress, Date date) {
		Timestamp datetime = new Timestamp(date.getTime());
		System.out.println(datetime);
		String sql = "insert into readLog(userId,bookId,progress,readTime) value(?,?,?,?)";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			pst.setFloat(3, progress);
			pst.setTimestamp(4, datetime);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
