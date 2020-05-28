package com.action.bookshelf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.pojo.baseData.BaseListPojo;
import com.pojo.bookshelf.BookList;
import com.pojo.bookshelf.Bookshelf;
import com.service.BookShelfService;


/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/deleteBook_bookshelf")
public class DeleteBook_Bookshelf extends HttpServlet {
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
		 * 通过用户id,和书籍id将用户书籍移除书架
		 */
		/**
		 * 通过用户id,和书籍id将用户书籍移除书架
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		
		String bookList = req.getParameter("bookList");
		List<BookList> booklist = new ArrayList<>();
		booklist = JSONObject.parseArray(bookList, BookList.class);
		if(BookShelfService.deleteBookshelfByBookId(booklist)) {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("成功移出书架", true, null)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<Bookshelf>("移出失败", false, null)));
		}
	}

}
