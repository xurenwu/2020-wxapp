package com.action.readLog;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.service.ReadLog;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getReadProgress")
public class GetReadProgressServlet extends HttpServlet {
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
		 * 通过书籍id和用户id来返回该书的阅读进度，-1（没有阅读记录，点击书籍封面进入书籍info界面） 
		 * 否则直接进入阅读界面
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		
		//获取前台传回的书籍id
		int userId = Integer.parseInt(req.getParameter("userId"));
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		float progress = ReadLog.selectProgress(userId, bookId);
		out.print(new Gson().toJson(progress));
	}

}
