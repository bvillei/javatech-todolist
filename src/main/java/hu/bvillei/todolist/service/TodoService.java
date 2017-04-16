package hu.bvillei.todolist.service;

import hu.bvillei.todolist.model.Todo;

public interface TodoService {
	Todo save(String username, Todo todo);
	void delete(Integer todoId);
}
