package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dao.CommentDAO;
import com.pojo.comment.Comment;
import com.utils.DBUtil;

public class CommentService {
	/**
	 * 评论处理业务层
	 */
	
	public static void main(String []args) {
		System.out.println(getCommentById(20201000).get(0).getCommentTime());
		cancleComment(getCommentById(20201000).get(0).getCommentId());
//		insertComment(20200001,20201000,"徐仁武");
	}
	
	/**
	 * 前端没有传入评论时间的时候调用
	 * @param userId
	 * @param bookId
	 * @param content
	 * @return
	 */
	
	public static boolean insertComment(int userId,int bookId,String content) {
		Date date=new Date();
		Timestamp t = new Timestamp(date.getTime());
		if(insertCommentByuserId(userId,bookId,content,t)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 更根据用户id和评论书籍id来插入书籍评论
	 * @param userId
	 * @param bookId
	 * @param content
	 * @param commentDate
	 * @return
	 */
	public static boolean insertCommentByuserId(int userId,int bookId,String content,Date commentDate) {
		
		Connection conn = DBUtil.getConnection();
		CommentDAO commentDAO = new CommentDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(commentDAO.insertComment(userId, bookId, content, commentDate)) {
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null)
			DBUtil.closeConnection(conn);
		}
		return false;
	}

	/**
	 * 撤销评论(要进行评论的言论安全检测)
	 */
	
	public static boolean cancleComment(int commentId) {
		Connection conn = DBUtil.getConnection();
		CommentDAO commentDAO = new CommentDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(commentDAO.deleteComment(commentId)) {
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null)
			DBUtil.closeConnection(conn);
		}
		return false;
	}
	public static boolean cancleComment(List<Integer> commentIdList) {
		Connection conn = DBUtil.getConnection();
		CommentDAO commentDAO = new CommentDAO(conn);
		try {
			conn.setAutoCommit(false);
			if(commentIdList.size()>0) {
				for(int i=0;i<commentIdList.size();i++) {
					if(commentDAO.deleteComment(commentIdList.get(i))) {
						continue;
					}else {
						return false;
					}
				}
				conn.commit();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null)
				DBUtil.closeConnection(conn);
		}
		return false;
	}
	
	/**
	 * 获取所有的评论内容通过评论id
	 * @param bookId
	 * @return
	 */
	public static List<Comment> getCommentById(int bookId) {
		Connection conn = DBUtil.getConnection();
		CommentDAO commentDAO = new CommentDAO(conn);
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			conn.setAutoCommit(false);
			commentList = commentDAO.selectComment(bookId);
			if(commentList != null) {
				conn.commit();
				return commentList;
			}
		}catch(Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			if(conn != null)
			DBUtil.closeConnection(conn);
		}
		return null;
	}
	
	/**
	 * 获取所有的评论集合
	 * @return
	 */
	public static List<Comment> getCommentList() {
		Connection conn = DBUtil.getConnection();
		CommentDAO commentDAO = new CommentDAO(conn);
		List<Comment> commentList = new ArrayList<Comment>();
		try {
			commentList = commentDAO.selectCommentList();
			if(commentList != null) {
				return commentList;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null)
				DBUtil.closeConnection(conn);
		}
		return null;
	}
}
