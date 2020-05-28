package com.action.comment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.Catelogue.Catelogue;
import com.pojo.baseData.BaseDataPojo;
import com.service.CommentService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/cancelComment")
public class CancelCommentServlet extends HttpServlet {
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
		 * 通过评论id撤销评论
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		
		int commentId = Integer.parseInt(req.getParameter("commentId"));
		boolean condition = CommentService.cancleComment(commentId);
		if(condition) {
			out.print(new Gson().toJson(new BaseDataPojo<Catelogue> ("撤销成功", true, null)));
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<Catelogue> ("撤销失败", false, null)));
		}
	}

}
