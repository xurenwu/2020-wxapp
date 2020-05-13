package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.pojo.Catelogue.CatelogueUrl;

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

}
