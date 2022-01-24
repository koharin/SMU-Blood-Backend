package org.smu.blood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import org.smu.blood.database.UserRepository;
import org.smu.blood.database.User;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Controller
public class UserController {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@RequestMapping("save")
	public String save(@ModelAttribute("user") User user, Model model) {
		mongoTemplate.insert(user, "User");
		//repository.save(user);
		//User findUser = repository.findById(user.getuserId()).get();
		User findUser = mongoTemplate.findOne(query(where("userId").is(user.getid())), User.class);
		model.addAttribute("finduser", findUser);
		return "save";
	}
	
	@PostMapping("signIn")
	@ResponseBody
	public String signInResponse(@RequestBody User user) {

		System.out.println("Connection from Android");
		System.out.println("id: " + user.getid() + ", pw: " + user.getpassword());
		
		return "1";
	}
	
	// 회원가입
	@PostMapping("signUp")
	@ResponseBody
	public String signUpResponse(@RequestBody User user) {

		System.out.println("[+] Connection from Android");
		System.out.println("[+] " + user.toString());
		System.out.println("[+] Create user document in User collection");
		mongoTemplate.insert(user, "User");
		return "1";
	}
	
	
}
