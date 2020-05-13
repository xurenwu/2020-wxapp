package com.action.login;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseDataPojo;
import com.pojo.login.LoginSession;
import com.service.LoginService;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/checkLogin")
public class CheckLogin extends HttpServlet {
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
		
		PrintWriter out = res.getWriter();	
		String token = req.getParameter("token");		
		LoginService loginService = new LoginService();
		int id = loginService.checkToken(token);
		if(id != -1){		// token有效
			out.print(new Gson().toJson(new BaseDataPojo<LoginSession>("token有效", true, null)));
		}else{			// token无效
			out.print(new Gson().toJson(new BaseDataPojo<LoginSession>("token无效或错误", false, null)));
		};
	}

}
