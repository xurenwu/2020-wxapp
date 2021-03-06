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
import com.pojo.baseData.BaseDataPojo;
import com.service.AllChapterService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/addChapter")
public class AddChapterServlet extends HttpServlet {
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
		String str = req.getParameter("chapter");
		Chapter chapter = JSON.parseObject(str,new TypeReference<Chapter>() {});
		if(chapter!=null) {
			if(AllChapterService.insertChapter(chapter)) {
				out.print(new Gson().toJson(new BaseDataPojo<Chapter>("添加章节成功",true,null)));
			}else {
				out.print(new Gson().toJson(new BaseDataPojo<Chapter>("添加章节失败",false,null)));
			}
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<Chapter>("添加章节信息为空",false,null)));
		}
	}

}
