package org.smu.blood.api;

import java.lang.reflect.*;
import javax.servlet.http.HttpServletRequest;
import org.smu.blood.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.smu.blood.database.UserRepository;

public class insertTest{
	@Autowired
	UserRepository repository;

	public void insert(User user) {
		repository.save(user);
	}
}
