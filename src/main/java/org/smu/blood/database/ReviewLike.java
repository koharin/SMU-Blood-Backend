package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ReviewLike")
public class ReviewLike {
	@Id private int id;
	private int reviewId;
	private String userId;
	private boolean heartState;
	
	public ReviewLike() {}
	public ReviewLike(int id, int reviewId, String userId, boolean heartState) {
		this.id = id;
		this.userId = userId;
		this.reviewId = reviewId;
		this.heartState = heartState;
	}
	
	public void setId(int value) { id = value; }
	public void setUserId(String value) { userId = value; }
	public void setReviewId(int value) { reviewId = value; }
	public void setHeartState(boolean value) { heartState = value; }
	public int getId() { return id; }
	public String getUserId() { return userId; }
	public int getReviewId() { return reviewId; }
	public boolean getHeartState() { return heartState; }
	
	public String toString() { return String.format("ReviewLike[id: %d, reviewId: %d, userId: %s, heartState: %b", id, reviewId, userId, heartState); }
}
