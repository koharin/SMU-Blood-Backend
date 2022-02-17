package org.smu.blood.controller;


import java.util.HashMap;
import java.util.List;

import org.smu.blood.api.JWTService;
import org.smu.blood.database.Comment;
import org.smu.blood.database.CommentRepository;
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
	CommentRepository commentRepository;
	@Autowired
	JWTService jwtService;
	
	// get my nickname
	@GetMapping(value="review/myNickname", produces="application/json; charset=utf8")
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
		int id;
		System.out.println("[+] Post user review from Android");
		System.out.println("[+] token: " + token);
		
		// token 유효성 검증
		if(jwtService.checkTokenExp(token)) {
			// token에서 사용자 id 가져오기
			String userId = jwtService.getClaim(token).get("id").toString();
			
			// User document에서 nickname 가져오기
			String userNickname = repository.findById(userId).get().getNickname();
			
			// reviewId에서 DuplicationKey exception 방지 위해 reviewId 설정, 나중에 더 좋은 방법으로 수정 필요
			id = (int)reviewRepository.count()+1;
			while(reviewRepository.findByReviewId(id) != null) id += 1;
			review.setReviewId(id);
			review.setId(userId);
			review.setNickname(userNickname);
			System.out.println(review);
			
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
				System.out.println("[+] get editing review: " + review);
				review.setTitle(editInfo.get("editTitle"));
				review.setContents(editInfo.get("editContent"));
				review.setWriteTime(editInfo.get("editTime"));
				
				System.out.println("[+] after editing review: " + review);
				
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
				System.out.println("[+] get editing review: " + review);
				
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
				System.out.println("[+] get editing review: " + review);
				// commentId에서 DuplicationKey exception 방지 위해 commentId 설정, 나중에 더 좋은 방법으로 수정 필요
				int id = (int)commentRepository.count()+1;
				while(commentRepository.findByCommentId(id) != null) id += 1;
				Comment commentInfo = new Comment(id, review.getReviewId(), requestInfo.get("commentNickname"), requestInfo.get("commentTime"), requestInfo.get("comment"));
				System.out.println("[+] Add Comment: " + commentInfo);
				
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
	
	// edit comment
	@PostMapping("review/editComment")
	public boolean editComment(@RequestHeader String token, @RequestBody HashMap<String,String> editInfo) {
		System.out.println("[+] edit comment from Android");
		
		if(jwtService.checkTokenExp(token)) {
			//Comment comment = commentRepository.findByNicknameAndTime(editInfo.get("commentNickname").toString(), editInfo.get("writeTime").toString());
			Comment comment = commentRepository.findByCommentId(Integer.parseInt(editInfo.get("commentId")));
			
			if(comment != null) {
				System.out.println("[+] "+ comment);
				comment.setComment(editInfo.get("editComment"));
				comment.setTime(editInfo.get("editTime"));
				// update comment document
				commentRepository.save(comment);
				System.out.println("[+] updated comment: " + comment);
				return true;
			}
		}
		System.out.println("[-] update failed");
		return false;
		
	}
	
	// delete comment
	@PostMapping("review/deleteComment")
	public boolean deleteComment(@RequestHeader String token, @RequestBody HashMap<String,String> deleteInfo) {
		System.out.println("[+] delete comment request from Android");
		
		if(jwtService.checkTokenExp(token)) {
			//Comment comment = commentRepository.findByNicknameAndTime(deleteInfo.get("commentNickname").toString(), deleteInfo.get("commentTime").toString());
			Comment comment = commentRepository.findByCommentId(Integer.parseInt(deleteInfo.get("commentId")));
			
			if(comment != null) {
				System.out.println("[+] "+ comment);
				int reviewId = comment.getReviewId();
				
				// delete comment document
				commentRepository.delete(comment);
				System.out.println("[+] comment deleted" );
				
				// decrease commentCount in review document and update review document
				Review review = reviewRepository.findByReviewId(reviewId);
				System.out.println("[+] " + review);
				review.setCommentCount(review.getCommentCount()-1);
				reviewRepository.save(review);
				System.out.println("[+] updated review: " + review);
				return true;
			}
		}
		System.out.println("[-] comment deletion failed");
		return false;
	}
	
	// heart event
	@PostMapping("review/heart")
	public boolean heartEvent(@RequestHeader String token, @RequestBody HashMap<String,String> reviewInfo) {
System.out.println("[+] heart event request from Android");
		
		if(jwtService.checkTokenExp(token)) {
			Review review = reviewRepository.findByNicknameAndWriteTime(reviewInfo.get("reviewNickname"), reviewInfo.get("reviewTime"));
			if(review != null) {
				System.out.println("[+] " + review);
				
				// set new heart num
				review.setLikeNum(Integer.parseInt(reviewInfo.get("reviewHeart")));
				System.out.println("[+] Updated review: " + review);
				
				// save change in review document
				reviewRepository.save(review);
				System.out.println("[+] review updated with changed heartNum");
				return true;
			}
		}
		System.out.println("[+] heartNum change failed");
		return false;
		
	}
}
