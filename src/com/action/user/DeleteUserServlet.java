package com.action.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseDataPojo;
import com.pojo.login.User;
import com.service.UserService;


/**
 * Servlet implementation class Login
 */
@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {
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
		
		int userId = Integer.parseInt(req.getParameter("userId"));
		int len = Integer.parseInt(req.getParameter("len"));       //len=-1的时候页面
		UserService userService = new UserService();
		if(userService.deleteByUserId(userId)) {
			if(len != -1) {
				req.getRequestDispatcher("getUserList").forward(req, res);
			}
			else {
				out.print(new Gson().toJson(new BaseDataPojo<User>("用户删除成功",true,null)));
			}
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<User>("用户删除失败",false,null)));
		}
		
	}

}
