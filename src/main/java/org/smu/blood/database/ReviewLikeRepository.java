package org.smu.blood.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends MongoRepository<ReviewLike, String>, CrudRepository<ReviewLike, String>{
	ReviewLike findByReviewIdAndUserId(int reviewId, String userId);
}
