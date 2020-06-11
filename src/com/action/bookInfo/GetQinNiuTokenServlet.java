package com.action.bookInfo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseDataPojo;
import com.utils.QiniuUtil;



/**
 * Servlet implementation class GetBookInfo
 */
@WebServlet("/getQiNiuToken")
public class GetQinNiuTokenServlet extends HttpServlet {
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
		 * 生成token
		 */
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		String key = req.getParameter("key");
		String token = "";
		token = QiniuUtil.getToken(key);
		if(token.length()>0) {
			out.print(new Gson().toJson(new BaseDataPojo<String>("获取token成功",true,token)));
		}else {
			out.print(new Gson().toJson(new BaseDataPojo<String>("获取token失败",false,null)));
		}
	}

}
