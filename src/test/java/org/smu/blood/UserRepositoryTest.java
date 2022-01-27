package org.smu.blood;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
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
public class UserRepositoryTest {
	@Autowired
	private UserRepository repository;
	
	@Test
	public void existsByNicknameTest() {
		User createUser = repository.insert(new User("asdf@naver.com", "qwe123", "asdf", 1, false));
		Optional<User> result = repository.findByNickname(createUser.getNickname());
		assertEquals(result.equals(Optional.empty()), false);
	}
	
}
