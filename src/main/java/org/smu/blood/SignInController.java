package org.smu.blood;

import java.util.HashMap;
import java.util.List;

import org.smu.blood.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories
@RestController
public class SignInController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@PostMapping("signIn")
	@ResponseBody
	public User signInAuth(@RequestBody HashMap<String,String> loginInfo) {
		
		System.out.println("[+] id: " + loginInfo.get("id") + ", password: " + loginInfo.get("password"));
		List<User> list = mongoTemplate.find(new Query(new Criteria("_id").is(loginInfo.get("id")).and("password").is(loginInfo.get("password"))), User.class, "User");
		if(list.size() > 0) {
			System.out.println("[+] Login Success");
			System.out.println(list.get(0).toString());
			return list.get(0);
		}else {
			System.out.println("[+] Login Failed");
			return null; 
		}
		
	}
}
