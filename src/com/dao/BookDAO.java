package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.pojo.BookInfo.BookInfo;
import com.pojo.Catelogue.ChapterObject;

public class BookDAO {
	private Connection conn = null;
	private PreparedStatement pst = null;

	public BookDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	/**
	 * 
	 * @param bookId
	 * @return
	 */
	public BookInfo selectById(int bookId) {
		try {
			String sql = "select * from bookInfo where bookId=?";
			pst = conn.prepareStatement(sql);
			BookInfo bookInfo = new BookInfo();
			pst.setInt(1, bookId);
			ResultSet rst = pst.executeQuery();
			if (rst.next()) {
				bookInfo.setBookId(rst.getInt("bookId"));
				bookInfo.setAuthor(rst.getString("author"));
				bookInfo.setBookName(rst.getString("bookName"));
				bookInfo.setCategory(rst.getString("category"));
				bookInfo.setBookImage(rst.getString("bookImage"));
				bookInfo.setDesc(rst.getString("desc"));
				bookInfo.setLastChapter(rst.getString("lastChapter"));
				bookInfo.setLastChapterUrl(rst.getString("lastChapterUrl"));
				bookInfo.setHeat(rst.getInt("heat"));
				bookInfo.setState(rst.getString("state"));
				bookInfo.setEnterTime(rst.getString("enterTime"));
				return bookInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询书籍某一章节在数据库中是否已经被插入了
	 * 
	 * @param bookId      书籍id
	 * @param chapterName 章节名
	 * @return 章节id
	 */

	public int findChapter(int bookId, String chapterName) {
		String sql = "select * from chapter where bookId=? and chapterName=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, bookId);
			pst.setString(2, chapterName);
			ResultSet rst = pst.executeQuery();
			if (rst.next()) {
				return rst.getInt("chapterId");
			} else
				return -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 将书籍的所有的章节目录都插入到数据库中
	 * 
	 * @param chapterObj 章节对象
	 * @param bookId     书籍id
	 * @return
	 */
	public boolean insertChapter(ChapterObject chapterObj, int bookId) {
		String sql = "";
		try {
			int chapterId = findChapter(bookId, chapterObj.getChapterName());
			if (chapterId == -1) {
				sql = "insert into chapter (bookId,chapterName,chapterUrl) value(?,?,?)";
			} else {
				sql = "update chapter set bookId=?,chapterName=?,chapterUrl=? where chapterId=" + chapterId;
			}
			pst = conn.prepareStatement(sql);
			pst.setInt(1, bookId);
			pst.setString(2, chapterObj.getChapterName());
			pst.setString(3, chapterObj.getChapterUrl());
			pst.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
