package com.action.comment;

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
import com.pojo.comment.Comment;
import com.service.CommentService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getComment")
public class GetCommentServlet extends HttpServlet {
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
		int bookId = Integer.parseInt(req.getParameter("bookId"));
		List<Comment> commentList = CommentService.getCommentById(bookId);
		if(commentList != null) {
			out.print(new Gson().toJson(new BaseListPojo<Comment> ("获取评论成功", true, commentList)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<Comment> ("获取评论失败", false, null)));
		}
	}

}
