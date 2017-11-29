package com.ccblife.jira;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

public class UserTest {
	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl;
	private String loginTimeBaseline= "2017/11/27 01:00";
	private String adminMenuById = "system-admin-menu";
	private String adminUserById = "admin_users_menu";
	private String authenticatePasswordById = "login-form-authenticatePassword";
	private String adminLoginById = "login-form-submit";
	private String nextById = ".pagination .icon-next";
	private String usersPerPageByCss = "#user_browser_table tbody tr";
	private String userNameCss = "#user_browser_table tbody tr td[data-cell-type='username'] a";
	private String userLoginDetailsCss = "#user_browser_table tbody tr td[data-cell-type='login-details']";
	private String userEditPrefixId = "edituser_link_";
	private String userEditActiveId= "user-edit-active";
	private String userEditSubmitId = "user-edit-submit";
	private String userEditCancelId = "user-edit-cancel";

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
		// ChromeOptions options = new ChromeOptions();
		// options.addArguments("-user-data-dir=C:/Users/P000801199/AppData/Local/Google/Chrome/User
		// Data/");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 6);
		driver.manage().window().maximize();
		baseUrl = "http://10.100.17.80:8080/login.jsp";
		driver.get(baseUrl);
	}

	@Test
	public void disableUserTest() {
		driver.findElement(By.id("login-form-username")).clear();
		driver.findElement(By.id("login-form-username")).sendKeys("zhijun.yang");
		driver.findElement(By.id("login-form-password")).clear();
		driver.findElement(By.id("login-form-password")).sendKeys("1");
		driver.findElement(By.id("login-form-remember-me")).click();
		driver.findElement(By.id("login-form-submit")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(adminMenuById)));
		driver.findElement(By.id(adminMenuById)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(adminUserById)));
		driver.findElement(By.id(adminUserById)).click();
		//administrator authentication again
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(authenticatePasswordById)));
		driver.findElement(By.id(authenticatePasswordById)).sendKeys("1");
		driver.findElement(By.id(adminLoginById)).click();

		
		do {
			List<WebElement> users = driver.findElements(By.cssSelector(usersPerPageByCss));
			for (WebElement user : users) {
				WebElement userNameWebElement = user.findElement(By.cssSelector(userNameCss));
				WebElement userLoginDetails = user.findElement(By.cssSelector(userLoginDetailsCss));
				String lastLoginTime = StringUtils.substringAfter(userLoginDetails.getText(), "Last: ");
				
				//if never login system, the system shows "Not recorded"
				if (!"".equals(lastLoginTime) && !StringUtils.contains(userNameWebElement.getText(), "(Inactive)")) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
					/*
					 * <li>if login system today, the system shows "today" and actual time
					 * 	   change the last login time to today implemented by Date()
					 * <li>if login failed, the system shows "CAPTCHA required at next login Last Failed Login:"
					 */
					try {
							dateFormat.parse(lastLoginTime);
					}
					catch (ParseException e) {
						lastLoginTime = dateFormat.format(new Date());
					}
					
					//disable the users whose last login time after baseline
					try {
						if (dateFormat.parse(lastLoginTime).before(dateFormat.parse(loginTimeBaseline))) {
							System.out.println("Disable " + userNameWebElement.getText() + ": whose last login time is " + lastLoginTime);
							disableUser(userNameWebElement.getText());
						}
					} catch (ParseException e) {
						
						System.out.println("Date format is wrong: " + userNameWebElement.getText() + userLoginDetails.getText()
								+ "the right format is: yyyy/MM/dd HH:mm");
					}
				}
			}
			if (hasNext()) {
				driver.findElement(By.cssSelector(nextById)).click();
			} else
				break;
		} while (true);
		
	}
	
	public void disableUser(String userName){
		driver.findElement(By.id(userEditPrefixId + userName)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(userEditActiveId)));
		if ("checked".equals(driver.findElement(By.id(userEditActiveId)).getAttribute("checked"))){
			driver.findElement(By.id(userEditActiveId)).click();
			driver.findElement(By.id(userEditCancelId)).click();
		}
	}

	public boolean hasNext() {
		try {
			WebElement nextWebElement = driver.findElement(By.cssSelector(nextById));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	@After
	public void tearDown() throws Exception {
		// driver.quit();
	}

}
