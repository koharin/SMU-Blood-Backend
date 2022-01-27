package org.smu.blood;

import java.util.List;
import java.util.Optional;

import org.smu.blood.database.User;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;

@EnableMongoRepositories
@RestController
public class SignInController {
	@Autowired
	private UserRepository repository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@PostMapping("signIn")
	@ResponseBody
	public User signInAuth(@RequestParam String id, @RequestParam String password) {
		HttpSession session;
		System.out.println("[+] id: " + id + ", password: " + password);
		List<User> list = mongoTemplate.find(new Query(new Criteria("_id").is(id).and("password").is(password)), User.class, "User");
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
