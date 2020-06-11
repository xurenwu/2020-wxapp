package com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.pojo.Catelogue.ChapterObject;

public class TemporaryFileUtil {
	
	public static void main(String args[]) {
		List<ChapterObject> list = new ArrayList<>();
		ChapterObject obj = new ChapterObject();
		obj.setChapterName("百年孤独");
		obj.setChapterUrl("foreign/bngd/1000.txt");
		list.add(obj);
		list.add(obj);
		list.add(obj);
		list.add(obj);
		try {
			System.out.println(updateFile_chapterUrl("E:\\File\\temporary.txt",list));
			System.out.println(updateFile_chapterName("E:\\File\\temporary.txt",list));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//更新本地临时文件的内容:更新阅读地址文件
	public static boolean updateFile_chapterUrl(String localFile,List<ChapterObject> ChapterList) throws Exception{
		
		File f = new File(localFile);
        if (f.exists()) {
            System.out.print("文件存在");
        } else {
            System.out.print("文件不存在");
            f.createNewFile();// 不存在则创建
        }
        String s1 = new String();
        for(int i=0;i<ChapterList.size();i++) {
        	s1 += ChapterList.get(i).getChapterUrl() + "\n";
        }
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
        output.write(s1);
        output.close();
		
		return true;
	}
	
	//更新本地临时文件的内容:更新章节名文件
	//

	public static boolean updateFile_chapterName(String localFile,List<ChapterObject> ChapterList) throws Exception{
		File f = new File(localFile);
        if (f.exists()) {
            System.out.print("文件存在");
        } else {
            System.out.print("文件不存在");
            f.createNewFile();// 不存在则创建
        }
        String s1 = new String();
        for(int i=0;i<ChapterList.size();i++) {
        	s1 += ChapterList.get(i).getChapterName() + "\n";
        }
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
        output.write(s1);
        output.close();
		
		return true;
	}
}
