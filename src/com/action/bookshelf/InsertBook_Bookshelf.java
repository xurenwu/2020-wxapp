package com.action.bookshelf;

import java.io.IOException;
import java.io.PrintWriter;

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
@WebServlet("/insertBook_bookshelf")
public class InsertBook_Bookshelf extends HttpServlet {
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
		
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		int userId = Integer.parseInt(req.getParameter("userId"));
		float progress = Float.parseFloat(req.getParameter("progress"));
		
		if(BookShelfService.insertBookshelfByBookId(userId, bookId, progress)) {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("成功移出书架", true, null)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("移出失败", false, null)));
		}
	}

}
