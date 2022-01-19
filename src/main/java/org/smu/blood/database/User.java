package org.smu.blood.database;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Spring Data MongoDB에서 User 클래스를 자동으로 User 이름의 collection에 매핑해서 
 * @Document annotation 사용하지 않아도 상관없음. 
 */
@Document(collection="User")
public class User {
	@Id
	private String userId; // _id로 지정
	private String password;
	private String nickname;
	
	public User() {}
	
	public User(String userId, String password, String nickname) {
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
	}
	public String getuserId() {
		return userId;
	}
	public String getpassword() {
		return password;
	}
	public String getnickname() {
		return nickname;
	}
	public String toString() {
		return String.format("User[userId:%s, password: %s, nickname: %s]", userId, password, nickname);
	}
}