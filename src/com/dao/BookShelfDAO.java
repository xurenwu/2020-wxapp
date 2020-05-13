package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pojo.bookshelf.Bookshelf;

public class BookShelfDAO {
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	public BookShelfDAO(Connection conn) {
		super();
		this.conn = conn;
	}
	
	
	/**
	 * 根据用户的id来获取用户的书架书籍信息
	 * @param userID 用户id
	 * @return List<Bookshelf> 书架的书籍基本信息的集合
	 */
	public List<Bookshelf> selectBookshelfByUserId(int userId) {
		String sql = "select * from bookshelf_info where userId=?";
		List<Bookshelf> booklist = new ArrayList<>();
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				Bookshelf bookshelf = new Bookshelf();
				bookshelf.setUserId(rst.getInt("userId"));
				bookshelf.setBookId(rst.getInt("bookId"));
				bookshelf.setProgress(rst.getFloat("lastProgress"));
				bookshelf.setBookName(rst.getString("bookName"));
				bookshelf.setBookImage(rst.getString("bookImage"));
				booklist.add(bookshelf);
			}
			return booklist;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 用户删除用户书架数据
	 * @param userId 用户id
	 * @param bookId 书籍id
	 * @return true or false
	 */
	
	public boolean deleteBookshelf(int userId,int bookId) {
		String sql = "delete from bookshelf where userId=? and bookId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 没有传阅读进度的方法
	 * @param userId
	 * @param bookId
	 * @return
	 */
	public boolean insertBookshelf(int userId,int bookId) {
		ReadLogDAO readlog = new ReadLogDAO(conn);
		if (readlog.selectProgress(userId, bookId) == (float)-1) {
			try {
				String sql = "insert into bookshelf where userId=? and bookId=?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, userId);
				pst.setInt(2, bookId);
				pst.executeUpdate();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
				String sql = "insert into bookshelf where userId=? and bookId=? and lastProgress=?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, userId);
				pst.setInt(2, bookId);
				pst.setFloat(3, readlog.selectProgress(userId, bookId));
				pst.executeUpdate();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 有传入阅读进度的插入方法
	 */
	
	public boolean insertBppkshelfByprogress(int userId, int bookId, float progress) {
		try {
			String sql = "insert into bookshelf where userId=? and bookId=? and lastProgress=?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			pst.setFloat(3,progress);
			pst.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
