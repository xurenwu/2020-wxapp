package com.action.comment;

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
import com.pojo.baseData.BaseDataPojo;
import com.pojo.comment.Comment;
import com.service.CommentService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/deleteCommentList")
public class DeleteCommentListServlet extends HttpServlet {
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
		
		String str = req.getParameter("commentIdList");
		List<Integer> commentIdList = JSON.parseArray(str, Integer.class);
		if(commentIdList.size()>0) {
			if(CommentService.cancleComment(commentIdList)) {
				req.getRequestDispatcher("getCommentList").forward(req, res);
			}else {
				out.print(new Gson().toJson(new BaseDataPojo<Comment>("删除失败",false,null)));
			}
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<Comment>("删除失败",false,null)));
		}
	}

}
