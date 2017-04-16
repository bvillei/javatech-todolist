package hu.bvillei.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public Todo save(String username, Todo todo) {
		User user = userRepository.findByUsername(username);
		todo.setUser(user);
		return todoRepository.save(todo);
	}

	@Override
	public void delete(Integer todoId) {
		Todo todo = todoRepository.findOne(todoId);
		todoRepository.delete(todoId);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		String role = auth.getAuthorities().iterator().next().getAuthority();
		
		if (!username.equals(todo.getUser().getUsername()) && RoleType.ROLE_ADMIN.name().equals(role)) {
			notificationService.sendNotification(todo.getUser().getEmail(),
					"Todo item deleted", "Task '" + todo.getTask() + "' has been deleted by admin.");
		}
	}
}
