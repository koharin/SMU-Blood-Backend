package org.smu.blood.controller;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.FCMToken;
import org.smu.blood.database.FCMTokenRepository;
import org.smu.blood.database.User;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories(basePackages="org.smu.blood")
@RestController
public class SignInController {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FCMTokenRepository fcmTokenRepository;
	@Autowired
	JWTService jwtService;
	
	// 토큰 유효성 검증
	@GetMapping("signIn/tokenValid")
	public boolean tokenValid(@RequestHeader String token) {
		System.out.println("[+] Check token validation from Android");
		System.out.println("[+] token: " + token);
		
		if(jwtService.checkTokenExp(token)) { 
			System.out.println("[+] Valid token");
			return true; 
		}
		else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
	
	// 로그인
	@PostMapping("signIn/general")
	public User signInAuth(@RequestBody HashMap<String,String> loginInfo, HttpServletResponse response) {
		System.out.println("[+] Login authentication from Android");
		
		System.out.println("[+] id: " + loginInfo.get("id") + ", password: " + loginInfo.get("password") + ", AutoLogin: " + loginInfo.get("AutoLogin"));
		List<User> list = mongoTemplate.find(new Query(new Criteria("_id").is(loginInfo.get("id")).and("password").is(loginInfo.get("password"))), User.class, "User");
		if(list.size() > 0) {
			
			System.out.println("[+] Login Success");
			System.out.println(list.get(0).toString());
			// JWT Token 생성
			String token = jwtService.createToken(list.get(0), loginInfo.get("AutoLogin"));
			System.out.println("token: " + token);
			// HTTP 헤더에 token 넢기
			response.setHeader("jwt-token", token); 
		
			return list.get(0);
		}else {
			System.out.println("[+] Login Failed");
			return null; 
		}
	}
	
	// 로그인 (구글 연동 시)
	@PostMapping("signIn/google")
	public User gSignInAuth(@RequestBody HashMap<String,String> loginInfo, HttpServletResponse response) {
		System.out.println("[+] Login authentication(google) from Android");
		
		if(!userRepository.findById(loginInfo.get("id")).isPresent()) {
			System.out.println("[+] Login Failed");
			return null;
		}else {
			User user = userRepository.findById(loginInfo.get("id")).get();
			System.out.println("[+] " + user);
			// 앱 내 자원 사용 위한 token 발급
			String token = jwtService.createToken(user, loginInfo.get("AutoLogin"));
			System.out.println("token: " + token);
			// HTTP 헤더에 token 넢기
			response.setHeader("jwt-token", token); 
			
			return user;
		}
	}
	// save FCM Token
	@PostMapping("fcm/saveToken")
	public int saveFCMToken(@RequestHeader String token, @RequestBody String fcmToken){
		System.out.println("[+] Save FCM token request from Android");
		fcmToken = fcmToken.substring(1, fcmToken.length()-1);
		System.out.println("[+] token: " + token + "FCM token: " + fcmToken);

		if(jwtService.checkTokenExp(token)){
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId: " + userId);

			FCMToken newToken = new FCMToken(userId, fcmToken);
			System.out.println("[+] FCM Token: " + newToken);
			fcmTokenRepository.save(newToken);
			return 200;
		}else{
			System.out.println("[+] Invalid token");
		}
		return 401;
	}
}
