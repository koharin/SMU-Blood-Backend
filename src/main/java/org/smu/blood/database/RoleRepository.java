package org.smu.blood.database;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleRepository extends MongoRepository<Role, String>{
	Optional<Role> findByRole(ERole role);
}
