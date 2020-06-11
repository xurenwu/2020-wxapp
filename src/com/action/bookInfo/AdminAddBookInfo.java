package com.action.bookInfo;

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
import com.pojo.BookInfo.BookInfo;
import com.pojo.baseData.BaseDataPojo;
import com.service.BookInfoService;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/addBookInfo")
public class AdminAddBookInfo extends HttpServlet {
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
		 * 通过获取要删除的书籍id
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		String str = req.getParameter("bookInfo");
		BookInfo bookInfo = JSON.parseObject(str,new TypeReference<BookInfo>() {});
		if(BookInfoService.addBookInfo(bookInfo)) {
			out.print(new Gson().toJson(new BaseDataPojo<String>("书籍添加成功",true,null)));
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<String>("书籍添加失败",false,null)));
		}
	}

}
