package com.service;

import java.sql.Connection;

import com.dao.BookDAO;
import com.pojo.BookInfo.BookInfo;
import com.utils.DBUtil;

public class BookInfoService {
	
	/**
	 * 根据小说id来获取小说信息
	 * @param bookId
	 * @return BookInfo
	 */
	public static BookInfo getBookInfo(int bookId) {
		BookInfo bookInfo = selectByBookId(bookId);
		return bookInfo;
	}
	
	/**
	 * 
	 * @param bookId
	 * @return BookInfo
	 */
//	public static BookInfo getBookInfoById(int bookId) {
//		BookInfo bookInfo = new BookInfo();
//		bookInfo = selectByBookId(bookId);
//		if(bookInfo != null) {
//			return bookInfo;
//		}
//		return null;
//	}

	/**
	 * 
	 * @param bookId
	 * @return BookInfo
	 */
	private static BookInfo selectByBookId(int bookId) {
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		BookInfo bookInfo = new BookInfo();
		bookInfo = bookDAO.selectById(bookId);
		if (bookInfo != null) {
			if(conn!=null)DBUtil.closeConnection(conn);
			return bookInfo;
		}
		return null;
	}

}
