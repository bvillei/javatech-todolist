package hu.bvillei.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.bvillei.todolist.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
	
	@Query("select t.task from Todo t where t.user.username=?1")
	List<String> findTasksByUsername(String username);
}
