package com.pojo.baseData;

import java.util.HashMap;
import java.util.Map;

public class BaseMapPojo <E,T>{
	private boolean success;	// 是否成功
	private String msg;			// 返回信息
	private Map<E,T> dataMap = new HashMap<>();
	

	public BaseMapPojo() {
		
	}
	
	
	public BaseMapPojo(boolean success, String msg, Map<E, T> dataMap) {
		super();
		this.success = success;
		this.msg = msg;
		this.dataMap = dataMap;
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

	public Map<E, T> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<E, T> dataMap) {
		this.dataMap = dataMap;
	}
	

}
