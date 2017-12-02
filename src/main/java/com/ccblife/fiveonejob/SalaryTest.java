package com.ccblife.fiveonejob;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class SalaryTest {
	private JdbcTemplate jdbcTemplate;
	private WebDriver driver;
	private WebDriverWait wait;
	private String baseUrl;
	private String nextCss = "#rtNext.dicon.Dm.on";
	private String jobsPerPageCss  = "#resultList .el:not(.title)";
	private String idCss = ".t1 input";
	private String titleCss = ".t1 a";
	private String companyCss = ".t2 a";
	private String locationCss = ".t3";
	private String salaryCss = ".t4";
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
				String strSalary = jobWebElement.findElement(By.cssSelector(salaryCss)).getText();
				String lowSalary = StringUtils.substringBefore(strSalary, "-");
				String highSalary = StringUtils.substringBetween(strSalary, "-", "Íò");
				BigDecimal salary;
				try{
					salary = (new BigDecimal(lowSalary).add(new BigDecimal(highSalary))).divide(new BigDecimal(2));
				}
				catch (Exception e){
					continue; 
				}
				
				String SQL = "insert into job (id, title, company, location, salary) values ('" 
						+ id +"', '"
						+ title + "', '"
						+ company + "', '"
						+ location +"', "
						+ salary +")";
				
				jdbcTemplate.update(SQL);

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
	
	public JdbcTemplate jdbcTemplate(){
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:hsql://localhost/job");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		return jt;
//		EmbeddedDatabaseBuilder  builder = new EmbeddedDatabaseBuilder();
//		EmbeddedDatabase db = builder
//							  .setType(EmbeddedDatabaseType.HSQL)
//							  .addScript("db/sql/create-table.sql")
//							  .build();
//		return new NamedParameterJdbcTemplate(db);
	}
}