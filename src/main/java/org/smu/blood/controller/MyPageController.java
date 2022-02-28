package org.smu.blood.controller;

import java.util.HashMap;
import java.util.List;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Apply;
import org.smu.blood.database.ApplyRepository;
import org.smu.blood.database.Request;
import org.smu.blood.database.RequestRepository;
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
	@Autowired
	RequestRepository requestRepository;
	@Autowired
	ApplyRepository applyRepository;
	
	// 내 아이디 가져오기
	@GetMapping("myPage/myId")
	public String getMyId(@RequestHeader String token) {
		System.out.println("[+] Connection from Android");
		System.out.println("[+] token: " + token);
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);
			return userId;
		}else {
			System.out.println("[-] Invalid token");
			return null;
		}
	}
	
	
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

			if(repository.findById(userId).isPresent()){
				User user = repository.findById(userId).get();
				System.out.println("[+] get user info: "+ user);
				return user;
			} else{
				System.out.println("[-] get user info: failed");
			}
		}else {
			System.out.println("[-] Invalid token");
		}
		// if no user or invalid token
		return null;
	}
	// 내 정보 수정
	@PostMapping("myPage/edit")
	public HashMap<String,Integer> setMyData(@RequestHeader String token, @RequestBody HashMap<String,String> editInfo) {
		System.out.println("[+] Connection from Android");
		System.out.println("[+] token: " + token);
		HashMap<String,Integer> result = new HashMap<>();
		
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);

			// 사용자 정보 내 패스워드와 사용자 입력 패스워드 일치하지 않는 경우
			if(!repository.findByIdAndPassword(userId, editInfo.get("current_pw")).isPresent()){
				System.out.println("Incorrect password");
				result.put("wrong_pw", 1);
			}else { // 사용자 정보 내 패스워드와 사용자 입력 패스워드 일치하는 경우
				User user = repository.findByIdAndPassword(userId, editInfo.get("current_pw")).get();
				System.out.println("Current User: "+user);

				// 변경 비밀번호 값 있는 경우
				if(editInfo.containsKey("new_pw")) user.setPassword(editInfo.get("new_pw"));
				// 변경 닉네임 값 있는 경우
				if(editInfo.containsKey("new_nickname")) user.setNickname(editInfo.get("new_nickname"));

				// 새로운 패스워드 또는 닉네임으로 사용자 정보 업데이트
				System.out.println("Update User: "+user);
				repository.save(user);
			}
		}else {
			// Invalid token
			System.out.println("[-] Invalid token");
			result.put("invalid_token",1);
		}
		return result;
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

			if(repository.findById(userId).isPresent()){
				repository.delete(repository.findById(userId).get());

				System.out.println("[+] User Deletion Success");
				return true;
			}else System.out.println("[-] no user info. deletion failed.");
		}else {
			System.out.println("[-] Invalid token");
		}
		// invalid token or no user info to delete
		return false;
	}
	//get my request list
    @GetMapping("myPage/myRequest/myRequestList")
    public List<Request> requestList(@RequestHeader String token){
    	System.out.println("[+] Get my request list");
    	
    	if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            // find request list by userId
            List<Request> requestlist = requestRepository.findByUserId(userId);
            for(int i=0; i<requestlist.size(); i++) System.out.println("[+] Request["+i+"]: " + requestlist.get(i));
            
            return requestlist;
    	}
    	
    	return null;
    }
    
    // get apply list of my request
    @PostMapping("myPage/myRequest/applyList")
    public List<Apply> applyOfMyRequest(@RequestHeader String token, @RequestBody int requestId){
    	System.out.println("[+] Get apply list of my request");
    	
    	if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            List<Apply> applylist = applyRepository.findByRequestId(requestId);
        	for(int i=0; i<applylist.size(); i++) System.out.println("[+] Apply["+i+"]: " + applylist.get(i));
        	
        	return applylist;
    	}
    	return null;
    }
    
    // get my apply list
    @GetMapping("myPage/myApply/myApplyList")
    public List<Apply> applyList(@RequestHeader String token){
    	System.out.println("[+] Get my apply list request from Android");
    	
        if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            // find apply list by userId
            List<Apply> applyList = applyRepository.findByUserId(userId);
            for(int i=0; i<applyList.size(); i++) System.out.println("[+] Apply["+i+"]: " + applyList.get(i));
            
            return applyList;
        }else {
        	return null;
        }
    }
    
    // get request of my apply
    @PostMapping("myPage/myApply/request")
    public Request requestOfMyApply(@RequestHeader String token, @RequestBody int requestId){
    	System.out.println("[+] Get request of my apply request from Android");
    	
    	if(jwtService.checkTokenExp(token)){
            // token에서 userId 가져오기
            String userId = jwtService.getClaim(token).get("id").toString();
            System.out.println("[+] userId from token: " + userId);
            
            Request requestInfo = requestRepository.findByRequestId(requestId);
        	System.out.println("[+] request info of my apply: " + requestInfo.toString());
        	
        	return requestInfo;
    	}
    	return null;
    }
}
