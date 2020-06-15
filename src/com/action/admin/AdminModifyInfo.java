package com.action.admin;

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
import com.pojo.admin.AdminInfo;
import com.pojo.baseData.BaseDataPojo;
import com.service.AdminService;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/adminModifyInfo")
public class AdminModifyInfo extends HttpServlet {
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
		String str = req.getParameter("adminInfo");
		AdminInfo adminInfo = JSON.parseObject(str,new TypeReference<AdminInfo>() {});
		if(adminInfo!=null) {
			AdminInfo admin = new AdminInfo();
			if(AdminService.modify(adminInfo)) {
				admin = AdminService.getAdminInfo(adminInfo.getAdmin_name());
				if(admin!=null) {
					out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("修改成功",true,admin)));
				}else out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("修改成功返回用户失败",false,null)));
			}else {
				out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("修改失败",false,null)));
			}
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<AdminInfo>("参数为空",false,null)));
		}
	}

}
