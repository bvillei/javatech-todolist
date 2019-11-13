package hu.bvillei.todolist;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

	private final String USER_NAME_ALEX = "alex";

	@Autowired
	private UserRepository userRepository;

	@After
	public void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	public void givenEmptyDBWhenFindByUserNameThenReturnNull() {
		assertThat(userRepository.findByUsername(USER_NAME_ALEX)).isNull();
	}

	@Test
	public void whenFindByUserNameThenReturnUser() {
		// given
		User user = new User(USER_NAME_ALEX, "password", "alex@a.com", RoleType.ROLE_USER);
		userRepository.save(user);

		// when
		User found = userRepository.findByUsername(user.getUsername());

		// then
		assertThat(found.getUsername()).isEqualTo(user.getUsername());
	}

}