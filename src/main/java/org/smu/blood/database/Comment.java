package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Comment")
public class Comment {
	@Id private int commentId; // unique comment order in review
	private int reviewId; // review where comment belongs to
	private String userId;
	private String nickname;
	private String time;
	private String comment;
	
	Comment(){}
	public Comment(int commentId, int reviewId, String userId, String nickname, String time, String comment) {
		this.commentId = commentId;
		this.reviewId = reviewId;
		this.userId = userId;
		this.nickname = nickname;
		this.time = time;
		this.comment = comment;
	}

	public void setCommentId(int value) { commentId = value; }
	public void setReviewId(int value) { reviewId = value; }
	public void setTime(String value) {time = value; }
	public void setComment(String value) { comment = value; }
	public void setUserId(String value){ userId = value; }

	public int getCommentId() { return commentId; }
	public int getReviewId() { return reviewId; }
	public String getNickname() { return nickname; }
	public String getTime() { return time; }
	public String getComment() { return comment; }
	public String getUserId(){ return userId; }
	
	public String toString() { return String.format("Comment[commentId: %d, reviewId: %d, userId: %s, nickname: %s, time: %s, comment: %s]", commentId, reviewId, userId, nickname, time, comment); }
}
