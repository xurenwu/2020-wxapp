package com.action.bookshelf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseListPojo;
import com.pojo.bookshelf.Bookshelf;
import com.service.BookShelfService;

/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/bookshelfInfo")
public class BookshelfInfo extends HttpServlet {
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
		 * 通过用户id获取用户书架信息
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		
		int userId = Integer.parseInt(req.getParameter("userId"));
		List<Bookshelf> bookList = BookShelfService.selectBookShelfByUserId(userId);
		if(bookList != null) {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("成功获取书架信息", true, bookList)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("获取失败", false, null)));
		}
	}

}
