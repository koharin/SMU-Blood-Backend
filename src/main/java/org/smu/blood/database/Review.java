package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Review")
public class Review {
	@Id private int reviewId;
	private String userId;
	private String nickname;
	private String title;
	private String contents;
	private String writeTime;
	private int likeNum;
	private int commentCount;
	private boolean deleteState;
	
	public Review() {}
	public Review(String userId, String nickname, String title, String writeTime, String contents, int likeNum, int commentCount, boolean deleteState) {
		this.userId = userId;
		this.nickname = nickname;
		this.title = title;
		this.contents = contents;
		this.writeTime = writeTime;
		this.likeNum = likeNum;
		this.commentCount = commentCount;
		this.deleteState = deleteState;
	}
	public int getReviewId() { return reviewId; }
	public String getId() { return userId; }
	public String getNickname() { return nickname; }
	public String getTitle() { return title; }
	public String getContents() { return contents; }
	public String getWriteTime() { return writeTime; }
	public int getLikeNum() { return likeNum; }
	public int getCommentCount() { return commentCount; }
	public boolean getDeleteState() { return deleteState; }
	
	public void setReviewId(int value) { reviewId = value; }
	public void setId(String value) { userId = value; }
	public void setNickname(String value) { nickname = value; }
	public void setTitle(String value) { title = value; }
	public void setWriteTime(String value) { writeTime = value; }
	public void setContents(String value) { contents = value; }
	public void setLikeNum(int value) { likeNum = value; }
	public void setCommentCount(int value) { commentCount = value; }
	public void setDeleteState(boolean value) { deleteState = value; }
	
	public String toString() {
		return String.format("Review[reviewId: %d, userId: %s, nickname: %s, title: %s, writeTime: %s, contents: %s, likeNum: %d, commentCount: %d, deleteStaet: %b]", reviewId, userId, nickname, title, writeTime, contents, likeNum, commentCount, deleteState);
	}
}
