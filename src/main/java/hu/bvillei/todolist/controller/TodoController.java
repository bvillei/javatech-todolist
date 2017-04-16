package hu.bvillei.todolist.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.service.TodoService;

@Controller
public class TodoController {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private TodoService todoService;

	@RequestMapping(path = {"/", "/todo-list"})
	public String getTodos(Model model, Principal principal) {
		String username = principal.getName();
		model.addAttribute("todos", todoRepository.findTodosByUsername(username));
		return "todo-list";
	}
	
	@GetMapping("todo-edit")
	public String todoEdit(Model model){
		model.addAttribute("todo", new Todo());
		return "todo-edit";
	}
	
	@PostMapping("todo-save")
	public String todoSave(@ModelAttribute Todo todo, Principal principal){
		
		todoService.save(principal.getName(), todo);
		return "redirect:/todo-list";
	}
		
}
