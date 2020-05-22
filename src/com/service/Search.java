package com.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.dao.SearchDAO;
import com.pojo.BookInfo.BookInfo;
import com.utils.DBUtil;

public class Search {

	/**
	 * 搜索业务层
	 * 模糊搜索（关键字搜索关键字包括author and bookName）
	 * 1.作者搜索
	 * 2.作品搜索
	 */
	
	public static void main(String[] args) {
		System.out.println(searchByKeywords("百年马尔克斯").get(0).getAuthor());
	}
	
	/**
	 * 关键字搜索(包含书籍名和作者名字的检索)
	 * @param keywords
	 * @return
	 */
	public static List<BookInfo> searchByKeywords(String keywords) {
		List <BookInfo>booklist = new ArrayList<>();
		List <BookInfo>booklist01 = new ArrayList<>();
		String []key01 = formateKey(keywords);
		String []key02 = formateKey(keywords.substring(1));
		Connection conn = DBUtil.getConnection();
		SearchDAO searchDAO = new SearchDAO(conn);
		if(key01 == null) {
			return null;
		}else{
			booklist = searchDAO.searchByKeywords(key01);
			if(key02 != null){
				booklist01 = searchDAO.searchByKeywords(key02);
				if(booklist01 != null) {
					booklist.addAll(booklist01);
					booklist = duplicateRemove(booklist);
				}
			}
		}
		return booklist;
	}
	
	/**
	 * 书籍对象集合去重
	 * @param booklist 书籍列表
	 * @return List
	 */
	public static List<BookInfo> duplicateRemove(List<BookInfo> booklist){
		List<Integer> idList = new ArrayList<>();
		for(int i=0;i<booklist.size();i++) {
			if(idList.contains(booklist.get(i).getBookId())) {
				booklist.remove(i);
				i--;
			}else {
				idList.add(booklist.get(i).getBookId());
			}
		}
		return booklist;
	}
	
	/**
	 * 关键字的处理将关键字按两个字符进行分割
	 * 再分别将字符串数组进行模糊查询
	 * @param key
	 * @return String[] 关键字数组
	 */
	public static String[] formateKey(String key) {
		if(key != null && key.length() != 0) {
			int i=0,j=0;
			String[] str = new String[key.length()%2==0 ? key.length()/2 : key.length()/2+1];
			while(i<key.length()) {
				if(i<key.length()-1) {
					str[j++] = key.substring(i, i+2);
				}else {
					str[j] = key.substring(i, key.length());
				}
				i=i+2;
			}
			return str;
		}else {
			return null;
		}
	}
}
