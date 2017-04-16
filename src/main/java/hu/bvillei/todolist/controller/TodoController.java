package hu.bvillei.todolist.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.service.TodoService;

@Controller
public class TodoController {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private TodoService todoService;

	@GetMapping(path = {"/", "/todo-list"})
	public String getTodos(Model model, Authentication auth) {
		List<Todo> todos;
		if (RoleType.ROLE_ADMIN.name().equals(auth.getAuthorities().iterator().next().getAuthority())) {
			todos = todoRepository.findAll();
		} else {
			String username = auth.getName();
			todos = todoRepository.findTodosByUsername(username);
		}
		model.addAttribute("todos", todos);
		return "todo-list";
	}
	
	@GetMapping("todo-edit")
	public String todoNew(Model model, @PathVariable(required = false) Integer id){
		model.addAttribute("todo", new Todo());
		return "todo-edit";
	}

	@GetMapping("todo-edit/{id}")
	public String todoEdit(Model model, @PathVariable Integer id){
		Todo todo = todoRepository.findOne(id);
		model.addAttribute("todo", todo);
		return "todo-edit";
	}

	
	@PostMapping("todo-save")
	public String todoSave(@ModelAttribute Todo todo, Principal principal){
		
		todoService.save(principal.getName(), todo);
		return "redirect:/todo-list";
	}
	
	@DeleteMapping("todo-delete")
	public String todoDelete(@ModelAttribute Todo todo){
		
		todoService.delete(todo.getId());
		return "redirect:/todo-list";
	}
		
}
