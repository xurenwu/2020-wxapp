package com.action.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.admin.AdminInfo;
import com.pojo.baseData.BaseDataPojo;
import com.service.AdminService;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/modifyAdminPsd")
public class ModifyAdminPsd extends HttpServlet {
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
		String admin_name = req.getParameter("admin_name");
		String password = req.getParameter("password");
		
		AdminInfo adminInfo = new AdminInfo();
		if( AdminService.updatePsd(password, admin_name)) {
			adminInfo = AdminService.getAdminInfo(admin_name);
			out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("修改成功",true,adminInfo)));
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("修改失败",false,null)));
		}
		
	}

}
