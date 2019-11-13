package hu.bvillei.todolist;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.TodoRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TodoRepositoryTest {

	private final String USER_NAME_ALEX = "alex";

	@Autowired
	private TodoRepository todoRepository;

	@Test
	public void whenValidName_thenTodosOfTheUserShouldBeFound() {

		User user = new User(USER_NAME_ALEX, "password", "alex@a.com", RoleType.ROLE_USER);

		Todo todo1 = new Todo("Clean the house", user);
		Todo todo2 = new Todo("Go for a walk", user);

		List<Todo> todoList = new ArrayList<Todo>();
		todoList.add(todo1);
		todoList.add(todo2);

		List<Todo> found = todoRepository.findTodosByUsername(USER_NAME_ALEX);

		assertThat(found.containsAll(todoList));
	}

}