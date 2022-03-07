package org.smu.blood.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FCMTokenRepository extends MongoRepository<FCMToken, String>, CrudRepository<FCMToken, String> {
}