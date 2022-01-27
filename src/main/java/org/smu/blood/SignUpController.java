package org.smu.blood;

import java.util.HashMap;
import java.util.Optional;

import org.smu.blood.database.User;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories
@RestController
public class SignUpController{

	@Autowired
	private UserRepository repository;
	
	// 회원가입
	@PostMapping("signUp")
	@ResponseBody
	private HashMap<String, Integer> SignUp(@RequestBody User user){
		HashMap<String,Integer> result = new HashMap<String, Integer>();
		System.out.println("[+] Connection from Android");
		System.out.println("[+] " + user.toString());
		// id 중복 체크
		if(!repository.findById(user.getId()).equals(Optional.empty())) {
			System.out.println("[-] Same ID exists.");
			result.put("sameId", 1);
		}
		// nickname 중복 체크
		if(!repository.findByNickname(user.getNickname()).equals(Optional.empty())) {
			System.out.println("[-] Same Nickname exists.");
			result.put("sameNickname", 1);
		}
		if(!result.containsKey("sameId") && !result.containsKey("sameNickname")) {
			System.out.println("[+] create new user account.");
			repository.insert(user);
			//result.put("sameId", 0);
			//result.put("sameNickname", 0);
			result.put("create", 1);
			//return result;
		}
		if(!result.containsKey("sameId")) result.put("sameId", 0);
		if(!result.containsKey("sameNickname")) result.put("sameNickname", 0);
		if(!result.containsKey("create"))result.put("create", 0);
		return result;
			
		
	}
}