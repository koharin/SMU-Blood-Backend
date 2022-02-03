package org.smu.blood.database;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Spring Data MongoDB에서 User 클래스를 자동으로 User 이름의 collection에 매핑해서 
 * @Document annotation 사용하지 않아도 상관없음. 
 */
@Document(collection="User")
public class User {
	@Id private String id; // _id로 지정
	private String password;
	private String nickname;
	private int bloodType;
	private boolean rhType;
	
	public User() {}
	
	public User(String id, String password, String nickname, int bloodType, boolean rhType) {
		this.id = id;
		this.password = password;
		this.nickname = nickname;
		this.bloodType = bloodType;
		this.rhType = rhType;
	}
	
	public String getId() { return id; }
	public String getPassword() { return password; }
	public String getNickname() { return nickname; }
	public int getBloodType() { return bloodType; }
	public boolean getRhType() {return rhType; }
	
	public void setPassword(String value) { this.password = value; }
	public void setNickname(String value) { this.nickname = value; }
	
	public String toString() {
		return String.format("User[id:%s, password: %s, nickname: %s, bloodType: %d, rhType: %b]", id, password, nickname, bloodType, rhType);
	}
}