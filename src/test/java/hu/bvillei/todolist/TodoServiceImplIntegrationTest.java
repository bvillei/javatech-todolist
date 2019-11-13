package hu.bvillei.todolist;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoServiceImplIntegrationTest {

	private final String TEST_USERNAME = "user1";
	private final String TEST_PASSWORD = "user1";
	private final String TEST_TASK = "Clean the house";

	private static HtmlUnitDriver browser;

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate rest;

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
		browser.get(homePageUrl());
		assertEquals(loginPageUrl(), browser.getCurrentUrl());
		doLoginWithInvalidData("invalid", "data");
		doLogin(TEST_USERNAME, TEST_PASSWORD);
		assertTodoListPageElements(TEST_USERNAME);
		clickCreateNewTask();
		createNewTodo(TEST_TASK);
		assertEquals(listPageUrl(), browser.getCurrentUrl());
		doLogout();
	}

	private void assertTodoListPageElements(String username) {
		assertEquals(homePageUrl(), browser.getCurrentUrl());

		String h1Text = browser.findElementByTagName("h1").getText();
		assertEquals("Hello " + username + "!", h1Text);
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

}
