package org.smu.blood.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FCMTokenRepository extends MongoRepository<FCMToken, String>, CrudRepository<FCMToken, String> {
    Optional<FCMToken> findByUserId(String id);
}
