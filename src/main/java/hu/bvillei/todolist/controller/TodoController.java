package hu.bvillei.todolist.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.bvillei.todolist.repository.TodoRepository;

@Controller
public class TodoController {
	
	@Autowired
	private TodoRepository todoRepository;

	@RequestMapping(path = {"/", "/todo-list"})
	public String getTodos(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("tasks", todoRepository.findTasksByUsername(username));
		return "todo-list";
	}
		
}
