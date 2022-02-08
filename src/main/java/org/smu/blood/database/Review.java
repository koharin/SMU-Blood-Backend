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
	
	public Review() {}
	public Review(int reviewId, String userId, String nickname, String title, String writeTime, String contents, int likeNum) {
		this.reviewId = reviewId;
		this.userId = userId;
		this.nickname = nickname;
		this.title = title;
		this.contents = contents;
		this.writeTime = writeTime;
		this.likeNum = likeNum;
	}
	public int getReviewId() { return reviewId; }
	public String getId() { return userId; }
	public String getNickname() { return nickname; }
	public String getTitle() { return title; }
	public String getContents() { return contents; }
	public String getWriteTime() { return writeTime; }
	public int getLikeNum() { return likeNum; }
	
	public void setReviewId(int value) { this.reviewId = value; }
	public void setId(String value) { this.userId = value; }
	public void setNickname(String value) { this.nickname = value; }
	public void setTitle(String value) { this.title = value; }
	public void setWriteTime(String value) { this.writeTime = value; }
	public void setContents(String value) { this.contents = value; }
	public void setLikeNum(int value) { this.likeNum = value; }
	
	public String toString() {
		return String.format("Review[reviewId: %d, userId: %s, nickname: %s, title: %s, writeTime: %s, contents: %s, likeNum: %d]", reviewId, userId, nickname, title, writeTime, contents, likeNum);
	}
}
