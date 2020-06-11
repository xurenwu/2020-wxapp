package com.action.catelogue;

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
import com.pojo.Catelogue.Chapter;
import com.pojo.baseData.BasePojo;
import com.service.AllChapterService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getChapterList")
public class GetAllChapterServlet extends HttpServlet {
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
		System.out.println(len+" "+inital);
		List<Chapter> list = new ArrayList<>();
		list = AllChapterService.selectChapterList();
		List<Chapter> chapterlist = new ArrayList<>();
		System.out.println(AllChapterService.selectChapterList().get(0).getBookId()+list.size());
		if(list != null) {
			for(int i=inital;i<list.size()&&i<inital+len;i++) {
				chapterlist.add(list.get(i));
			}
			if(chapterlist != null) {
				System.out.println(chapterlist);
				out.print(new Gson().toJson(new BasePojo<Chapter,Integer>("获取章节信息成功",true,list.size(),chapterlist)));
			}
			else out.print(new Gson().toJson(new BasePojo<Chapter,Integer>("获取章节信息失败",false,0,null)));
		}
		else out.print(new Gson().toJson(new BasePojo<Chapter,Integer>("获取章节信息失败",false,0,null)));
	}

}
