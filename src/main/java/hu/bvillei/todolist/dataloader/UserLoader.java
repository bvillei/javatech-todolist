package hu.bvillei.todolist.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.UserRepository;

@Component
public class UserLoader implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		userRepository.save(new User("admin", "admin", "admin@a.com", RoleType.ROLE_ADMIN));
		userRepository.save(new User("user1", "user1", "user1@a.com", RoleType.ROLE_USER));
		userRepository.save(new User("user2", "user2", "user2@a.com", RoleType.ROLE_USER));
		
		//userRepository.findAll().stream().forEach(System.out::println);
		for (User user : userRepository.findAll()) {
			System.out.println(user);
		}
	}

}
