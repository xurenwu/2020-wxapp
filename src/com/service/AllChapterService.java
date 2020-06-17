package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.BookDAO;
import com.dao.CatelogueDAO;
import com.pojo.Catelogue.Catelogue;
import com.pojo.Catelogue.CatelogueUrl;
import com.pojo.Catelogue.Chapter;
import com.pojo.Catelogue.ChapterObject;
import com.utils.DBUtil;
import com.utils.HttpUtils;
import com.utils.JavaUploadToQiNiuUtil;
import com.utils.TemporaryFileUtil;

public class AllChapterService {
	
	public static void main(String args[]) {
		Catelogue catelogue = checkBookId(20201000);
		System.out.println(insertAllChapter(catelogue,20201000));
//		Chapter chapter = new Chapter();
//		chapter.setBookId(20201002);
////		chapter.setChapterId(2050);
//		chapter.setChapterName("译后记");
//		chapter.setBookName("百年孤独");
//		chapter.setChapterUrl("/foreign/bngd/1019.txt");
//		System.out.println(insertChapter(chapter));
//		String[] len = chapter.getChapterUrl().split("/");
//		System.out.println(len[len.length-3]+"/"+len[len.length-2]);
//		System.out.println(chapter.getBookId());
//		updateChapter(chapter);
//		List<Chapter> list = selectChapterList();
//		System.out.println(list.get(0).getBookName());
//		List<Integer> list = new ArrayList<>();
//		list.add(2069);
//		System.out.println(deleteChapter(list));
//		Connection conn = DBUtil.getConnection();
//		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
//		System.out.println(cataLogueDao.selectByChapterId(2050));
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
				insertAllChapter(catelogue, bookId);
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
	public static boolean updateChapter(Chapter chapter) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
//			conn.setAutoCommit(false);
			if(cataLogueDao.updateChapter(chapter)) {
				//数据库更新成功后
				//更新七牛的章节目录文件
				CatelogueUrl catelogueUrl = selectBookId(chapter.getBookId()); 		//获取当前书籍的目录地址
				System.out.println(chapter.getBookId());
				System.out.println(catelogueUrl==null);
				if(catelogueUrl != null) {
					System.out.println(catelogueUrl);
					String name_url = catelogueUrl.getAllChapterNameUrl();				//书籍目录的章节名字的文件地址
					String read_url = catelogueUrl.getAllChapterReadUrl();			//书籍目录的章节阅读内容地址
					List<ChapterObject> chapterlist = new ArrayList<>();
					chapterlist = cataLogueDao.selectByBookId(chapter.getBookId());		//本书所有的章节的对象
					if(chapterlist != null) {
						//返回书籍章节成功
						System.out.println(chapterlist);
						TemporaryFileUtil.updateFile_chapterName("E:\\File\\temporary_name.txt", chapterlist);		//更新临时章节名文件
						TemporaryFileUtil.updateFile_chapterUrl("E:\\File\\temporary_url.txt", chapterlist);		//更新临时章节阅读地址文件
						boolean upload = JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_name.txt", name_url.substring(33));		//更新书籍章节名文件
						boolean upload01 =JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_url.txt", read_url.substring(33));		//更新书籍章节阅读地址文件
						if(upload&&upload01) {
//							conn.commit();
							return true;
						}
					}
				}
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
	 * 插入一条章节
	 * @param chapter
	 * @return
	 */
	public static boolean insertChapter(Chapter chapter) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			if(cataLogueDao.insertChapter(chapter)) {			//书籍章节插入数据库成功
				//更新七牛的章节目录文件
				CatelogueUrl catelogueUrl = selectBookId(chapter.getBookId()); 		//获取当前书籍的目录地址
				if(catelogueUrl == null) {
					CatelogueUrl catelogueUrl01 = new CatelogueUrl();
					String[] url = chapter.getChapterUrl().split("/");
					String url01 = url[url.length-3]+"/"+url[url.length-2];
					String chapterNameUrl = "http://qbhvuddzp.bkt.clouddn.com/"+url01+"/all_chapter_name.txt";
					String chapterReadUrl = "http://qbhvuddzp.bkt.clouddn.com/"+url01+"/all_chapter_url.txt";
					System.out.println(chapterNameUrl+' '+chapterReadUrl);
					catelogueUrl01.setAllChapterNameUrl(chapterNameUrl);
					catelogueUrl01.setAllChapterReadUrl(chapterReadUrl);
					catelogueUrl01.setBookId(chapter.getBookId());
					if(insertCatelogueUrl(catelogueUrl01)) {
						catelogueUrl = catelogueUrl01;
					}else {
						return false;
					}
				}
				if(catelogueUrl != null) {
					String name_url = catelogueUrl.getAllChapterNameUrl();				//书籍目录的章节名字的文件地址
					String read_url = catelogueUrl.getAllChapterReadUrl();			//书籍目录的章节阅读内容地址
					List<ChapterObject> chapterlist = cataLogueDao.selectByBookId(chapter.getBookId());		//本书所有的章节的对象
					if(chapterlist != null) {
						//返回书籍章节成功
						TemporaryFileUtil.updateFile_chapterName("E:\\File\\temporary_name.txt", chapterlist);		//更新临时章节名文件
						TemporaryFileUtil.updateFile_chapterUrl("E:\\File\\temporary_url.txt", chapterlist);		//更新临时章节阅读地址文件
						boolean upload = JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_name.txt", name_url.substring(33));		//更新书籍章节名文件
						boolean upload01 =JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_url.txt", read_url.substring(33));		//更新书籍章节阅读地址文件
						if(upload&&upload01) {
							return true;
						}
					}
				}
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return false;
	}
	
	//批量删除章节
	public static boolean deleteChapter(List<Integer> chapterIdList) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			int i=0;
			int count=0;
			Map<Integer,Integer> bookIdmap = new HashMap<Integer, Integer>();
			int bookId = -1;
			for( ;i<chapterIdList.size();i++) {
				//获取书籍id
				bookId = cataLogueDao.selectByChapterId(chapterIdList.get(i));
				if(bookId != -1&&cataLogueDao.deleteChapterList(chapterIdList.get(i))) {
					//map中已经有这本书的时候不放入map中
					if(!bookIdmap.containsValue(bookId)){
						bookIdmap.put(count++, bookId);
					}
					//删除成功后继续
					continue;
				}else {
					return false;
				}
			}
			//全部删除
			if(i==chapterIdList.size()) {
				//数据库更新成功后
				//更新七牛的章节目录文件
				for(int j=0;j<bookIdmap.size();i++) {
					CatelogueUrl catelogueUrl = selectBookId(bookIdmap.get(j)); 		//获取当前书籍的目录地址
					System.out.println(catelogueUrl==null);
					if(catelogueUrl != null) {
						System.out.println(catelogueUrl);
						String name_url = catelogueUrl.getAllChapterNameUrl();				//书籍目录的章节名字的文件地址
						String read_url = catelogueUrl.getAllChapterReadUrl();			//书籍目录的章节阅读内容地址
						List<ChapterObject> chapterlist = new ArrayList<>();
						chapterlist = cataLogueDao.selectByBookId(bookIdmap.get(j));		//本书所有的章节的对象
						if(chapterlist != null) {
							//返回书籍章节成功
							System.out.println(chapterlist);
							TemporaryFileUtil.updateFile_chapterName("E:\\File\\temporary_name.txt", chapterlist);		//更新临时章节名文件
							TemporaryFileUtil.updateFile_chapterUrl("E:\\File\\temporary_url.txt", chapterlist);		//更新临时章节阅读地址文件
							boolean upload = JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_name.txt", name_url.substring(33));		//更新书籍章节名文件
							boolean upload01 =JavaUploadToQiNiuUtil.allUpload("E:\\File\\temporary_url.txt", read_url.substring(33));		//更新书籍章节阅读地址文件
							if(upload&&upload01) {
								return true;
							}
						}
					}
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
	
	public static boolean insertCatelogueUrl(CatelogueUrl catelogueUrl) {
		Connection conn = DBUtil.getConnection();
		CatelogueDAO cataLogueDao = new CatelogueDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(cataLogueDao.insertCatelogueUrl(catelogueUrl)) {
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

}
