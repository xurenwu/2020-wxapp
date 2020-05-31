package com.action.catelogue;

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
import com.service.AllChapterService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/updateCatelogue")
public class UpdateCatelogueServlet extends HttpServlet {
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
		Catelogue catelogue = AllChapterService.checkBookId(bookId);
		boolean condition = AllChapterService.insertAllChapter(catelogue, bookId);
		if(condition) {
			out.print(new Gson().toJson(new BaseDataPojo<Catelogue> ("目录修改成功", true, null)));
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<Catelogue> ("目录修改失败", false, null)));
		}
	}

}
