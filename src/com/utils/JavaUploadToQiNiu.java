package com.utils;

import java.io.*;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;




public class JavaUploadToQiNiu {
	//设置好账号的ACCESS_KEY和SECRET_KEY
	static String accessKey = "Nffg8yFOQYDOPZnw_PV5dOfBU50RCP8HhUur63Ft";
	//这两个登录七牛 账号里面可以找到
	static String secretKey = "_KURs4mChIxVTMYDhGNZtHlh7UljHQzjVBqf4fKl";
	//要上传的空间
	static String bucket = "2020-wxapp-qiniu";
	//对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）
	
	
	//简单上传
	public static String  upload(String localFilePath,String key) throws IOException{
 
      Configuration cfg = new Configuration(Zone.zone0());
      //...其他参数参考类注释
      UploadManager uploadManager = new UploadManager(cfg);
      //...生成上传凭证，然后准备上传
      //如果是Windows情况下，格式是 D:\\qiniu\\test.png
      //默认不指定key的情况下，以文件内容的hash值作为文件名
      Auth auth = Auth.create(accessKey, secretKey);
      String upToken = auth.uploadToken(bucket);
      try {
          Response response = uploadManager.put(localFilePath, key, upToken);
          //解析上传成功的结果
          DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
          System.out.println(putRet.key);
          System.out.println(putRet.hash);
          return putRet.key;
      } catch (QiniuException ex) {
          Response r = ex.response;
          System.err.println(r.toString());
          try {
              System.err.println(r.bodyString());
          } catch (QiniuException ex2) {
              //ignore
          }
      }
	return null;
 
    }
	
	//覆盖上传
	public static String coverUpload(String localFilePath,String key) throws IOException{
		Configuration cfg = new Configuration(Zone.zone0());
	      //...其他参数参考类注释
	      UploadManager uploadManager = new UploadManager(cfg);
	      //...生成上传凭证，然后准备上传
	      //如果是Windows情况下，格式是 D:\\qiniu\\test.png
	      //默认不指定key的情况下，以文件内容的hash值作为文件名
	      Auth auth = Auth.create(accessKey, secretKey);
	      String upToken = auth.uploadToken(bucket, key);
	      try {
	          Response response = uploadManager.put(localFilePath, key, upToken);
	          //解析上传成功的结果
	          DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
	          System.out.println(putRet.key);
	          System.out.println(putRet.hash);
	          return putRet.key;
	      } catch (QiniuException ex) {
	          Response r = ex.response;
	          System.err.println(r.toString());
	          try {
	              System.err.println(r.bodyString());
	          } catch (QiniuException ex2) {
	              //ignore
	          }
	      }
		return null;
	}
	
	public static void main(String[] args) {
	    try {
	    	String fileName = coverUpload("E:\\File\\外国小说\\百年孤独\\第一章.txt","foreign/bngd/1000.txt");
	    	System.out.println(fileName);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}