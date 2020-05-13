package com.action.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.pojo.baseData.BaseDataPojo;
import com.pojo.login.LoginSession;
import com.service.LoginService;
import com.utils.HttpUtil;

/**
 * Servlet implementation class LoginSession
 */
@WebServlet("/login")
public class Login extends HttpServlet {
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
		
		PrintWriter out = res.getWriter();		// 用PrintWriter对象返回数据
		
		String appId = "wxa792a8d2e3fc9583";
		String appSecret = "3f985f0b446531e3a59c062ac8765bfc";
		
		String code = req.getParameter("code"); 
		String url = "https://api.weixin.qq.com/sns/jscode2session" + "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code +
				"&grant_type=authorization_code";
		// 发送网络请求，获取openid、session_key
		HttpUtil httpUtil = new HttpUtil();
		Map<String, String> map = new HashMap<>();
	    String jsonStr = httpUtil.doGet(url, map);
	    // 字符串转换为对象
	    map = new Gson().fromJson(jsonStr, Map.class);
	    String openId = map.get("openid");
	    System.out.println("openId=" + openId);
	    
	    // 生成token，这里使用时间戳，实际开发中推荐用更成熟的机制生成token
	    String token = "token_" + new Date().getTime();
	    System.out.println("token=" + token);
	    
	    // 调用service层执行登录业务
	    LoginService loginService = new LoginService();
	    BaseDataPojo<LoginSession> basePojo =  loginService.login(openId, token);	    
		out.print(new Gson().toJson(basePojo));		// 转换为Json数据并返回
	}

}
