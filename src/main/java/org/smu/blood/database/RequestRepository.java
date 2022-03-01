package org.smu.blood.database;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends MongoRepository<Request, String>, CrudRepository<Request, String> {
	
	Optional<Request> findByRequestId(int requestId);
	List<Request> findByUserId(String userId);
	List<Request> findByOrderByEndDateAsc();
	List<Request> findByOrderByApplicantNumAsc();
}
