package com.ccblife.fiveonejob;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SalaryTest {
	private WebDriver driver;
	private String baseUrl;
	private String nextId = "#rtNext .on";
	private String jobsPerPageCss  = "#resultList .el .t1 a";
	private String titleCss = "#resultList .el .t1 a:nth(0)";
	private List<Job> jobs = new ArrayList();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		baseUrl = "http://51job.com";
		driver.get(baseUrl);
	}

	@Test
	public void getJobs() {
		driver.findElement(By.id("kwdselectid")).clear();
		driver.findElement(By.id("kwdselectid")).sendKeys("¼Ü¹¹Ê¦");
		driver.findElement(By.id("kwdselectid")).sendKeys(Keys.ENTER);

		WebDriverWait wait = new WebDriverWait(driver, 6);
		
		do {
			List<WebElement> jobWebElements = driver.findElements(By.cssSelector(jobsPerPageCss));
			for (WebElement jobWebElement : jobWebElements) {
//				WebElement title = driver.findElement(By.cssSelector(titleCss));
//				System.out.println(title.getText());
			}
			if (hasNext()){
				driver.findElement(By.id(nextId)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(nextId)));
			}
			else
				break;
		} while (true);
	}

	public boolean hasNext() {
		try {
			WebElement nextWebElement = driver.findElement(By.id(nextId));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}