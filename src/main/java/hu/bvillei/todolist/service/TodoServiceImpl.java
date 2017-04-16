package hu.bvillei.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Override
	public Todo save(String username, Todo todo) {
		User user = userRepository.findByUsername(username);
		todo.setUser(user);
		return todoRepository.save(todo);
	}
}
