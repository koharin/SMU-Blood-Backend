package org.smu.blood.database;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends MongoRepository<Request, String>, CrudRepository<Request, String> {
	Request findByRequestId(int requestId);
	List<Request> findByUserId(String userId);
}
