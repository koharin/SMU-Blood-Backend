package org.smu.blood.controller;


import java.util.List;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.Review;
import org.smu.blood.database.ReviewRepository;
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
public class ReviewController {
	@Autowired
	UserRepository repository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	JWTService jwtService;
	
	// get my nickname
	@GetMapping("review/myNickname")
	public String getMyId(@RequestHeader String token) {
		System.out.println("[+] Get my nickname from Android");
		System.out.println("[+] token: " + token);
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] userId from token: " + userId);
			String userNickname = repository.findById(userId).get().getNickname();
			System.out.println("[+] userNickname: " + userNickname);
			return userNickname;
		}else {
			System.out.println("[-] Invalid token");
			return null;
		}
	}
	// 글쓰기 등록
	@PostMapping("review/write")
	public Review reviewWrite(@RequestHeader String token, @RequestBody Review review) {
		System.out.println("[+] Post user review from Android");
		System.out.println("[+] token: " + token);
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			// User document에서 nickname 가져오기
			String userNickname = repository.findById(userId).get().getNickname();
			review.setReviewId((int) reviewRepository.count());
			review.setId(userId);
			review.setNickname(userNickname);
			System.out.println(review.toString());
			// Review Collection에 사용자 글 document 넣기
			reviewRepository.insert(review);
			return review;
		}else {
			System.out.println("[-] Invalid token");
			return null;
		}
	}
	// 모든 후기 가져오기
	@GetMapping("review/list")
	public List<Review> reviewList(){
		System.out.println("[+] Get all reviews from Android");
		
		List<Review> list = reviewRepository.findAll();
		for(int i=0; i<list.size(); i++) System.out.println("review["+i+"]: " + list.get(i).toString());
		return list;
	}
}
