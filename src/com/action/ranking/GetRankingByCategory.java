package com.action.ranking;

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
import com.pojo.BookInfo.BookInfo;
import com.pojo.baseData.BasePojo;
import com.service.RankingService;

/**
 * Servlet implementation class GetCatelogue
 */
@WebServlet("/getRankingByCategory")
public class GetRankingByCategory extends HttpServlet {
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
		
		//获取前台传回的排行榜的索引
		String rankingKey = req.getParameter("rankingKey");
		String category = req.getParameter("category");
		int inital = Integer.parseInt(req.getParameter("currentPoint"));
		int len = Integer.parseInt(req.getParameter("len"));
		List<BookInfo> list = new ArrayList<>();
		List<BookInfo> rank_booklist = new ArrayList<>();
		if("全部".equals(category) && rankingKey.length()>0) {
			list = RankingService.getBookListByHeatOrLatest(1000, rankingKey);
		}else if(rankingKey.length()>0) {
			list = RankingService.getRankBookListByCategory(rankingKey, category);
		}
			
		if(list != null) {
			for(int i=inital ;i<list.size()&&i<inital+len ;i++) {
				rank_booklist.add(list.get(i));
			}
			if(rank_booklist != null && rank_booklist.size()>0) {
				out.print(new Gson().toJson(new BasePojo<BookInfo,Integer>("获取排行榜成功",true,list.size(),rank_booklist)));
			}else {
				out.print(new Gson().toJson(new BasePojo<BookInfo,Integer>("获取排行榜失败",false,0,null)));
			}
		}else {
			out.print(new Gson().toJson(new BasePojo<BookInfo,Integer>("获取排行榜失败",false,0,null)));
		}
	}

}
