package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Comment")
public class Comment {
	@Id private int reviewId;
	private String nickname;
	private String time;
	private String comment;
	
	Comment(){}
	public Comment(int reviewId, String nickname, String time, String comment) {
		this.reviewId = reviewId;
		this.nickname = nickname;
		this.time = time;
		this.comment = comment;
	}
}
