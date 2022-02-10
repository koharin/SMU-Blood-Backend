package org.smu.blood.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Comment;
import org.smu.blood.database.CommentRepository;
import org.smu.blood.database.Review;
import org.smu.blood.database.ReviewRepository;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableMongoRepositories(basePackages="org.smu.blood")
@RestController
public class ReviewController {
	@Autowired
	UserRepository repository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	CommentRepository commentRepository;
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
	
	// get only my review list
	@GetMapping("review/myList")
	public List<Review> myReviewList(@RequestHeader String nickname){
		System.out.println("[+] Get all my reviews from Android");
			
		List<Review> mylist = reviewRepository.findByNickname(nickname);
		for(int i=0; i<mylist.size(); i++) System.out.println("review["+i+"]: " + mylist.get(i).toString());
		return mylist;
	}
		
	// add comment to review
	@PostMapping(value="review/addComment")
	public boolean addComment(@RequestHeader String token, @RequestBody HashMap<String, String> requestInfo) {
		System.out.println("[+] save review comment from Android");
			
		if(jwtService.checkTokenExp(token)) {
			Review review = reviewRepository.findByNicknameAndWriteTime(requestInfo.get("reviewNickname").toString(), requestInfo.get("reviewTime").toString());
			
			if(review != null) { 
				System.out.println("[+] get editing review: " + review.toString());
				Comment commentInfo = new Comment((int)commentRepository.count(), review.getReviewId(), requestInfo.get("commentNickname"), requestInfo.get("commentTime"), requestInfo.get("comment"));
				System.out.println("[+] Add Comment: " + commentInfo.toString());
				
				// save review comment
				commentRepository.insert(commentInfo);
				
				// review document에서 commentCount 업데이트
				review.setCommentCount(commentRepository.findByReviewId(review.getReviewId()).size());
				reviewRepository.save(review);
				System.out.println("[+] update review: " + review.toString());
				return true;
			}else { // review 없는 경우
				return false;
			}
		}else {
			System.out.println("[-] Invalid token");
			return false;
		}
	}
	
	// get comment list of review
	@PostMapping("review/commentList")
	public List<Comment> commentList(@RequestBody HashMap<String,String> reviewInfo){
		System.out.println("[+] Get all reviews from Android");
		// find review document by editing review's nickname and writeTime
		Review review = reviewRepository.findByNicknameAndWriteTime(reviewInfo.get("reviewNickname"), reviewInfo.get("reviewTime"));
		
		List<Comment> list = commentRepository.findByReviewId(review.getReviewId());
		for(int i=0; i<list.size(); i++) System.out.println("Comment["+i+"]: " + list.get(i).toString());
		return list;
	}
}
