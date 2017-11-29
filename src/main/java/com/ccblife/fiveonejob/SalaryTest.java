package com.ccblife.fiveonejob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class SalaryTest {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl;
	private String nextCss = "#rtNext.dicon.Dm.on";
	private String jobsPerPageCss  = "#resultList .el:not(.title)";
	private String idCss = ".t1 input";
	private String titleCss = ".t1 a";
	private String companyCss = ".t2 a";
	private String locationCss = ".t1 a";
	private String salaryCss = ".t1 a";
//	private List<Job> jobs = new ArrayList();

	@Before
	public void setUp() throws Exception {
		//prepare the database
		jdbcTemplate = jdbcTemplate();
		System.setProperty("webdriver.chrome.driver", "D:\\Program Files\\selenium\\chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 6);
		driver.manage().window().maximize();
		baseUrl = "http://51job.com";
		driver.get(baseUrl);
	}

	@Test
	public void getJobs() {
		driver.findElement(By.id("kwdselectid")).clear();
		driver.findElement(By.id("kwdselectid")).sendKeys("¼Ü¹¹Ê¦");
		driver.findElement(By.id("kwdselectid")).sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(jobsPerPageCss)));
		
		do {
			List<WebElement> jobWebElements = driver.findElements(By.cssSelector(jobsPerPageCss));
			for (WebElement jobWebElement : jobWebElements) {

				String id = jobWebElement.findElement(By.cssSelector(idCss)).getAttribute("value");
				String title = jobWebElement.findElement(By.cssSelector(titleCss)).getText();
				String company = jobWebElement.findElement(By.cssSelector(companyCss)).getText();
				String location = jobWebElement.findElement(By.cssSelector(locationCss)).getText();
				int salary = Integer.valueOf(jobWebElement.findElement(By.cssSelector(salaryCss)).getText());
				
				Map<String, Object> params = new HashMap<>();
				params.put("id", id);
				params.put("title", title);
				params.put("company", company);
				params.put("location", location);
				params.put("salary", salary);
				
				String SQL = "insert into job (id, title, company, location, salary) values (?,?,?,?,?)";
				jdbcTemplate.update(SQL, params);

				System.out.println(id + title + company + location + salary);
			}
			if (hasNext()){
				driver.findElement(By.cssSelector(nextCss)).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(nextCss)));
			}
			else
				break;
		} while (true);
	}

	public boolean hasNext() {
		try {
			driver.findElement(By.cssSelector(nextCss));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public NamedParameterJdbcTemplate jdbcTemplate(){
		EmbeddedDatabaseBuilder  builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder
							  .setType(EmbeddedDatabaseType.HSQL)
							  .addScript("db/sql/create-table.sql")
							  .build();
		return new NamedParameterJdbcTemplate(db);
	}
}