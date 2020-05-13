package com.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

//import org.springframework.stereotype.Component;

/**
 * Http请求工具类
 */
//@Component
public class HttpUtil {

    /**
     * 设置连接超时时间,20000,消除魔术数字
     */
    private static final int TWOTHOUSAND = 20000;

    /**
     * 发送post请求
     *
     * @param url    请求路径
     * @param header 请求头
     * @param body   请求体
     * @return String
     */
    public String doPost(String url, Map<String, String> header, String body) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // 设置 url
            URL realUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置 header
            for (String key : header.keySet()) {
                connection.setRequestProperty(key, header.get(key));
            }
            // 设置请求 body
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //设置连接超时和读取超时时间
            connection.setConnectTimeout(TWOTHOUSAND);
            connection.setReadTimeout(TWOTHOUSAND);
            try {
                out = new PrintWriter(connection.getOutputStream());
                // 保存body
                out.print(body);
                // 发送body
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // 获取响应body
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @param url    请求路径
     * @param header 请求头
     * @return String
     */
    public String doGet(String url, Map<String, String> header) {
        String result = "";
        BufferedReader in = null;
        try {
            // 设置 url
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            // 设置 header
            for (String key : header.keySet()) {
                connection.setRequestProperty(key, header.get(key));
            }
            // 设置请求 body
//            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
