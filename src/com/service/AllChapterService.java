package com.service;

import java.sql.Connection;
import java.util.List;

import com.dao.BookDAO;
import com.dao.CatelogueDAO;
import com.pojo.Catelogue.Catelogue;
import com.pojo.Catelogue.CatelogueUrl;
import com.pojo.Catelogue.ChapterObject;
import com.utils.DBUtil;
import com.utils.HttpUtils;

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
				return catelogue;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	
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
			for(i=0;i<catelogue.getAllChapter().size();i++) {
				if(bookDAO.insertChapter(catelogue.getAllChapter().get(i), bookId)) continue;
				else break;
			}
			if(i == catelogue.getAllChapter().size()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}

}
