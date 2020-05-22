package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pojo.comment.Comment;

public class CommentDAO {

	private Connection conn = null;
	private PreparedStatement pst = null;
	
	// 定义构造函数，实例化时完成连接的注入
	public CommentDAO(Connection conn){
		super();
		this.conn = conn;
	}
	
	/**
	 * 插入评论到数据库中
	 * @param userId
	 * @param bookId
	 * @param content
	 * @param commentDate
	 * @return
	 */
	public boolean insertComment(int userId,int bookId,String content,Date commentDate) {
		Timestamp datetime = new Timestamp(commentDate.getTime());
		String sql = "insert into comment(userId,bookId,commentContent, commentTime) values(?,?,?,?)";
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, bookId);
			pst.setString(3, content);
			pst.setTimestamp(4, datetime);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 将数据中某一条评论删除
	 * @param commentId
	 * @return
	 */
	public boolean deleteComment(int commentId) {
		String sql = "delete from comment where commentId=?";
		try{
			pst = conn.prepareStatement(sql);
			pst.setInt(1, commentId);
			pst.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Comment> selectComment(int bookId) {
		String sql = "select * from comment where bookId=?";
		List<Comment> commentList = new ArrayList<>();
		try{
			pst = conn.prepareStatement(sql);
			pst.setInt(1, bookId);
			ResultSet rst = pst.executeQuery();
			while(rst.next()) {
				Comment comment = new Comment();
				comment.setBookId(rst.getInt("bookId"));
				comment.setCommentId(rst.getInt("commentId"));
				comment.setUserId(rst.getInt("userId"));
				comment.setContent(rst.getString("commentContent"));
				comment.setCommentTime(rst.getTimestamp("commentTime"));
				commentList.add(comment);
			}
			return commentList;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
