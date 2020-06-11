package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.BookDAO;
import com.pojo.BookInfo.BookInfo;
import com.utils.DBUtil;

public class BookInfoService {
	
	public static void main(String args[]) {
//		System.out.println(getBookList().get(1).getBookId());
//		System.out.println(updateBookInfo(selectByBookId(20201001)));
		List<Integer> list = new ArrayList<>();
		list.add(20201004);
		System.out.print(deleteBook(list));
	} 
	
	/**
	 * 根据小说id来获取小说信息
	 * @param bookId
	 * @return BookInfo
	 */
	public static BookInfo getBookInfo(int bookId) {
		BookInfo bookInfo = selectByBookId(bookId);
		if(bookInfo != null)
		return bookInfo;
		else return null;
	}
	

	/**
	 * 获取全部小说列表
	 * @return
	 */
	public static List<BookInfo> getBookList() {
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		List<BookInfo> bookList = new ArrayList<>();
		try {
			bookList = bookDAO.getBookList();
			if(bookList != null) {
				return bookList;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(conn!=null)DBUtil.closeConnection(conn);
		}
		return null;
	}

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
	
	/**
	 * 修改书籍信息
	 * @param bookinfo
	 * @return true or false
	 */
	public static boolean updateBookInfo(BookInfo bookinfo) {
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(bookinfo != null) {
				if(bookDAO.updateBookInfo(bookinfo)) {
					conn.commit();
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}

	//删除书籍信息
	public static boolean deleteBook(List<Integer> bookIdlist) {
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		try {
			conn.setAutoCommit(false);
			int i= 0;
			if(bookIdlist != null) {
				for(;i<bookIdlist.size();i++) {
					if(bookIdlist.get(i) != null) {
						if(bookDAO.deleteBook(bookIdlist.get(i))) {
							continue;
						}else {
							return false;
						}
					}else return false;
				}
				if(i == bookIdlist.size()) {
					conn.commit();
					return true;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}
	
	//添加书籍信息
	public static boolean addBookInfo(BookInfo bookInfo) {
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(bookDAO.addBookInfo(bookInfo)) {
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}
	
}
