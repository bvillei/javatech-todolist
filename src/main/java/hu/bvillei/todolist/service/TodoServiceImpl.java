package hu.bvillei.todolist.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
	
	@Override
	public List<Todo> getTodos(Authentication auth) {
		if (RoleType.ROLE_ADMIN.name().equals(auth.getAuthorities().iterator().next().getAuthority())) {
			return todoRepository.findAll();
		} else {
			String username = auth.getName();
			return todoRepository.findTodosByUsername(username);
		}
	}
	
	@Override
	public Resource getExcelReport() throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Todo");
		HSSFRow row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("blablabla");
		File f = new File("excel-report.xls");
		workbook.write(f);
		workbook.close();
		return new FileSystemResource(f);
	}
}
