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
import com.pojo.baseData.BasePojo;
import com.pojo.login.User;
import com.service.UserService;


/**
 * Servlet implementation class Login
 */
@WebServlet("/getUserList")
public class GetUserList extends HttpServlet {
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
		
		int inital = Integer.parseInt(req.getParameter("inital"));
		int len = Integer.parseInt(req.getParameter("len"));
		
		System.out.println("len: "+len+"inital:"+inital);
		List<User> list = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		
		
		UserService userService = new UserService();
		list = userService.getUserList();
		for(int i=inital; i<list.size()&&i<len+inital;i++) {
			userList.add(list.get(i));
		}
		if(list != null && userList != null) {
			out.print(new Gson().toJson(new BasePojo<User,Integer>("获取用户列表成功",true,list.size(), userList)));
		}else {
			out.print(new Gson().toJson(new BasePojo<User,Integer>("获取用户列表失败",true,0, null)));
		}
	}

}
