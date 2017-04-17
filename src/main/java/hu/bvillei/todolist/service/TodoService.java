package hu.bvillei.todolist.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

import hu.bvillei.todolist.model.Todo;

public interface TodoService {
	Todo save(Todo todo);
	void delete(Integer todoId);
	List<Todo> getTodos();
	Resource getExcelReport() throws IOException;
}
