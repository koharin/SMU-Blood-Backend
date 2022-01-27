package org.smu.blood;

import static org.junit.Assert.*;
import java.util.Optional;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.smu.blood.database.User;
import org.smu.blood.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@EnableMongoRepositories
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@Rollback(value = true)
public class SignUpTest{
	@Autowired
	private UserRepository repository;
	
	//private static final Logger logger = LoggerFactory.getLogger(SignUpTest.class);
	
	@Test
	@DisplayName("Same id user test")
	public void CheckSameId() {
		repository.insert(new User("koharin@naver.com", "qwe123", "koharin", 1, false));
		
		User newUser = new User("koharin@naver.com", "qwerty", "asdf", 2, true);
		Optional<User> userById = repository.findById(newUser.getId());
		assertEquals(userById.equals(Optional.empty()), false);
		
	}
	@Test
	@DisplayName("Same nickname user test")
	public void CheckSameNickname() {
		repository.insert(new User("qwe@naver.com", "qwe123", "qwe", 1, false));
		
		User newUser = new User("asdf@naver.com", "qwerty", "qwe", 2, true);
		Optional<User> userByNickname = repository.findByNickname(newUser.getNickname());
		assertEquals(userByNickname.equals(Optional.empty()), false);
	}
	
}