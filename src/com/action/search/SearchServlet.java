package com.action.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.BookInfo.BookInfo;
import com.pojo.baseData.BaseListPojo;
import com.service.Search;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		/**
		 * 通过书籍id获取书籍评论
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		
		//搜索关键字
		String Key = req.getParameter("Key");
		System.out.println(Key);
		List<BookInfo> booklist = Search.searchByKeywords(Key);
		if(booklist != null) {
			System.out.println(123);
			out.print(new Gson().toJson(new BaseListPojo<BookInfo> ("获取书籍成功", true, booklist)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<BookInfo> ("获取书籍失败", false, null)));
		}
	}

}
