package org.smu.blood.database;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review,String>, CrudRepository<Review,String>{
	Review findByNicknameAndWriteTime(String nickname, String writeTime);
	List<Review> findByNickname(String nickname);
	Review findByReviewId(int reviewId);
	Optional<Review> findByReviewIdAndUserId(int reviewId, String userId);
	
	@Query("{'deleteState': false}")
	List<Review> findAll();
}
