package com.action.bookInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.pojo.BookInfo.BookInfo;
import com.pojo.baseData.BaseListPojo;
import com.service.BookInfoService;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/deleteBookInfo")
public class DeleteBookInfoServlet extends HttpServlet {
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
		 * 通过获取要删除的书籍id
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		String str = req.getParameter("bookIdArr");
        List<Integer> bookIdlist = JSON.parseArray(str, Integer.class);
        int len = Integer.parseInt(req.getParameter("len"));
        System.out.println("len"+len);
        System.out.println(bookIdlist);
		if(BookInfoService.deleteBook(bookIdlist)) {
			if(len != -1) {
				System.out.println(bookIdlist);
				req.getRequestDispatcher("getBookList").forward(req, res);
			}else {
				req.getRequestDispatcher("search").forward(req, res);
			}
		}else {
			out.print(new Gson().toJson(new BaseListPojo<BookInfo>("删除失败",false,null)));
		}
		
	}

}
