package com.pojo.baseData;

import java.util.ArrayList;
import java.util.List;

/** 封装返回数据的主pojo类 **/
public class BaseListPojo<T> {
	private boolean success;	// 是否成功
	private String msg;			// 返回信息
	private List<T> list = new ArrayList<>(); 
	
	public BaseListPojo(String msg, boolean success, List<T> list) {
		this.msg = msg;
		this.success = success;
		this.list = list;
	}
	
	public BaseListPojo() {
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
}
