package com.ccblife.jira;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserTest {
	private WebDriver driver;
	private String baseUrl;
	
	private String next = "#pagination .aui-nav-next";
	private String projectsOnePage = "#projects .projects-list tr";

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();
//	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		baseUrl = "http://10.100.17.80:8080/login.jsp";
		driver.get(baseUrl);
	}

	@Test
	public void login() {
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys("zhijun.yang");
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys("1");
		driver.findElement(By.id("login-form-remember-me")).click();
		driver.findElement(By.id("login-form-submit")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 1);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("browse_link")));
		
		driver.findElement(By.id("browse_link")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("project_view_all_link_lnk")));

		driver.findElement(By.id("project_view_all_link_lnk")).click();
		
		List <WebElement> projects = driver.findElements(By.cssSelector(projectsOnePage));
		
		
		driver.findElement(By.cssSelector(next)).click();
		driver.findElement(By.cssSelector(next)).click();
		driver.findElement(By.cssSelector(next)).click();
	}
	
	  @After
	  public void tearDown() throws Exception {
//	    driver.quit();
	  }

}
