package com.action.bookInfo;

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
import com.service.BookInfoService;

/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/getBookList")
public class GetBookListServlet extends HttpServlet {
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
		 * 通过书籍id获取书籍目录
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		
		int inital = Integer.parseInt(req.getParameter("inital")); 		//页面初始位置
		int len = Integer.parseInt(req.getParameter("len"));			//页面数据长度
		
		List<BookInfo> booklist = new ArrayList<>();
		List<BookInfo> list = new ArrayList<>();
		list = BookInfoService.getBookList();
		if(list != null) {
			for(int i=inital; i<list.size()&&i<inital+len; i++) {
				booklist.add(list.get(i));
			}
			if(booklist != null) {
				out.print(new Gson().toJson(new BasePojo<BookInfo,Integer>("获取书籍列表成功",true,list.size(),booklist)));
			}
		}
		else {
			out.print(new Gson().toJson(new BasePojo<BookInfo,Integer>("获取书籍列表失败",false,0,null)));
		}
	}

}
