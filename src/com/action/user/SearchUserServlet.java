package com.action.user;

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
import com.pojo.baseData.BaseListPojo;
import com.pojo.login.User;
import com.service.UserService;


/**
 * Servlet implementation class Login
 */
@WebServlet("/searchUser")
public class SearchUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//编码处理
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		
		//获取数据		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		

		String keyword = req.getParameter("keyword");
		UserService userService = new UserService();
		List<User> userlist = new ArrayList<>();
		userlist = userService.search(keyword);
		if(userlist != null) {
			out.print(new Gson().toJson(new BaseListPojo<User>("搜索成功",true,userlist)));
		}else {
			out.print(new Gson().toJson(new BaseListPojo<User>("搜索失败",false,null)));
		}
	}

}
