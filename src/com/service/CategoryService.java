package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.BookDAO;
import com.pojo.BookInfo.BookInfo;
import com.utils.DBUtil;

public class CategoryService {
	
	/**
	 * 根据书籍分类返回一定数目的书籍信息
	 */
	
	public static void main(String []args) {
		System.out.println(getBookInfoByCategory("外国小说",10).get(0).getAuthor());
	}
	
	/**
	 * 根据图书分类来返回一定数量的书籍
	 * @param category
	 * @return
	 */
	public static List<BookInfo> getBookInfoByCategory(String category,int bookNum) {
		List<BookInfo> bookList = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		try {
			conn.setAutoCommit(false);
			bookList = bookDAO.selectBookListByCategory(category,bookNum);
			if(bookList != null) {
				conn.commit();
				return bookList;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null)
			DBUtil.closeConnection(conn);
		}
		return null;
	}

}
