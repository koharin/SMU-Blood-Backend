package org.smu.blood.controller;

import java.util.HashMap;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.User;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories(basePackages="org.smu.blood")
@RestController
public class MyPageController {
	@Autowired
	UserRepository repository;
	@Autowired
	JWTService jwtService;
	// 내 정보
	@GetMapping("myPage/info")
	public User getMyData(@RequestHeader String token) {
		System.out.println("[+] Connection from Android");
		System.out.println("[+] token: " + token);
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);
			User user = repository.findById(userId).get();
			if(user != null) { System.out.println("[+] get user info: "+ user.toString()); return user; }
			else return null;
		}else {
			System.out.println("[-] Invalid token");
			return null;
		}
	}
	// 내 정보 수정
	@PostMapping("myPage/edit")
	public boolean setMyData(@RequestHeader String token, @RequestBody HashMap<String,String> editInfo) {
		System.out.println("[+] Connection from Android");
		System.out.println("[+] token: " + token);
		
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);
			// 사용자 정보 내 패스워드와 사용자 입력 일치 확인
			User user = repository.findByIdAndPassword(userId, editInfo.get("current_pw")).get();
			if(user != null){
				System.out.println("Current User: "+user.toString());
				// 변경 비밀번호 값 있는 경우
				if(editInfo.containsKey("new_pw")) user.setPassword(editInfo.get("new_pw"));
				// 변경 닉네임 값 있는 경우
				if(editInfo.containsKey("new_nickname")) user.setNickname(editInfo.get("new_nickname"));
				System.out.println("Update User: "+user.toString());
				// 새로운 패스워드 또는 닉네임으로 사용자 정보 업데이트
				repository.save(user);
				return true;
			}else {
				System.out.println("Incorrect password");
				return false;
			}
		}else {
			System.out.println("[-] Invalid token");
			return false;
			
		}
	}
	// 탈퇴
	@GetMapping("myPage/withdraw")
	public boolean setWithdraw(@RequestHeader String token) {
		System.out.println("[+] Connection from Android");
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);
			repository.delete(repository.findById(userId).get());
			System.out.println("[+] User Deletion Success");
			return true;
		}else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
}
