package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pojo.BookInfo.BookInfo;

public class SearchDAO {
	
	private Connection conn = null;
	private PreparedStatement pst = null;
	
	public SearchDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	/**
	 * 更具关键字返回搜索到的书籍列表
	 * @param keywords 关键字数组
	 * @return
	 */
	
	public List<BookInfo> searchByKeywords(String[] keywords){
		List<BookInfo> bookList = new ArrayList<>();
		if(keywords != null) {
			String sql = "select * from bookInfo where ";
			try {
				for(int i=0;i<keywords.length;i++) {
					if(i == 0) {
						sql = sql + "concat_ws('',bookName,author) like '%" + keywords[i] + "%'";
					}
					else {
						sql = sql + " or concat_ws('',bookName,author) like '%" + keywords[i] + "%'";
					}
				}
				pst = conn.prepareStatement(sql);
				ResultSet rst = pst.executeQuery();
				while(rst.next()) {
					BookInfo bookinfo = new BookInfo();
					bookinfo.setBookId(rst.getInt("bookId"));
					bookinfo.setAuthor(rst.getString("author"));
					bookinfo.setBookImage(rst.getString("bookImage"));
					bookinfo.setBookName(rst.getString("bookName"));
					bookinfo.setDesc(rst.getString("desc"));
					bookList.add(bookinfo);
				}
				return bookList;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
