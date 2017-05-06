package hu.bvillei.todolist.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.Todo;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.repository.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {
	
	private final Log logger = LogFactory.getLog(TodoServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private AuthorizationService authService;
	
	@Override
	public Todo save(Todo todo) {
		User user = userRepository.findByUsername(authService.getLoggedInUsername());
		todo.setUser(user);
		return todoRepository.save(todo);
	}

	@Override
	public void delete(Integer todoId) {
		Todo todo = todoRepository.findOne(todoId);
		todoRepository.delete(todoId);
		
		String username = authService.getLoggedInUsername();
		if (authService.hasRole(RoleType.ROLE_ADMIN) && 
				!username.equals(todo.getUser().getUsername())) {
			try {
				notificationService.sendNotification(todo.getUser().getEmail(),
						"Todo item deleted", "Task '" + todo.getTask() + "' has been deleted by " + username + ".");
			} catch (MailException e) {
				logger.error("Failed to send e-mail about task deletion.", e);
			}
		}
	}
	
	@Override
	public List<Todo> getTodos() {
		if (authService.hasRole(RoleType.ROLE_ADMIN)) {
			return todoRepository.findAll();
		} else {
			String username = authService.getLoggedInUsername();
			return todoRepository.findTodosByUsername(username);
		}
	}
	
	@Override
	public Resource getExcelReport() throws IOException {
		List<Todo> todos = getTodos();
		File reportFile = new File("excel-report.xls");
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("Todo");
			int i = 0;
			for (Todo todo : todos) {
				HSSFRow row = sheet.createRow(i++);
				Cell cell = row.createCell(0);
				cell.setCellValue(todo.getTask());
				if (authService.hasRole(RoleType.ROLE_ADMIN)) {
					cell = row.createCell(1);
					cell.setCellValue(todo.getUser().getUsername());
				}
			}
			workbook.write(reportFile);
			return new FileSystemResource(reportFile);
		} finally {
			workbook.close();
		}
	}
}
