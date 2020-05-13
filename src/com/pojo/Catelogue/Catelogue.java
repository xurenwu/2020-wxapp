package com.pojo.Catelogue;

import java.util.ArrayList;
import java.util.List;

public class Catelogue {
	/**
	 * bookId:书籍id
	 * ChapterName:书籍章节名字集合
	 * ChapterUrl:书籍章节地址url集合
	 */
	
	private int bookId;
	private List<ChapterObject> allChapter = new ArrayList<>();
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public List<ChapterObject> getAllChapter() {
		return allChapter;
	}
	public void setAllChapter(List<ChapterObject> allChapter) {
		this.allChapter = allChapter;
	}
	public void addChapterObject(ChapterObject chapterObject) {
		this.allChapter.add(chapterObject);
	}
	

}
