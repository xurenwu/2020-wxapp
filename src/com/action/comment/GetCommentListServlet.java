package com.action.comment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BasePojo;
import com.pojo.comment.Comment;
import com.service.CommentService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getCommentList")
public class GetCommentListServlet extends HttpServlet {
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
		
		int inital = Integer.parseInt(req.getParameter("inital"));
		int len = Integer.parseInt(req.getParameter("len"));
		
		List<Comment> list = new ArrayList<>();
		List<Comment> commentList = new ArrayList<>();
		if(inital>=0&&len>=0) {
			//获取前台传回的书籍id
			list = CommentService.getCommentList();
			for(int i=inital;i<inital+len&&i<list.size();i++) {
				commentList.add(list.get(i));
			}
			if(commentList != null &&commentList.size()>0) {
				out.print(new Gson().toJson(new BasePojo<Comment,Integer> ("获取评论成功", true, list.size(),commentList)));
			}else {
				out.print(new Gson().toJson(new BasePojo<Comment,Integer> ("获取评论失败", false, 0,null)));
			}
		}else {
			out.print(new Gson().toJson(new BasePojo<Comment,Integer> ("提交参数有问题", false, 0,null)));
		}
	}

}
