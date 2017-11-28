package com.ccblife.jira;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserTest {
	private WebDriver driver;
	private String baseUrl;
	private String adminMenuId = "system-admin-menu";
	private String adminUserId = "admin_users_menu";
	private String authenticatePasswordId = "login-form-authenticatePassword";
	
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("-user-data-dir=C:/Users/P000801199/AppData/Local/Google/Chrome/User Data/");
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		baseUrl = "http://10.100.17.80:8080/login.jsp";
		driver.get(baseUrl);
	}
	@Test
	public void DisableUser() {
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys("zhijun.yang");
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys("1");
		driver.findElement(By.id("login-form-remember-me")).click();
		driver.findElement(By.id("login-form-submit")).click();

		WebDriverWait wait = new WebDriverWait(driver, 6);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(adminMenuId)));
		driver.findElement(By.id(adminMenuId)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(adminUserId)));
		driver.findElement(By.id(adminUserId)).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(authenticatePasswordId)));
		driver.findElement(By.id(authenticatePasswordId)).sendKeys("1");
		
//		if (){
//			
//		}
//		else{
//			
//		}
//		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(adminUserId)));
		driver.findElement(By.id(adminUserId)).click();

	}
	@After
	public void tearDown() throws Exception {
//		 driver.quit();
	}


}
