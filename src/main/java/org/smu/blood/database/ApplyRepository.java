package org.smu.blood.database;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends MongoRepository<Apply, String>, CrudRepository<Apply, String> {
	
	List<Apply> findByUserId(String userId);
	List<Apply> findByRequestId(int requestId);
	Apply findByRequestIdAndUserId(int requestId, String userId);
}
