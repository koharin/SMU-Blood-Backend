package org.smu.blood.controller;


import java.util.HashMap;
import java.util.List;
import org.smu.blood.api.JWTService;
import org.smu.blood.database.Comment;
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
	// check if review nickname == my nickname
	@PostMapping("review/checkReviewNickname")
	public boolean checkMy(@RequestHeader String token, @RequestBody String reviewNickname) {
		reviewNickname = reviewNickname.replaceAll("\"", "");
		System.out.println("Check review nickname from Android");
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] current id: " + userId);
			String currentNickname = repository.findById(userId).get().getNickname();
			System.out.println("[+] current nickname: " + currentNickname + ", reviewNickname: " + reviewNickname);
			if(currentNickname.equals(reviewNickname)) {
				System.out.println("[+] review writen by me");
				return true;
			}
			System.out.println("[-] review writen by other");
			return false;
		}else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
	// edit my review
	@PostMapping("review/edit")
	public boolean reviewEdit(@RequestHeader String token, @RequestBody HashMap<String,String> editInfo) {
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] current id: " + userId);
			// find review document by editing review's nickname and writeTime
			Review review = reviewRepository.findByNicknameAndWriteTime(editInfo.get("nickname"), editInfo.get("originTime"));
			if(review != null) { 
				System.out.println("[+] get editing review: " + review.toString());
				review.setTitle(editInfo.get("editTitle"));
				review.setContents(editInfo.get("editContent"));
				review.setWriteTime(editInfo.get("editTime"));
				System.out.println("[+] after editing review: " + review.toString());
				// update review document with new title and contents
				reviewRepository.save(review);
				return true;
			}else { // review 없는 경우
				return false;
			}
		}else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
	//delete my review
	@PostMapping("review/delete")
	public boolean reviewDelete(@RequestHeader String token, @RequestBody HashMap<String,String> deleteInfo) {
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			System.out.println("[+] current id: " + userId);
			// find review document by editing review's nickname and writeTime
			Review review = reviewRepository.findByNicknameAndWriteTime(deleteInfo.get("nickname"), deleteInfo.get("writeTime"));
			if(review != null) { 
				System.out.println("[+] get editing review: " + review.toString());
				// delete review document from Review collection
				reviewRepository.delete(review);
				return true;
			}else { // review 없는 경우
				return false;
			}
		}else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
}
