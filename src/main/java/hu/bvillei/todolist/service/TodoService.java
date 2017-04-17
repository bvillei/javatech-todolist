package hu.bvillei.todolist.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;

import hu.bvillei.todolist.model.Todo;

public interface TodoService {
	Todo save(String username, Todo todo);
	void delete(Integer todoId);
	List<Todo> getTodos(Authentication auth);
	Resource getExcelReport() throws IOException;
}
