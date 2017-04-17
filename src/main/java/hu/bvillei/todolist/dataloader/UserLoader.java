package hu.bvillei.todolist.dataloader;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.repository.UserRepository;

@Component
public class UserLoader implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TodoRepository todoRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		userRepository.save(new User("admin", "admin", "admin@a.com", RoleType.ROLE_ADMIN));
		
		User user1 = new User("user1", "user1", "user1@a.com", RoleType.ROLE_USER);
		user1 = userRepository.save(user1);
		userRepository.save(new User("user2", "user2", "user2@a.com", RoleType.ROLE_USER));
		
		for (User user : userRepository.findAll()) {
			System.out.println(user);
		}
		
		
		todoRepository.save(new Todo("task1", user1));
		todoRepository.save(new Todo("task2", user1));
	}

}
