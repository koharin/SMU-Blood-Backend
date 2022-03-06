package org.smu.blood.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, CrudRepository<Notification, String> {
    List<Notification> findByUserId(String id);
    boolean findByUserIdAndNotState(String userId, boolean notState);
    Optional<Notification> findByNoticeId(int noticeId);
}
