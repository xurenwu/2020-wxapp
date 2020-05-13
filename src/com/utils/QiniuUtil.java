package com.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

public class QiniuUtil {

	// ����ţע����õ�accessKey��secretKey����Ϊ�Լ��ģ�
	private static String accessKey = "Nffg8yFOQYDOPZnw_PV5dOfBU50RCP8HhUur63Ft";
	private static String secretKey = "_KURs4mChIxVTMYDhGNZtHlh7UljHQzjVBqf4fKl";
	private static String bucket = "xurrenwu-web-upload"; // ��ţ�ռ�������Ϊ�Լ��ģ�

	// ��ȡ����ͬ���ļ����ϴ�ƾ֤
	public static String getToken(String key) {
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket, key, 3600, null, true); // key���ϴ�����ţ���ļ���������key�����ɸ�����ͬkey�ļ����ϴ�Token
		return upToken;
	}

	// ɾ����ţ��ָ��key���ļ�
	public static void delFile(String key) {
		// ����һ����ָ��Zone�����������
		Configuration cfg = new Configuration(Zone.zone0());
		Auth auth = Auth.create(accessKey, secretKey);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		try {
			bucketManager.delete(bucket, key);
		} catch (QiniuException ex) {
			// ��������쳣��˵��ɾ��ʧ��
			System.err.println(ex.code());
			System.err.println(ex.response.toString());
		}
	}
}
