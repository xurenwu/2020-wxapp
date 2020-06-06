package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println(sql);
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
	
	/**
	 * 根据category来返回一定数量的书籍信息列表
	 */
	
	public List<BookInfo> selectBookListByCategory(String category,int bookNum){
		List<BookInfo> booklist = new ArrayList<>();
		String sql = "select * from bookInfo where category=? order by heat desc";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, category);
			ResultSet rst = pst.executeQuery();
			int num = 0;
			while(rst.next() && num < bookNum) {
				BookInfo book = new BookInfo();
				book.setBookId(rst.getInt("bookId"));
				book.setBookName(rst.getString("bookName"));
				book.setAuthor(rst.getString("author"));
				book.setBookImage(rst.getString("bookImage"));
				book.setCategory(rst.getString("category"));
				book.setDesc(rst.getString("desc"));
				book.setEnterTime(rst.getString("enterTime"));
				book.setHeat(rst.getInt("heat"));
				book.setLastChapter(rst.getString("lastChapter"));
				book.setLastChapterUrl(rst.getString("lastChapterUrl"));
				book.setState(rst.getString("state"));
				booklist.add(book);
				num++;
			}
			return booklist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//ranking
	public List<BookInfo> selectBookListByHeatOrLatest(int bookNum,String name) {
		String sql = "select * from bookInfo order by "+name+" desc";
		List<BookInfo> booklist = new ArrayList<>();
		try {
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			int num = 0;
			while(rst.next() && num < bookNum) {
				BookInfo book = new BookInfo();
				book.setBookId(rst.getInt("bookId"));
				book.setBookName(rst.getString("bookName"));
				book.setAuthor(rst.getString("author"));
				book.setBookImage(rst.getString("bookImage"));
				book.setCategory(rst.getString("category"));
				book.setDesc(rst.getString("desc"));
				book.setEnterTime(rst.getString("enterTime"));
				book.setHeat(rst.getInt("heat"));
				book.setLastChapter(rst.getString("lastChapter"));
				book.setLastChapterUrl(rst.getString("lastChapterUrl"));
				book.setState(rst.getString("state"));
				booklist.add(book);
				num++;
			}
			return booklist;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public List<BookInfo> getBookList() {
		try {
			List<BookInfo> booklist = new ArrayList<>();
			String sql = "select * from bookInfo";
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				BookInfo book = new BookInfo();
				book.setBookId(rst.getInt("bookId"));
				book.setBookName(rst.getString("bookName"));
				book.setAuthor(rst.getString("author"));
				book.setBookImage(rst.getString("bookImage"));
				book.setCategory(rst.getString("category"));
				book.setDesc(rst.getString("desc"));
				book.setEnterTime(rst.getString("enterTime"));
				book.setHeat(rst.getInt("heat"));
				book.setLastChapter(rst.getString("lastChapter"));
				book.setLastChapterUrl(rst.getString("lastChapterUrl"));
				book.setState(rst.getString("state"));
				booklist.add(book);
			}
			if(booklist != null) {
				return booklist;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateBookInfo(BookInfo bookinfo) {
		String sql = "update bookinfo set bookName=?,author=?,bookInfo.desc=?,category=?,lastChapter=?,lastChapterUrl=?,bookImage=?,state=? where bookId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, bookinfo.getBookName());
			pst.setString(2, bookinfo.getAuthor());
			pst.setString(3, bookinfo.getDesc());
			pst.setString(4, bookinfo.getCategory());
			pst.setString(5, bookinfo.getLastChapter());
			pst.setString(6, bookinfo.getLastChapterUrl());
			pst.setString(7, bookinfo.getBookImage());
			pst.setString(8, bookinfo.getState());
			pst.setInt(9, bookinfo.getBookId());
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteBook(int bookId) {
		String sql = "select bookId from bookinfo where bookId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, bookId);
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				String sql1 = "delete from bookinfo where bookId="+bookId;
				pst = conn.prepareStatement(sql1);
				pst.executeUpdate();
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
