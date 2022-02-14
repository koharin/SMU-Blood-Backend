package org.smu.blood.database;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String>, CrudRepository<Comment,String>{
	List<Comment> findByReviewId(int reviewId);
	Comment findByNicknameAndTime(String nickname, String time);
	Comment findByCommentId(int commentId);
}
