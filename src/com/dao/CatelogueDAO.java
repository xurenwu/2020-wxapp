package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pojo.Catelogue.CatelogueUrl;
import com.pojo.Catelogue.Chapter;
import com.pojo.Catelogue.ChapterObject;

public class CatelogueDAO {
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	// 定义构造函数，实例化时完成连接的注入
	public CatelogueDAO(Connection conn){
		super();
		this.conn = conn;
	}

	
	public CatelogueUrl checkBookId(int bookId) {
		try {
			String sql = "select * from Catelogue where bookId=?";
			pst = conn.prepareStatement(sql);
			CatelogueUrl catelogueUrl = new CatelogueUrl();
			pst.setInt(1, bookId);
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				catelogueUrl.setBookId(rst.getInt("bookId"));
				catelogueUrl.setAllChapterReadUrl(rst.getString("allChapterUrl"));
				catelogueUrl.setAllChapterNameUrl(rst.getString("allChapterName"));
				return catelogueUrl;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取书籍的所有的章节
	 * @return List<Chapter>
	 */
	public List<Chapter> selectChapterList() {
		try {
			String sql = "select * from chapterInfo order by chapterId";
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			List<Chapter> list = new ArrayList<>();
			while(rst.next()) {
				Chapter chapter = new Chapter();
				chapter.setChapterId(rst.getInt("chapterId"));
				chapter.setBookId(rst.getInt("bookId"));
				chapter.setBookName(rst.getString("bookName"));
				chapter.setChapterName(rst.getString("chapterName"));
				chapter.setChapterUrl(rst.getString("chapterUrl"));
				list.add(chapter);
			}
			if(list != null) {
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新书籍章节
	 * @param chapter章节对象
	 * @return
	 */
	public boolean updateChapter(Chapter chapter) {
		try {
			String sql = "update chapter set chapterName=?,chapterUrl=? where chapterId=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, chapter.getChapterName());
			pst.setString(2, chapter.getChapterUrl());
			pst.setInt(3, chapter.getChapterId());
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 插入一条章节记录
	 * @param chapter
	 * @return
	 */
	public boolean insertChapter(Chapter chapter) {
		try {
			String sql = "";
			if(chapter.getChapterId() != 0) {
				sql = "update chapter set bookId=?,chapterName=?,chapterUrl=? where chapterId=?";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, chapter.getBookId());
				pst.setString(2, chapter.getChapterName());
				pst.setString(3, chapter.getChapterUrl());
				pst.setInt(4, chapter.getChapterId());
			}else {
				sql ="insert into chapter(bookId,chapterName,chapterUrl) value(?,?,?)";
				pst = conn.prepareStatement(sql);
				pst.setInt(1, chapter.getBookId());
				pst.setString(2, chapter.getChapterName());
				pst.setString(3, chapter.getChapterUrl());
			}
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取某一书籍的所有章节名和章节地址
	 * @param bookId
	 * @return
	 */
	public List<ChapterObject> selectByBookId(int bookId){
		try {
			List<ChapterObject> list = new ArrayList<>(); 
			String sql = "select chapterName,chapterUrl from chapter where bookId=? ORDER BY chapterId";
			pst = conn.prepareStatement(sql);
			pst.setInt(1,bookId);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				ChapterObject chapterObj = new ChapterObject();
				chapterObj.setChapterName(rst.getString("chapterName"));
				chapterObj.setChapterUrl(rst.getString("chapterUrl"));
				list.add(chapterObj);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 删除章节
	 * @param chapterId	章节id
	 * @return
	 */
	public boolean deleteChapterList(int chapterId) {
		String sql = "delete from chapter where chapterId=?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, chapterId);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int selectByChapterId(int chapterId) {
		String sql = "select * from chapter where chapterId="+chapterId;
		try {
			pst = conn.prepareStatement(sql);
			ResultSet rst = pst.executeQuery();
			if(rst.next()) {
				return rst.getInt("bookId");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
