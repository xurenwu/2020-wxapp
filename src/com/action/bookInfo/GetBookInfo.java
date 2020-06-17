package com.action.bookInfo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.BookInfo.BookInfo;
import com.pojo.baseData.BaseDataPojo;
import com.service.BookInfoService;

/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/getBookInfo")
public class GetBookInfo extends HttpServlet {
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
		 * 通过书籍id获取书籍目录
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		if(bookId>0) {
			BookInfo bookInfo = BookInfoService.getBookInfo(bookId);
			if(bookInfo != null) {
				out.print(new Gson().toJson(new BaseDataPojo<BookInfo>("获书籍成功",true,bookInfo)));
			}else {
				out.print(new Gson().toJson(new BaseDataPojo<BookInfo>("没有该书籍信息",false,null)));
			}
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<BookInfo>("获书籍失败",false,null)));
		}
	}

}
