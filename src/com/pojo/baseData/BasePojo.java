package com.pojo.baseData;

import java.util.ArrayList;
import java.util.List;

public class BasePojo<T,S> {
	private boolean success;	// 是否成功
	private String msg;			// 返回信息
	private List<T> list = new ArrayList<>();
	private S data;
	
	public BasePojo(String msg, boolean success, S data, List<T> list) {
		this.msg = msg;
		this.success = success;
		this.list = list;
		this.data = data;
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

	public S getData() {
		return data;
	}

	public void setData(S data) {
		this.data = data;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	
}
