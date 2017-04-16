package hu.bvillei.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.bvillei.todolist.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
}
