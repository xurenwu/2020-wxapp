package com.action.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseMapPojo;
import com.service.AdminService;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/getUserAndBookNum")
public class GetUserAndBookNum extends HttpServlet {
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
		
		Map<String,List<Integer>> map = AdminService.getUserAndBookNum();
		if( map!= null) {
			out.print(new Gson().toJson(new BaseMapPojo<String,List<Integer>>(true,"获取数据成功",map)));
		}else {
			out.print(new Gson().toJson(new BaseMapPojo<String,List<Integer>>(false,"获取数据失败",null)));
		}
		
	}

}
