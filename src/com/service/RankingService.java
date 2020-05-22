package com.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.dao.BookDAO;
import com.pojo.BookInfo.BookInfo;
import com.utils.DBUtil;

public class RankingService {
	/**
	 * 按照一定条件生成榜单
	 * 返回的是一个书籍列表
	 */
	
	public static void main(String []args) {
		System.out.println(getBookListByHeatOrLatest(12,"enterTime"));
	}
	
	public static List<BookInfo> getBookListByHeatOrLatest(int bookNum, String name) {
		List<BookInfo> bookList = new ArrayList<>();
		Connection conn = DBUtil.getConnection();
		BookDAO bookDAO = new BookDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(bookNum != 0) {
				bookList = bookDAO.selectBookListByHeatOrLatest(bookNum, name);
				if(bookList != null) {
					return bookList;
				}else {
					return null;
				}
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
