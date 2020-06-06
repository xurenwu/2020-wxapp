package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dao.BookDAO;
import com.dao.CatelogueDAO;
import com.pojo.Catelogue.Catelogue;
import com.pojo.Catelogue.CatelogueUrl;
import com.pojo.Catelogue.Chapter;
import com.pojo.Catelogue.ChapterObject;
import com.utils.DBUtil;
import com.utils.HttpUtils;
import com.utils.JavaUploadToQiNiuUtil;

public class AllChapterService {
	
	public static void main(String args[]) {
		Catelogue catelogue = checkBookId(20201000);
		System.out.println(insertAllChapter(catelogue,20201000));
	}
	
	/**
	 * 获取书籍目录业务
	 * 1.通过书籍id来访问数据库查看数据库中是否有该书籍，如果有则返回目录信息，否则返回错误其实信息
	 * 2.通过网络请求读取章节目录文件并返回目录信息
	 * 3.将目录信息格式化后并返回给servlet接口层
	 */
	public static Catelogue checkBookId(int bookId) {
		Catelogue catelogue = new Catelogue();
		CatelogueUrl catelogueUrl = selectBookId(bookId);
		if(catelogueUrl != null) {
			catelogue = formdate(catelogueUrl,bookId);
			if(catelogue != null) {
				/**
				 * 插入目录的操作有待商榷,最好可以先获取该书的状态，如果是完结的就不必更新
				 */
//				insertAllChapter(catelogue, bookId);
				return catelogue;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	/**
	 * 返回书籍目录地址
	 * @param bookId
	 * @return
	 */
	public static CatelogueUrl selectBookId(int bookId) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		CatelogueUrl catelogueUrl = cataLogueDao.checkBookId(bookId);
		return catelogueUrl;
	}
	
	/**
	 * 书籍目录格式化
	 * @param catelogueUrl 目录文件地址对象
	 * @param bookId 书籍id
	 * @return  目录对象
	 */
	public static Catelogue formdate(CatelogueUrl catelogueUrl,int bookId) {
		Catelogue catelogue = new Catelogue();
		if(catelogueUrl != null) {
			catelogue.setBookId(bookId);
			String url = catelogueUrl.getAllChapterNameUrl();
			String readUrl = catelogueUrl.getAllChapterReadUrl();
			List<String> chapterName = HttpUtils.sendGet(url, null);
			List<String> chapterUrl = HttpUtils.sendGet(readUrl, null);
			for(int i=0; i<chapterUrl.size()&&i<chapterName.size(); i++) {
				ChapterObject chapterObj = new ChapterObject();
				chapterObj.setChapterUrl(chapterUrl.get(i));
				chapterObj.setChapterName(chapterName.get(i));
				catelogue.addChapterObject(chapterObj);
			}
			return catelogue;
		}else {
			return null;
		}
	}
	
	/**
	 * 将书籍的所有目录都写入数据库中
	 * 1.通过读取目录文件获取目录文件对象
	 * 2.通过sql语言将所有的书籍目录信息写入到数据库chapter中
	 */
	
	public static boolean insertAllChapter(Catelogue catelogue, int bookId) {
		int i;
		if(catelogue != null) {
			Connection conn = DBUtil.getConnection();
			BookDAO bookDAO = new BookDAO(conn);
			try {
				conn.setAutoCommit(false);
				for(i=0;i<catelogue.getAllChapter().size();i++) {
					if(bookDAO.insertChapter(catelogue.getAllChapter().get(i), bookId)) continue;
					else break;
				}
				if(i == catelogue.getAllChapter().size()) {
					conn.commit();
					return true;
				}else {
					conn.rollback();
					return false;
				}
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				if(conn != null) {
					DBUtil.closeConnection(conn);
				}
			}
		}
		return false;
	}
	
	//获取所有的书籍的所有章节
	public static List<Chapter> selectChapterList(){
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			List<Chapter> chapterList = new ArrayList<>();
			chapterList = cataLogueDao.selectChapterList();
			if(chapterList != null) {
				return chapterList;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return null;
	}
	
	/**
	 * 更新章节信息
	 * @param chapter
	 * @return
	 */
	public boolean updateChapter(Chapter chapter) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(cataLogueDao.updateChapter(chapter)) {
				//数据库更新成功后
				//更新七牛的章节目录文件
				CatelogueUrl catelogueUrl = selectBookId(chapter.getBookId()); 		//获取当前书籍的目录地址
				if(catelogueUrl != null) {
					String url = catelogueUrl.getAllChapterNameUrl();				//书籍目录的章节名字的文件地址
					String readUrl = catelogueUrl.getAllChapterReadUrl();			//书籍目录的章节阅读内容地址
					JavaUploadToQiNiuUtil.coverUpload("E:\\File\\temporary.txt", );	//E:\\File\\temporary.txt是本地的临时目录文件
				}
				
				conn.commit();
				return true;
			}else {
				return false;
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
	
	
	/**
	 * 插入一条章节记录
	 * @param chapter
	 * @return
	 */
	public boolean insertChapter(Chapter chapter) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(cataLogueDao.insertChapter(chapter)) {
				//更新七牛的章节目录文件
				List<ChapterObject> chapterList = new ArrayList<>();
				chapterList = cataLogueDao.selectByBookId(chapter.getBookId());
				if(chapterList != null) {
					JavaUploadToQiNiuUtil.upload("E:\\File\\temporary.txt", );
					conn.commit();
				}
				return true;
			}else {
				return false;
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

}
