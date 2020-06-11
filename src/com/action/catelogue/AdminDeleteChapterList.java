package com.action.catelogue;

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
import com.pojo.Catelogue.Chapter;
import com.pojo.baseData.BasePojo;
import com.service.AllChapterService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/deleteChapterList")
public class AdminDeleteChapterList extends HttpServlet {
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
		int len = Integer.parseInt(req.getParameter("len"));
		int inital = Integer.parseInt(req.getParameter("inital"));
		String str = req.getParameter("chapterIdList");
		List<Integer> chapterIdList = JSON.parseArray(str, Integer.class);
		if(AllChapterService.deleteChapter(chapterIdList)) {
			System.out.println("删除成功");
			req.getRequestDispatcher("getChapterList").forward(req, res);
		}else {
			out.print(new Gson().toJson(new BasePojo<Chapter,Integer>("删除失败",false,0,null)));
		}
	}

}
