package org.smu.blood.database;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Role")
public class Role {
	@Id
	private String id;
	private ERole role;
	
	public Role() {}
	public Role(ERole role) { this.role = role; }
	public String getId() { return id; }
	public ERole getRole() { return role; }
}


