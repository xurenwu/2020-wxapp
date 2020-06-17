package com.action.catelogue;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.pojo.Catelogue.Chapter;
import com.pojo.baseData.BasePojo;
import com.service.AllChapterService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/updateChapterInfo")
public class UpdateChapterInfoServlet extends HttpServlet {
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
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		String str = req.getParameter("chapter");
		Chapter chapter = JSON.parseObject(str,new TypeReference<Chapter>() {});
		//获取前台传回的章节信息
		if(AllChapterService.updateChapter(chapter)) {
			req.getRequestDispatcher("getChapterList").forward(req, res);
		}else {
			out.print(new Gson().toJson(new BasePojo<Chapter,Integer>("更新章节信息失败",false,0,null)));
		}
	}

}
