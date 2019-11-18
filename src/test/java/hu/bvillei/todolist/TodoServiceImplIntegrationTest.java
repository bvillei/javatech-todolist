package hu.bvillei.todolist;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import hu.bvillei.todolist.model.RoleType;
import hu.bvillei.todolist.model.User;
import hu.bvillei.todolist.repository.TodoRepository;
import hu.bvillei.todolist.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoServiceImplIntegrationTest {

	private final String TEST_USER_USERNAME = "testUserUsername";
	private final String TEST_USER_PASSWORD = "testUserPassword";
	private final String TEST_ADMIN_USERNAME = "testAdminPassword";
	private final String TEST_ADMIN_PASSWORD = "testAdminPassword";
	private final String TEST_TASK = "Clean the house";

	private static HtmlUnitDriver browser;

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate rest;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TodoRepository todoRepository;

	@BeforeClass
	public static void setup() {
		browser = new HtmlUnitDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void closeBrowser() {
		browser.quit();
	}

	@Test
	public void testGetTodosForLoggedInUser() throws Exception {
		userRepository.save(new User(TEST_USER_USERNAME, TEST_USER_PASSWORD, "testuser@a.com", RoleType.ROLE_USER));
		browser.get(homePageUrl());
		assertEquals(loginPageUrl(), browser.getCurrentUrl());
		doLoginWithInvalidData("invalid", "data");
		doLogin(TEST_USER_USERNAME, TEST_USER_PASSWORD);
		assertTodoListPageElements(userRepository.findByUsername(TEST_USER_USERNAME));
		clickCreateNewTask();
		createNewTodo(TEST_TASK);
		assertEquals(listPageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	@Test
	public void testGetTodosForLoggedInAdmin() throws Exception {
		userRepository.save(new User(TEST_ADMIN_USERNAME, TEST_ADMIN_PASSWORD, "testadmin@a.com", RoleType.ROLE_ADMIN));
		browser.get(homePageUrl());
		assertEquals(loginPageUrl(), browser.getCurrentUrl());
		doLogin(TEST_ADMIN_USERNAME, TEST_ADMIN_PASSWORD);
		assertTodoListPageElements(userRepository.findByUsername(TEST_ADMIN_USERNAME));
		clickCreateNewTask();
		createNewTodo(TEST_TASK);
		assertEquals(listPageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	private void assertTodoListPageElements(User user) {
		assertEquals(homePageUrl(), browser.getCurrentUrl());

		String h1Text = browser.findElementByTagName("h1").getText();
		assertEquals("Hello " + user.getUsername() + "!", h1Text);

		int numberOfTableRows = browser.findElements(By.xpath("//table[@class='table']/tbody/tr")).size();
		if (user.getRole() == RoleType.ROLE_ADMIN) {
			assertEquals(numberOfTableRows, todoRepository.findAll().size());
		} else {
			assertEquals(numberOfTableRows, todoRepository.findTodosByUsername(user.getUsername()).size());
		}

		WebElement newTaskButton = browser.findElementById("newTask");
		assertEquals("New task", newTaskButton.getText());
		assertEquals(editPageUrl(), newTaskButton.getAttribute("href"));

		WebElement ExportButton = browser.findElementById("excel-report");
		assertEquals("Export", ExportButton.getText());
		assertEquals(ExcelReportUrl(), ExportButton.getAttribute("href"));

		WebElement LogOutButton = browser.findElementById("logout-submit");
		assertEquals("Sign Out", LogOutButton.getAttribute("value"));
		assertEquals("submit", LogOutButton.getAttribute("type"));
	}

	private void createNewTodo(String task) {
		assertEquals(editPageUrl(), browser.getCurrentUrl());

		String h1Text = browser.findElementByTagName("h1").getText();
		assertEquals("Todo Edit", h1Text);

		browser.findElementByCssSelector("input#task").sendKeys(task);
		browser.findElementByCssSelector("form#editForm").submit();
	}

	private void doLoginWithInvalidData(String username, String password) {
		doLogin(username, password);
		assertEquals(loginErrorPageUrl(), browser.getCurrentUrl());
		String divErrorMessage = browser.findElementByTagName("div").getText();
		assertEquals("Invalid username and password.", divErrorMessage);
	}

	private void doLogin(String username, String password) {
		browser.findElementByName("username").sendKeys(username);
		browser.findElementByName("password").sendKeys(password);
		browser.findElementByCssSelector("form#loginForm").submit();
	}

	private void doLogout() {
		WebElement logoutForm = browser.findElementByCssSelector("form#logoutForm");
		if (logoutForm != null) {
			logoutForm.submit();
		}
	}

	private void clickCreateNewTask() {
		assertEquals(homePageUrl(), browser.getCurrentUrl());
		browser.findElementByCssSelector("a[id='newTask']").click();
	}

	//
	// URL helper methods
	//
	private String loginPageUrl() {
		return homePageUrl() + "login";
	}

	private String loginErrorPageUrl() {
		return homePageUrl() + "login?error";
	}

	private String homePageUrl() {
		return "http://localhost:" + port + "/";
	}

	private String listPageUrl() {
		return "http://localhost:" + port + "/todo-list";
	}

	private String editPageUrl() {
		return "http://localhost:" + port + "/todo-edit";
	}

	private String ExcelReportUrl() {
		return "http://localhost:" + port + "/todo/excel-report";
	}

}
