package com.action.readLog;

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
import com.pojo.readLog.ReadLogBook;
import com.service.ReadLog;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getReadLog")
public class GetReadLogServlet extends HttpServlet {
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
		
		//获取前台传回的书籍id
		int userId = Integer.parseInt(req.getParameter("userId"));
		List<ReadLogBook> list = ReadLog.getReadlog(userId);
		if(list != null) {
			out.print(new Gson().toJson(new BaseListPojo<ReadLogBook> ("获取历史阅读记录成功", true, list)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<ReadLogBook> ("获取历史阅读记录失败", false, null)));
		}
	}

}
