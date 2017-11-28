package com.ccblife.jira;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectTest {
	private WebDriver driver;
	private String baseUrl;

	private String next = "#pagination .aui-nav-next a";
	private String projectsPerPage = "#projects .projects-list tr";
	private String nameCss = "#projects .projects-list tr td[data-cell-type='name'] a";

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		baseUrl = "http://10.100.17.80:8080/login.jsp";
		driver.get(baseUrl);
	}

	public boolean hasNext() {
		try {
			WebElement nextWebElement = driver.findElement(By.cssSelector(next));
			if ("true".equals(nextWebElement.getAttribute("aria-disabled"))){
				return false;
			}
			else{
				return true;
			}
		} catch (NoSuchElementException e) {
			return true;
		} 
	}

	@Test
	public void login() {
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys("zhijun.yang");
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys("1");
		driver.findElement(By.id("login-form-remember-me")).click();
		driver.findElement(By.id("login-form-submit")).click();

		WebDriverWait wait = new WebDriverWait(driver, 6);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("browse_link")));

		driver.findElement(By.id("browse_link")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("project_view_all_link_lnk")));

		driver.findElement(By.id("project_view_all_link_lnk")).click();
		do {
			List<WebElement> projects = driver.findElements(By.cssSelector(projectsPerPage));
			for (WebElement project : projects) {
				WebElement name = project
						.findElement(By.cssSelector(nameCss));
				System.out.println(name.getText());
			}
			if (hasNext()){
				driver.findElement(By.cssSelector(next)).click();
			}
			else
				break;
		} while (true);
	}

	@After
	public void tearDown() throws Exception {
		 driver.quit();
	}

}
