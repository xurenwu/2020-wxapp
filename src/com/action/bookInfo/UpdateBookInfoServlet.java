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
import com.pojo.baseData.BaseListPojo;
import com.service.BookInfoService;

/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/updateBookInfo")
public class UpdateBookInfoServlet extends HttpServlet {
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
		 * 通过书籍id获取书籍信息
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		
		BookInfo bookinfo = new BookInfo();
		
		bookinfo.setBookId(Integer.parseInt(req.getParameter("bookId")));
		bookinfo.setBookName(req.getParameter("bookName"));
		bookinfo.setAuthor(req.getParameter("author"));
		bookinfo.setCategory(req.getParameter("category"));
		bookinfo.setDesc(req.getParameter("desc"));
		bookinfo.setLastChapter(req.getParameter("lastChapter"));
		bookinfo.setLastChapterUrl(req.getParameter("lastChapterUrl"));
		bookinfo.setHeat(Integer.parseInt(req.getParameter("heat")));
		bookinfo.setState(req.getParameter("state"));
		bookinfo.setBookImage(req.getParameter("bookImage"));
		bookinfo.setEnterTime(req.getParameter("enterTime"));
		int len = Integer.parseInt(req.getParameter("len"));
		if(bookinfo != null) {
			if(BookInfoService.updateBookInfo(bookinfo)) {
				if(len==-1) {
//					在search页面
					
					req.getRequestDispatcher("search").forward(req, res);
				}else {
					req.getRequestDispatcher("getBookList").forward(req, res);
				}
			}
			out.print(new Gson().toJson(new BaseListPojo<BookInfo>("修改失败",false,null)));
		}
	}

}
