package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.BookShelfDAO;
import com.pojo.bookshelf.BookList;
import com.pojo.bookshelf.Bookshelf;
import com.utils.DBUtil;

public class BookShelfService {

	/**
	 * 书架业务层 书架是用户对于书籍的和保存信息 用户可以将已经存在书架上的书籍移出书架删除书记库表bookshelf的数据
	 */

	public static void main(String args[]) {
		List<BookList> list = new ArrayList<>();
		BookList booklist = new BookList();
		booklist.setBookId(20201001);
		booklist.setUserId(20200001);
		list.add(booklist);
		System.out.println(deleteBookshelfByBookId(list));
		System.out.println(selectBookShelfByUserId(20200001));
	}

	/**
	 * 通过用户id返回用户书架所有书籍信息
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Bookshelf> selectBookShelfByUserId(int userId) {
		List<Bookshelf> booklist = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		BookShelfDAO bookshelfDAO = new BookShelfDAO(conn);
		booklist = bookshelfDAO.selectBookshelfByUserId(userId);
		if(conn != null) {
			DBUtil.closeConnection(conn);
		}
		if (booklist != null) {
			return booklist;
		}
		return null;
	}

	/**
	 * 用户移除书架书籍接口
	 * @param userId 用户id
	 * @param bookId 书籍id
	 * @return
	 */
	public static boolean deleteBookshelfByBookId(int userId, int bookId) {
		Connection conn = DBUtil.getConnection();
		BookShelfDAO bookshelfDAO = new BookShelfDAO(conn);
		if (bookshelfDAO.deleteBookshelf(userId, bookId))
			return true;
		else
			return false;
	}
	
	/**
	 * 批量移出书架
	 * @param List
	 * @return
	 */
	public static boolean deleteBookshelfByBookId(List<BookList> booklist) {
		Connection conn = DBUtil.getConnection();
		BookShelfDAO bookshelfDAO = new BookShelfDAO(conn);
		try {
			conn.setAutoCommit(false);   
			for (int i = 0; i < booklist.size(); i++) {
				if(bookshelfDAO.deleteBookshelf(booklist.get(i).getUserId(), booklist.get(i).getBookId()))continue;
				else return false;
			}
			conn.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}
	
	/**
	 * 没有progress传入的插入方法
	 * @param userId 用户id
	 * @param bookId
	 * @return
	 */
	public static boolean insertBookshelfByBookId(int userId, int bookId) {
		Connection conn = DBUtil.getConnection();
		BookShelfDAO bookshelfDAO = new BookShelfDAO(conn);
		if (bookshelfDAO.insertBookshelf(userId, bookId))
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param userId
	 * @param bookId
	 * @param progress 前端传入的用户阅读进度
	 * @return
	 */
	public static boolean insertBookshelfByBookId(int userId, int bookId, float progress) {
		Connection conn = DBUtil.getConnection();
		BookShelfDAO bookshelfDAO = new BookShelfDAO(conn);
		if (bookshelfDAO.insertBppkshelfByprogress(userId, bookId,progress))
			return true;
		else
			return false;
	}

}
