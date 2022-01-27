package org.smu.blood.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, CrudRepository<User, String>{
	
	Optional<User> findByNickname(String nickname);
}